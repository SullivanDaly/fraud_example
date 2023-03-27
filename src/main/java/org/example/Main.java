package org.example;

import com.typedb.examples.fraud.dao.*;
import com.typedb.examples.fraud.model.Cardholder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        TypeDB_SessionWrapper typeDBw = new TypeDB_SessionWrapper();
        typeDBw.init_connection();

        typeDBw.load_schema();

        CardholderDAO cardholderDAO = new CardholderDAO(typeDBw, typeDBw.getlCardholder());
        MerchantDAO merchantDAO = new MerchantDAO(typeDBw, typeDBw.getlMerchant());
        BankDAO bankDAO = new BankDAO(typeDBw, typeDBw.getlBank());
        CreditCardDAO creditCardDAO = new CreditCardDAO(typeDBw, typeDBw.getlCreditCard());
        TransactionDAO transactionDAO = new TransactionDAO(typeDBw, typeDBw.getlTransaction());

        cardholderDAO.insert_all();
        merchantDAO.insert_all();
        bankDAO.insert_all();
        creditCardDAO.insert_all();
        transactionDAO.insert_all();

        typeDBw.close_connection();
    }
}