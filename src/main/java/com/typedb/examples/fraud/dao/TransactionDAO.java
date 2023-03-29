package com.typedb.examples.fraud.dao;

import com.typedb.examples.fraud.model.Merchant;
import com.typedb.examples.fraud.model.Transaction;
import org.example.TypeDB_SessionWrapper;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class TransactionDAO {
    private final TypeDB_SessionWrapper wrapper;

    private final String query1 = "match \n"
            + "$per isa Person, has first_name \"%s\", has last_name \"%s\";\n"
            + "$ban isa Bank, has name \"%s\";\n"
            + "$car isa Card, has card_number %s;\n"
            + "$com isa Company, has name \"%s\";\n";

    private final String query2 = "insert \n"
            +   "$r1 (owner: $per, attached_card: $car, attached_bank: $ban) isa bank_account;\n"
            +   "$r2 (used_card: $car ,to: $com) isa transaction, has timestamp %s, has amount %s, "
            +   "has transaction_number \"%s\";";

    public TransactionDAO(TypeDB_SessionWrapper wrapper) {
        this.wrapper = wrapper;
    }

    private String getQueryStr(Transaction currentTransaction){
        String result = query1.formatted(
                    currentTransaction.getCardholder().getPerson_first_name(),
                    currentTransaction.getCardholder().getPerson_last_name(),
                    currentTransaction.getCardholder().getCreditCard().getBank().getBank_name(),
                    currentTransaction.getCardholder().getCreditCard().getCard_number(),
                    currentTransaction.getMerchant().getCompany_name()
            );
            result += query2.formatted(
                    currentTransaction.getDate_transaction(),
                    currentTransaction.getAmount(),
                    currentTransaction.getTransaction_number()
            );
        return result;
    }

    public void insert_all(Set<Transaction> lTransaction) throws IOException {
        Set<String> queryStrs = lTransaction.stream().map(this::getQueryStr).collect(Collectors.toSet());
        wrapper.load_data(queryStrs);
    }
}

