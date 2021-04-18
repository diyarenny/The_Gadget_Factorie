package com.example.thegadgetfactorie.Model;

import android.text.TextUtils;

public class User implements IUserModel{
    public String username;
    public String email;
    public String password;

    //empty constructor
    public User(){

    }
    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int isValid() {
        if (getPassword().length() < 6) {
            return 0;
        }
        if (validatePasswrod(getPassword()) == true) {
            return 1;
        }
        if (TextUtils.isEmpty(getEmail())) {
            return 2;

        }
        if (TextUtils.isEmpty(getPassword())) {
            return 3;
        }
        else
            return -1;
    }

    public boolean validatePasswrod(String pas) {
        boolean valid = true;
        for (int i = 0; i < pas.length(); i++) {
            char ch = pas.charAt(i);
            if (ch == ' ') {
                valid = true;
                break;
            } else {
                valid = false;
            }
        }
        return valid;
    }
}
