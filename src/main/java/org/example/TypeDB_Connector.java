package org.example;

import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.TypeDB;


public class TypeDB_Connector {
    private TypeDBClient client;
    private final String ip_server;
    private final String port_server;
    private final String database_name;

    public TypeDB_Connector(String ip_server, String port_server, String database_name) {
        this.ip_server = ip_server;
        this.port_server = port_server;
        this.database_name = database_name;
    }

    public TypeDB_Connector() {
        ip_server = "127.0.0.1";
        port_server = "1729";
        database_name = "test_FRAUD";
    }

    public void connection() {
        client = TypeDB.coreClient(ip_server + ":" + port_server);
    }

    public void close() {
        client.close();
    }

    public TypeDBClient getClient() {
        return client;
    }

    public String getDatabase_name() {
        return database_name;
    }

}
