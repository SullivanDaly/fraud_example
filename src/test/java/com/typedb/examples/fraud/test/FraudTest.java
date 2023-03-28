package com.typedb.examples.fraud.test;

import com.opencsv.bean.CsvToBeanBuilder;
import com.typedb.examples.fraud.dao.*;
import com.typedb.examples.fraud.model.*;
import com.vaticle.typedb.client.TypeDB;
import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typeql.lang.TypeQL;
import org.example.TypeDB_SessionWrapper;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FraudTest {
    private static TypeDB_SessionWrapper typeDBw;
    private static final String path = "./src/main/resources/";
    private static Set<Cardholder> hCardholder;
    private static Set<Merchant> hMerchant;
    private static Set<Transaction> hTransaction;
    private static Set<Bank> hBank;
    private static TypeDBClient client;

    @BeforeClass
    public static void loadTestData() throws IOException {

        hCardholder = new HashSet<>();
        hMerchant = new HashSet<>();
        hTransaction = new HashSet<>();
        hBank = new HashSet<>();

        typeDBw = new TypeDB_SessionWrapper();
        String ip_server = "127.0.0.1";
        String port_server = "1729";
        client = TypeDB.coreClient(ip_server + ":" + port_server);
        if (client.databases().contains(typeDBw.getDatabase_name())) {
            client.databases().get(typeDBw.getDatabase_name()).delete();
        }
        client.databases().create(typeDBw.getDatabase_name());
    }

    @Test
    public void parseData() throws FileNotFoundException {
        String fileName = path + "small_dataset_fraud.csv";

        hTransaction = new CsvToBeanBuilder<Transaction>(new FileReader(fileName)).withType(Transaction.class).build().stream().collect(Collectors.toSet());

        hBank.add(new Bank("ABC", new BankCoordinates("30.5", "-90.3")));
        hBank.add(new Bank("MNO", new BankCoordinates("33.986391", "-81.200714")));
        hBank.add(new Bank("QRS", new BankCoordinates("43.7", "-88.2")));
        hBank.add(new Bank("XYZ", new BankCoordinates("40.98", "-90.4")));

        hTransaction.forEach(currentTransaction -> {
            if(hBank.stream().skip((int) (hBank.size() * Math.random())).findFirst().isPresent()) {
                Bank currentBank = hBank.stream().skip((int) (hBank.size() * Math.random())).findFirst().get();
                currentTransaction.getCardholder().getCreditCard().setBank(currentBank);
            }
        });

        hMerchant = hTransaction.stream().map(Transaction::getMerchant).collect(Collectors.toSet());
        hCardholder = hTransaction.stream().map(Transaction::getCardholder).collect(Collectors.toSet());
    }

    @Test
    public void loadSchema() throws IOException {
        // MANUAL
        TypeDBSession session = client.session(typeDBw.getDatabase_name(), TypeDBSession.Type.SCHEMA);
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
    public void loadData() throws IOException {

        CardholderDAO cardholderDAO = new CardholderDAO(typeDBw);
        MerchantDAO merchantDAO = new MerchantDAO(typeDBw);
        BankDAO bankDAO = new BankDAO(typeDBw);
        TransactionDAO transactionDAO = new TransactionDAO(typeDBw);

        cardholderDAO.insert_all(hCardholder);
        merchantDAO.insert_all(hMerchant);
        bankDAO.insertAll(hBank);
        transactionDAO.insert_all(hTransaction);

    }

    @AfterClass
    public static void close() throws IOException {
        client.close();
    }


}
