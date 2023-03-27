package org.example;

import com.typedb.examples.fraud.dao.*;
import com.typedb.examples.fraud.model.Cardholder;
import com.vaticle.typedb.client.api.TypeDBOptions;
import com.vaticle.typedb.client.api.TypeDBSession;
import com.vaticle.typedb.client.api.TypeDBTransaction;
import com.vaticle.typedb.client.api.answer.ConceptMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {

    }

    /*
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

     */
}