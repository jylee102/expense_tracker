package com.expenseTracker.config;

import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class JasyptConfigTest {
    public String key = "haseo_key"; //암복화에 필요한 키

    @Test
    void stringEncryptor() {
        String url = "";
        String userName = "";
        String password = "";

        String mail_host = "";
        String mail_port = "";
        String mail_username = "";
        String mail_password = "";

        System.out.println("En_url : " + jasyptEncoding(url));
        System.out.println("En_username : " + jasyptEncoding(userName));
        System.out.println("En_password : " + jasyptEncoding(password));

        System.out.println("mail_host: " + jasyptEncoding(mail_host));
        System.out.println("mail_port: " + jasyptEncoding(mail_port));
        System.out.println("mail_username: " + jasyptEncoding(mail_username));
        System.out.println("mail_password: " + jasyptEncoding(mail_password));
    }

    @Test
    void stringDecryptor() {
        String url = null;
        String userName = null;
        String password = null;

        System.out.println("De_url : " + jasyptDecoding(url));
        System.out.println("De_username : " + jasyptDecoding(userName));
        System.out.println("De_password : " + jasyptDecoding(password));
    }

    public String jasyptEncoding(String value) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.encrypt(value);
    }

    public String jasyptDecoding(String value) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setAlgorithm("PBEWithMD5AndDES");
        pbeEnc.setPassword(key);
        return pbeEnc.decrypt(value);
    }
}
