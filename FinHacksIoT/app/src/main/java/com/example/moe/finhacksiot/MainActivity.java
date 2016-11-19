package com.example.moe.finhacksiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button mFinanceBtn;
    private Button mLocationBtn;
    private Button mAnalysisBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFinanceBtn = (Button) findViewById(R.id.graphBtn);
        mLocationBtn = (Button) findViewById(R.id.locationBtn);
        mAnalysisBtn = (Button) findViewById(R.id.analysisBtn);

        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(i);
            }
        });


    }
}
