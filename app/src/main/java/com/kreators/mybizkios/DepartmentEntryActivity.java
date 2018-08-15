package com.kreators.mybizkios;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DepartmentEntryActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnsave, btncancel;
    EditText edtcode, edtname;
    String head;
    Integer moduletype, act, idData;
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
        setContentView(R.layout.activity_entry_department);
        Helper.getInstance().scaleImage(this);

        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        headername = (TextView) findViewById(R.id.headername);
        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        edtcode = (EditText) findViewById(R.id.edtcode);
        edtname = (EditText) findViewById(R.id.edtname);
        entryLay = (RelativeLayout) findViewById(R.id.activity_entry);

        idData = 0;
        act = Integer.valueOf(getIntent().getExtras().getString("act"));
        idData = Integer.valueOf(getIntent().getExtras().getString("id"));

        dba = DBAdapter.getInstance(this);

        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        if (act == uc.M_NEW){
            head = "(NEW)";
        } else {
            head = "(EDIT)";
            String strquery = "SELECT id, code, name FROM dbmdepartment WHERE id = " + idData;
            Cursor c = dba.loadLocalTableFromRawQuery(strquery);
            if ((c != null ) && (c.moveToFirst())) {
                edtcode.setText(c.getString(1));
                edtname.setText(c.getString(2));
            }
            c.close();

            edtcode.setEnabled(false);
        }
        headername.setText("DEPARTMENT "+head);
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

        fieldList.add("name");
        fieldList.add("status");


    }

    private void insertData(){
        int id;
        valueList.clear();

        if (act == uc.M_NEW) {
            valueList.add(edtcode.getText().toString().toUpperCase());
            valueList.add("1");
        }
        valueList.add(edtname.getText().toString());
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
                        ug.clearForm(entryLay);
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
