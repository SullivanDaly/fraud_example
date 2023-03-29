package org.example;

import com.vaticle.typedb.client.TypeDB;
import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.api.TypeDBTransaction;

import java.io.*;
import java.util.Set;

public class TypeDB_SessionWrapper {
    private final String ip_server;
    private final String port_server;
    private final String database_name;

    public TypeDB_SessionWrapper(String ip_server, String port_server, String database_name) throws FileNotFoundException {
        this.ip_server = ip_server;
        this.port_server = port_server;
        this.database_name = database_name;
    }

    public TypeDB_SessionWrapper() throws FileNotFoundException {
        this.ip_server = "127.0.0.1";
        this.port_server = "1729";
        this.database_name = "test_FRAUD";
    }

    public void load_data(Set<String> lInsert) throws IOException {
        TypeDBClient client = TypeDB.coreClient(ip_server + ":" + port_server);
        TypeDBSession session = client.session(database_name, TypeDBSession.Type.DATA);
        try (TypeDBTransaction writeTransaction = session.transaction(TypeDBTransaction.Type.WRITE)) { // WRITE transaction is open
            for(String currentInsert : lInsert){
                writeTransaction.query().insert(currentInsert);
            }
            writeTransaction.commit(); // to persist changes, a write transaction must always be committed
            System.out.println("Data Loaded");
        }
        client.close();
    }

    public String getDatabase_name() {
        return database_name;
    }

}
