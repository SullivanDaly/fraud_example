package com.typedb.examples.fraud.dao;

import org.example.Fraud;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class CreditCareDAO {
    private final List<Fraud> lFraud;
    private final HashMap<String, Integer> hNumberCardholder;
    private final HashMap<String, Integer> hNumberMerchant;
    public CreditCareDAO(List<Fraud> lFraud, HashMap<String, Integer> hNumberCardholder, HashMap<String, Integer> hNumberMerchant) {
        this.hNumberCardholder = hNumberCardholder;
        this.hNumberMerchant = hNumberMerchant;
        this.lFraud = lFraud;
    }

    public String get_insert_query(){
        StringBuilder query = new StringBuilder("");
        int current = 0;
        // for (Map.Entry<String, Cardholder> currentHash : hCardholder.entrySet()) {
        for(Fraud currentFraud : lFraud){

            query.append("$car").append(current).append(" isa Card, has card_number ").append(currentFraud.getCreditCare().getCard_number()).append(";\n");

            int numberCardHolder = hNumberCardholder.get(currentFraud.getCardholder().getPerson_first_name() + currentFraud.getCardholder().getPerson_last_name());
            int random = ThreadLocalRandom.current().nextInt(0, 4);

            query.append("$r1").append(current).append("(owner: $per").append(numberCardHolder)
                .append(", attached_card: $car").append(current).append(", attached_bank: $ban").append(random).append(") isa bank_account;\n");

            int numberMerchant = hNumberMerchant.get(currentFraud.getMerchant().getCompany_name());

            query.append("$r2").append(current).append("(used_card: $car").append(current).append(", to: $com").append(numberMerchant).append(") isa transaction");
                query.append(", has timestamp ").append(currentFraud.getCreditCare().getDate_transaction());
                query.append(", has amount ").append(currentFraud.getCreditCare().getAmount());
                query.append(", has transaction_number '").append(currentFraud.getCreditCare().getTransaction_number()).append("';\n");

            current++;
        }

        return query.toString();
    }
}
