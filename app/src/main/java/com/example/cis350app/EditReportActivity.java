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
import java.util.Date;

public class EditReportActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private EditReportTask task = null;
    private String editItems = "";
    private EditText name;
    //private EditText date;
    private EditText subject;
    private EditText description;

@Override
protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editreport);

        name = (EditText) findViewById(R.id.edit_name);
       // date = (EditText) findViewById(R.id.edit_date);
        subject = (EditText) findViewById(R.id.edit_subject);
        description = (EditText) findViewById(R.id.edit_description);

        //profileTask = new profileInfoTask(LoginActivity.getsessionUserName());
        //profileTask.execute((Void) null);
        }

//to be changed
public void edit(View v){
        //concatenate changes to editItems
        if(name.getText().toString().length() > 0){
        editItems = editItems + "You changed your password to " + name.getText().toString() + ".";
        }

        if(subject.getText().toString().length() > 0){
        editItems = editItems + "You changed your gender to " + subject.getText().toString() + ".";
        }

        if(description.getText().toString().length() > 0){
        editItems = editItems + "You changed your school to " + description.getText().toString() + ".";
        }

        //if there are no changes, let toast know
        if(editItems.equals("")){
        Toast myToast =Toast.makeText(getApplicationContext(), "You made no changes.",Toast.LENGTH_LONG);
        myToast.setMargin(50,50);
        myToast.show();
        } else { //show the toast with changes
            Date daDate = new Date();
            Date rep_date = daDate;
        task =  new EditReportTask(LoginActivity.getsessionUserName(), name.getText().toString(),
        daDate, subject.getText().toString(), description.getText().toString());
        task.execute((Void) null);
        }
        }





public static class EditReportTask extends AsyncTask<Void, Void, String>{
    private final String mId;
    private final String mName;
    private final Date mDate;
    private final String mSubject;
    private final String mDescription;

    EditReportTask(String id, String name, Date date, String subject, String description)
    {
        mId = id;
        mName = name;
        mDate = date;
        mSubject = subject;
        mDescription = description;
    }

    @Override
    protected String doInBackground(Void...params){
        try{
            URL url = new URL("http://10.0.2.2:3000/editReport?id=" + mId + "&name=" + mName + "&Date=" + mDate
                    + "&Subject=" + mSubject + "&Description=" + mDescription);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();
            JSONObject jo = new JSONObject(msg);

            String result = jo.getString("result");
            return result;
        } catch(Exception e){
            return e.getMessage();
        }
    }

}
    public void home_button(View view) {
        startActivity(new Intent(EditReportActivity.this, HomeActivity.class));
    }
}
