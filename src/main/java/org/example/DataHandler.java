package org.example;

import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.Iterator;
import java.util.concurrent.ThreadLocalRandom;

public class DataHandler {

    private final String path;
    private final List<Fraud> frauds;

    public DataHandler(String path) throws FileNotFoundException {
        this.path = path;
        frauds = parse_data_init();
    }

    private List<Fraud> parse_data_init() throws FileNotFoundException {
        String fileName = path + "small_dataset_fraud.csv";
        return new CsvToBeanBuilder<Fraud>(new FileReader(fileName)).withType(Fraud.class).build().parse();
    }

    public String get_insert_query() {
        return get_insert_query(0, frauds.size());
    }

    public String get_insert_query(int offset, int limit) {
        String[][] fakeBanks = {{"ABC", "30.5", "-90.3"}, {"MNO", "33.986391", "-81.200714"},
                {"QRS", "43.7", "-88.2"}, {"XYZ", "40.98", "-90.4"}};
        Iterator<Fraud> iterator = frauds.iterator();
        int current = 0;
        Fraud currentFraud;
        StringBuilder query = new StringBuilder("insert ");

        for (int i = 0; i < 3; i++) {
            query.append("$ban").append(i).append(" isa Bank, has name '").append(fakeBanks[i][0]).append("'");
            query.append(", has company_type 'Bank';\n");

            query.append("$gcb").append(i).append(" isa Geo_coordinate, has latitude ").append(fakeBanks[i][1]);
            query.append(", has longitude ").append(fakeBanks[i][2]).append(";\n");

            query.append("$r5").append(i).append("(geo: $gcb").append(i).append(", identify: $ban")
                    .append(i).append(") isa geolocate;\n");
        }
        while (iterator.hasNext() && current < limit) {
            currentFraud = iterator.next();
            if (current >= offset) {
                query.append("$gcp").append(current).append(" isa Geo_coordinate, has longitude ").append(currentFraud.getLongitude_person());
                query.append(", has latitude ").append(currentFraud.getLatitude_person()).append(";\n");

                query.append("$gcc").append(current).append(" isa Geo_coordinate, has longitude ").append(currentFraud.getLongitude_company());
                query.append(", has latitude ").append(currentFraud.getLatitude_company()).append(";\n");

                query.append("$add").append(current).append(" isa Address, has street '").append(currentFraud.getStreet()).append("'");
                query.append(", has city '").append(currentFraud.getCity()).append("'");
                query.append(", has state '").append(currentFraud.getState()).append("'");
                query.append(", has zip ").append(currentFraud.getZip()).append(";\n");

                query.append("$car").append(current).append(" isa Card, has card_number ").append(currentFraud.getCard_number()).append(";\n");

                query.append("$per").append(current).append(" isa Person, has first_name \"").append(currentFraud.getPerson_first_name()).append("\"");
                query.append(", has last_name \"").append(currentFraud.getPerson_last_name()).append("\"");
                query.append(", has gender '").append(currentFraud.getGender()).append("'");
                query.append(", has job \"").append(currentFraud.getJob()).append("\"");
                query.append(", has date_of_birth ").append(currentFraud.getDate_of_birth()).append(";\n");

                query.append("$com").append(current).append(" isa Company, has name \"").append(currentFraud.getCompany_name()).append("\"");
                query.append(", has company_type '").append(currentFraud.getCompany_cat()).append("';\n");

                int random = ThreadLocalRandom.current().nextInt(0, 3);


                query.append("$r1").append(current).append("(owner: $per").append(current)
                        .append(", attached_card: $car").append(current).append(", attached_bank: $ban").append(random).append(") isa bank_account;\n");

                query.append("$r2").append(current).append("(used_card: $car").append(current).append(", to: $com").append(current).append(") isa transaction");
                query.append(", has timestamp ").append(currentFraud.getDate_transaction());
                query.append(", has amount ").append(currentFraud.getAmount());
                query.append(", has transaction_number '").append(currentFraud.getTransaction_number()).append("';\n");

                query.append("$r3").append(current).append("(location: $add").append(current).append(", geo: $gcp")
                        .append(current).append(", identify: $per").append(current).append(") isa locate;\n");

                query.append("$r4").append(current).append("(geo: $gcc").append(current).append(", identify: $com")
                        .append(current).append(") isa geolocate;\n");


            }
            current += 1;
        }
        return query.toString();

    }

    public String get_match_query(int query_choice) {
        String query = "";
        switch (query_choice) {
            case 1:
                query = "toto";
                break;

            default:
                query = "match" +
                        "$p isa Person, has $z;" +
                        "$com isa Company, has $y;" +
                        "$d($p, $com, $x) isa same_place; get $p, $com, $z, $y;";
        }
        return query;
    }


}
