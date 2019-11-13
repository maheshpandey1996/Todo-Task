package com.androstock.todotask.RegisterInterface;

public interface RegisterView {

    void showProgress(boolean showProgress);

    void setUserNameError(int messageResId);

    void setPasswordError(int messageResId);

    void successAction();
}
