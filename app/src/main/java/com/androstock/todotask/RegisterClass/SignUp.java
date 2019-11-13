package com.androstock.todotask.RegisterClass;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.androstock.todotask.DatabaseHelper;
import com.androstock.todotask.R;

public class SignUp extends AppCompatActivity {
    AutoCompleteTextView firstname,lastname,email;
    EditText pass,cpass;
    Button signup;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstname = (AutoCompleteTextView)findViewById(R.id.fname);
        lastname = (AutoCompleteTextView)findViewById(R.id.lname);
        email = (AutoCompleteTextView)findViewById(R.id.emailsignup);
        pass = (EditText) findViewById(R.id.signuppassword);
        cpass = (EditText) findViewById(R.id.confirmpassword);
        signup = (Button) findViewById(R.id.signupform);

        databaseHelper = new DatabaseHelper(this);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFirstNameEmpty(firstname) && isLastNameEmpty(lastname)  && isEmailValid(email)
                         && isPasswordEmpty(pass) && isConfirmPasswordEmpty(cpass)) {
                    String first = firstname.getText().toString();
                    String last = lastname.getText().toString();
                    String em = email.getText().toString().trim();
                    String pas = pass.getText().toString();
                    String cpas = cpass.getText().toString().trim();


                    if (pas.equals(cpas)) {

                        if (databaseHelper.isEmailsame(em)) {
                            Toast.makeText(SignUp.this, "email already used", Toast.LENGTH_SHORT).show();


                        } else {

                            Toast.makeText(SignUp.this, "Signup Successful", Toast.LENGTH_SHORT).show();
                            //Here database takes values
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("FirstName", first);
                            contentValues.put("LastName", last);
                            contentValues.put("Email", em);
                            contentValues.put("Password", pas);
                            contentValues.put("ConfirmPassword", cpas);
                            databaseHelper.insertUser(contentValues);
                            finish();
                        }
                    } else {
                        Toast.makeText(SignUp.this, "Enter same passwords in both fields", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public boolean isFirstNameEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("Enter Your First Name");
            //password.setError("Enter Password");
            return false;
        }
    }

    public boolean isLastNameEmpty(EditText view) {
        if (view.getText().toString().length() > 0) {
            return true;
        } else {
            view.setError("Enter Your Last Name");
            //password.setError("Enter Password");
            return false;
        }
    }

    public boolean isEmailValid(EditText view) {
        if (view.getText().toString().length() > 0) {
            String value = view.getText().toString();
            if (Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
                return true;
            } else {
                view.setError("Enter Valid email");
                return false;
            }
        }
        return false;
    }

    public boolean isPasswordEmpty(EditText view) {
        if (view.getText().toString().length() > 4) {
            return true;
        } else {
            view.setError("password is too short");
            //password.setError("Enter Password");
            return false;
        }
    }

    public boolean isConfirmPasswordEmpty(EditText view) {
        if (view.getText().toString().length() > 4) {
            return true;
        } else {
            view.setError("enter confirm password");
            //password.setError("Enter Password");
            return false;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

}
