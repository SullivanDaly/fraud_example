package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Cardholder;
import org.example.Fraud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CardholderDAO {

        private final HashMap<String, Cardholder> hCardholder;
        private HashMap<String, Integer> hNumberCardholder;

        public CardholderDAO(List<Fraud> lFraud) {
            this.hCardholder = new HashMap<String, Cardholder>();
            hNumberCardholder = new HashMap<String, Integer>() ;

            lFraud.forEach(currentFraud -> hCardholder.put(currentFraud.getCardholder().getPerson_first_name() +
                    currentFraud.getCardholder().getPerson_last_name(), currentFraud.getCardholder()));
    }

    public String get_insert_query() {
        StringBuilder query = new StringBuilder("");
        int current = 0;

        for (Map.Entry<String, Cardholder> currentHash : hCardholder.entrySet()) {
            Cardholder currentCardholder = currentHash.getValue();
            String currentName = currentHash.getKey();

            query.append("$gcp").append(current).append(" isa Geo_coordinate, has longitude ").append(currentCardholder.getCardholderCoordinates().getLongitude_person());
            query.append(", has latitude ").append(currentCardholder.getCardholderCoordinates().getLatitude_person()).append(";\n");

            query.append("$add").append(current).append(" isa Address, has street '").append(currentCardholder.getAddress().getStreet()).append("'");
            query.append(", has city '").append(currentCardholder.getAddress().getCity()).append("'");
            query.append(", has state '").append(currentCardholder.getAddress().getState()).append("'");
            query.append(", has zip ").append(currentCardholder.getAddress().getZip()).append(";\n");

            query.append("$per").append(current).append(" isa Person, has first_name \"").append(currentCardholder.getPerson_first_name()).append("\"");
            query.append(", has last_name \"").append(currentCardholder.getPerson_last_name()).append("\"");
            query.append(", has gender '").append(currentCardholder.getGender()).append("'");
            query.append(", has job \"").append(currentCardholder.getJob()).append("\"");
            query.append(", has date_of_birth ").append(currentCardholder.getDate_of_birth()).append(";\n");

            query.append("$r3").append(current).append("(location: $add").append(current).append(", geo: $gcp")
                        .append(current).append(", identify: $per").append(current).append(") isa locate;\n");

            hNumberCardholder.put(currentName, current);
            current++;
        }
        return query.toString();
    }

    public HashMap<String, Cardholder> gethCardholder() {
        return hCardholder;
    }

    public HashMap<String, Integer> gethNumberCardholder() {
        return hNumberCardholder;
    }
}
