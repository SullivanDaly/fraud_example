package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;

public class Merchant {

    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "merchant")
    private String company_name;

    @CsvBindByName(column = "category")
    private String company_cat;

    @CsvRecurse
    private MerchantCoordinates merchantCardholderCoordinates;

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_cat() {
        return company_cat;
    }

    public MerchantCoordinates getMerchantCoordinates() {
        return merchantCardholderCoordinates;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "company_name='" + company_name + '\'' +
                ", company_cat='" + company_cat + '\'' +
                ", merchantCoordinates=" + merchantCardholderCoordinates +
                '}';
    }
}
