package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class MerchantCoordinates {
    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "merch_lat")
    private String latitude_company;

    @CsvBindByName(column = "merch_long")
    private String longitude_company;

    public String getLatitude_company() {
        return latitude_company;
    }

    public String getLongitude_company() {
        return longitude_company;
    }

    @Override
    public String toString() {
        return "MerchantCoordinates{" +
                "latitude_company='" + latitude_company + '\'' +
                ", longitude_company='" + longitude_company + '\'' +
                '}';
    }
}
