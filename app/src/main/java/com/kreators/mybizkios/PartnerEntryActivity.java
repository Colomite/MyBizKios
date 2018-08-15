package com.kreators.mybizkios;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class PartnerEntryActivity extends AppCompatActivity implements View.OnClickListener {

    String head;
    Integer moduletype, act, positionStatus, positionCust, idData;

    Button btnsave, btncancel, btndetail, btnaddress;
    TextView headername;
    EditText edtcode, edtname, edtnpwp, edtaddress, edtcity, edtprovince, edtcountry, edtpostalcode, edtphone1, edtphone2, edtfax, edtemail;
    Spinner edtstatus, edtcustcat;
    LinearLayout lindetail, linaddress;

    List<Integer> typeList = new ArrayList<Integer>();
    List<String> mustList = new ArrayList<String>();
    List<String> fieldList = new ArrayList<String>();
    List<String> valueList = new ArrayList<String>();
    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();

    UnitConstant uc = new UnitConstant();
    UnitGlobal ug = new UnitGlobal();
    DBAdapter dba;
    Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_partner);
        Helper.getInstance().scaleImage(this);

        dba = DBAdapter.getInstance(this);

        btndetail = (Button) findViewById(R.id.btndetail);
        btnaddress = (Button) findViewById(R.id.btnaddress);
        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        headername = (TextView) findViewById(R.id.headername);

        edtcode = (EditText) findViewById(R.id.edtcode);
        edtname = (EditText) findViewById(R.id.edtname);
        edtnpwp = (EditText) findViewById(R.id.edtnpwp);
        edtaddress = (EditText) findViewById(R.id.edtaddress);
        edtcity = (EditText) findViewById(R.id.edtcity);
        edtprovince = (EditText) findViewById(R.id.edtprovince);
        edtcountry = (EditText) findViewById(R.id.edtcountry);
        edtpostalcode = (EditText) findViewById(R.id.edtpostalcode);
        edtphone1 = (EditText) findViewById(R.id.edtphone1);
        edtphone2 = (EditText) findViewById(R.id.edtphone2);
        edtfax = (EditText) findViewById(R.id.edtfax);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtstatus = (Spinner) findViewById(R.id.edtstatus);
        edtcustcat = (Spinner) findViewById(R.id.edtcustcat);

        lindetail = (LinearLayout) findViewById(R.id.layoutdetail);
        linaddress = (LinearLayout) findViewById(R.id.layoutaddress);

        linaddress.setVisibility(View.GONE);

        btndetail.setOnClickListener(this);
        btnaddress.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);

        List<SpinnerWithTag> spinCustcat = new ArrayList<SpinnerWithTag>();
        c = dba.loadLocalTableFromRawQuery("SELECT id, code, status FROM dbmcusttype");
        if ((c != null ) && (c.moveToFirst())) {
            do {
                spinCustcat.add(new SpinnerWithTag(c.getString(1), c.getInt(0)));

            } while (c.moveToNext());
        }

        ArrayAdapter<SpinnerWithTag> custAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                android.R.layout.simple_spinner_item, spinCustcat);

        custAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtcustcat.setAdapter(custAdapter);

        edtcustcat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                positionCust = s.tag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        act = Integer.valueOf(getIntent().getExtras().getString("act"));
        idData = Integer.valueOf(getIntent().getExtras().getString("id"));

        setSpinner();

        if (act == uc.M_NEW){
            head = "(NEW)";
        } else {
            head = "(EDIT)";

            String strquery = "SELECT a.id, a.code, a.name, a.npwp, b.code, a.status, " + //5
                                    "a.address, a.city, a.province, a.country, a.postalCode, a.phone1, a.phone2, " + //12
                                    "a.fax, a.email " +
                              "FROM dbmpartner a left join dbmcusttype b on b.id = a.ctpId WHERE a.id = " + idData;
            Cursor c = dba.loadLocalTableFromRawQuery(strquery);
            if ((c != null ) && (c.moveToFirst())) {
                edtcode.setText(c.getString(1));
                edtname.setText(c.getString(2));
                edtnpwp.setText(c.getString(3));
                Log.d("PartnerEntryActivity","Cust Type ID : " + c.getInt(4));

                int tmppos = ug.getIndexByString(edtcustcat, c.getString(4));

                edtcustcat.setSelection(tmppos);
                edtstatus.setSelection(c.getInt(5));

                edtaddress.setText(c.getString(6));
                edtcity.setText(c.getString(7));
                edtprovince.setText(c.getString(8));
                edtcountry.setText(c.getString(9));
                edtpostalcode.setText(c.getString(10));
                edtphone1.setText(c.getString(11));
                edtphone2.setText(c.getString(12));
                edtfax.setText(c.getString(13));
                edtemail.setText(c.getString(14));

                edtcode.setEnabled(false);

                /*
                //pengecekan untuk partner yg sudah digunakan dlm transaksi, maka cust type nya nda boleh di ubah ...
                String strpartnertrans = "SELECT count(*) as jumRec " +
                        "FROM dbtsalesdoc a WHERE a.void = 0 and a.partnerId = " + idData;
                Cursor ctmppartner = dba.loadLocalTableFromRawQuery(strpartnertrans);
                if ((ctmppartner != null ) && (ctmppartner.moveToFirst()) && (ctmppartner.getInt(0) > 0)) {
                    edtcustcat.setEnabled(false);
                }
                ctmppartner.close();
                */
            }
            c.close();

            edtcode.setEnabled(false);
        }
        headername.setText("PARTNER "+head);

        setField();

    }

    private void setSpinner() {
        List<SpinnerWithTag> spinStatus = new ArrayList<SpinnerWithTag>();
        spinStatus.add(new SpinnerWithTag(UnitConstant.ITE_STATUS_NAME[UnitConstant.ITE_STATUS_ACTIVE], UnitConstant.ITE_STATUS_ACTIVE));
        spinStatus.add(new SpinnerWithTag(UnitConstant.ITE_STATUS_NAME[UnitConstant.ITE_STATUS_NONACTIVE], UnitConstant.ITE_STATUS_NONACTIVE));

        ArrayAdapter<SpinnerWithTag> statusAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                android.R.layout.simple_spinner_item, spinStatus);

        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtstatus.setAdapter(statusAdapter);

        edtstatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                positionStatus = s.tag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setField(){

        if (act == uc.M_NEW){
            typeList.add(uc.CHECK_CODE_DOUBLE);
            typeList.add(uc.CHECK_EMPTY_FIELD);

            mustList.add("code");

            fieldList.add("code");
            fieldList.add("userCreateId");
        }

        fieldList.add("ctpId");
        fieldList.add("name");
        fieldList.add("npwp");
        fieldList.add("address");
        fieldList.add("city");
        fieldList.add("province");
        fieldList.add("country");
        fieldList.add("postalCode");
        fieldList.add("phone1");
        fieldList.add("phone2");
        fieldList.add("fax");
        fieldList.add("email");
        fieldList.add("status");

        fieldList.add("empId");
        fieldList.add("type");
        fieldList.add("arLimit");
        fieldList.add("arTerm");
        fieldList.add("apLimit");
        fieldList.add("apTerm");


    }
    private void insertData() {
        valueList.clear();
        int id;

        if (act == uc.M_NEW){
            valueList.add(edtcode.getText().toString().toUpperCase());
            valueList.add("1");
        }

        valueList.add(positionCust.toString());
        valueList.add(edtname.getText().toString());
        valueList.add(edtnpwp.getText().toString());
        valueList.add(edtaddress.getText().toString());
        valueList.add(edtcity.getText().toString());
        valueList.add(edtprovince.getText().toString());
        valueList.add(edtcountry.getText().toString());
        valueList.add(edtpostalcode.getText().toString());
        valueList.add(edtphone1.getText().toString());
        valueList.add(edtphone2.getText().toString());
        valueList.add(edtfax.getText().toString());
        valueList.add(edtemail.getText().toString());
        valueList.add(positionStatus.toString());

        Log.d("PosActivity", "positionStatus : " + positionStatus);

        valueList.add("0");
        valueList.add(String.valueOf(uc.PARTNER_TYPE_CUSTOMER));
        valueList.add("0");
        valueList.add("0");
        valueList.add("0");
        valueList.add("0");


        //kalau waktu save gagal dan ga keluar errornya di compiler, try catchnya di komen aja. Kalau udah itu dihidupin lagi ya...
        try {
            if(ug.checkValidData(this, moduletype, typeList, mustList, fieldList, valueList, dba)){
                if (act == uc.M_NEW) {
                    id = ug.insertMasterData(ug.tableName[moduletype], fieldList, valueList, dba);
                } else {
                    id = ug.updateMasterData(ug.tableName[moduletype], fieldList, valueList, dba, idData);
                }
                if (id > 0){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    if (act == uc.M_NEW) {
                        ug.clearForm(linaddress);
                        ug.clearForm(lindetail);
                    } else {
                        close();
                    }
                } else {
                    Toast.makeText(this, "Failed. Contact to the administrator", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            Log.d("lognya ", e.getMessage());
            Toast.makeText(this, "Failed. Contact to the administrator ", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btndetail:
                lindetail.setVisibility(View.VISIBLE);
                linaddress.setVisibility(View.GONE);
                break;
            case R.id.btnaddress:
                lindetail.setVisibility(View.GONE);
                linaddress.setVisibility(View.VISIBLE);
                break;
            case R.id.btnsave:
                insertData();
                break;
            default:
                close();
                break;
        }
    }

    private void close(){
        finish();
        intentFieldList.add("moduletype");
        intentvalueList.add(moduletype.toString());
        ug.OpenIntent(this, MasterFolderActivity.class, intentFieldList, intentvalueList);
    }

    @Override
    public void onBackPressed(){
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        close();
    }
}
