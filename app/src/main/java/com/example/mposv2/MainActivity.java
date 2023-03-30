package com.example.mposv2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.mposv2.databinding.ActivityMainBinding;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    HashMap<String, String> response;
    MPOSService mposService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mposService = MPOSService.getInstance(this);
        checkDeviceAvailability();

        binding.connectBut.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                    return;
                }
            } else {
                MPOSService.getInstance(this).showDeviceListDialog();
            }

        });
        binding.nextBut.setOnClickListener(v -> {
            checkLastTransactionResult();
        });
    }

    public void checkLastTransactionResult() {
        //startActivity(new Intent(getApplicationContext(), PayActivity.class));

        mposService.getLastTransactionResult(new MPOSServiceCallback() {
            @Override
            public void onComplete(int i, String s, Object result) {
                HashMap<String, String> response = mposService.parseTransactionResponse((String) result);
                binding.orderNumber.setText(response.get("AdditionalData"));
                binding.textApproverCode.setText(response.get("ApprovalCode"));
                binding.textTERMINALSTATUSCODE.setText(response.get("TerminalStatusCode"));
                binding.textAmount.setText(response.get("Amount"));
                binding.textRRN.setText(response.get("RRN"));
            }

            @Override
            public void onFailed(int i, String s) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void checkDeviceAvailability() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_DENIED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_CONNECT}, 2);
                return;
            } else {
                mposService.connectToDevice(new MPOSServiceCallback() {
                    @Override
                    public void onComplete(int i, String s, Object o) {
                        binding.statusTxt.setText(s);
                        binding.connectBut.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailed(int i, String s) {
                        binding.connectBut.setVisibility(View.VISIBLE);
                    }
                });
            }
        } else {
            mposService.connectToDevice(new MPOSServiceCallback() {
                @Override
                public void onComplete(int i, String s, Object o) {
                    binding.statusTxt.setText(s);
                    binding.connectBut.setVisibility(View.GONE);
                }

                @Override
                public void onFailed(int i, String s) {
                    binding.connectBut.setVisibility(View.VISIBLE);

                }
            });
        }
    }
}