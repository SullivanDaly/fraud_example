package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

public class CardholderCoordinates {
    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "lat")
    private String latitude_person;

    @CsvBindByName(column = "long")
    private String longitude_person;

        public String getLatitude_person() {
        return latitude_person;
    }

    public String getLongitude_person() {
        return longitude_person;
    }

    @Override
    public String toString() {
        return "CardholderCoordinates{" +
                "latitude_person='" + latitude_person + '\'' +
                ", longitude_person='" + longitude_person + '\'' +
                '}';
    }
}
