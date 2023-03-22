package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvRecurse;

public class Cardholder {
    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "first")
    private String person_first_name;

    @CsvBindByName(column = "last")
    private String person_last_name;

    @CsvBindByName(column = "gender")
    private String gender;

    @CsvBindByName(column = "job")
    private String job;

    @CsvBindByName(column = "dob")
    private String date_of_birth;

    @CsvRecurse
    private Address address;

    @CsvRecurse
    private CardholderCoordinates cardholderCoordinates;

    public String getPerson_first_name() {
        return person_first_name;
    }

    public String getPerson_last_name() {
        return person_last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getJob() {
        return job;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public Address getAddress() {
        return address;
    }

    public CardholderCoordinates getCardholderCoordinates() {
        return cardholderCoordinates;
    }

    @Override
    public String toString() {
        return "Cardholder{" +
                "person_first_name='" + person_first_name + '\'' +
                ", person_last_name='" + person_last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", job='" + job + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", address=" + address +
                ", cardholderCoordinates=" + cardholderCoordinates +
                '}';
    }
}
