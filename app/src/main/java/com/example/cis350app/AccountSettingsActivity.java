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
import com.example.cis350app.data.SearchContent;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountSettingsActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private acctSettingTask task = null;
    private profileInfoTask profileTask = null;
    private String editItems = "";
    private EditText password;
    private EditText age;
    private EditText gender;
    private EditText school;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acctsettings);

        password = (EditText) findViewById(R.id.edit_password);
        age = (EditText) findViewById(R.id.edit_age);
        gender = (EditText) findViewById(R.id.edit_gender);
        school = (EditText) findViewById(R.id.edit_school);

        profileTask = new profileInfoTask(LoginActivity.getsessionUserName());
        profileTask.execute((Void) null);
    }

    //to be changed
    public void edit(View v){
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
            Toast myToast =Toast.makeText(getApplicationContext(), "You made no changes.",Toast.LENGTH_LONG);
            myToast.setMargin(50,50);
            myToast.show();
        } else { //show the toast with changes
            task =  new acctSettingTask(LoginActivity.getsessionUserName(), password.getText().toString(),
                    age.getText().toString(), gender.getText().toString(), school.getText().toString());
            task.execute((Void) null);
        }
    }

    public class profileInfoTask extends AsyncTask<Void, Void, String> {
        private final String mUsername;

        profileInfoTask(String username) {
            mUsername = username;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                System.out.println("line 91");
                URL url = new URL(
                        "http://10.0.2.2:3000/getInfo?username=" + mUsername);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();
                System.out.println("line 95");

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                String result = jo.getString("result");
                System.out.println("result: " + result);
                return result;
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            try{
                if (result.contains("error")) {
                    Toast myToast =Toast.makeText(getApplicationContext(), "There was an error in editing. Try again.",
                            Toast.LENGTH_LONG);
                    myToast.setMargin(50,50);
                    myToast.show();
                } else {
                    JSONObject resultOb = new JSONObject(result);
                    password.setHint("Password: " + resultOb.getString("password"));
                    school.setHint("School: " + resultOb.getString("school"));
                    age.setHint("Age: " +resultOb.getString("age"));
                    gender.setHint("Gender: " +resultOb.getString("gender"));
                }
            } catch (Exception e){
                Toast myToast =Toast.makeText(getApplicationContext(), "There was an exception in JSON: " + e,
                        Toast.LENGTH_LONG);
                myToast.setMargin(50,50);
                myToast.show();
            }
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
                        "http://10.0.2.2:3000/changeInfo?username=" + mUsername +
                                "&password=" + mPassword + "&school=" + mSchool + "&age=" + mAge +
                                "&gender=" + mGender);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
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
                Toast myToast =Toast.makeText(getApplicationContext(), editItems,Toast.LENGTH_LONG);
                myToast.setMargin(50,50);
                myToast.show();
            } else if (result.contains("error")) {
                Toast myToast =Toast.makeText(getApplicationContext(), "There was an error in editing. Try again.",
                        Toast.LENGTH_LONG);
                myToast.setMargin(50,50);
                myToast.show();
            }
        }

    }

}
