package com.example.moe.finhacksiot;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PayActivity extends AppCompatActivity {

    private TextView mItemTxt;
    private TextView mPriceTxt;
    private ImageView mImageView;
    private Button mNFCPayBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

        mItemTxt = (TextView) findViewById(R.id.ItemTxt);
        mPriceTxt = (TextView) findViewById(R.id.PriceTxt);
        mImageView = (ImageView) findViewById(R.id.itemImg);
        mNFCPayBtn = (Button) findViewById(R.id.NFCPayBtn);

    }
}
