package com.example.cis350app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AccountSettingsActivity extends AppCompatActivity {

    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acctsettings);
    }

    //to be changed
    public void edit(View v){
        String editItems = "";
        EditText password = (EditText) findViewById(R.id.edit_password);
        EditText age = (EditText) findViewById(R.id.edit_age);
        EditText gender = (EditText) findViewById(R.id.edit_gender);
        EditText school = (EditText) findViewById(R.id.edit_school);

        System.out.println("password.getText().toString().length() > 0)" + (password.getText().toString().length() > 0));
        System.out.println("age.getText().toString().length() > 0)" + (age.getText().toString().length() > 0));
        System.out.println("gender.getText().toString().length() > 0)" + (gender.getText().toString().length() > 0));
        System.out.println("school.getText().toString().length() > 0)" + (school.getText().toString().length() > 0));

        //concatenate changes to editItems
        if(password.getText().toString().length() > 0){
            editItems = editItems + "You changed your password to " + password.getText().toString() + ".";
        }

        if(age.getText().toString().length() > 0){
            editItems = editItems + "You changed your age to " + age.getText().toString() + ".";
        }

        if(gender.getText().toString().length() > 0){
            editItems = editItems + "You changed your gender to " + gender.getText().toString() + ".";
        }

        if(school.getText().toString().length() > 0){
            editItems = editItems + "You changed your school to " + school.getText().toString() + ".";
        }

        //if there are no changes, let toast know
        if(editItems.equals("")){
            Toast myToast =Toast.makeText(getApplicationContext(), "You made no changes.",Toast.LENGTH_SHORT);
            myToast.setMargin(50,50);
            myToast.show();
        } else { //show the toast with changes
            Toast myToast =Toast.makeText(getApplicationContext(),editItems,Toast.LENGTH_SHORT);
            myToast.setMargin(50,50);
            myToast.show();
        }
    }

}
