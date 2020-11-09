package es.source.code.activity;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MyAuthenticator extends Authenticator {
    String UserName=null;
    String PassWord=null;

    public MyAuthenticator(){
    }
    public MyAuthenticator(String username, String password) {
        this.UserName = username;
        this.PassWord = password;
    }
    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(UserName, PassWord);
    }
}
