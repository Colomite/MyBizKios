package com.kreators.mybizkios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class UtilityActivity extends AppCompatActivity implements View.OnClickListener {

    Integer moduletype;

    Button btncustcat, btnuser, btncashbank, btnsession;
    ImageView btnpos;
    Class cls;
    UnitGlobal ug = new UnitGlobal();
    UnitConstant uc;

    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);
        Helper.getInstance().scaleImage(this);

        btncustcat = (Button) findViewById(R.id.btncustcat);
        btnuser = (Button) findViewById(R.id.btnuser);
        btncashbank = (Button) findViewById(R.id.btncashbank);
        btnsession = (Button) findViewById(R.id.btnsession);

        btncustcat.setOnClickListener(this);
        btnuser.setOnClickListener(this);
        btncashbank.setOnClickListener(this);
        btnsession.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Boolean click = false;
        switch(v.getId()){
            case R.id.btncustcat:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_CUST_TYPE;
                click = true;
                break;
            case R.id.btnuser:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_USER;
                click = true;
                break;
            case R.id.btncashbank:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_ACCOUNTS;
                click = true;
                break;
            case R.id.btnsession:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_SESSION;
                click = true;
                break;
            default:
                break;
        }

        if (click)
            startModule();
    }

    private void startModule(){
        intentFieldList.add("moduletype");
        intentvalueList.add(moduletype.toString());
        ug.OpenIntent(this, cls, intentFieldList, intentvalueList);
    }
}
