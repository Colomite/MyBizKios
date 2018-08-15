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


public class CashBankEntryActivity extends AppCompatActivity implements View.OnClickListener {

    String head;
    Integer moduletype, act, positionStatus, positionType, idData;

    Button btnsave, btncancel;
    TextView headername;
    EditText edtcode, edtname, edtnotes;
    Spinner edtstatus, edttype;
    LinearLayout laydet;

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
        setContentView(R.layout.activity_entry_cashbank);
        Helper.getInstance().scaleImage(this);

        dba = DBAdapter.getInstance(this);

        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        headername = (TextView) findViewById(R.id.headername);

        edtcode = (EditText) findViewById(R.id.edtcode);
        edtname = (EditText) findViewById(R.id.edtname);
        edtnotes = (EditText) findViewById(R.id.edtnotes);
        edtstatus = (Spinner) findViewById(R.id.edtstatus);
        edttype = (Spinner) findViewById(R.id.edttype);

        laydet = (LinearLayout) findViewById(R.id.layoutdetail);

        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);

        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        act = Integer.valueOf(getIntent().getExtras().getString("act"));
        idData = Integer.valueOf(getIntent().getExtras().getString("id"));

        setSpinner();

        if (act == uc.M_NEW){
            head = "(NEW)";
        } else {
            head = "(EDIT)";

            String strquery = "SELECT a.id, a.code, a.name, a.isBank, a.notes, a.status " +
                    "FROM dbmaccount a WHERE a.id = " + idData;
            Cursor c = dba.loadLocalTableFromRawQuery(strquery);
            if ((c != null ) && (c.moveToFirst())) {
                edtcode.setText(c.getString(1));
                edtname.setText(c.getString(2));
                edttype.setSelection(c.getInt(3));
                edtnotes.setText(c.getString(4));
                edtstatus.setSelection(c.getInt(5));
                Log.d("Cash Bank Entry", "Type : " + c.getInt(3));
            }
            c.close();

            edtcode.setEnabled(false);

            //cek apakah sudah pernah di pake di trans apa belon, kalo sudah isbank nya didisabled
            String stracctrans = "SELECT count(*) as jumRec " +
                    "FROM dbtamtpaymentdoc a " +
                    "WHERE a.void = 0 and a.accId = " + idData;
            Cursor ctmpacc = dba.loadLocalTableFromRawQuery(stracctrans);
            if ((ctmpacc != null ) && (ctmpacc.moveToFirst()) && (ctmpacc.getInt(0) > 0)) {
                edttype.setEnabled(false);
            }
            ctmpacc.close();
        }
        headername.setText("CASH/BANK "+head);

        setField();

    }

    private void setSpinner(){
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

        List<SpinnerWithTag> spinType = new ArrayList<SpinnerWithTag>();
        spinType.add(new SpinnerWithTag(UnitConstant.ACCOUNT_TYPE_NAME[UnitConstant.ACCOUNT_TYPE_CASH], UnitConstant.ACCOUNT_TYPE_CASH));
        spinType.add(new SpinnerWithTag(UnitConstant.ACCOUNT_TYPE_NAME[UnitConstant.ACCOUNT_TYPE_BANK], UnitConstant.ACCOUNT_TYPE_BANK));

        ArrayAdapter<SpinnerWithTag> typeAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                android.R.layout.simple_spinner_item, spinType);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edttype.setAdapter(typeAdapter);

        edttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                positionType = s.tag;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void setField(){
        if (act == uc.M_NEW) {
            typeList.add(uc.CHECK_CODE_DOUBLE);
            typeList.add(uc.CHECK_EMPTY_FIELD);

            mustList.add("code");

            fieldList.add("code");
            fieldList.add("userCreateId");
        }

        fieldList.add("name");
        fieldList.add("isBank");
        fieldList.add("status");
        fieldList.add("notes");

        fieldList.add("isCredit");
        fieldList.add("isDebit");

    }
    private void insertData(){
        valueList.clear();
        int id;

        if (act == uc.M_NEW) {
            valueList.add(edtcode.getText().toString().toUpperCase());
            valueList.add("1");
        }

        valueList.add(edtname.getText().toString());
        valueList.add(positionType.toString());
        valueList.add(positionStatus.toString());
        valueList.add(edtnotes.getText().toString());

        valueList.add("1");
        valueList.add("1");

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
                        ug.clearForm(laydet);
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
        super.onBackPressed();
        close();
    }
}
