package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Bank;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class BankDAO {
    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert \n"
            + "$ban isa Bank, has name \"%s\", has company_type \"Bank\"; \n"
            + "$gcb isa Geo_coordinate, has latitude %s, has longitude %s;\n"
            + "$rel(geo: $gcb, identify: $ban) isa geolocate;";
    public BankDAO(TypeDB_SessionWrapper wrapper) {
        this.wrapper = wrapper;
    }

    private String getQueryStr(Bank currentBank) {
        String result = query.formatted(
                    currentBank.getBank_name(),
                    currentBank.getBankCoordinates().getLatitude(),
                    currentBank.getBankCoordinates().getLongitude()
            );
        return (result);
    }

    public void insertAll(Set<Bank> lBank) throws IOException {

        Set<String> queryStrs = lBank.stream().map(this::getQueryStr).collect(Collectors.toSet());
        wrapper.load_data(queryStrs);
    }
}
