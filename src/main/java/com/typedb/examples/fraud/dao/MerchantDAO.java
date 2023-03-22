package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Merchant;
import org.example.Fraud;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MerchantDAO {
    private final HashMap<String, Merchant> hMerchant;
    private HashMap<String, Integer> hNumberMerchant;
    private final String[][] fakeBanks;


    public MerchantDAO(List<Fraud> lFraud) {
        this.hMerchant = new HashMap<String, Merchant>();
        hNumberMerchant = new HashMap<String, Integer>() ;

        lFraud.forEach(currentFraud -> hMerchant.put(currentFraud.getMerchant().getCompany_name(), currentFraud.getMerchant()));
        String[][] tmpBanks = {{"ABC", "30.5", "-90.3"}, {"MNO", "33.986391", "-81.200714"},
            {"QRS", "43.7", "-88.2"}, {"XYZ", "40.98", "-90.4"}};
        fakeBanks = tmpBanks;
    }

    public String get_insert_query(){

        StringBuilder query = new StringBuilder("");
        int current = 0;
        for (int i = 0; i < 4; i++) {
            query.append("$ban").append(i).append(" isa Bank, has name '").append(fakeBanks[i][0]).append("'");
            query.append(", has company_type 'Bank';\n");

            query.append("$gcb").append(i).append(" isa Geo_coordinate, has latitude ").append(fakeBanks[i][1]);
            query.append(", has longitude ").append(fakeBanks[i][2]).append(";\n");

            query.append("$r5").append(i).append("(geo: $gcb").append(i).append(", identify: $ban")
                    .append(i).append(") isa geolocate;\n");
        }
        for (Map.Entry<String, Merchant> currentHash : hMerchant.entrySet()) {
            Merchant currentMerchant = currentHash.getValue();
            String currentName = currentHash.getKey();

            query.append("$gcc").append(current).append(" isa Geo_coordinate, has longitude ").append(currentMerchant.getMerchantCoordinates().getLongitude_company());
            query.append(", has latitude ").append(currentMerchant.getMerchantCoordinates().getLatitude_company()).append(";\n");

            query.append("$com").append(current).append(" isa Company, has name \"").append(currentMerchant.getCompany_name()).append("\"");
            query.append(", has company_type '").append(currentMerchant.getCompany_cat()).append("';\n");

            query.append("$r4").append(current).append("(geo: $gcc").append(current).append(", identify: $com")
                .append(current).append(") isa geolocate;\n");

            hNumberMerchant.put(currentName, current);
            current++;
        }
        return query.toString();
    }

    public HashMap<String, Merchant> gethMerchant() {
        return hMerchant;
    }

    public HashMap<String, Integer> gethNumberMerchant() {
        return hNumberMerchant;
    }

    public String[][] getFakeBanks() {
        return fakeBanks;
    }


}
