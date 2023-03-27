package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;

public class CardholderCoordinates {
    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "lat")
    private String latitude;

    @CsvBindByName(column = "long")
    private String longitude;

    public CardholderCoordinates() {
    }

    public CardholderCoordinates(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
