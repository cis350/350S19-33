package com.example.cis350app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import com.example.cis350app.data.SearchContent;
import com.example.cis350app.data.SearchContent.Profile;
import android.widget.*;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminprofile);

        Profile profileInfo = (Profile) getIntent().getSerializableExtra("AdminName");

        //change the values of the textviews based on layview clicked on / aka admin
        TextView nameText = (TextView)findViewById(R.id.nameText);
        nameText.setText(profileInfo.name, TextView.BufferType.EDITABLE);

        TextView roleText = (TextView)findViewById(R.id.roleText);
        roleText.setText(profileInfo.role, TextView.BufferType.EDITABLE);

        TextView genderText = (TextView)findViewById(R.id.genderText);
        genderText.setText(profileInfo.gender, TextView.BufferType.EDITABLE);

        TextView phoneText = (TextView)findViewById(R.id.phoneText);
        phoneText.setText(profileInfo.phone, TextView.BufferType.EDITABLE);

        TextView emailText = (TextView)findViewById(R.id.emailText);
        emailText.setText(profileInfo.email, TextView.BufferType.EDITABLE);

        TextView officeText = (TextView)findViewById(R.id.locationText);
        officeText.setText(profileInfo.location, TextView.BufferType.EDITABLE);
    }
}