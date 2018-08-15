package com.kreators.mybizkios;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kreators.mybizkios.report.ReportView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Integer moduletype;

    Button btndept, btnpartner, btnitem, btnutility, btnpos, btnsalesret, btnsalesrep;
    Class cls;
    UnitGlobal ug = new UnitGlobal();
    UnitConstant uc;
    DBAdapter dba;
    Helper helper;

    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = Helper.getInstance();
        helper.scaleImage(this);

        dba = DBAdapter.getInstance(this);

        btndept = (Button) findViewById(R.id.btndept);
        btnitem = (Button) findViewById(R.id.btnitem);
        btnpartner = (Button) findViewById(R.id.btnpartner);
        btnutility = (Button) findViewById(R.id.btnutility);
        btnpos = (Button) findViewById(R.id.btnpos);
        btnsalesret = (Button) findViewById(R.id.btnsalesret);
        btnsalesrep = (Button) findViewById(R.id.btnsalesrep);

        btndept.setOnClickListener(this);
        btnpartner.setOnClickListener(this);
        btnitem.setOnClickListener(this);
        btnutility.setOnClickListener(this);
        btnpos.setOnClickListener(this);
        btnsalesret.setOnClickListener(this);
        btnsalesrep.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String querycust, querycusttype;
        Cursor ccust, ccusttype;
        Boolean click = false;
        switch(v.getId()){
            case R.id.btndept:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_DEPT;
                click = true;
                break;
            case R.id.btnpartner:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_PARTNER;
                click = true;
                break;
            case R.id.btnitem:
                cls = MasterFolderActivity.class;
                moduletype = uc.TT_MASTER_ITEM;
                Cursor c = dba.loadLocalTableFromRawQuery("select count(id) from dbmdepartment");

                if(c.moveToFirst() && c != null)
                    if(c.getInt(0) > 0)click = true;
                else Toast.makeText(this, "Please have at least 1 item dept.", Toast.LENGTH_SHORT).show();

                c.close();

                break;
            case R.id.btnpos:

                querycust = "select count(*) as jumRec from dbmpartner where status = 0";
                querycusttype = "select count(*) as jumRec from dbmcusttype where status = 0";

                ccust = dba.loadLocalTableFromRawQuery(querycust);
                ccusttype = dba.loadLocalTableFromRawQuery(querycusttype);

                if ((ccust != null ) && (ccust.moveToFirst()) && (ccust.getInt(0) > 0) &&
                        (ccusttype != null ) && (ccusttype.moveToFirst()) && (ccusttype.getInt(0) > 0)) {
                    cls = PosActivity.class;
                    moduletype = 0;
                    click = true;
                } else {
                    click = false;
                    Toast.makeText(getBaseContext(), "Cust / cust type masih kosong, mohon diisi lebih dulu.", Toast.LENGTH_SHORT).show();
                }
                ccust.close();
                ccusttype.close();

                break;
            case R.id.btnsalesret:

                querycust = "select count(*) as jumRec from dbmpartner where status = 0";
                querycusttype = "select count(*) as jumRec from dbmcusttype where status = 0";

                ccust = dba.loadLocalTableFromRawQuery(querycust);
                ccusttype = dba.loadLocalTableFromRawQuery(querycusttype);

                if ((ccust != null ) && (ccust.moveToFirst()) && (ccust.getInt(0) > 0) &&
                        (ccusttype != null ) && (ccusttype.moveToFirst()) && (ccusttype.getInt(0) > 0)) {
                    cls = SalesReturnActivity.class;
                    moduletype = uc.TT_SALES_RETURN;
                    click = true;
                } else {
                    click = false;
                    Toast.makeText(getBaseContext(), "Cust / cust type masih kosong, mohon diisi lebih dulu.", Toast.LENGTH_SHORT).show();
                }
                ccust.close();
                ccusttype.close();

                break;
            case R.id.btnutility:
                cls = UtilityActivity.class;
                moduletype = 0;
                click = true;
                break;
            case R.id.btnsalesrep:
                cls = ReportView.class;
                moduletype = 101;
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
