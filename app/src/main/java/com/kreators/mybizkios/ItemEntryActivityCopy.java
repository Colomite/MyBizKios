package com.kreators.mybizkios;

import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ItemEntryActivityCopy extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;
    final String filePath = Environment.getExternalStorageDirectory().toString() + "/MBKiosk/";

    Button btnsave, btncancel, btndetail, btnproperties, btnbrowse;
    String head;
    Float valuePrice;
    Integer moduletype, act, positionStatus, positionDep, positionItemtype, masterId, countType;

    TextView headername, t1, t2, t3, t4, t5;
    EditText edtcode, edtname, edtvendorcode, edt1,edt2,edt3,edt4,edt5;
    LinearLayout laydet, layprice, layb1,layb2,layb3,layb4,layb5;
    Spinner edtstatus, edtdep, edtitemtype;

    List<Integer> typeList = new ArrayList<Integer>();
    List<String> mustList = new ArrayList<String>();
    List<String> fieldList = new ArrayList<String>();
    List<String> valueList = new ArrayList<String>();
    List<String> priceList = new ArrayList<String>();
    List<String> unitList = new ArrayList<String>();
    List<String> valuePriceList = new ArrayList<String>();
    List<String> valueUnitList = new ArrayList<String>();
    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();


    List<Integer> custtypeList = new ArrayList<Integer>();

    ImageView imageview;
    Bitmap bitmap;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;

    String imagePath;


    UnitConstant uc;
    UnitGlobal ug = new UnitGlobal();
    Cursor c;
    DBAdapter dba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_item);

        dba = DBAdapter.getInstance(this);
        edt1 = (EditText) findViewById(R.id.edt1);
        edt2 = (EditText) findViewById(R.id.edt2);
        edt3 = (EditText) findViewById(R.id.edt3);
        edt4 = (EditText) findViewById(R.id.edt4);
        edt5 = (EditText) findViewById(R.id.edt5);
        t1 = (TextView) findViewById(R.id.label1);
        t2 = (TextView) findViewById(R.id.label2);
        t3 = (TextView) findViewById(R.id.label3);
        t4 = (TextView) findViewById(R.id.label4);
        t5 = (TextView) findViewById(R.id.label5);
        edtcode = (EditText) findViewById(R.id.edtcode);
        edtname = (EditText) findViewById(R.id.edtname);
        edtvendorcode = (EditText) findViewById(R.id.edtvendorcode);
        edtdep = (Spinner) findViewById(R.id.edtdep);
        edtitemtype = (Spinner) findViewById(R.id.edtitemtype);
        edtstatus = (Spinner)findViewById(R.id.edtstatus);
        masterId = -1;

        laydet = (LinearLayout) findViewById(R.id.layoutdetail);
        layprice = (LinearLayout) findViewById(R.id.layoutprice);
        layb1 = (LinearLayout) findViewById(R.id.layoutb1);
        layb2 = (LinearLayout) findViewById(R.id.layoutb2);
        layb3 = (LinearLayout) findViewById(R.id.layoutb3);
        layb4 = (LinearLayout) findViewById(R.id.layoutb4);
        layb5 = (LinearLayout) findViewById(R.id.layoutb5);

        layb1.setVisibility(View.GONE);
        layb2.setVisibility(View.GONE);
        layb3.setVisibility(View.GONE);
        layb4.setVisibility(View.GONE);
        layb5.setVisibility(View.GONE);


        layprice.setVisibility(View.GONE);

        btnsave = (Button) findViewById(R.id.btnsave);
        btncancel = (Button) findViewById(R.id.btncancel);
        btndetail = (Button) findViewById(R.id.btndetail);
        btnproperties = (Button) findViewById(R.id.btnproperties);
        btnbrowse = (Button) findViewById(R.id.btnbrowse);

        headername = (TextView) findViewById(R.id.headername);
        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        btndetail.setOnClickListener(this);
        btnproperties.setOnClickListener(this);
        btnbrowse.setOnClickListener(this);

        imageview = (ImageView)findViewById(R.id.imageView1);
        //bytearrayoutputstream = new ByteArrayOutputStream();

        imagePath = "";

        /*
        private ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.imageViewId);
        mImageView.setImageBitmap(BitmapFactory.decodeFile("pathToImageFile"));
        */



        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        act = Integer.valueOf(getIntent().getExtras().getString("act"));

        if (act == uc.M_NEW){
            head = "(NEW)";
        } else {
            head = "(EDIT)";
        }
        headername.setText("ITEM "+head);

        setStatus();
        setDepartment();
        setItemtype();
        setPrice();
        setField();
    }

    private void setStatus(){
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

    private void setItemtype(){
        List<SpinnerWithTag> spinCustcat = new ArrayList<SpinnerWithTag>();
        spinCustcat.add(new SpinnerWithTag(UnitConstant.ITE_TYPE_NAME[UnitConstant.ITE_TYPE_NONSTOCK], UnitConstant.ITE_TYPE_NONSTOCK));
        spinCustcat.add(new SpinnerWithTag(UnitConstant.ITE_TYPE_NAME[UnitConstant.ITE_TYPE_STOCK], UnitConstant.ITE_TYPE_STOCK));
        //spinCustcat.add(new SpinnerWithTag(UnitConstant.ITE_TYPE_NAME[UnitConstant.ITE_TYPE_ASSEMBLY], UnitConstant.ITE_TYPE_ASSEMBLY));
        spinCustcat.add(new SpinnerWithTag(UnitConstant.ITE_TYPE_NAME[UnitConstant.ITE_TYPE_BUNDLE], UnitConstant.ITE_TYPE_BUNDLE));

        ArrayAdapter<SpinnerWithTag> itemAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                android.R.layout.simple_spinner_item, spinCustcat);

        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtitemtype.setAdapter(itemAdapter);

        edtitemtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                positionItemtype = s.tag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void setDepartment(){
        List<SpinnerWithTag> spinDep = new ArrayList<SpinnerWithTag>();
        Cursor c = dba.loadLocalTableFromRawQuery("SELECT id, '[' || code || '] - ' || name FROM dbmdepartment");
        if ((c != null ) && (c.moveToFirst())) {
            do {
                spinDep.add(new SpinnerWithTag(c.getString(1), c.getInt(0)));

            } while (c.moveToNext());
        }

        ArrayAdapter<SpinnerWithTag> depAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                android.R.layout.simple_spinner_item, spinDep);

        depAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtdep.setAdapter(depAdapter);

        edtdep.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                positionDep = s.tag;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setPrice(){
        countType = 1;
        custtypeList.clear();
        Cursor c = dba.loadLocalTableFromRawQuery("SELECT id, code FROM dbmcusttype ORDER BY id ASC");
        if ((c != null ) && (c.moveToFirst())) {
            do {
                custtypeList.add(c.getInt(0));
                caseLayout(countType, c.getString(1));
                countType++;
                //spinDep.add(new SpinnerWithTag(c.getString(1), c.getInt(0)));

            } while (c.moveToNext());
        }
    }

    private String getIdEdt(Integer id){
        String pr = "";
        switch(id) {
            case 0:
                pr = edt1.getText().toString();
                break;
            case 1:
                pr = edt2.getText().toString();
                break;
            case 2:
                pr = edt3.getText().toString();
                break;
            case 3:
                pr = edt4.getText().toString();
                break;
            case 4:
                pr = edt5.getText().toString();
                break;

        }

        return pr;
    }
    public void caseLayout(Integer id, String name){
        valuePrice = (float) 0;

        Cursor c = dba.loadLocalTableFromRawQuery("SELECT id, price FROM dbmprice WHERE ctpId = "+id+" AND unitId = 1 AND itemId = "+masterId+" ORDER BY id ASC");
        if ((c != null ) && (c.moveToFirst())) {
            do {
                valuePrice = c.getFloat(1);
                //spinDep.add(new SpinnerWithTag(c.getString(1), c.getInt(0)));

            } while (c.moveToNext());
        }
        switch(id){
            case 1:
                layb1.setVisibility(View.VISIBLE);
                t1.setText(name);
                edt1.setText(valuePrice.toString());
                break;
            case 2:
                layb2.setVisibility(View.VISIBLE);
                t2.setText(name);
                edt2.setText(valuePrice.toString());
                break;
            case 3:
                layb3.setVisibility(View.VISIBLE);
                t3.setText(name);
                edt3.setText(valuePrice.toString());
                break;
            case 4:
                layb4.setVisibility(View.VISIBLE);
                t4.setText(name);
                edt4.setText(valuePrice.toString());
                break;
            case 5:
                layb5.setVisibility(View.VISIBLE);
                t5.setText(name);
                edt5.setText(valuePrice.toString());
                break;

        }
    }
    private void setField(){
        typeList.add(uc.CHECK_CODE_DOUBLE);
        typeList.add(uc.CHECK_EMPTY_FIELD);

        mustList.add("code");
        mustList.add("depId");

        fieldList.add("code");
        fieldList.add("name");
        fieldList.add("depId");
        fieldList.add("vendorCode");
        fieldList.add("type");
        fieldList.add("status");

        fieldList.add("curId");
        fieldList.add("level");
        fieldList.add("minStock");
        fieldList.add("snIn");
        fieldList.add("snOut");
        fieldList.add("snCorrelation");
        fieldList.add("isSell");
        fieldList.add("isBuy");
        fieldList.add("isMenu");
        fieldList.add("isTaxed");
        fieldList.add("isService");
        fieldList.add("userAccessId");
        fieldList.add("image");

        unitList.add("itemId");
        unitList.add("code");
        unitList.add("isBase");
        unitList.add("conversion");

        priceList.add("itemId");
        priceList.add("unitId");
        priceList.add("ctpId");
        priceList.add("locId");
        priceList.add("price");
    }

    private void insertData(){

        int id, unitId, cx;
        valueList.clear();
        valuePriceList.clear();
        valueUnitList.clear();
        valueList.add(edtcode.getText().toString());
        valueList.add(edtname.getText().toString());
        valueList.add(positionDep.toString());
        valueList.add(edtvendorcode.getText().toString());
        valueList.add(positionItemtype.toString());
        valueList.add(positionStatus.toString());

        valueList.add("1");
        valueList.add("1");
        valueList.add("0");
        valueList.add("0");
        valueList.add("0");
        valueList.add("0");
        valueList.add("1");
        valueList.add("1");
        valueList.add("0");
        valueList.add("0");
        valueList.add("0");
        valueList.add("0");

        if(imagePath.equalsIgnoreCase("")) {
            valueList.add("0");
        } else {
            valueList.add(edtcode.getText().toString() + ".jpg");
        }

        //kalau waktu save gagal dan ga keluar errornya di compiler, try catchnya di komen aja. Kalau udah itu dihidupin lagi ya...
        try {
            if(ug.checkValidData(this, moduletype, typeList, mustList, fieldList, valueList, dba)){
                id = ug.insertMasterData(ug.tableName[moduletype], fieldList, valueList, dba);
                if (id > 0){

                    if(!imagePath.equalsIgnoreCase("")) {
                        File mydir = new File(filePath + "itemimage/");

                        if (!mydir.exists()) {
                            if (!mydir.mkdir()) {
                                Toast.makeText(getApplicationContext(), "unable to create dir", Toast.LENGTH_LONG).show();
                            }
                        }

                        File from = new File(imagePath);
                        File to = new File(filePath + "itemimage/" + edtcode.getText().toString() + ".jpg");
                        from.renameTo(to);
                    }

                    valueUnitList.add(String.valueOf(id));
                    valueUnitList.add("PCS");
                    valueUnitList.add("1");
                    valueUnitList.add("1");
                    unitId = ug.insertMasterData("dbmitemunit", unitList, valueUnitList, dba);

                    for(cx = 0;cx < countType-1;cx++){
                        valuePriceList.clear();
                        Log.d("CX", "insertData: "+cx);
                        valuePriceList.add(String.valueOf(id));
                        valuePriceList.add(String.valueOf(unitId));
                        valuePriceList.add(String.valueOf(custtypeList.get(cx)));
                        valuePriceList.add("1");
                        valuePriceList.add(getIdEdt(cx));
                        ug.insertMasterData("dbmprice", priceList, valuePriceList, dba);
                    }

                    Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                    ug.clearForm(laydet);
                    ug.clearForm(layprice);
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
                laydet.setVisibility(View.VISIBLE);
                layprice.setVisibility(View.GONE);
                break;
            case R.id.btnproperties:
                laydet.setVisibility(View.GONE);
                layprice.setVisibility(View.VISIBLE);
                break;
            case R.id.btnsave:
                insertData();
                break;
            case R.id.btncancel:
                close();
                break;
            case R.id.btnbrowse:
                browseImg();
                break;
            default:
                break;
        }
    }

    private void browseImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //intent.setAction(Intent.ACTION_SEARCH);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri uri = data.getData();

                Log.d("mbkiosk", "uri : " + uri);
                imagePath = getPath(uri);
                Log.d("mbkiosk", "file path : " + imagePath);


                int targetW = imageview.getWidth();
                int targetH = imageview.getHeight();

                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                bmOptions.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(imagePath, bmOptions);
                int photoW = bmOptions.outWidth;
                int photoH = bmOptions.outHeight;

                int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

                bmOptions.inJustDecodeBounds = false;
                bmOptions.inSampleSize = scaleFactor;

                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, bmOptions);
                imageview.setImageBitmap(bitmap);
            }

        }

    }

    public String getPath(Uri contentUri) {
        String wholeID = DocumentsContract.getDocumentId(contentUri);

        Log.d("mbkiosk", "wholeID : " + wholeID);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = { MediaStore.Images.Media.DATA };

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);

        String tmpFilePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            tmpFilePath = cursor.getString(columnIndex);
        }

        cursor.close();

        return tmpFilePath;
    }


    private void close() {
        finish();
        intentFieldList.add("moduletype");
        intentvalueList.add(moduletype.toString());
        ug.OpenIntent(this, MasterFolderActivity.class, intentFieldList, intentvalueList);
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
        close();
    }
}
