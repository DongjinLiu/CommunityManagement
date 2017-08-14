package com.example.jin.communitymanagement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this,"傻逼",Toast.LENGTH_SHORT).show();


        Toast.makeText(this,"test",Toast.LENGTH_SHORT).show();
    }
}
