package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Merchant;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class MerchantDAO {
    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert \n"
            + "$gcc isa Geo_coordinate, has longitude %s, has latitude %s;\n"
            + "$com isa Company, has name \"%s\", has company_type \"%s\";\n"
            + "$rel(geo: $gcc, identify: $com) isa geolocate;";

    public MerchantDAO(TypeDB_SessionWrapper wrapper) {
        this.wrapper = wrapper;
    }

    private String getQueryStr(Merchant currentMerchant){

        String result = query.formatted(
                    currentMerchant.getMerchantCoordinates().getLongitude(),
                    currentMerchant.getMerchantCoordinates().getLatitude(),
                    currentMerchant.getCompany_name(),
                    currentMerchant.getCompany_cat()
            );

        return result;
    }

    public void insert_all(Set<Merchant> lMerchant) throws IOException {
        Set<String> queryStrs = lMerchant.stream().map(this::getQueryStr).collect(Collectors.toSet());

        wrapper.load_data(queryStrs);
    }


}
