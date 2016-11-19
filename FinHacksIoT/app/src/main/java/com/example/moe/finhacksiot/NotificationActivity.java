package com.example.moe.finhacksiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class NotificationActivity extends AppCompatActivity {

    private Button mSendBtn;
    private EditText mMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mSendBtn = (Button) findViewById(R.id.sendNotifBtn);
        mMessageView = (EditText) findViewById(R.id.pebbleMessage);
        
    }
}
