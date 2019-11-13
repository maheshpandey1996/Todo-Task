package com.androstock.todotask.RegisterInterface;

public interface RegisterModel {

     interface OnRegisterFinishedListener{
        void onCancelled();

        void onSuccess();

        void onPasswordError();
    }

    void register(String username, String password, OnRegisterFinishedListener listener);
}
