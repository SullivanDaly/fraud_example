package com.typedb.examples.fraud.model;



public class Bank {

    private String bank_name;

    private String bank_cat;

    private Coordinates bankCoordinates;



    public String getBank_name() {
        return bank_name;
    }

    public String getBank_cat_cat() {
        return bank_cat;
    }

    public Coordinates getBankCoordinates() {
        return bankCoordinates;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bank_name='" + bank_name + '\'' +
                ", bank_cat='" + bank_cat + '\'' +
                ", bankCoordinates=" + bankCoordinates +
                '}';
    }
}
