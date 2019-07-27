package com.nightowldevelopers.levelup;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.inapplib.InAppDataModel;
import com.inapplib.InAppPurchase;
import com.inapplib.OnPaymentListener;
import com.util.Purchase;


public class MainActivity extends AppCompatActivity implements OnPaymentListener {

    private static final String TAG = "MainActivity";
    private InAppPurchase inAppPurchase;
    private String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArtKkh+YssGTH6hXsgoj1opFsj3kRWA2SOcIClK/G++OSkioU/QEVoeik2KqIiS9c+ed8LLq3qHZfi8ynpgBAG/biI5V/3Vp+tp9+Ly7yGOc1jj710UPJLJ7ptRgj1bdd78PDz4CX1vIcWwd0aDwC9rqBWB7j75TXqsGaJB5fSkH3PalJBMVCRMZzT7h+itixv3DFlv+bS2JBlYyu76fqwalAcm8RrlhnZ7LOy5VbGflLttrjJ393Sd7hSdcf/pqjki3qVIxdOwN8UM6aVNN55ox2SkOxt1GbacHFws2vmUDHAn7MqMQDf4feJprQBPUKVPb7LjM0meKETnvSBticIwIDAQAB";
    private String SKU = "donate1";
    private String payLoad = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inAppPurchase = new InAppPurchase(MainActivity.this, base64EncodedPublicKey,
                SKU, payLoad, this);
        inAppPurchase.setUpInApp();

        findViewById(R.id.btnPurchase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inAppPurchase.subscriptionPurchase();
            }
        });
    }

    @Override
    public void onSuccessPayment() {
        Toast.makeText(this, "Success Purchase", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSuccessPaymentDetails(InAppDataModel model) {
        Log.e(TAG,"SKU : "+model.getPackageName());
        Log.e(TAG,"Order ID : "+model.getOrderId());
    }

    @Override
    public void onFailurePayment(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAlreadyPurchase(Purchase purchase) {
        Log.e(TAG,"Already purchase : "+purchase.getSku());
        findViewById(R.id.btnPurchase).setVisibility(View.GONE);
        Toast.makeText(this, "onAlreadyPurchase " +purchase.getSku(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (InAppPurchase.RC_REQUEST == requestCode)
            inAppPurchase.InAppDetails(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        inAppPurchase.unregisterReceiver();
    }
}
