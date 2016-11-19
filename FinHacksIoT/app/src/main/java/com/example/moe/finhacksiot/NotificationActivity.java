package com.example.moe.finhacksiot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NotificationActivity extends AppCompatActivity {

    private Button mSendBtn;
    private EditText mMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mSendBtn = (Button) findViewById(R.id.sendNotifBtn);
        mMessageView = (EditText) findViewById(R.id.pebbleMessage);

        // Create a new dictionary
        final PebbleDictionary dict = new PebbleDictionary();

        // The key representing a contact name is being transmitted
        final int AppKeyContactName = 0;
        final int AppKeyAge = 1;

        // Get data from the app
        final String contactName = "Moe";
        final int age = 19;

        // Add data to the dictionary
        dict.addString(AppKeyContactName, contactName);
        dict.addInt32(AppKeyAge, age);

        final UUID appUuid = UUID.fromString("EC7EE5C6-8DDF-4089-AA84-C3396A11CC95");

        mSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the dictionary
//                PebbleKit.sendDataToPebble(getApplicationContext(), appUuid, dict);
                String message = mMessageView.getText().toString();
                SendMessage("E-Advisor says:",message);
                mMessageView.setText("");
            }
        });

    }

    public void SendMessage(String title, String body)
    {
        // Is the watch connected?
        boolean isConnected = PebbleKit.isWatchConnected(this);

        if(isConnected) {
            // Push a notification
            final Intent i = new Intent("com.getpebble.action.SEND_NOTIFICATION");

            final Map data = new HashMap();
            data.put("title", title);
            data.put("body", body);
            final JSONObject jsonData = new JSONObject(data);
            final String notificationData = new JSONArray().put(jsonData).toString();

            i.putExtra("messageType", "PEBBLE_ALERT");
            i.putExtra("sender", "PebbleKit Android");
            i.putExtra("notificationData", notificationData);
            sendBroadcast(i);
        }
    }
}
