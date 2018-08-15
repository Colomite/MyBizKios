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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CustomerTypeEntryActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnsave, btncancel;
    EditText edtcode, edtnotes;
    Spinner edtstatus;
    String head;
    Integer moduletype, act, positionStatus, idData;
    TextView headername;
    RelativeLayout entryLay;
    UnitConstant uc = new UnitConstant();
    UnitGlobal ug = new UnitGlobal();

    List<Integer> typeList = new ArrayList<Integer>();
    List<String> mustList = new ArrayList<String>();
    List<String> fieldList = new ArrayList<String>();
    List<String> valueList = new ArrayList<String>();
    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();

    DBAdapter dba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_customertype);
        Helper.getInstance().scaleImage(this);

        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        headername = (TextView) findViewById(R.id.headername);
        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        edtcode = (EditText) findViewById(R.id.edtcode);
        edtnotes = (EditText) findViewById(R.id.edtnotes);
        edtstatus = (Spinner) findViewById(R.id.edtstatus);

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

        entryLay = (RelativeLayout) findViewById(R.id.activity_entry);

        act = Integer.valueOf(getIntent().getExtras().getString("act"));
        idData = Integer.valueOf(getIntent().getExtras().getString("id"));

        dba = DBAdapter.getInstance(this);

        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        if (act == uc.M_NEW){
            head = "(NEW)";
        } else {
            head = "(EDIT)";

            String strquery = "SELECT id, code, notes, status FROM dbmcusttype WHERE id = " + idData;
            Cursor c = dba.loadLocalTableFromRawQuery(strquery);
            if ((c != null ) && (c.moveToFirst())) {
                edtcode.setText(c.getString(1));
                edtnotes.setText(c.getString(2));
                edtstatus.setSelection(c.getInt(3));
            }
            c.close();

            edtcode.setEnabled(false);
        }
        headername.setText("Customer Category "+head);
        setField();
    }

    private void setField(){
        if (act == uc.M_NEW){
            typeList.add(uc.CHECK_CODE_DOUBLE);
            typeList.add(uc.CHECK_EMPTY_FIELD);

            mustList.add("code");

            fieldList.add("code");
            fieldList.add("userCreateId");
        }

        fieldList.add("notes");
        fieldList.add("status");
    }

    private void insertData(){
        int id;
        boolean status = true;
        valueList.clear();
        if (act == uc.M_NEW) {
            valueList.add(edtcode.getText().toString().toUpperCase());
            valueList.add("1");
        }
        valueList.add(edtnotes.getText().toString());
        valueList.add(positionStatus.toString());

        //kalau waktu save gagal dan ga keluar errornya di compiler, try catchnya di komen aja. Kalau udah itu dihidupin lagi ya...
        try {
            if(ug.checkValidData(this, moduletype, typeList, mustList, fieldList, valueList, dba)){
                Cursor c = dba.loadLocalTableFromRawQuery("SELECT count(code) as code FROM "+ug.tableName[moduletype]);
                if ((c != null ) && (c.moveToFirst())) {
                    do {
                        if(c.getInt(0) > 4 ){
                            Toast.makeText(this, "Create new customer category is not allowed, because max category is 4", Toast.LENGTH_SHORT).show();
                            status = false;
                        }
                    } while (c.moveToNext());
                }
                if (status) {
                    if (act == uc.M_NEW) {
                        id = ug.insertMasterData(ug.tableName[moduletype], fieldList, valueList, dba);
                    } else {
                        id = ug.updateMasterData(ug.tableName[moduletype], fieldList, valueList, dba, idData);
                    }
                    if (id > 0) {
                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        if (act == uc.M_NEW) {
                            ug.clearForm(entryLay);
                        } else {
                            close();
                        }
                    } else {
                        Toast.makeText(this, "Failed. Contact to the administrator", Toast.LENGTH_SHORT).show();
                    }
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
            case R.id.btncancel:
                close();
                break;
            default:
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
