package org.example;

import com.opencsv.bean.CsvRecurse;
import com.typedb.examples.fraud.model.*;


public class Fraud {
    @CsvRecurse
    private Merchant merchant;

    @CsvRecurse
    private Cardholder cardholder;

    @CsvRecurse
    private Address address;

    @CsvRecurse
    private CardholderCoordinates cardholderCoordinates;

    @CsvRecurse
    private MerchantCoordinates merchantCoordinates;

    @CsvRecurse
    private CreditCare creditCare;

    public Merchant getMerchant() {
        return merchant;
    }

    public Cardholder getCardholder() {
        return cardholder;
    }

    public Address getAddress() {
        return address;
    }

    public CardholderCoordinates getCardholderCoordinates() {
        return cardholderCoordinates;
    }

    public MerchantCoordinates getMerchantCoordinates() {
        return merchantCoordinates;
    }

    public CreditCare getCreditCare() {
        return creditCare;
    }

    @Override
    public String toString() {
        return "Fraud{" +
                "merchant=" + merchant +
                ", cardholder=" + cardholder +
                ", address=" + address +
                ", cardholderCoordinates=" + cardholderCoordinates +
                ", merchantCoordinates=" + merchantCoordinates +
                ", creditCare=" + creditCare +
                '}';
    }
}