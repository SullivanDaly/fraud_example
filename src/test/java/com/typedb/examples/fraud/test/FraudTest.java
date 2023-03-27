package com.typedb.examples.fraud.test;

import com.opencsv.bean.CsvToBeanBuilder;
import com.typedb.examples.fraud.dao.*;
import com.typedb.examples.fraud.model.*;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typeql.lang.TypeQL;
import org.example.TypeDB_SessionWrapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Order;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class FraudTest {
    private static TypeDB_SessionWrapper typeDBw;
    private static final String path = "./src/main/resources/";
    private static List<Cardholder> lCardholder;
    private static List<CreditCard> lCreditCard;
    private static List<Merchant> lMerchant;
    private static List<Transaction> lTransaction;
    private static List<Fraud> lFraud;
    private static List<Bank> lBank;

    @BeforeClass
    public static void loadTestData() throws IOException {

        lCardholder = new ArrayList<>();
        lCreditCard = new ArrayList<>();
        lMerchant = new ArrayList<>();
        lTransaction = new ArrayList<>();
        lBank = new ArrayList<>();
        lFraud = new ArrayList<>();

        typeDBw = new TypeDB_SessionWrapper();
        typeDBw.init_connection();
        if (typeDBw.getClient().databases().contains(typeDBw.getDatabase_name())) {
            typeDBw.getClient().databases().get(typeDBw.getDatabase_name()).delete();
        }
        typeDBw.getClient().databases().create(typeDBw.getDatabase_name());
    }

    @Test
    public void parse_data() throws FileNotFoundException {
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

    @Test
    public void load_schema() throws IOException {

        TypeDBSession session = typeDBw.getClient().session(typeDBw.getDatabase_name(), TypeDBSession.Type.SCHEMA);
        try (session) {
            TypeDBTransaction writeTransaction = session.transaction(TypeDBTransaction.Type.WRITE);
            byte[] encoded = Files.readAllBytes(Paths.get(path + "schema_fraud.tql"));
            String query = new String(encoded, StandardCharsets.UTF_8);
            writeTransaction.query().define(TypeQL.parseQuery(query).asDefine());
            writeTransaction.commit();
            System.out.println("Loaded the " + typeDBw.getDatabase_name() + " schema");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void load_data() throws IOException {

        CardholderDAO cardholderDAO = new CardholderDAO(typeDBw, lCardholder);
        MerchantDAO merchantDAO = new MerchantDAO(typeDBw, lMerchant);
        BankDAO bankDAO = new BankDAO(typeDBw, lBank);
        CreditCardDAO creditCardDAO = new CreditCardDAO(typeDBw, lCreditCard);
        TransactionDAO transactionDAO = new TransactionDAO(typeDBw, lTransaction);

        cardholderDAO.insert_all();
        merchantDAO.insert_all();
        bankDAO.insert_all();
        creditCardDAO.insert_all();
        transactionDAO.insert_all();

    }

    @AfterClass
    public static void close() throws IOException {
        typeDBw.close_connection();
    }



}
