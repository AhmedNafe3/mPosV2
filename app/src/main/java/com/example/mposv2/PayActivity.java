package com.example.mposv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mposv2.databinding.ActivityPayBinding;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;

public class PayActivity extends AppCompatActivity {
    ActivityPayBinding binding;
    MPOSServiceCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button3.setOnClickListener(v -> {
            requestTransaction();
        });

    }

    private void requestTransaction() {
        String xmlRequest = "<TransactionRequest>" + "<Command>SALE</Command>" + "<Amount>100</Amount>" + "<Cashback></Cashback>" + "<MaskedPAN></MaskedPAN>" + "<PrintFlag>01</PrintFlag>" + "<Phone></Phone>" + "<Email></Email>" + "<UserId></UserId>" + "<DeviceId></DeviceId>" + "<RRN></RRN>" + "<AuthCode></AuthCode>" + "<AdditionalData>1020304050</AdditionalData>" + "</TransactionRequest>";

        MPOSService.getInstance(this).startTransaction(xmlRequest, new MPOSServiceCallback() {
            @Override
            public void onComplete(int status, String message, Object result) {
                binding.status.setText(message);

            }

            @Override
            public void onFailed(int status, String message) {
                binding.status.setText(message);

            }
        });
    }


}