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


    public TypeDB_SessionWrapper(String ip_server, String port_server, String database_name) throws FileNotFoundException {
        this.ip_server = ip_server;
        this.port_server = port_server;
        this.database_name = database_name;
        this.path = "./src/main/resources/";
    }

    public TypeDB_SessionWrapper() throws FileNotFoundException {
        this.ip_server = "127.0.0.1";
        this.port_server = "1729";
        this.database_name = "test_FRAUD";
        this.path = path = "./src/main/resources/";
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
        client = TypeDB.coreClient(ip_server + ":" + port_server);
    }

    public void close_connection() throws IOException {
        client.close();
    }

    public String getDatabase_name() {
        return database_name;
    }

    public TypeDBClient getClient() {
        return client;
    }
}
