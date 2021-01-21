package com.rajendra.colourdrive;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((TextView)findViewById(R.id.username)).setText(Login.userData.getUsername());
        ((TextView)findViewById(R.id.password)).setText(Login.userData.getPassword());
        ((TextView)findViewById(R.id.id)).setText(Login.userData.getId());
        ((TextView)findViewById(R.id.name)).setText(Login.userData.getName());
        ((TextView)findViewById(R.id.age)).setText(Login.userData.getAge()+"");
        ((TextView)findViewById(R.id.city)).setText(Login.userData.getCity());
        ((TextView)findViewById(R.id.company)).setText(Login.userData.getCompany());
    }
}
