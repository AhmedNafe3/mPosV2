package com.example.mposv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mposv2.databinding.ActivityPayBinding;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;

public class PayActivity extends AppCompatActivity {
    ActivityPayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.button3.setOnClickListener(v -> {
            checkLastTransactionResult();
        });
    }


    private void requestTransaction() {
        String xmlRequest = "<TransactionRequest>" + "<Command>SALE</Command>" + "<Amount>100</Amount>" + "<Cashback></Cashback>" + "<MaskedPAN></MaskedPAN>" + "<PrintFlag>01</PrintFlag>" + "<Phone></Phone>" + "<Email></Email>" + "<UserId></UserId>" + "<DeviceId></DeviceId>" + "<RRN></RRN>" + "<AuthCode></AuthCode>" + "<AdditionalData></AdditionalData>" + "</TransactionRequest>";

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

    public void checkLastTransactionResult() {
        MPOSService.getInstance(this).getLastTransactionResult(new MPOSServiceCallback() {
            @Override
            public void onComplete(int i, String mes, Object result) {
                requestTransaction();
            }

            @Override
            public void onFailed(int i, String s) {

            }
        });
    }
}