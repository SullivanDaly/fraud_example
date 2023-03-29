package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Cardholder;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class CardholderDAO {

    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert\n"
            + "$gcp isa Geo_coordinate, has longitude %s, has latitude %s;\n"
            + "$add isa Address, has street \"%s\", has city \"%s\", has state \"%s\", has zip %s;\n"
            + "$per isa Person, has first_name \"%s\", has last_name \"%s\", has gender \"%s\", has job \"%s\", has date_of_birth %s;\n"
            + "$car isa Card, has card_number %s;"
            + "$r3(location: $add, geo: $gcp, identify: $per) isa locate;\n";

    public CardholderDAO( TypeDB_SessionWrapper wrapper) {
        this.wrapper = wrapper;
    }

    private String getQueryStr(Cardholder currentCardholder) {
        String result = query.formatted(
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
                    currentCardholder.getDate_of_birth(),
                    currentCardholder.getCreditCard().getCard_number()
            );
        return (result);
    }


    public void insert_all(Set<Cardholder> lCardholder) throws IOException {
        Set<String> queryStrs = lCardholder.stream().map(this::getQueryStr).collect(Collectors.toSet());
        wrapper.load_data(queryStrs);
    }


}
