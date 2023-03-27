package org.example;

import com.opencsv.bean.CsvToBeanBuilder;
import com.typedb.examples.fraud.model.*;
import com.vaticle.typedb.client.TypeDB;
import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBOptions;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typedb.client.api.answer.ConceptMap;
import com.vaticle.typeql.lang.TypeQL;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class TypeDB_SessionWrapper {
    private TypeDBClient client;
    private TypeDBSession session;
    private final String ip_server;
    private final String port_server;
    private final String database_name;
    private String path;
    private List<Fraud> lFraud;
    private List<Bank> lBank;
    private List<Cardholder> lCardholder;
    private List<CreditCard> lCreditCard;
    private List<Merchant> lMerchant;
    private List<Transaction> lTransaction;

    public TypeDB_SessionWrapper(String ip_server, String port_server, String database_name) throws FileNotFoundException {
        this.ip_server = ip_server;
        this.port_server = port_server;
        this.database_name = database_name;
        this.path = "./src/main/resources/";
        lBank = new ArrayList<Bank>();
        lCardholder = new ArrayList<Cardholder>();
        lCreditCard = new ArrayList<CreditCard>();
        lMerchant = new ArrayList<Merchant>();
        lTransaction = new ArrayList<Transaction>();
        this.parse_data_init();
    }

    public TypeDB_SessionWrapper() throws FileNotFoundException {
        this.ip_server = "127.0.0.1";
        this.port_server = "1729";
        this.database_name = "test_FRAUD";
        this.path = path = "./src/main/resources/";
        lBank = new ArrayList<Bank>();
        lCardholder = new ArrayList<Cardholder>();
        lCreditCard = new ArrayList<CreditCard>();
        lMerchant = new ArrayList<Merchant>();
        lTransaction = new ArrayList<Transaction>();
        this.parse_data_init();
    }

    private void connection() {
        client = TypeDB.coreClient(ip_server + ":" + port_server);
    }

    private void parse_data_init() throws FileNotFoundException {
        HashMap<String, Merchant> hMerchant = new HashMap<String, Merchant>();
        HashMap<String, CreditCard> hCreditCard = new HashMap<String, CreditCard>();
        HashMap<String, Cardholder> hCardholder = new HashMap<String, Cardholder>();
        String fileName = path + "small_dataset_fraud.csv";

        lFraud = new CsvToBeanBuilder<Fraud>(new FileReader(fileName)).withType(Fraud.class).build().parse();


        String[][] fakeBanks = {{"ABC", "30.5", "-90.3"}, {"MNO", "33.986391", "-81.200714"},
            {"QRS", "43.7", "-88.2"}, {"XYZ", "40.98", "-90.4"}};

        for(String[] currentBank : fakeBanks){
            lBank.add(new Bank(currentBank[0], new BankCoordinates(currentBank[1], currentBank[2])));
        }

        for (Fraud currentFraud : lFraud) {

            int random = ThreadLocalRandom.current().nextInt(0, 4);
            currentFraud.getCardholder().getCreditCard().setBank(lBank.get(random));

            currentFraud.getTransaction().setCardholder(currentFraud.getCardholder());
            currentFraud.getTransaction().setMerchant(currentFraud.getMerchant());

            hCardholder.put(currentFraud.getCardholder().getPerson_first_name() +
                    currentFraud.getCardholder().getPerson_last_name(), currentFraud.getCardholder());
            hMerchant.put(currentFraud.getMerchant().getCompany_name(), currentFraud.getMerchant());
            hCreditCard.put(currentFraud.getCardholder().getCreditCard().getCard_number(), currentFraud.getCardholder().getCreditCard());
            lTransaction.add(currentFraud.getTransaction());
        }

        lCardholder = new ArrayList<Cardholder>(hCardholder.values());
        lCreditCard = new ArrayList<CreditCard>(hCreditCard.values());
        lMerchant = new ArrayList<Merchant>(hMerchant.values());

    }
    public void load_schema() throws IOException {
        boolean writing_flag = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (client.databases().contains(database_name)) {
            System.out.println("Do you want to Delete the existing Database? Yes/No");
            System.out.print("Answer: ");
            String answer = reader.readLine();
            answer = answer.toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                client.databases().get(database_name).delete();
                client.databases().create(database_name);
            } else {
                writing_flag = false;
            }
        }

        // writing part, if is useless but it may be clearer like that
        if (writing_flag) {
            session = client.session(database_name, TypeDBSession.Type.SCHEMA);
            TypeDBTransaction writeTransaction = session.transaction(TypeDBTransaction.Type.WRITE);
            try {
                byte[] encoded = Files.readAllBytes(Paths.get(path + "schema_fraud.tql"));
                String query = new String(encoded, StandardCharsets.UTF_8);
                writeTransaction.query().define(TypeQL.parseQuery(query).asDefine());
                writeTransaction.commit();
                System.out.println("Loaded the " + database_name + " schema");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
    }


    private void read_data() throws IOException {
        String query = "match" +
                    "$p isa Person, has $z;" +
                    "$com isa Company, has $y;" +
                    "$d($p, $com, $x) isa same_place; get $p, $com, $z, $y;";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        session = client.session(database_name, TypeDBSession.Type.DATA);
        TypeDBTransaction readTransaction = session.transaction(TypeDBTransaction.Type.READ, TypeDBOptions.core().infer(true));
        if (readTransaction.isOpen()) {
            System.out.println("Which query do you want to try ? 1 or 2");
            System.out.print("Answer: ");
            String answer = reader.readLine();

            Stream<ConceptMap> queryAnswers = readTransaction.query().match(query);
            queryAnswers.forEach(queryAnswer -> System.out.println(queryAnswer.get("z").asAttribute().getValue()));
            System.out.println("Read DONE");
        }
    }

    public void load_data(List<String> lInsert) throws IOException {

        session = client.session(database_name, TypeDBSession.Type.DATA);
        try (TypeDBTransaction writeTransaction = session.transaction(TypeDBTransaction.Type.WRITE)) { // WRITE transaction is open
            for(String currentInsert : lInsert){
                writeTransaction.query().insert(currentInsert);
            }
            writeTransaction.commit(); // to persist changes, a write transaction must always be committed
            System.out.println("Data Loaded");
        }
    }

    public void init_connection() throws IOException {
        this.connection();
    }

    public void close_connection() throws IOException {
        client.close();
    }

    public List<Bank> getlBank() {
        return lBank;
    }

    public List<Cardholder> getlCardholder() {
        return lCardholder;
    }

    public List<CreditCard> getlCreditCard() {
        return lCreditCard;
    }

    public List<Merchant> getlMerchant() {
        return lMerchant;
    }

    public List<Transaction> getlTransaction() {
        return lTransaction;
    }

    public String getDatabase_name() {
        return database_name;
    }

    public TypeDBClient getClient() {
        return client;
    }
}
