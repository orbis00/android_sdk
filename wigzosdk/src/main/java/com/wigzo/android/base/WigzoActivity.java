package com.wigzo.android.base;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class WigzoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        WigzoApplication.setAppContext(this);
    }
}