package org.example;

import com.opencsv.bean.CsvRecurse;
import com.typedb.examples.fraud.model.*;


public class Fraud {
    @CsvRecurse
    private Merchant merchant;

    @CsvRecurse
    private Cardholder cardholder;

    @CsvRecurse
    private CreditCare creditCare;

    public Merchant getMerchant() {
        return merchant;
    }

    public Cardholder getCardholder() {
        return cardholder;
    }

    public CreditCare getCreditCare() {
        return creditCare;
    }

    @Override
    public String toString() {
        return "Fraud{" +
                "merchant=" + merchant +
                ", cardholder=" + cardholder +
                ", creditCare=" + creditCare +
                '}';
    }
}