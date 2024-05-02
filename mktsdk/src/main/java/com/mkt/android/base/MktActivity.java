package com.mkt.android.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MktActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MktApplication.setAppContext(this);
    }
}