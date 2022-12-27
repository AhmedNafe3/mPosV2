package com.example.mposv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mposv2.databinding.ActivityMainBinding;
import com.mpos.mpossdk.api.MPOSService;
import com.mpos.mpossdk.api.MPOSServiceCallback;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        MPOSService.getInstance(this).checkExistence(new MPOSServiceCallback() {
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
            startActivity(new Intent(this, PayActivity.class));
        });
    }



}