package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cis350app.data.NotificationContent;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountSettingsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private acctSettingTask task = null;


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
            task =  new acctSettingTask(LoginActivity.getsessionUserName(), password.getText().toString(),
                    school.getText().toString(), age.getText().toString(), gender.getText().toString());
            task.execute((Void) null);
        }
    }

    public class acctSettingTask extends AsyncTask<Void, Void, String> {
        private final String mUsername;
        private final String mPassword;
        private final String mSchool;
        private final String mAge;
        private final String mGender;

        acctSettingTask(String username, String password, String age, String gender, String school) {
            mUsername = username;
            mPassword = password;
            mSchool = school;
            mAge = age;
            mGender = gender;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(
                        "http://10.0.2.2:3000/change_info?username=" + mUsername +
                                "&password=" + mPassword + "&school=" + mSchool + "&age=" + mAge +
                                "&gender=" + mGender);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                String result = jo.getString("result");
                return result;
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result.equals("edited")) {
                Toast myToast =Toast.makeText(getApplicationContext(), "Successfully edited!",Toast.LENGTH_SHORT);
                myToast.setMargin(50,50);
                myToast.show();
            } else if (result.equals("error")) {
                Toast myToast =Toast.makeText(getApplicationContext(), "There was an error in editing. Try again.",Toast.LENGTH_SHORT);
                myToast.setMargin(50,50);
                myToast.show();
            }
        }

    }

}
