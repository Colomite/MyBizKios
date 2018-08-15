package com.kreators.mybizkios;

import android.app.TimePickerDialog;
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
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SessionEntryActivity extends AppCompatActivity implements View.OnClickListener {

    String head;
    Integer moduletype, act, positionStatus, positionType;

    Button btnsave, btncancel;
    TextView headername;
    EditText edtcode, edtopentime, edtclosetime, edtnotes;
    Spinner edtstatus;
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
        setContentView(R.layout.activity_entry_session);
        Helper.getInstance().scaleImage(this);

        dba = DBAdapter.getInstance(this);

        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        headername = (TextView) findViewById(R.id.headername);

        edtcode = (EditText) findViewById(R.id.edtcode);
        edtopentime = (EditText) findViewById(R.id.edtopentime);
        edtnotes = (EditText) findViewById(R.id.edtnotes);
        edtstatus = (Spinner) findViewById(R.id.edtstatus);
        edtclosetime = (EditText) findViewById(R.id.edtclosetime);

        laydet = (LinearLayout) findViewById(R.id.layoutdetail);

        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        edtopentime.setOnClickListener(this);
        edtclosetime.setOnClickListener(this);

        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        act = Integer.valueOf(getIntent().getExtras().getString("act"));

        if (act == uc.M_NEW)
        {
            head = "(NEW)";
        }
        else
        {
            head = "(EDIT)";
            edtcode.setEnabled(false);

            c = dba.loadLocalTableFromRawQuery("SELECT id, code, openTime, closeTime, status FROM " +
                    ug.tableName[uc.TT_MASTER_SESSION] +
                    " where id = " + getIntent().getExtras().getString("id"));

            if ((c != null ) && (c.moveToFirst()))
            {
                edtcode.setText(c.getString(1));
                edtopentime.setText(c.getString(2));
                edtclosetime.setText(c.getString(3));
                edtstatus.setSelection(c.getInt(4));
            }
            c.close();
        }
        headername.setText("SESSION "+head);

        setSpinner();
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
    }

    private void setField(){
        if(act == uc.M_NEW)
            typeList.add(uc.CHECK_CODE_DOUBLE);
        typeList.add(uc.CHECK_EMPTY_FIELD);

        mustList.add("code");

        fieldList.add("code");
        fieldList.add("openTime");
        fieldList.add("closeTime");
        fieldList.add("status");
        fieldList.add("notes");

        fieldList.add("locId");
        fieldList.add("userAccessId");

    }
    private void insertData(boolean newData){
        valueList.clear();
        int id;
        valueList.add(edtcode.getText().toString());
        valueList.add(edtopentime.getText().toString());
        valueList.add(edtclosetime.getText().toString());
        valueList.add(positionStatus.toString());
        valueList.add(edtnotes.getText().toString());

        valueList.add("1");
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
                    ug.clearForm(laydet);
                } else {
                    Toast.makeText(this, "Failed. Contact the administrator", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            Log.d("lognya ", e.getMessage());
            Toast.makeText(this, "Failed. Contact the administrator ", Toast.LENGTH_SHORT).show();
        }
    }

    private void dialogTime(final EditText edttime){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);

        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(SessionEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                edttime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnsave:
                if(head != "SESSION (EDIT)") insertData(false);
                else insertData(true);
                break;
            case R.id.edtopentime:
                dialogTime(edtopentime);
                break;
            case R.id.edtclosetime:
                dialogTime(edtclosetime);
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
