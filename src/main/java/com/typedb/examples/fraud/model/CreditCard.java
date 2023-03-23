package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;

public class CreditCard {
    //,trans_date_trans_time,cc_num,merchant,category,amt,first,last,gender,street,city,state,zip,lat,long,city_pop,job,dob,trans_num,unix_time,merch_lat,merch_long,is_fraud

    @CsvBindByName(column = "trans_date_trans_time")
    private String date_transaction;

    @CsvBindByName(column = "cc_num")
    private String card_number;

    @CsvBindByName(column = "amt")
    private String amount;

    @CsvBindByName(column = "trans_num")
    private String transaction_number;

    public String getDate_transaction() {
        String tmp[] = date_transaction.split(" ");
        return tmp[0] + "T" + tmp[1];
    }

    public String getCard_number() {
        return card_number;
    }

    public String getAmount() {
        return amount;
    }

    public String getTransaction_number() {
        return transaction_number;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "date_transaction='" + date_transaction + '\'' +
                ", card_number='" + card_number + '\'' +
                ", amount='" + amount + '\'' +
                ", transaction_number='" + transaction_number + '\'' +
                '}';
    }
}
