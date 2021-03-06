package com.androstock.todotask.RegisterClass;

import android.text.TextUtils;

import com.androstock.todotask.LoginClass.LoginModelImpl;
import com.androstock.todotask.LoginInterface.LoginModel;
import com.androstock.todotask.LoginInterface.LoginPresenter;
import com.androstock.todotask.LoginInterface.LoginView;
import com.androstock.todotask.R;

public class RegisterPresenterImpl implements LoginPresenter, LoginModel.OnLoginFinishedListener{

    private LoginModel loginModel;
    private LoginView loginView;

    public RegisterPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }


    @Override
    public void validateCredentials(String username, String password) {
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            loginView.setPasswordError(R.string.error_invalid_password);
            return;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            loginView.setUserNameError(R.string.error_field_required);
            return;

        } else if (!isEmailValid(username)) {
            loginView.setUserNameError(R.string.error_invalid_email);
            return;

        }

        loginView.showProgress(true);
        loginModel.login(username, password, this);
    }



    @Override
    public void onCancelled() {
        loginView.showProgress(false);
    }

    @Override
    public void onSuccess() {
        loginView.showProgress(false);
        //loginView.successAction();
    }

    @Override
    public void onPasswordError() {
        loginView.showProgress(false);
        loginView.setUserNameError(R.string.error_incorrect_password);
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
