package com.kreators.mybizkios;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemEntryActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int SELECT_PICTURE = 1;
    final String filePath = Environment.getExternalStorageDirectory().toString() + "/MBKiosk/";
    Uri uri, oldUri;

    Button btnsave, btncancel, btndetail, btnproperties, btnbrowse, btnmaterial;
    Button btnaddnewitem, btnScanBarcode;
    String head;
    Float valuePrice;
    Integer moduletype, act, positionStatus, positionDep, positionItemtype, countType, idData;

    TextView headername, t1, t2, t3, t4, t5, textViewBarcode;
    EditText edtcode, edtname, edtvendorcode, edt1,edt2,edt3,edt4,edt5;
    EditText edtitem, edtqty;
    LinearLayout laydet, layprice, laymat, layb1,layb2,layb3,layb4,layb5;
    Spinner edtstatus, edtdep, edtitemtype;

    List<Integer> typeList = new ArrayList<Integer>();
    List<String> mustList = new ArrayList<String>();
    List<String> fieldList = new ArrayList<String>();
    List<String> valueList = new ArrayList<String>();
    List<String> priceList = new ArrayList<String>();
    List<String> valuePriceList = new ArrayList<String>();
    List<String> intentFieldList = new ArrayList<String>();
    List<String> intentvalueList = new ArrayList<String>();


    List<Integer> custtypeList = new ArrayList<Integer>();

    ImageView imageview;
    Bitmap bitmap;
    ByteArrayOutputStream bytearrayoutputstream;
    File file;
    FileOutputStream fileoutputstream;
    final int CAMERA_REQUEST = 1888;
    final int REQ_CAMERA = 1919;

    String imagePath;

    DetailAdapter detailAdpt;
    List<ObjEntityItem> detailList;
    ListView lvdetail;

    UnitConstant uc;
    UnitGlobal ug = new UnitGlobal();
    //Cursor c;
    DBAdapter dba;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_item);
        Helper.getInstance().scaleImage(this);

        dba = DBAdapter.getInstance(this);
        edt1 = (EditText) findViewById(R.id.edt1);
        edt1.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));

        edt2 = (EditText) findViewById(R.id.edt2);
        edt2.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));

        edt3 = (EditText) findViewById(R.id.edt3);
        edt3.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));

        edt4 = (EditText) findViewById(R.id.edt4);
        edt4.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));

        edt5 = (EditText) findViewById(R.id.edt5);
        edt5.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));

        edtitem = (EditText) findViewById(R.id.edtitem);
        edtqty = (EditText) findViewById(R.id.edtqtyitem);
        edtqty.setText("1");

        t1 = (TextView) findViewById(R.id.label1);
        t2 = (TextView) findViewById(R.id.label2);
        t3 = (TextView) findViewById(R.id.label3);
        t4 = (TextView) findViewById(R.id.label4);
        t5 = (TextView) findViewById(R.id.label5);
        textViewBarcode = (TextView)findViewById(R.id.textViewBarcode);

        edtcode = (EditText) findViewById(R.id.edtcode);
        edtname = (EditText) findViewById(R.id.edtname);
        edtvendorcode = (EditText) findViewById(R.id.edtvendorcode);
        edtdep = (Spinner) findViewById(R.id.edtdep);
        edtitemtype = (Spinner) findViewById(R.id.edtitemtype);
        edtstatus = (Spinner)findViewById(R.id.edtstatus);
        idData = -1;

        laydet = (LinearLayout) findViewById(R.id.layoutdetail);
        layprice = (LinearLayout) findViewById(R.id.layoutprice);
        laymat = (LinearLayout) findViewById(R.id.layoutmaterial);
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
        btnmaterial = (Button) findViewById(R.id.btnMaterial);
        btnbrowse = (Button) findViewById(R.id.btnbrowse);
        btnaddnewitem = (Button) findViewById(R.id.btnaddnewitem);
        btnScanBarcode = (Button)findViewById(R.id.btnScanBarcode);

        headername = (TextView) findViewById(R.id.headername);
        btnsave.setOnClickListener(this);
        btncancel.setOnClickListener(this);
        btndetail.setOnClickListener(this);
        btnproperties.setOnClickListener(this);
        btnmaterial.setOnClickListener(this);
        btnbrowse.setOnClickListener(this);
        btnaddnewitem.setOnClickListener(this);
        btnScanBarcode.setOnClickListener(this);

        imageview = (ImageView)findViewById(R.id.imageView1);
        imageview.setOnClickListener(this);
        //bytearrayoutputstream = new ByteArrayOutputStream();

        detailList = new ArrayList<ObjEntityItem>();
        detailAdpt = new DetailAdapter(getApplicationContext(), R.layout.simple_list_itembundle, detailList);
        lvdetail = (ListView) findViewById(R.id.itemlist);
        lvdetail.setAdapter(detailAdpt);

        uri = null;
        oldUri = null;
        imagePath = "";

        /*
        private ImageView mImageView;
        mImageView = (ImageView) findViewById(R.id.imageViewId);
        mImageView.setImageBitmap(BitmapFactory.decodeFile("pathToImageFile"));
        */

        moduletype = Integer.valueOf(getIntent().getExtras().getString("moduletype"));
        act = Integer.valueOf(getIntent().getExtras().getString("act"));
        idData = Integer.valueOf(getIntent().getExtras().getString("id"));

        setStatus();
        setDepartment();
        setItemtype();
        setPrice();

        if (act == uc.M_NEW){
            head = "(NEW)";
        } else {
            head = "(EDIT)";

            Log.d("Item Entry", "Editing ... get data");


            String strquery = "SELECT a.id, a.code, a.name, a.vendorCode, a.depId, a.type, " +
                    "a.notes, a.status, a.image, '[' || b.code || '] - ' || b.name as dept, " +
                    "a.barcode " +
                    "FROM dbmitem a left join dbmdepartment b on b.id = a.depId WHERE a.id = " + idData;
            Cursor c = dba.loadLocalTableFromRawQuery(strquery);
            if ((c != null ) && (c.moveToFirst())) {
                edtcode.setText(c.getString(1));
                edtname.setText(c.getString(2));
                edtvendorcode.setText(c.getString(3));

                int tmppos = ug.getIndexByString(edtdep, c.getString(9));
                edtdep.setSelection(tmppos);
                int itemtype = c.getInt(5);


                /*if (itemtype == UnitConstant.ITE_TYPE_BUNDLE) {
                    btnmaterial.setEnabled(true);
                } else {
                    btnmaterial.setEnabled(false);
                }*/

                textViewBarcode.setText(c.getString(10));

                edtitemtype.setSelection(itemtype);
                edtstatus.setSelection(c.getInt(7));
                Log.d("Item Entry", "Path : " + c.getString(8));

                imagePath = c.getString(8);
                imageview.setImageBitmap(BitmapFactory.decodeFile(imagePath));
                uri = Uri.fromFile(new File(imagePath));
                Log.d("Item Entry", "uri : " + uri);
                oldUri = uri;

                Log.d("Item Entry", "item type : " + itemtype);
                if (itemtype == UnitConstant.ITE_TYPE_BUNDLE) {
                    Log.d("Item Entry", "isi brg bundle");
                    String strqueryitem =   "select b.id, b.code, b.name, a.qty " +
                                            "from dbmitemmaterial a left join dbmitem b on b.id = a.compId " +
                                            "where a.itemId = " + idData;
                    Cursor citem = dba.loadLocalTableFromRawQuery(strqueryitem);

                    Log.d("Item Entry", "query : " + strqueryitem);

                    if ((citem != null) && (citem.moveToFirst())) {
                        do {
                            int itemid = citem.getInt(0);
                            String itemcode = citem.getString(1);
                            String itemname = citem.getString(2);
                            int qty = citem.getInt(3);

                            Log.d("Item Entry", "brg bundle : " + itemname);

                            detailAdpt.add(new ObjEntityItem(itemid, "", "[" + itemcode + "] - " + itemname, "", qty, 0, "0"));

                            detailAdpt.notifyDataSetChanged();
                        } while (citem.moveToNext());

                        edtitem.setText("");
                        edtqty.setText("1");
                    }
                    citem.close();
                }



            }
            c.close();

            edtcode.setEnabled(false);

        }
        headername.setText("ITEM "+head);

        lvdetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                initiateDetailPopupWindow(arg2);
            }

        });

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
        //spinCustcat.add(new SpinnerWithTag(UnitConstant.ITE_TYPE_NAME[UnitConstant.ITE_TYPE_MENU], UnitConstant.ITE_TYPE_MENU));

        ArrayAdapter<SpinnerWithTag> itemAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                android.R.layout.simple_spinner_item, spinCustcat);

        itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtitemtype.setAdapter(itemAdapter);

        edtitemtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                positionItemtype = s.tag;

                /*if (positionItemtype == UnitConstant.ITE_TYPE_BUNDLE) {
                    btnmaterial.setEnabled(true);
                } else {
                    btnmaterial.setEnabled(false);
                }
*/
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
                //pr = edt1.getText().toString();
                pr = String.valueOf(ug.getFloatValue(edt1));
                break;
            case 1:
                //pr = edt2.getText().toString();
                pr = String.valueOf(ug.getFloatValue(edt2));
                break;
            case 2:
                //pr = edt3.getText().toString();
                pr = String.valueOf(ug.getFloatValue(edt3));
                break;
            case 3:
                //pr = edt4.getText().toString();
                pr = String.valueOf(ug.getFloatValue(edt4));
                break;
            case 4:
                //pr = edt5.getText().toString();
                pr = String.valueOf(ug.getFloatValue(edt5));
                break;

        }

        return pr;
    }
    public void caseLayout(Integer id, String name){
        valuePrice = (float) 0;

        String strsql = "SELECT id, price FROM dbmprice WHERE ctpId = "+id+" AND itemId = "+idData+" ORDER BY id ASC";
        Log.d("Item Entry", "query utk isi price : " + strsql);
        Cursor c = dba.loadLocalTableFromRawQuery(strsql);
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
                edt1.setText(UnitGlobal.convertToStrCurr(valuePrice));
                break;
            case 2:
                layb2.setVisibility(View.VISIBLE);
                t2.setText(name);
                edt2.setText(UnitGlobal.convertToStrCurr(valuePrice));
                break;
            case 3:
                layb3.setVisibility(View.VISIBLE);
                t3.setText(name);
                edt3.setText(UnitGlobal.convertToStrCurr(valuePrice));
                break;
            case 4:
                layb4.setVisibility(View.VISIBLE);
                t4.setText(name);
                edt4.setText(UnitGlobal.convertToStrCurr(valuePrice));
                break;
            case 5:
                layb5.setVisibility(View.VISIBLE);
                t5.setText(name);
                edt5.setText(UnitGlobal.convertToStrCurr(valuePrice));
                break;

        }
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
        //fieldList.add("userAccessId");
        fieldList.add("image");

        priceList.add("itemId");
        priceList.add("ctpId");
        priceList.add("price");
    }

    private void initiateDetailPopupWindow(int pos) {
        try {

            LayoutInflater inflater = (LayoutInflater) ItemEntryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.detailitembundlepopup, (ViewGroup) findViewById(R.id.itempopuplayout));

            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            alertDialog = builder.show();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final ObjEntityItem objdetail = detailAdpt.detailList.get(pos);
            TextView txtitem = (TextView) layout.findViewById(R.id.txtitem);
            TextView txtname = (TextView) layout.findViewById(R.id.txtname);

            final EditText myedtqty = (EditText) layout.findViewById(R.id.edtqty);

            String strqueryitem =   "select a.code, a.name from dbmitem a where a.id = " + objdetail.getId();
            Cursor citem = dba.loadLocalTableFromRawQuery(strqueryitem);

            Log.d("Item Entry", "query : " + strqueryitem);

            if ((citem != null) && (citem.moveToFirst())) {
                txtitem.setText(citem.getString(0));
                txtname.setText(citem.getString(1));
            }
            citem.close();

            myedtqty.setText(String.valueOf(objdetail.getQty()));

            myedtqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        if ((myedtqty.getText().toString().equalsIgnoreCase("")) ||
                                (Integer.parseInt(myedtqty.getText().toString()) <= 0)) {
                            myedtqty.setText("1");
                        }
                    }
                }
            });

            myedtqty.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    //System.out.println("masuk seleksi click" +myedtqty.getText().length());
                    myedtqty.setSelection(myedtqty.getText().length());

                }
            });


            Button btncancel = (Button) layout.findViewById(R.id.btncancel);
            btncancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    alertDialog.dismiss();
                }
            });

            Button btnsave = (Button) layout.findViewById(R.id.btnsave);
            btnsave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    if (myedtqty.getText().toString().equalsIgnoreCase("")) {
                        myedtqty.setText("1");
                    }

                    int tmpqty;

                    try {
                        tmpqty = Integer.parseInt(myedtqty.getText().toString());
                    } catch (Exception e) {
                        // TODO: handle exception
                        Toast.makeText(getApplicationContext(), "Format angka tidak valid, mohon dicek kembali.", Toast.LENGTH_SHORT).show();
                        tmpqty = 1;
                        myedtqty.setText("1");
                    }

                    if (tmpqty <= 0) {
                        Toast.makeText(getBaseContext(), "Qty harus lebih besar dari nol.", Toast.LENGTH_SHORT).show();
                    } else {
                        objdetail.update("",
                                Integer.parseInt(myedtqty.getText().toString()),
                                0, "");
                        detailAdpt.notifyDataSetChanged();
                        alertDialog.dismiss();
                    }


                }
            });

            Button btndelete = (Button) layout.findViewById(R.id.btndelete);
            btndelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    new AlertDialog.Builder(ItemEntryActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Confirmation")
                            .setMessage("Do you really want to delete this item ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    detailAdpt.detailList.remove(objdetail);
                                    detailAdpt.notifyDataSetChanged();
                                    alertDialog.dismiss();
                                }

                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertData(){
        btnsave.requestFocus();
        boolean valid = true;
        int id, cx;
        valueList.clear();
        valuePriceList.clear();

        if (act == uc.M_NEW) {
            valueList.add(edtcode.getText().toString());
            valueList.add("1");

        }
        Log.d("Item Entry","positionDep : " + positionDep);
        Log.d("Item Entry","positionItemtype : " + positionItemtype);
        Log.d("Item Entry","positionStatus : " + positionStatus);

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

        Log.d("Item Entry", "uri waktu ngesave : " + uri);
        Log.d("Item Entry", "uri waktu ngesave : " + oldUri);

        if (uri == null) {
            valueList.add("");
        } else if ((oldUri != null) && (uri.toString().equalsIgnoreCase(oldUri.toString()))) {
            valueList.add(imagePath);
        } else {
            Log.d("Item Entry","imagePath sebelum savefile : " + imagePath);
            if (savefile(uri, edtcode.getText().toString() + ".jpg")) {
                //do nothing
                valueList.add(imagePath);
                Log.d("Item Entry","imagePath sesudah savefile : " + imagePath);
            } else {
                valid = false;
                Toast.makeText(getApplicationContext(), "unable to save image, please check your image", Toast.LENGTH_LONG).show();
            }
        }


        String barcode = textViewBarcode.getText().toString();
        if (!barcode.equalsIgnoreCase("")) {
            Cursor c = dba.loadLocalTableFromRawQuery(
                    "SELECT code FROM dbmitem where status = 0 and barcode = '" +
                            barcode + "'");
            if (c != null && c.moveToFirst()) //barcode sudah ada
            {
                if(act == uc.M_NEW || (act == uc.M_EDIT && !c.getString(0).toString().equals(edtcode.getText().toString()))) {
                    valid = false;
                    textViewBarcode.setText("");
                    Toast.makeText(this, "Barcode sudah dipergunakan untuk item lain", Toast.LENGTH_SHORT).show();
                }
            } else {
                fieldList.add("barcode");
                valueList.add(barcode);
            }
            c.close();
        }

        if (valid) {
            //kalau waktu save gagal dan ga keluar errornya di compiler, try catchnya di komen aja. Kalau udah itu dihidupin lagi ya...
            try {
                if (ug.checkValidData(this, moduletype, typeList, mustList, fieldList, valueList, dba)) {
                    if (act == uc.M_NEW) {
                        id = ug.insertMasterData(ug.tableName[moduletype], fieldList, valueList, dba);
                    } else {
                        id = ug.updateMasterData(ug.tableName[moduletype], fieldList, valueList, dba, idData);
                    }
                    if (id > 0) {

                        //delete(String tablename, String where) {
                        dba.delete("dbmprice","where itemId = " + id);
                        Log.d("CX", "delete sql where: " + " where itemId = " + id);
                        for (cx = 0; cx < countType - 1; cx++) {
                            valuePriceList.clear();
                            Log.d("CX", "insertData: " + cx);
                            Log.d("CX", "id: " + id);
                            valuePriceList.add(String.valueOf(id));
                            Log.d("CX", "custtypeList: " + String.valueOf(custtypeList.get(cx)));
                            valuePriceList.add(String.valueOf(custtypeList.get(cx)));
                            Log.d("CX", "getIdEdt: " + getIdEdt(cx));
                            valuePriceList.add(getIdEdt(cx));
                            ug.insertMasterData("dbmprice", priceList, valuePriceList, dba);
                        }

                        ObjEntityItem objitemdtl;
                        dba.delete("dbmitemmaterial","where itemId = " + id);
                        if (positionItemtype == UnitConstant.ITE_TYPE_BUNDLE) {
                            for (int i = 0; i < detailAdpt.detailList.size(); i++) {
                                objitemdtl = detailAdpt.detailList.get(i);

                                ContentValues datatrans = new ContentValues();
                                Log.d("ISI BUNDLE", "id: " + id);
                                datatrans.put("itemId", id);
                                Log.d("ISI BUNDLE", "ItemId: " + objitemdtl.getId());
                                datatrans.put("compId", objitemdtl.getId());
                                datatrans.put("compUnitId", 0);
                                Log.d("ISI BUNDLE", "Qty: " + objitemdtl.getQty());
                                datatrans.put("qty", objitemdtl.getQty());

                                dba.saveToDB("dbmitemmaterial", datatrans);
                            }
                        }

                        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                        if (act == uc.M_NEW) {
                            ug.clearForm(laydet);
                            ug.clearForm(layprice);
                            imageview.setImageBitmap(null);
                            detailAdpt.detailList.clear();
                            uri = null;
                        } else {
                            close();
                        }
                    } else {
                        Toast.makeText(this, "Failed. Contact the administrator", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Log.d("lognya ", e.getMessage());
                Toast.makeText(this, "Failed. Contact the administrator ", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected boolean additem() {
        boolean result = false;
        Cursor citem = null;

        String itemcode = edtitem.getText().toString();
        //c = database.loadLocalTableFromRawQuery("select id, code, name, unit from dbmitem where id <> " + docid + " AND code = '" + itemcode + "' AND type <> 3 ");
        String strqueryitem =   "select id, code, name " +
                                "from dbmitem " +
                                "where id <> " + idData + " AND code = '" + itemcode.toUpperCase() + "' " +
                                    "AND type <> " + UnitConstant.ITE_TYPE_BUNDLE;
        citem = dba.loadLocalTableFromRawQuery(strqueryitem);

        if ((citem != null) && (citem.moveToFirst())) {
            int itemid = citem.getInt(0);
            itemcode = citem.getString(1);
            String itemname = citem.getString(2);

            int qty = 0;

            if (edtqty.getText().toString().equalsIgnoreCase("")) {
                qty = 1;
            } else {
                try {
                    qty = Integer.parseInt(edtqty.getText().toString());
                } catch (Exception e) {
                    // TODO: handle exception
                    Toast.makeText(getApplicationContext(), "Format angka tidak valid, mohon dicek kembali.", Toast.LENGTH_SHORT).show();
                    qty = 1;
                    edtqty.setText("1");
                }

            }

            detailAdpt.add(new ObjEntityItem(itemid, "", "[" + itemcode + "] - " + itemname, "", qty, 0, "0"));

            detailAdpt.notifyDataSetChanged();


            result = true;
            edtitem.setText("");
            edtqty.setText("1");
        }
        citem.close();


        return result;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btndetail:
                laydet.setVisibility(View.VISIBLE);
                layprice.setVisibility(View.GONE);
                laymat.setVisibility(View.GONE);
                break;
            case R.id.btnproperties:
                laydet.setVisibility(View.GONE);
                layprice.setVisibility(View.VISIBLE);
                laymat.setVisibility(View.GONE);
                break;
            case R.id.btnMaterial:
                if (positionItemtype == UnitConstant.ITE_TYPE_BUNDLE) {
                    laydet.setVisibility(View.GONE);
                    layprice.setVisibility(View.GONE);
                    laymat.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getApplicationContext(), "Item Material tidak bisa dibuka, karna tipe barang bukan bundle", Toast.LENGTH_LONG).show();
                }
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
            case R.id.imageView1:
                browseImg();
                break;
            case R.id.btnaddnewitem:
                if (!additem()) {
                    //tdk ditemukan
                    Log.d("ItemEntryActivity", "brg tdk ditemukan");
                    Bundle bundle = new Bundle();
                    bundle.putInt("Module", 5);
                    bundle.putInt("janganbundle", 1);
                    bundle.putInt("id", idData);
                    bundle.putString("InitialSearch", edtitem.getText().toString());

                    Intent i = new Intent(ItemEntryActivity.this, SearchScreen.class);
                    i.putExtras(bundle);
                    startActivityForResult(i, 2);
                }
                break;
            case R.id.btnScanBarcode:
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M || Build.VERSION.SDK_INT>=23)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
                else
                    readBarcode();
                break;
            default:
                break;
        }
    }

    private void readBarcode()
    {
        Intent cameraIntent = new Intent(this, BarcodeReaderActivity.class);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    private void browseImg() {
        /*
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        //intent.setAction(Intent.ACTION_SEARCH);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
        */

        // This always works
        Intent i = new Intent(getApplicationContext(), FilePickerActivity.class);

        // This works if you defined the intent filter
        // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

        // Set these depending on your use case. These are the defaults.
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);

        // Configure initial directory by specifying a String.
        // You could specify a String like "/storage/emulated/0/", but that can
        // dangerous. Always use Android's API calls to get paths to the SD-card or
        // internal memory.
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

        startActivityForResult(i, SELECT_PICTURE);





    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                uri = data.getData();
                String imgpath = uri.getPath();
                Log.d("PosActivity", "URI : " + imgpath);
                imageview.setImageURI(uri);
                //savefile(uri);
            } else if (requestCode == 2) {
                edtitem.setText(data.getStringExtra("code"));
                additem();
            }
            //barcode scanner
            else if (requestCode == CAMERA_REQUEST) {
                String result = data.getStringExtra("barcode");
                textViewBarcode.setText(result);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case REQ_CAMERA:
                if(grantResults.length >0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        readBarcode();
                    else
                        Toast.makeText(this, "Please grant permission to access the camera.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    public boolean savefile(Uri sourceuri, String filename)
    {
        String sourceFilename= sourceuri.getPath();
        String destinationDir = filePath + "itemimage/";
        String destinationFilename = destinationDir + filename;

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        boolean result = false;
        boolean isDirOk = true;

        File mydir = new File(destinationDir);

        if (!mydir.exists()) {
            isDirOk = false;

            if (!mydir.mkdir()) {
                Toast.makeText(getApplicationContext(), "unable to create dir", Toast.LENGTH_LONG).show();
            } else {
                isDirOk = true;
            }
        }

        if(isDirOk) {

            try {
                bis = new BufferedInputStream(new FileInputStream(sourceFilename));
                bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
                byte[] buf = new byte[1024];
                bis.read(buf);
                do {
                    bos.write(buf);
                } while (bis.read(buf) != -1);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) bis.close();
                    if (bos != null) bos.close();

                    result = true;
                    imagePath = destinationFilename;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
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