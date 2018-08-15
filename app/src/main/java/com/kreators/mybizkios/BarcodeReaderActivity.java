package com.kreators.mybizkios;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodeReaderActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler
{
    private ZXingScannerView scanner;
    private List<BarcodeFormat> formats;

    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        scanner = new ZXingScannerView(this);
        formats = new ArrayList<>();

        initBarcodeFormats();
    }

    private void initBarcodeFormats()
    {
        formats.add(BarcodeFormat.DATA_MATRIX);
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.UPC_A);

        scanner.setFormats(formats);
        scanner.setResultHandler(this);
        setContentView(scanner);
        scanner.startCamera();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        scanner.stopCamera();
    }

    @Override
    public void handleResult(Result result)
    {
        Intent barcode = new Intent();
        barcode.putExtra("barcode", result.getText());
        setResult(Activity.RESULT_OK, barcode);
        finish();
    }
}

