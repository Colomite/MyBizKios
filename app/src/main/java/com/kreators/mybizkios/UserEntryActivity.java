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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class UserEntryActivity extends AppCompatActivity implements View.OnClickListener {

    String head;
    Integer moduletype, act, positionStatus;

    Button btnsave, btncancel, btndetail, btnaccount;
    TextView headername;
    EditText edtcode, edtname, edtpassword, edtconfpassword, edtemail, edtnotes;
    Spinner edtstatus;
    LinearLayout lindetail, linaccount;

    List<Integer> typeList = new ArrayList<Integer>();
    List<String> mustList = new ArrayList<String>();
    List<String> fieldList = new ArrayList<String>();
    List<String> valueList = new ArrayList<String>();
    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();

    List<ObjEntity> accountList;

    CheckboxAdapter masterAdpt;
    UnitConstant uc = new UnitConstant();
    UnitGlobal ug = new UnitGlobal();
    DBAdapter dba;
    Cursor c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_user);
        Helper.getInstance().scaleImage(this);

        dba = DBAdapter.getInstance(this);

        btndetail = (Button) findViewById(R.id.btndetail);
        btnaccount = (Button) findViewById(R.id.btnaccount);
        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        headername = (TextView) findViewById(R.id.headername);

        edtcode = (EditText) findViewById(R.id.edtcode);
        edtname = (EditText) findViewById(R.id.edtname);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        edtconfpassword = (EditText) findViewById(R.id.edtconfpassword);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtnotes = (EditText) findViewById(R.id.edtnotes);
        edtstatus = (Spinner) findViewById(R.id.edtstatus);

        lindetail = (LinearLayout) findViewById(R.id.layoutdetail);
        linaccount = (LinearLayout) findViewById(R.id.layoutaccount);

        linaccount.setVisibility(View.GONE);

        btndetail.setOnClickListener(this);
        btnaccount.setOnClickListener(this);
        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);

        accountList =  new ArrayList<ObjEntity>();

        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        act = Integer.valueOf(getIntent().getExtras().getString("act"));

        if (act == uc.M_NEW)
        {
            head = "(NEW)";
        }
        else
        {
            head = "(EDIT)";
            c = dba.loadLocalTableFromRawQuery("SELECT id, code, password, name, email, status FROM "+
                    ug.tableName[moduletype] + " where id = " + getIntent().getExtras().getString("id"));
            if ((c != null ) && (c.moveToFirst()))
            {
                edtcode.setEnabled(false);

                edtcode.setText(c.getString(1));
                edtpassword.setText(c.getString(2));
                edtconfpassword.setText(c.getString(2));
                edtname.setText(c.getString(3));
                edtemail.setText(c.getString(4));
                edtstatus.setSelection(c.getInt(5));
            }
            c.close();
        }

        headername.setText("USER "+head);

        setSpinner();
        setField();
        setListAccount();

    }

    private void setListAccount(){
        String sql, ada;

        sql = "SELECT a.id, a.code, a.name, b.id ada FROM dbmaccount a LEFT JOIN dbsaccuse b ON a.id = b.accId";
        c = dba.loadLocalTableFromRawQuery(sql);
        if ((c != null) && (c.moveToFirst())) {
            do {
                if (c.getInt(3) > 0){
                    ada = "ada";
                } else {
                    ada = "";
                }
                accountList.add(new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), ada));

            } while (c.moveToNext());
        }
        masterAdpt = new CheckboxAdapter(getApplicationContext(), R.layout.listview_account, accountList);
        ListView lvdetail = (ListView) findViewById(R.id.lvaccount);
        lvdetail.setAdapter(masterAdpt);
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


    }

    private void setField(){
        if(act == uc.M_NEW)
            typeList.add(uc.CHECK_CODE_DOUBLE);
        typeList.add(uc.CHECK_EMPTY_FIELD);

        mustList.add("code");
        mustList.add("password");

        fieldList.add("code");
        fieldList.add("name");
        fieldList.add("password");
        fieldList.add("email");
        fieldList.add("notes");
        fieldList.add("status");

        fieldList.add("userAccessId");
    }

    private void insertData(){
        valueList.clear();
        int id;

        String email = edtemail.getText().toString();

        //check if email has valid format xxxx@xxxx.xxxx
        if(!email.matches("^[^@]+[@][^.]+[.][^.]+") && !email.equalsIgnoreCase(""))
        {
            Toast.makeText(this, "Email is invalid", Toast.LENGTH_SHORT).show();
            return;
        }

        valueList.add(edtcode.getText().toString());
        valueList.add(edtname.getText().toString());
        valueList.add(edtpassword.getText().toString());
        valueList.add(email);
        valueList.add(edtnotes.getText().toString());
        valueList.add(positionStatus.toString());

        valueList.add("0");

        //kalau waktu save gagal dan ga keluar errornya di compiler, try catchnya di komen aja. Kalau udah itu dihidupin lagi ya...
        try {
            if(ug.checkValidData(this, moduletype, typeList, mustList, fieldList, valueList, dba)){
                if(act == uc.M_NEW)
                    id = ug.insertMasterData(ug.tableName[moduletype], fieldList, valueList, dba);
                else if (act == uc.M_EDIT)
                    id = ug.updateMasterData(ug.tableName[moduletype], fieldList, valueList, dba, Integer.parseInt(getIntent().getExtras().getString("id")));
                else id = 0;

                if (id > 0){
                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    ug.clearForm(linaccount);
                    ug.clearForm(lindetail);
                } else {
                    Toast.makeText(this, "Failed. Contact the administrator", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            Log.d("lognya ", e.getMessage());
            Toast.makeText(this, "Failed. Contact the administrator", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btndetail:
                lindetail.setVisibility(View.VISIBLE);
                linaccount.setVisibility(View.GONE);
                break;
            case R.id.btnaccount:
                lindetail.setVisibility(View.GONE);
                linaccount.setVisibility(View.VISIBLE);
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
