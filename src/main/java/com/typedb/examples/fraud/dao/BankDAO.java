package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Bank;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankDAO {
    private final List<Bank> lBank;
    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert \n"
            + "$ban isa Bank, has name \"%s\", has company_type \"Bank\"; \n"
            + "$gcb isa Geo_coordinate, has latitude %s, has longitude %s;\n"
            + "$rel(geo: $gcb, identify: $ban) isa geolocate;";
    public BankDAO(TypeDB_SessionWrapper wrapper, List<Bank> lBank) {
        this.lBank = lBank;
        this.wrapper = wrapper;
    }

    private List<String> get_insert_query() {

        String result = "";
        List<String> lResult = new ArrayList<String>();

        for(Bank currentBank : lBank){

            result = query.formatted(
                    currentBank.getBank_name(),
                    currentBank.getBankCoordinates().getLatitude(),
                    currentBank.getBankCoordinates().getLongitude()
            );

            lResult.add(result);
        }

        return lResult;
    }

    public void insert_all() throws IOException {
        wrapper.load_data(get_insert_query());
    }
}
