package com.vsb.ala.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vsb.ala.controller.ScannerAddController;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerAddActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    ZXingScannerView scannerView;
    ScannerAddController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
        controller = new ScannerAddController(this, this);

    }

    @Override
    public void handleResult(Result result) {


        NewProductActivity.edT_ks.setText("");

        controller.getProductByCode(result.getText());


        onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        scannerView.stopCamera();
        finish();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
        scannerView.setResultHandler(this);
        scannerView.startCamera();

    }


}
