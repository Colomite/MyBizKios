package com.kreators.mybizkios;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnlogin;
    DBAdapter dba;
    Spinner sessionSpinnerLogin;
    Cursor c;
    UnitGlobal ug = new UnitGlobal();
    UnitConstant uc = new UnitConstant();
    EditText username;
    EditText password;
    Helper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setup();
        setSpinner();
    }

    private void setSpinner()
    {
        try {
            c = dba.loadLocalTableFromRawQuery("SELECT id, code, openTime, closeTime FROM " + ug.tableName[uc.TT_MASTER_SESSION] + " order by code");
            List<ObjEntity> tmp = new ArrayList<>();
            if ((c != null) && (c.moveToFirst())) {
                do {
                    tmp.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), c.getString(3)));
                } while (c.moveToNext());

                SessionLogin customAdapter = new SessionLogin(this, tmp);
                sessionSpinnerLogin.setAdapter(customAdapter);

                sessionSpinnerLogin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                        String code = ((TextView) view.findViewById(R.id.spinnerLoginSessionCode)).getText().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                c.close();
            }
            else
            {
                sessionSpinnerLogin.setVisibility(View.INVISIBLE);
                ((TextView)findViewById(R.id.labelsession)).setVisibility(View.INVISIBLE);
            }
        }
        catch (SQLException ex)
        {
            sessionSpinnerLogin.setVisibility(View.GONE);
            ((TextView)findViewById(R.id.labelsession)).setVisibility(View.GONE);
        }
    }

    private void setup()
    {
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.edtusername);
        password = (EditText)findViewById(R.id.edtpassword);
        btnlogin = (Button) findViewById(R.id.btnsignin);
        sessionSpinnerLogin = (Spinner) findViewById(R.id.spinnerLoginSession);

        btnlogin.setOnClickListener(this);

        dba = DBAdapter.getInstance(this);

        helper = Helper.getInstance();
        helper.setDisplay(getWindowManager().getDefaultDisplay(), this);
        helper.scaleImage(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnsignin:
                //Intent i =new Intent(getApplicationContext(),MainActivity.class);
                //startActivity(i);

                ObjEntity obj = (ObjEntity) sessionSpinnerLogin.getSelectedItem();
                helper.setLoginUserId(0);

                if(username.getText().toString().equalsIgnoreCase("") &&
                        password.getText().toString().equalsIgnoreCase(""))
                {
                    //set class helper
                    helper.setLoginUserId(-1);
                    //helper.setSessionId(obj.getId());
                }
                else
                {
                    c = dba.loadLocalTableFromRawQuery("SELECT id, code, password FROM dbmuser where lower(code) = '" + username.getText().toString().toLowerCase() + "' and password = '" + password.getText().toString() + "'");
                    if ((c != null ) && (c.moveToFirst())) {
                        //set class helper
                        helper.setLoginUserId(c.getInt(0));
                        //helper.setSessionId(obj.getId());
                    }
                    c.close();
                }

                if(helper.getLoginUserId() != 0)
                {
                    username.setText("");
                    password.setText("");
                    username.requestFocus();
                    ug.OpenIntent(this, MainActivity.class, null, null);
                }
                else Toast.makeText(this, "Username and or password is invalid", Toast.LENGTH_SHORT).show();

                break;
            default:
                break;
        }
    }
}
