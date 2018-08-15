package com.kreators.mybizkios;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MasterFolderActivity  extends AppCompatActivity implements View.OnClickListener {

    Button btnnew, btnedit, btnexit;
    TextView tcode, tname, headername, tdepartment, ttype, titemunit, tminqty;
    Integer moduletype;
    String query;
    UnitConstant uc;
    UnitGlobal ug = new UnitGlobal();
    DBAdapter dba;
    Cursor c;
    Class cls;

    MasterAdapter masterAdpt;
    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();
    List<ObjEntity> detailList = new ArrayList<ObjEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_folder);
        Helper.getInstance().scaleImage(this);

        btnnew = (Button) findViewById(R.id.btnnew);
        btnedit = (Button) findViewById(R.id.btnedit);
        btnexit = (Button) findViewById(R.id.btnexit);

        headername = (TextView) findViewById(R.id.headername);
        tcode = (TextView) findViewById(R.id.tcode);
        tname = (TextView) findViewById(R.id.tname);
        tdepartment = (TextView) findViewById(R.id.tdepartment);
        ttype = (TextView) findViewById(R.id.ttype);
        titemunit = (TextView) findViewById(R.id.titemunit);
        tminqty = (TextView) findViewById(R.id.tminqty);

        btnnew.setOnClickListener(this);
        btnedit.setOnClickListener(this);
        btnexit.setOnClickListener(this);

        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        dba = DBAdapter.getInstance(this);

        initMaster();
    }


    private void initMaster(){
        tdepartment.setVisibility(View.GONE);
        ttype.setVisibility(View.GONE);
        titemunit.setVisibility(View.GONE);
        tminqty.setVisibility(View.GONE);

        if (moduletype == uc.TT_MASTER_PARTNER){
            ttype.setVisibility(View.VISIBLE);
            cls =PartnerEntryActivity.class;
            headername.setText("PARTNER");
            String strquerypartner = "SELECT a.id, a.code, a.name, b.code " +
                                     "FROM " + ug.tableName[moduletype] + " a left join dbmcusttype b on b.id = a.ctpId " +
                                     "ORDER by a.code";
            c = dba.loadLocalTableFromRawQuery(strquerypartner);
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), c.getString(3)));
                } while (c.moveToNext());
            }
        } else if(moduletype == uc.TT_MASTER_ITEM){
            tdepartment.setVisibility(View.VISIBLE);
            ttype.setVisibility(View.VISIBLE);
            cls = ItemEntryActivity.class;
            headername.setText("ITEM");

            c = dba.loadLocalTableFromRawQuery("SELECT item.id, item.code, item.name, item.type, dep.name depName  FROM dbmitem item LEFT JOIN dbmdepartment dep ON " +
                    "item.depId = dep.id order by item.code");
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), uc.ITE_TYPE_NAME[c.getInt(3)], c.getString(4)));
                } while (c.moveToNext());
            }

        } else if(moduletype == uc.TT_MASTER_DEPT){
            cls = DepartmentEntryActivity.class;
            headername.setText("DEPARTMENT");
            c = dba.loadLocalTableFromRawQuery("SELECT id, code, name FROM "+ug.tableName[moduletype] + " order by code");
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2)));
                } while (c.moveToNext());
            }
        } else if(moduletype == uc.TT_MASTER_CUST_TYPE){
            cls = CustomerTypeEntryActivity.class;
            headername.setText("CUSTOMER CATEGORY");
            c = dba.loadLocalTableFromRawQuery("SELECT id, code, status FROM "+ug.tableName[moduletype] + " order by code");
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), uc.ITE_STATUS_NAME[c.getInt(2)]));
                } while (c.moveToNext());
            }
        } else if (moduletype == uc.TT_MASTER_ACCOUNTS){
            ttype.setVisibility(View.VISIBLE);
            cls = CashBankEntryActivity.class;
            headername.setText("CASH/BANK");
            c = dba.loadLocalTableFromRawQuery("SELECT id, code, name, isBank FROM "+ug.tableName[moduletype] + " order by code");
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), uc.ACCOUNT_TYPE_NAME[c.getInt(3)]));
                } while (c.moveToNext());
            }
        } else if (moduletype == uc.TT_MASTER_USER){
            ttype.setVisibility(View.VISIBLE);
            ttype.setText("EMAIL");
            cls = UserEntryActivity.class;
            headername.setText("USER");
            c = dba.loadLocalTableFromRawQuery("SELECT id, code, name, email FROM "+ug.tableName[moduletype] + " order by code");
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), c.getString(3)));
                } while (c.moveToNext());
            }
        } else if (moduletype == uc.TT_MASTER_SESSION){
            ttype.setVisibility(View.VISIBLE);
            tname.setText("Open Time");
            ttype.setText("Close Time");
            cls = SessionEntryActivity.class;
            headername.setText("SESSION");

            c = dba.loadLocalTableFromRawQuery("SELECT id, code, openTime, closeTime FROM "+ug.tableName[moduletype] + " order by code");
            if ((c != null ) && (c.moveToFirst())) {
                do {
                    detailList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2),c.getString(3)));
                } while (c.moveToNext());
            }
        }

        ((Button)findViewById(R.id.btnedit)).setVisibility(View.GONE);

        masterAdpt = new MasterAdapter(getApplicationContext(), R.layout.listview_master_folder, detailList, moduletype);
        ListView lvdetail = (ListView) findViewById(R.id.itemlist);
        lvdetail.setAdapter(masterAdpt);

        lvdetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                Log.d("MasterFolder", "btn edit on clicked");
                ObjEntity tmpobj = masterAdpt.detailList.get(arg2);
                startModule(uc.M_EDIT, tmpobj.getId());
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnnew:
                boolean status = true;
                if (moduletype == uc.TT_MASTER_CUST_TYPE){
                    Cursor c = dba.loadLocalTableFromRawQuery("SELECT count(code) as code FROM "+ug.tableName[moduletype]);
                    if ((c != null ) && (c.moveToFirst())) {
                        do {
                            if(c.getInt(0) > 4 ){
                                Toast.makeText(this, "Create new customer category is not allowed, because max category is 4", Toast.LENGTH_SHORT).show();
                                status = false;
                            }
                        } while (c.moveToNext());
                    }

                }
                if (status){
                    startModule(uc.M_NEW, 0);
                }

                break;
            case R.id.btnedit:

                break;
            case R.id.btnexit:
                finish();
                break;
            default:
                break;
        }
    }

    private void startModule(Integer act, Integer parmid){
        finish();
        intentFieldList.add("moduletype");
        intentFieldList.add("act");
        intentFieldList.add("id");
        intentvalueList.add(moduletype.toString());
        intentvalueList.add(String.valueOf(act));
        intentvalueList.add(String.valueOf(parmid));

        ug.OpenIntent(this, cls, intentFieldList, intentvalueList);
    }
}
