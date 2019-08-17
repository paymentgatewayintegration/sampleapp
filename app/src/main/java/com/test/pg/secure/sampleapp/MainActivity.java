package com.test.pg.secure.sampleapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.test.pg.secure.pgsdkv4.PaymentParams;
import com.test.pg.secure.pgsdkv4.PaymentGatewayPaymentInitializer;
import com.test.pg.secure.pgsdkv4.PGConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ProgressBar pb;
    TextView transactionIdView;
    TextView transactionStatusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);


        Button clickButton = (Button) findViewById(R.id.ee);
        transactionIdView = (TextView) findViewById(R.id.transactionid);
        transactionStatusView = (TextView) findViewById(R.id.status);
        clickButton.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);

                Random rnd = new Random();
                int n = 100000 + rnd.nextInt(900000);
                SampleAppConstants.PG_ORDER_ID=Integer.toString(n);

                PaymentParams pgPaymentParams = new PaymentParams();
                pgPaymentParams.setAPiKey(SampleAppConstants.PG_API_KEY);
                pgPaymentParams.setAmount(SampleAppConstants.PG_AMOUNT);
                pgPaymentParams.setEmail(SampleAppConstants.PG_EMAIL);
                pgPaymentParams.setName(SampleAppConstants.PG_NAME);
                pgPaymentParams.setPhone(SampleAppConstants.PG_PHONE);
                pgPaymentParams.setOrderId(SampleAppConstants.PG_ORDER_ID);
                pgPaymentParams.setCurrency(SampleAppConstants.PG_CURRENCY);
                pgPaymentParams.setDescription(SampleAppConstants.PG_DESCRIPTION);
                pgPaymentParams.setCity(SampleAppConstants.PG_CITY);
                pgPaymentParams.setState(SampleAppConstants.PG_STATE);
                pgPaymentParams.setAddressLine1(SampleAppConstants.PG_ADD_1);
                pgPaymentParams.setAddressLine2(SampleAppConstants.PG_ADD_2);
                pgPaymentParams.setZipCode(SampleAppConstants.PG_ZIPCODE);
                pgPaymentParams.setCountry(SampleAppConstants.PG_COUNTRY);
                pgPaymentParams.setReturnUrl(SampleAppConstants.PG_RETURN_URL);
                pgPaymentParams.setMode(SampleAppConstants.PG_MODE);
                pgPaymentParams.setUdf1(SampleAppConstants.PG_UDF1);
                pgPaymentParams.setUdf2(SampleAppConstants.PG_UDF2);
                pgPaymentParams.setUdf3(SampleAppConstants.PG_UDF3);
                pgPaymentParams.setUdf4(SampleAppConstants.PG_UDF4);
                pgPaymentParams.setUdf5(SampleAppConstants.PG_UDF5);
                pgPaymentParams.setEnableAutoRefund("n");
                pgPaymentParams.setOfferCode("testcoupon");
                //pgPaymentParams.setSplitInfo("{\"vendors\":[{\"vendor_code\":\"24VEN985\",\"split_amount_percentage\":\"20\"}]}");

                PaymentGatewayPaymentInitializer pgPaymentInitialzer = new PaymentGatewayPaymentInitializer(pgPaymentParams,MainActivity.this);
                pgPaymentInitialzer.initiatePaymentProcess();

            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        pb.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PGConstants.REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK){
                try{
                    String paymentResponse=data.getStringExtra(PGConstants.PAYMENT_RESPONSE);
                    System.out.println("paymentResponse: "+paymentResponse);
                    if(paymentResponse.equals("null")){
                        System.out.println("Transaction Error!");
                        transactionIdView.setText("Transaction ID: NIL");
                        transactionStatusView.setText("Transaction Status: Transaction Error!");
                    }else{
                        JSONObject response = new JSONObject(paymentResponse);
                        transactionIdView.setText("Transaction ID: "+response.getString("transaction_id"));
                        transactionStatusView.setText("Transaction Status: "+response.getString("response_message"));
                    }

                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }

        }
    }

}
