package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TypeDB_Connector connector = new TypeDB_Connector();
        connector.connection();
        TypeDB_SessionHandler sessionHandler = new TypeDB_SessionHandler(connector);

        sessionHandler.load_schema();
        sessionHandler.load_data();
        sessionHandler.read_data();

        sessionHandler.close();
        connector.close();

    }
}