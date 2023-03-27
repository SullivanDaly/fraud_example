package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;

public class CreditCard {
    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "cc_num")
    private String card_number;

    private Bank bank;

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }

    public String getCard_number() {
        return card_number;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                ", card_number='" + card_number + '\'' +
                ", bank='" + bank + '\'' +
                '}';
    }
}
