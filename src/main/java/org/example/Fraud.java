package org.example;

import com.opencsv.bean.CsvRecurse;
import com.typedb.examples.fraud.model.*;


public class Fraud {
    @CsvRecurse
    private Merchant merchant;

    @CsvRecurse
    private Cardholder cardholder;

    @CsvRecurse
    private Transaction transaction;

    public Merchant getMerchant() {
        return merchant;
    }

    public Cardholder getCardholder() {
        return cardholder;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public String toString() {
        return "Fraud{" +
                "merchant=" + merchant +
                ", cardholder=" + cardholder +
                ", creditCard=" + transaction +
                '}';
    }
}