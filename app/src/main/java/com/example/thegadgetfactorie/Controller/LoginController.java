package com.example.thegadgetfactorie.Controller;

import com.example.thegadgetfactorie.Model.User;
import com.example.thegadgetfactorie.View.ILoginView;

public class LoginController implements ILoginController{
    ILoginView loginView;
    public LoginController(ILoginView loginView) {
        this.loginView = loginView;
    }

    @Override
    public void OnLogin(String email, String password) {

        User user = new User(email,password);
        int logincode = user.isValid();
        if (logincode == 0) {

            loginView.OnLoginError("password is less than 6 characters");
        }  if (logincode == 1) {

            loginView.OnLoginError("Whitespace not allowed in password ");
        } if (logincode == 2) {
            loginView.OnLoginError("Email is Required");
        }  if (logincode == 3) {
            loginView.OnLoginError("Password is Required");
        } else {
            loginView.OnLoginSuccess("login Successful");
        }


    }
}
