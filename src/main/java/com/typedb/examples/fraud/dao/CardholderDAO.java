package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Cardholder;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CardholderDAO {

    private final List<Cardholder> lCardholder;
    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert\n"
            + "$gcp isa Geo_coordinate, has longitude %s, has latitude %s;\n"
            + "$add isa Address, has street \"%s\", has city \"%s\", has state \"%s\", has zip %s;\n"
            + "$per isa Person, has first_name \"%s\", has last_name \"%s\", has gender \"%s\", has job \"%s\", has date_of_birth %s;\n"
            + "$r3(location: $add, geo: $gcp, identify: $per) isa locate;\n";

    public CardholderDAO( TypeDB_SessionWrapper wrapper, List<Cardholder> lCardholder) {
        this.lCardholder = lCardholder;
        this.wrapper = wrapper;
    }

    private List<String> get_insert_query() {

        String result = "";
        List<String> lResult = new ArrayList<String>();

        for (Cardholder currentCardholder : lCardholder) {
            result = query.formatted(
                    currentCardholder.getCardholderCoordinates().getLongitude(),
                    currentCardholder.getCardholderCoordinates().getLatitude(),
                    currentCardholder.getAddress().getStreet(),
                    currentCardholder.getAddress().getCity(),
                    currentCardholder.getAddress().getState(),
                    currentCardholder.getAddress().getZip(),
                    currentCardholder.getPerson_first_name(),
                    currentCardholder.getPerson_last_name(),
                    currentCardholder.getGender(),
                    currentCardholder.getJob(),
                    currentCardholder.getDate_of_birth()
            );

            lResult.add(result);
        }
        return lResult;
    }

    public void insert_all() throws IOException {
        wrapper.load_data(get_insert_query());
    }

    public List<Cardholder> getlCardholder() {
        return lCardholder;
    }

}
