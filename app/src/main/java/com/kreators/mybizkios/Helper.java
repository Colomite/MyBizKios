package com.kreators.mybizkios;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Display;

public class Helper
{
    private static Helper instance;

    private int loginUserId;
    private int sessionId;

    //variable untuk background pada tiap activity, sebenarnya ga perlu, cuma ketika di lollipop, ga muncul semua gambarnya
    //jadi harus dimanual scale sesuai ukuran layar
    private Display display;
    private Bitmap bg;
    private Bitmap scaled;
    private Point size;
    private int width;
    private int height;
    private Drawable drawable;

    //bluetooth device untuk prenter
    private BluetoothDevice btDevice;
    private BluetoothPrinter printer;

    private Helper()
    {
        size = new Point();
    }

    public static Helper getInstance()
    {
        if(instance == null) instance = new Helper();
        return instance;
    }

    public void setLoginUserId(int id)
    {
        loginUserId = id;
    }

    public void setSessionId(int id)
    {
        sessionId = id;
    }

    public int getLoginUserId()
    {
        return loginUserId;
    }

    public int getSessionId()
    {
        return sessionId;
    }

    public void setDisplay(Display display, Context ctx)
    {
        if(drawable != null) return;
        display.getSize(size);
        width = size.x;
        height = size.y;
        bg = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.bg);
        scaled = Bitmap.createScaledBitmap(bg, width, height, true);
        drawable = new BitmapDrawable(ctx.getResources(), scaled);
    }

    public void setBtDevice(BluetoothDevice device)
    {
        btDevice = device;
        if(printer != null) printer.finish();
        printer = new BluetoothPrinter(device);

        printer.connectPrinter(new PrinterConnectListener() {
            @Override
            public void onConnected() {
            }

            @Override
            public void onFailed() {
                printer = null;
            }
        });
    }

    public void setPrinter(BluetoothPrinter printer)
    {
        this.printer = printer;
    }

    public void scaleImage(Activity activity)
    {
        activity.getWindow().getDecorView().getRootView().setBackground(drawable);
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public BluetoothDevice getBtDevice() {
        return btDevice;
    }

    public boolean isBtPrinterConnected()
    {
        if(printer != null)
            return printer.isConnected();

        return false;
    }

    public BluetoothPrinter getPrinter()
    {
        return printer;
    }
}
