package com.kreators.mybizkios;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class BluetoothPrinterActivity extends AppCompatActivity
{
    private final int BLUETOOTH_REQUEST = 101;
    private ListView listviewBluetooth;
    private Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_printer);

        setup();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if(mBluetoothAdapter == null)
        {
            Intent btPrinter = new Intent();
            btPrinter.putExtra("connected", false);
            setResult(Activity.RESULT_OK, btPrinter);
            finish();
        }

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, BLUETOOTH_REQUEST);
        }
        else connectPrinter();
    }

    private void setup()
    {
        listviewBluetooth = (ListView)findViewById(R.id.listviewBluetooth);
        helper = Helper.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK)
        {
            if (requestCode == BLUETOOTH_REQUEST)
            {
                connectPrinter();
            }
        }
    }

    private void connectPrinter()
    {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        final Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if(pairedDevices.size() > 0)
        {
            final ArrayList<String> listItems = new ArrayList<String>();
            //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
            ArrayAdapter<String> adapter;

            for (BluetoothDevice device : pairedDevices)
            {
                listItems.add(device.getName());
            }

            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
            listviewBluetooth.setAdapter(adapter);
            listviewBluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(BluetoothPrinterActivity.this, listItems.get(i), Toast.LENGTH_SHORT).show();
                    //yeey, device selected
                    final Intent btPrinter = new Intent();
                    //helper.setBtDevice((BluetoothDevice) pairedDevices.toArray()[i]);

                    final BluetoothPrinter printer = new BluetoothPrinter((BluetoothDevice) pairedDevices.toArray()[i]);

                    Toast.makeText(BluetoothPrinterActivity.this, "Please wait", Toast.LENGTH_SHORT).show();

                    printer.connectPrinter(new PrinterConnectListener() {
                        @Override
                        public void onConnected() {
                            helper.setPrinter(printer);
                            btPrinter.putExtra("connected", true);
                            setResult(Activity.RESULT_OK, btPrinter);
                            finish();
                        }

                        @Override
                        public void onFailed() {
                            btPrinter.putExtra("connected", false);
                            setResult(Activity.RESULT_OK, btPrinter);
                            finish();
                        }
                    });

                }
            });
            adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(this, "No device connected!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
