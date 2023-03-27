package com.typedb.examples.fraud.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvRecurse;

public class Transaction {

    @CsvBindByName(column = "amt")
    private String amount;

    @CsvBindByName(column = "trans_num")
    private String transaction_number;

    @CsvBindByName(column = "trans_date_trans_time")
    private String date_transaction;
    @CsvRecurse
    private Merchant merchant;
    @CsvRecurse
    private Cardholder cardholder;

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setCardholder(Cardholder cardholder) {
        this.cardholder = cardholder;
    }

    public String getDate_transaction() {
        String tmp[] = date_transaction.split(" ");
        return tmp[0] + "T" + tmp[1];
    }

    public String getAmount() {
        return amount;
    }

    public String getTransaction_number() {
        return transaction_number;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Cardholder getCardholder() {
        return cardholder;
    }
}
