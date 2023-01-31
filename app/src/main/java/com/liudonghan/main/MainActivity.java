package com.liudonghan.main;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.liudonghan.kit.AliPayUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AliPayUtils.getInstance().pay();
    }
}