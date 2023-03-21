package org.example;

import com.opencsv.bean.CsvBindByPosition;


public class Fraud {

    @CsvBindByPosition(position = 1)
    private String date_transaction;

    @CsvBindByPosition(position = 2)
    private String card_number;

    @CsvBindByPosition(position = 3)
    private String company_name;

    @CsvBindByPosition(position = 4)
    private String company_cat;

    @CsvBindByPosition(position = 5)
    private String amount;

    @CsvBindByPosition(position = 6)
    private String person_first_name;

    @CsvBindByPosition(position = 7)
    private String person_last_name;

    @CsvBindByPosition(position = 8)
    private String gender;

    @CsvBindByPosition(position = 9)
    private String street;

    @CsvBindByPosition(position = 10)
    private String city;

    @CsvBindByPosition(position = 11)
    private String state;

    @CsvBindByPosition(position = 12)
    private String zip;

    @CsvBindByPosition(position = 13)
    private String latitude_person;

    @CsvBindByPosition(position = 14)
    private String longitude_person;

    @CsvBindByPosition(position = 16)
    private String job;

    @CsvBindByPosition(position = 17)
    private String date_of_birth;

    @CsvBindByPosition(position = 18)
    private String transaction_number;

    @CsvBindByPosition(position = 19)
    private String unix_time;

    @CsvBindByPosition(position = 20)
    private String latitude_company;

    @CsvBindByPosition(position = 21)
    private String longitude_company;

    @CsvBindByPosition(position = 22)
    private String is_fraud;

    //  getters, setters, toString


    @Override
    public String toString() {
        return "Fraud{" +
                "date_transaction='" + date_transaction + '\'' +
                ", card_number='" + card_number + '\'' +
                ", company_name='" + company_name + '\'' +
                ", company_cat='" + company_cat + '\'' +
                ", amount='" + amount + '\'' +
                ", person_first_name='" + person_first_name + '\'' +
                ", person_last_name='" + person_last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", zip='" + zip + '\'' +
                ", lattitude_person='" + latitude_person + '\'' +
                ", longitude_person='" + longitude_person + '\'' +
                ", job='" + job + '\'' +
                ", date_of_birth='" + date_of_birth + '\'' +
                ", transaction_number='" + transaction_number + '\'' +
                ", unix_time='" + unix_time + '\'' +
                ", lattitude_company='" + latitude_company + '\'' +
                ", longitude_company='" + longitude_company + '\'' +
                ", is_fraud='" + is_fraud + '\'' +
                '}';
    }

    public String getDate_transaction() {
        String tmp[] = date_transaction.split(" ");
        return tmp[0] + "T" + tmp[1];
    }

    public String getCard_number() {
        return card_number;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getCompany_cat() {
        return company_cat;
    }

    public String getAmount() {
        return amount;
    }

    public String getPerson_first_name() {
        return person_first_name;
    }

    public String getPerson_last_name() {
        return person_last_name;
    }

    public String getGender() {
        return gender;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip() {
        return zip;
    }

    public String getLatitude_person() {
        return latitude_person;
    }

    public String getLongitude_person() {
        return longitude_person;
    }

    public String getJob() {
        return job;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getTransaction_number() {
        return transaction_number;
    }

    public String getUnix_time() {
        return unix_time;
    }

    public String getLatitude_company() {
        return latitude_company;
    }

    public String getLongitude_company() {
        return longitude_company;
    }

    public String getIs_fraud() {
        return is_fraud;
    }

}