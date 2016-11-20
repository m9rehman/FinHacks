package com.example.moe.finhacksiot;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PayActivity extends AppCompatActivity {

    private TextView mItemTxt;
    private TextView mPriceTxt;
    private ImageView mImageView;
    private Button mNFCPayBtn;

    public static final String MIME_TEXT_PLAIN = "text/plain";
    private NfcAdapter mNfcAdapter;

    private static final String TAG = PayActivity.class.getSimpleName();

    //TODO: Save the instance state
    private int mbalance=5000;
    private int mprice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mItemTxt = (TextView) findViewById(R.id.ItemTxt);
        mImageView = (ImageView) findViewById(R.id.itemImg);
        mNFCPayBtn = (Button) findViewById(R.id.NFCPayBtn);
        mPriceTxt = (TextView) findViewById(R.id.PriceTxt);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            mPriceTxt.setText("NFC is disabled.");
        } else {
            mPriceTxt.setText("Enabled");
        }

        handleIntent(getIntent());

        //---------Pebble---------
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

        mNFCPayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Send the dictionary
//                PebbleKit.sendDataToPebble(getApplicationContext(), appUuid, dict);
               mbalance -= mprice;
                SendMessage("E-Advisor says:","$"+String.valueOf(mprice) +
                        " spent." + "Balance: " + "$"+String.valueOf(mbalance)  );
                mprice = 0;

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mprice = Integer.parseInt(result);
                //TODO: Fucntion for Item name
                mItemTxt.setText("Item");
                mPriceTxt.setText("$ "+String.valueOf(mprice));
            }
        }
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }


    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    //---------------PEBBLE------------------------------------

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







