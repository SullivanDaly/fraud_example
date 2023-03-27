package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Merchant;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MerchantDAO {
    private final List<Merchant> lMerchant;
    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert \n"
            + "$gcc isa Geo_coordinate, has longitude %s, has latitude %s;\n"
            + "$com isa Company, has name \"%s\", has company_type \"%s\";\n"
            + "$rel(geo: $gcc, identify: $com) isa geolocate;";

    public MerchantDAO(TypeDB_SessionWrapper wrapper, List<Merchant> lMerchant) {
        this.lMerchant = lMerchant;
        this.wrapper = wrapper;
    }

    private List<String> get_insert_query(){

        String result = "";
        List<String> lResult = new ArrayList<String>();
        for (Merchant currentMerchant : lMerchant) {

            result = query.formatted(
                    currentMerchant.getMerchantCoordinates().getLongitude(),
                    currentMerchant.getMerchantCoordinates().getLatitude(),
                    currentMerchant.getCompany_name(),
                    currentMerchant.getCompany_cat()
            );


            lResult.add(result);
        }
        return lResult;
    }

    public void insert_all() throws IOException {
        wrapper.load_data(get_insert_query());
    }

    public List<Merchant> getlMerchant() {
        return lMerchant;
    }

}
