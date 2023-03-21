package org.example;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.vaticle.typedb.client.api.TypeDBClient;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typedb.client.api.answer.ConceptMap;
import com.vaticle.typeql.lang.TypeQL;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class TypeDB_SessionHandler {

    private final TypeDBClient client;
    private TypeDBSession session;
    private final String path;
    private final String database_name;

    public TypeDB_SessionHandler(TypeDB_Connector my_connector) {
        this.client = my_connector.getClient();
        database_name = my_connector.getDatabase_name();
        path = "./data/";
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

    public void load_data() throws IOException {
        DataHandler myHandler = new DataHandler(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        session = client.session(database_name, TypeDBSession.Type.DATA);
        TypeDBTransaction writeTransaction = session.transaction(TypeDBTransaction.Type.WRITE);
        if (writeTransaction.isOpen()) {
            System.out.println("Do you want to Load the Data? Yes/No");
            System.out.print("Answer: ");
            String answer = reader.readLine();
            answer = answer.toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                writeTransaction.query().insert(TypeQL.parseQuery(myHandler.get_insert_query()).asInsert());
                writeTransaction.commit();
                System.out.println("Loaded the " + database_name + " data");
            }
        }
    }

    public void read_data() throws IOException {
        DataHandler myHandler = new DataHandler(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        session = client.session(database_name, TypeDBSession.Type.DATA);
        TypeDBTransaction readTransaction = session.transaction(TypeDBTransaction.Type.READ);
        if (readTransaction.isOpen()) {
            System.out.println("Which query do you want to try ? 1 or 2");
            System.out.print("Answer: ");
            String answer = reader.readLine();
            answer = answer.toLowerCase();
            if (answer.equals("1") || answer.equals("2")) {
                Stream<ConceptMap> queryAnswers = readTransaction.query().match(TypeQL.parseQuery(myHandler.get_match_query(Integer.parseInt(answer))).asMatch());
                queryAnswers.forEach(queryAnswer -> System.out.println(queryAnswer.get("p").asThing().getIID()));
            }
        }
    }

    public void close() {
        session.close();
    }
}
