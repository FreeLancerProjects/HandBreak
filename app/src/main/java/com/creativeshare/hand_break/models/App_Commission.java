package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class App_Commission implements Serializable {
    private double per;
private List<Data> data;
    public double getPer() {
        return per;
    }

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable{
        private String account_id;
        private String account_name;
        private String account_IBAN;
        private String account_bank_name;
        private String account_number;

        public String getAccount_id() {
            return account_id;
        }

        public String getAccount_name() {
            return account_name;
        }

        public String getAccount_IBAN() {
            return account_IBAN;
        }

        public String getAccount_bank_name() {
            return account_bank_name;
        }

        public String getAccount_number() {
            return account_number;
        }
    }
}
