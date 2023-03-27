package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.CreditCard;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDAO {
    private final List<CreditCard> lCreditCard;
    private final TypeDB_SessionWrapper wrapper;
    private final String query = "insert \n"
            + "$car isa Card, has card_number %s;";

    public CreditCardDAO(TypeDB_SessionWrapper wrapper, List<CreditCard> lCreditCard) {
        this.lCreditCard = lCreditCard;
        this.wrapper = wrapper;
    }

    private List<String> get_insert_query(){

        String result = "";
        List<String> lResult = new ArrayList<String>();

        for (CreditCard currentCreditCard : lCreditCard) {
            result = query.formatted(currentCreditCard.getCard_number());
            lResult.add(result);
        }
        return lResult;
    }

    public void insert_all() throws IOException {
        wrapper.load_data(get_insert_query());
    }

    public List<CreditCard> getlCreditCard() {
        return lCreditCard;
    }

}
