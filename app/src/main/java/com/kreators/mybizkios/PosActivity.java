package com.kreators.mybizkios;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.BoolRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class PosActivity extends AppCompatActivity implements View.OnClickListener  {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    DBAdapter dba;

    UnitGlobal ug = new UnitGlobal();
    DetailAdapter dtlAdapter;
    ListView detailList;
    List<ObjEntityItem> itemList;
    int moduletype, userid, sessionId;
    int partnerid, ctpid, paymentmethod, cashbankid;

    float subtotal, disc, tax, service, rounded, grandtotal;
    float pctdisc, pcttax, pctservice;
    Boolean pctflagdisc, pctflagtax, pctflagservice;
    private myTextWatcher disctextwatcher, pctdisctextwatcher;
    private myTextWatcher taxtextwatcher, pcttaxtextwatcher;
    private myTextWatcher servicetextwatcher, pctservicetextwatcher;
    private myTextWatcher roundedtextwatcher;

    EditText vcode;
    EditText ecustcat, ecustname, ecashbank;
    Spinner edtpaymentmethod;
    EditText tsubtotal, tdisc, ttax, tservice, trounded, tgrandtotal, etotal;
    EditText tpctdisc, tpcttax, tpctservice;
    EditText tcardnumber, tpaidtotal;
    Button btnopenmenupos, btnentryitem, btnpayment, btnscanbarcode, btncancel, btnsavetrans, btnprint;
    LinearLayout layoutmenuitem, layoutmenupayment, layoutnc, layoutec, layouttotal, layousearch, layoutmain, layoutmp10;
    TextView txtTotal;

    private final int CAMERA_REQUEST = 1888;
    private final int REQ_CAMERA = 101;
    private final int REQ_BLUETOOTH_PRINTER = 301;

    AlertDialog dialog;
    View dialogView;
    Helper helper;

    //untuk auto-scroll ketika nambahin item baru, ada di function addNewItem
    ListView itemListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        Helper.getInstance().scaleImage(this);

        dba = DBAdapter.getInstance(this);

        moduletype = UnitConstant.TT_SALES_INVOICE;
        userid = 1;
        sessionId = 1;

        itemList = new ArrayList<ObjEntityItem>();
        detailList = (ListView) findViewById(R.id.itemlist);
        dtlAdapter = new DetailAdapter(getApplicationContext(), R.layout.listview_item, itemList);
        detailList.setAdapter(dtlAdapter);

        initView();
        bindListener();


        for(int i=0; i < tabLayout.getTabCount(); i++) {
            View tab = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(i);
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) tab.getLayoutParams();
            p.setMargins(0, 0, 10, 0);
            tab.requestLayout();
        }

        inittrans();

        detailList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                initiateDetailPopupWindow(arg2);
            }

        });

        helper = Helper.getInstance();
        helper.scaleImage(this);
    }

    private void initView()
    {
        ecustcat = (EditText) findViewById(R.id.ecustcat);
        ecustname = (EditText) findViewById(R.id.ecustname);
        ecashbank = (EditText) findViewById(R.id.tmp9edtname);

        layoutmp10 = (LinearLayout)findViewById(R.id.layoutmp10);

        ctpid = 0;
        Cursor cpartnertype = dba.loadLocalTableFromRawQuery("select id, code from dbmcusttype where status = 0 order by id desc");
        Log.d("PosActivity", "sebelum ngisi partner type");

        if ((cpartnertype != null ) && (cpartnertype.moveToFirst())) {
            do {
                ctpid = cpartnertype.getInt(0);
                ecustcat.setText(cpartnertype.getString(1));
            } while (cpartnertype.moveToNext());
        }
        cpartnertype.close();

        partnerid = 0;
        String querypartner = "select id, code from dbmpartner where status = 0 and ctpId = " + String.valueOf(ctpid) + " order by id desc";
        Cursor cpartner = dba.loadLocalTableFromRawQuery(querypartner);
        if ((cpartner != null ) && (cpartner.moveToFirst())) {
            do {
                partnerid = cpartner.getInt(0);
                ecustname.setText(cpartner.getString(1));
            }while (cpartner.moveToNext());
        }
        cpartner.close();

        cashbankid = 0;
        paymentmethod = 0;

        edtpaymentmethod = (Spinner) findViewById(R.id.tmp8typepayment);
        List<SpinnerWithTag> spinType = new ArrayList<SpinnerWithTag>();
        spinType.add(new SpinnerWithTag(UnitConstant.PAYMENT_TYPE_NAME[UnitConstant.PAYMENT_TYPE_CASH], UnitConstant.PAYMENT_TYPE_CASH));
        spinType.add(new SpinnerWithTag(UnitConstant.PAYMENT_TYPE_NAME[UnitConstant.ACCOUNT_TYPE_DEBIT_CARD], UnitConstant.ACCOUNT_TYPE_DEBIT_CARD));
        spinType.add(new SpinnerWithTag(UnitConstant.PAYMENT_TYPE_NAME[UnitConstant.ACCOUNT_TYPE_CREDIT_CARD], UnitConstant.ACCOUNT_TYPE_CREDIT_CARD));

        ArrayAdapter<SpinnerWithTag> typeAdapter = new ArrayAdapter<SpinnerWithTag>(this,
                R.layout.spinner_style, spinType);

        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edtpaymentmethod.setAdapter(typeAdapter);

        edtpaymentmethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                paymentmethod = s.tag;
                ecashbank.setText("");
                cashbankid = 0;
                if(paymentmethod == UnitConstant.PAYMENT_TYPE_CASH) layoutmp10.setVisibility(View.GONE);
                else layoutmp10.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tsubtotal = (EditText) findViewById(R.id.tmp1ttotal);
        vcode = (EditText) findViewById(R.id.vcode);
        tdisc = (EditText) findViewById(R.id.tmp2tdisc);
        ttax = (EditText) findViewById(R.id.tmp3ttax);
        tservice = (EditText) findViewById(R.id.tmp4tservices);
        trounded = (EditText) findViewById(R.id.tmp5trounded);
        tgrandtotal = (EditText) findViewById(R.id.tmp6tgrandtotal);
        etotal = (EditText) findViewById(R.id.etotal);
        tcardnumber = (EditText) findViewById(R.id.tmp9edtnmr);
        tpaidtotal = (EditText) findViewById(R.id.tmp8tpayment);
        tpctdisc = (EditText) findViewById(R.id.tmp2edisc);
        tpcttax = (EditText) findViewById(R.id.tmp3etax);
        tpctservice = (EditText) findViewById(R.id.tmp4eservices);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        //btnclear = (Button) findViewById(R.id.btnclear);
        btnopenmenupos = (Button) findViewById(R.id.btnmenupos);
        //btnpayment = (Button) findViewById(R.id.btnpayment);
        //btncancel = (Button) findViewById(R.id.btncancel);
        btnsavetrans = (Button) findViewById(R.id.btnsave);
        //btnscanbarcodeentry = (Button)findViewById(R.id.btnscanbarcodeentry);

        layoutmenuitem = (LinearLayout) findViewById(R.id.layoutmenuitem);
        layoutmenupayment = (LinearLayout) findViewById(R.id.layoutmenupayment);
        layoutnc = (LinearLayout)findViewById(R.id.layoutnc);
        layoutec = (LinearLayout)findViewById(R.id.layoutec);
        layouttotal = (LinearLayout)findViewById(R.id.layouttotal);
        layousearch = (LinearLayout)findViewById(R.id.layousearch);
        layoutmain = (LinearLayout)findViewById(R.id.layoutmain);

        itemListView = (ListView)findViewById(R.id.itemlist);

        txtTotal = (TextView)findViewById(R.id.txtTotal);

        layoutnc.setVisibility(View.GONE);
        layoutec.setVisibility(View.GONE);
        layouttotal.setVisibility(View.GONE);
        layousearch.setVisibility(View.GONE);
        layoutmenupayment.setVisibility(View.GONE);

        //setStatType(View.VISIBLE, View.GONE);

        tabLayout.setupWithViewPager(viewPager);

        opendialogboxmenupost();
    }

    private void bindListener()
    {
        disctextwatcher = new myTextWatcher("disc");
        roundedtextwatcher = new myTextWatcher("rounded");
        servicetextwatcher = new myTextWatcher("service");
        taxtextwatcher = new myTextWatcher("tax");
        pctdisctextwatcher = new myTextWatcher("pctdisc");
        pcttaxtextwatcher = new myTextWatcher("pcttax");
        pctservicetextwatcher = new myTextWatcher("pctservice");

        tdisc.addTextChangedListener(disctextwatcher);
        tdisc.setOnFocusChangeListener(new myFocusListener(disctextwatcher, getApplicationContext()));

        ttax.addTextChangedListener(taxtextwatcher);
        ttax.setOnFocusChangeListener(new myFocusListener(taxtextwatcher, getApplicationContext()));

        tservice.addTextChangedListener(servicetextwatcher);
        tservice.setOnFocusChangeListener(new myFocusListener(servicetextwatcher, getApplicationContext()));

        tpaidtotal.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));
        trounded.addTextChangedListener(roundedtextwatcher);
        trounded.setOnFocusChangeListener(new myFocusListener(roundedtextwatcher, getApplicationContext()));


        tpctdisc.addTextChangedListener(pctdisctextwatcher);
        tpcttax.addTextChangedListener(pcttaxtextwatcher);
        tpctservice.addTextChangedListener(pctservicetextwatcher);

        //btnclear.setOnClickListener(this);
        btnopenmenupos.setOnClickListener(this);
        //btnpayment.setOnClickListener(this);
        //btncancel.setOnClickListener(this);
        btnsavetrans.setOnClickListener(this);

        ecustcat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PosActivity", "Cust Category has been clicked");
                Bundle bundle = new Bundle();
                bundle.putInt("Module", 3);

                bundle.putString("InitialSearch", "");

                Intent i = new Intent(PosActivity.this, SearchScreen.class);
                i.putExtras(bundle);
                startActivityForResult(i, 3);
            }
        });

        ecustcat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                partnerid = 0;
                ecustname.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ecustname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PosActivity", "Cust Name has been clicked");
                Bundle bundle = new Bundle();
                bundle.putInt("Module", 2);

                bundle.putString("InitialSearch", String.valueOf(ctpid));

                Intent i = new Intent(PosActivity.this, SearchScreen.class);
                i.putExtras(bundle);
                startActivityForResult(i, 2);
            }
        });

        ecashbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if (paymentmethod > 0) {
                Log.d("PosActivity", "Kas Bank has been clicked, cashbankid : " + cashbankid);
                Bundle bundle = new Bundle();
                bundle.putInt("Module", 4);

                bundle.putString("InitialSearch", String.valueOf(paymentmethod));

                Intent i = new Intent(PosActivity.this, SearchScreen.class);
                i.putExtras(bundle);
                startActivityForResult(i, 4);
                //}
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (dtlAdapter.detailList.size() > 0) {
            showAlertDialog("Apakah anda yakin akan keluar dan membatalkan transaksi ?");
        } else {
            finish();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {
                ecustname.setText("[" + data.getStringExtra("code") + "] - " + data.getStringExtra("name"));
                partnerid = data.getIntExtra("id", 0);
                Log.d("MB Android", "Cust ID : " + String.valueOf(data.getIntExtra("id", 0)));
                //custid = data.getIntExtra("id", 0);
            } else if (requestCode == 3) {
                ecustcat.setText(data.getStringExtra("code"));
                ctpid = data.getIntExtra("id", 0);
            } else if (requestCode == 4) {
                ecashbank.setText(data.getStringExtra("code"));
                cashbankid = data.getIntExtra("id", 0);
            }
            else if (requestCode == CAMERA_REQUEST) {
                String result = data.getStringExtra("barcode");

                Cursor c = dba.loadLocalTableFromRawQuery(
                        "SELECT id, code, name, image, type FROM dbmitem where status = 0 and barcode = '" +
                                result + "'");
                if(c != null && c.moveToFirst())
                {
                    ObjEntity tmp = new ObjEntity(c.getInt(0), c.getString(1), c.getString(2), c.getString(4));
                    addNewItem(tmp);
                }
                else Toast.makeText(this, "No item was found", Toast.LENGTH_SHORT).show();
                c.close();
                dialog.dismiss();
                setStatType(View.VISIBLE, View.GONE);
            }
            else if (requestCode == REQ_BLUETOOTH_PRINTER) {
                if(data.getBooleanExtra("connected", false))
                {
                    print();
                }
                else {
                    Toast.makeText(this, "Failed to connect", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode)
        {
            case REQ_CAMERA:
                if(grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        readBarcode();
                    else Toast.makeText(this, "Please grant permission to access the camera.", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void initiateSummaryPopupWindow(){
        final AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) PosActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDev = inflater.inflate(R.layout.finishpaymentpopup, null);

        TextView tggrandtotal, tgjumlah, tgkembalian;
        Button btnback = (Button) viewDev.findViewById(R.id.btnback);

        tggrandtotal = (TextView) viewDev.findViewById(R.id.tgrandtotal);
        tgjumlah = (TextView) viewDev.findViewById(R.id.tjumlah);
        tgkembalian = (TextView) viewDev.findViewById(R.id.tkembalian);
        btnprint = (Button) viewDev.findViewById(R.id.btnprint);


        float paidTotal = UnitGlobal.getFloatValue(tpaidtotal);

        tggrandtotal.setText(UnitGlobal.convertToStrCurr(grandtotal));
        tgjumlah.setText(UnitGlobal.convertToStrCurr(paidTotal));
        tgkembalian.setText(UnitGlobal.convertToStrCurr(paidTotal - grandtotal));
        AlertDialog.Builder builder;// = new AlertDialog.Builder(this);

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(false);
        builder.setView(viewDev);

        alertDialog = builder.show();

        alertDialog.getWindow().setLayout(Math.round(0.9f * (float)helper.getWidth()), ViewGroup.LayoutParams.WRAP_CONTENT);

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
                inittrans();
            }
        });

        btnprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ngeprint
                //alertDialog.dismiss();
                if(helper.getBtDevice() != null) {
                    print();
                    alertDialog.dismiss();
                }
                else
                {
                    Intent deviceSelectIntent = new Intent(getApplicationContext(), BluetoothPrinterActivity.class);
                    startActivityForResult(deviceSelectIntent, REQ_BLUETOOTH_PRINTER);
                }
            }
        });
    }

    private void print()
    {
        //max char per paper is 40
        List<ObjEntityItem> items = dtlAdapter.detailList;
        BluetoothPrinter printer = helper.getPrinter();
        printer.setAlign(BluetoothPrinter.ALIGN_CENTER);
        printer.printText("TOKO ABCDE");
        printer.feedPaper();
        printer.printLine();
        printer.feedPaper();
        printer.setAlign(BluetoothPrinter.ALIGN_LEFT);
        printer.printText("Item Name " + "          " + "    Qty" + "        Price");
        printer.feedPaper();
        printer.printLine();
        printer.feedPaper();

        for (ObjEntityItem item : items)
        {
            String text = item.getName();
            String itemNameSpace = "";
            for(int i = 0; i < 24 - text.length(); i++) itemNameSpace += " ";
            String qtySpace = "";
            String qty = item.getQty() + "";
            String price = UnitGlobal.convertToStrCurr(item.getPrice());
            for(int i = 0; i < 16 - qty.length() - price.length(); i++) qtySpace += " ";

            printer.printText(text + itemNameSpace + qty + qtySpace + price);
            printer.feedPaper();
        }

        //
        float payment = UnitGlobal.getFloatValue(tpaidtotal);
        printer.printLine();
        printer.feedPaper();
        printer.setAlign(BluetoothPrinter.ALIGN_RIGHT);
        printer.printText("Grand Total : " + UnitGlobal.convertToStrCurr(grandtotal));
        printer.feedPaper();
        printer.setAlign(BluetoothPrinter.ALIGN_RIGHT);
        printer.printText("Pembayaran : " + UnitGlobal.convertToStrCurr(payment));
        printer.feedPaper();
        printer.setAlign(BluetoothPrinter.ALIGN_RIGHT);
        printer.printText("Kembalian : " + UnitGlobal.convertToStrCurr(payment - grandtotal));
        printer.feedPaper();
        printer.printLine();
        printer.feedPaper();
        printer.setAlign(BluetoothPrinter.ALIGN_CENTER);
        printer.printText("Terima Kasih Sudah Berbelanja");
        printer.feedPaper();
        printer.feedPaper();
        printer.feedPaper();
        printer.finish();

        inittrans();
    }

    private void initiateDetailPopupWindow(int pos) {
        try {

            LayoutInflater inflater = (LayoutInflater) PosActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.detailpopup, (ViewGroup) findViewById(R.id.itempopuplayout));

            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            alertDialog = builder.show();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final ObjEntityItem objdetail = dtlAdapter.detailList.get(pos);
            TextView txtitem = (TextView) layout.findViewById(R.id.txtitem);

            final EditText edtnotes = (EditText) layout.findViewById(R.id.edtnotes);
            final EditText myedtqty = (EditText) layout.findViewById(R.id.edtqty);

            final EditText edtprice = (EditText) layout.findViewById(R.id.edtprice);
            final EditText edtdiscamt = (EditText) layout.findViewById(R.id.edtdiscamt);

            final TextView txttotal = (TextView) layout.findViewById(R.id.txttotal);

            DetailAdapter detailAdptBundle;
            List<ObjEntityItem> detailListBundle;
            ListView lvdetailBundle;

            detailListBundle = new ArrayList<ObjEntityItem>();
            detailAdptBundle = new DetailAdapter(getApplicationContext(), R.layout.simple_list_itembundle, detailListBundle);
            lvdetailBundle = (ListView) layout.findViewById(R.id.itemlist);
            lvdetailBundle.setAdapter(detailAdptBundle);

            LinearLayout laybundle = (LinearLayout) layout.findViewById(R.id.layoutbundle);
            int dtlitemtype = Integer.parseInt(objdetail.getType());

            if (dtlitemtype == UnitConstant.ITE_TYPE_BUNDLE) {
                String strqueryitem =   "select b.id, b.code, b.name, a.qty " +
                        "from dbmitemmaterial a left join dbmitem b on b.id = a.compId " +
                        "where a.itemId = " + objdetail.getId();
                Cursor citem = dba.loadLocalTableFromRawQuery(strqueryitem);

                Log.d("Item Entry", "query : " + strqueryitem);

                if ((citem != null) && (citem.moveToFirst())) {
                    do {
                        int itemid = citem.getInt(0);
                        String itemcode = citem.getString(1);
                        String itemname = citem.getString(2);
                        int qty = citem.getInt(3);

                        Log.d("Item Entry", "brg bundle : " + itemname);

                        detailAdptBundle.add(new ObjEntityItem(itemid, "", "[" + itemcode + "] - " + itemname, "", qty, 0, "0"));

                        detailAdptBundle.notifyDataSetChanged();
                    } while (citem.moveToNext());
                }
                citem.close();
            } else {
                laybundle.setVisibility(View.GONE);
            }


            txtitem.setText(objdetail.getName());
            //txtitem.requestFocus();
            edtnotes.setText(objdetail.getNotes());
            myedtqty.setText(String.valueOf(objdetail.getQty()));
            edtprice.setText(UnitGlobal.convertToStrCurr(objdetail.getPrice()));
            edtdiscamt.setText(objdetail.getDisc());

            final float itemtotalold = objdetail.getSubtotal();
            txttotal.setText(UnitGlobal.convertToStrCurr(itemtotalold));

            final TextWatcher dtltextwatcher = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    float tmpprice = ug.getFloatValue(edtprice);
                    float tmpdisc = UnitGlobal.getDisc(edtdiscamt.getText().toString(), tmpprice, getApplicationContext());
                    txttotal.setText(ug.convertToStrCurr(ug.calcSubTotal(ug.getIntValue(myedtqty), tmpprice, tmpdisc)));
                    //txttotal.setText(ug.convertToStrCurr(ug.calcSubTotal(ug.getIntValue(myedtqty), ug.getFloatValue(edtprice), ug.getFloatValue(edtdiscamt))));
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub

                }
            };

            myedtqty.addTextChangedListener(dtltextwatcher);
            edtprice.addTextChangedListener(dtltextwatcher);
            edtdiscamt.addTextChangedListener(dtltextwatcher);

            myedtqty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        try {
                            if ((myedtqty.getText().toString().equalsIgnoreCase("")) ||
                                    (Integer.parseInt(myedtqty.getText().toString()) <= 0)) {
                                myedtqty.setText("1");
                            }
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Format angka tidak valid, mohon dicek kembali.", Toast.LENGTH_SHORT).show();
                            myedtqty.setText("1");
                        }

                    }
                }
            });
            edtprice.setOnFocusChangeListener(new myFocusListener(dtltextwatcher, getApplicationContext()));

            /*
            edtdiscamt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {

                    }
                }
            });*/

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
                        float totaldetail = (subtotal - itemtotalold) + Float.parseFloat(txttotal.getText().toString().replace(".00", "").replace(",", ""));
                        tsubtotal.setText(ug.convertToStrCurr(totaldetail));

                        float tempprice = Float.parseFloat(edtprice.getText().toString().replace(".00", "").replace(",", ""));
                        //float tempdisc = Float.parseFloat(edtdiscamt.getText().toString().replace(".00", "").replace(",", ""));

                        objdetail.update(edtnotes.getText().toString(),
                                Integer.parseInt(myedtqty.getText().toString()),
                                tempprice, edtdiscamt.getText().toString());
                        dtlAdapter.notifyDataSetChanged();
                        calcSubTotal();
                        alertDialog.dismiss();
                    }


                }
            });

            Button btndelete = (Button) layout.findViewById(R.id.btndelete);
            btndelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    new AlertDialog.Builder(PosActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Confirmation")
                            .setMessage("Do you really want to delete this item ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    subtotal = subtotal - itemtotalold;
                                    tsubtotal.setText(UnitGlobal.convertToStrCurr(subtotal));

                                    dtlAdapter.detailList.remove(objdetail);
                                    dtlAdapter.notifyDataSetChanged();

                                    calcSubTotal();

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

    public void inittrans() {

        subtotal = 0;
        disc = 0;
        tax = 0;
        service = 0;
        rounded = 0;
        grandtotal = 0;

        pctdisc = 0;
        pcttax = 0;
        pctservice = 0;

        pctflagdisc = false;
        pctflagtax = false;
        pctflagservice = false;

        tsubtotal.setText(ug.convertToStrCurr(subtotal));
        tdisc.setText(ug.convertToStrCurr(disc));
        ttax.setText(ug.convertToStrCurr(tax));
        tservice.setText(ug.convertToStrCurr(service));
        trounded.setText(ug.convertToStrCurr(rounded));
        tgrandtotal.setText(ug.convertToStrCurr(grandtotal));

        tpctdisc.setText(String.valueOf(pctdisc));
        tpcttax.setText(String.valueOf(pcttax));
        tpctservice.setText(String.valueOf(pctservice));
        tpaidtotal.setText(UnitGlobal.convertToStrCurr(0));

        dtlAdapter.detailList.clear();
        dtlAdapter.notifyDataSetChanged();

        //setStatType(View.VISIBLE, View.INVISIBLE);
    }

    public void showAlertDialog(String message){

        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(PosActivity.this);
        builder.setMessage(message);

        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                finish();
            }

        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        AlertDialog alertDialog;
        alertDialog = builder.show();
    }

    public void readBarcode()
    {
        Intent cameraIntent = new Intent(this, BarcodeReaderActivity.class);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnmenupos:
                dialog.show();
                break;
            case R.id.btnclear:
                vcode.setText("");
                break;
            case R.id.btnitemsearch:
                setStatType(View.VISIBLE, View.GONE);
                dialog.dismiss();
                break;
            case R.id.btnpayment:
                setStatType(View.GONE, View.VISIBLE);
                dialog.dismiss();
                break;
            case R.id.btncancel:
                if (dtlAdapter.detailList.size() > 0) {
                    showAlertDialog("Apakah anda yakin akan keluar dan membatalkan transaksi?");
                } else {
                    dialog.dismiss();
                    finish();
                }
                break;
            case R.id.btnscanbarcodeentry:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || Build.VERSION.SDK_INT >= 23)
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQ_CAMERA);
                else
                    readBarcode();
                break;
            case R.id.btnsave:
                saveData();
                break;
            default:
                break;
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        FragmentItem fragmentBestItem = new FragmentItem();
        //ArrayList<Drawable> all_imagesBestItem  = new ArrayList<>();
        ArrayList<String> all_imagesBestItem  = new ArrayList<>();
        ArrayList<ObjEntity> myobjBesItem = new ArrayList<>();

        Calendar mycalendar = Calendar.getInstance();

        String startDate = UnitGlobal.dateFormat("yyyy-MM-dd", mycalendar.getTime());
        mycalendar.add(Calendar.DATE, 1);
        String endDate = UnitGlobal.dateFormat("yyyy-MM-dd", mycalendar.getTime());

        Log.d("PosActivity", "startDate : " + startDate);
        Log.d("PosActivity", "endDate : " + endDate);

        String strBestItem = "select b.id, b.code, b.name, b.image, b.type " +
                "from (" +
                "select b.itemId, count(*) as jumRec " +
                "from dbtsalesdoc a left join dbtsalestrans b on b.docId = a.id " +
                "where a.void = 0 and a.docDate >= '" + startDate + "' and a.docDate < '" + endDate + "' " +
                "group by b.itemId " +
                "order by count(*) desc " +
                ") a left join dbmitem b on b.id = a.itemId " +
                "limit 0,10";

        Cursor cItemBestItem = dba.loadLocalTableFromRawQuery(strBestItem);
        Log.d("PosActivity", "sebelum ngisi Best Item");
        if ((cItemBestItem != null ) && (cItemBestItem.moveToFirst())) {
            Log.d("PosActivity", "ngisi Best Item");
            do {
                //Log.d("PosActivity", "itemID : " + cItemBestItem.getInt(0) + ", code : " + cItemBestItem.getString(1));
                all_imagesBestItem.add(cItemBestItem.getString(3));
                myobjBesItem.add(new ObjEntity(cItemBestItem.getInt(0), cItemBestItem.getString(1), cItemBestItem.getString(2), cItemBestItem.getString(4)));
            }while (cItemBestItem.moveToNext());
        }
        cItemBestItem.close();
        GridViewAdapter gridViewAdapterBestItem = new GridViewAdapter(getApplicationContext(), all_imagesBestItem, myobjBesItem);
        fragmentBestItem.setAdapter(gridViewAdapterBestItem);

        adapter.addFrag(fragmentBestItem, "Best Selling");

        Cursor cItem;
        //ArrayList<Drawable> all_images;
        ArrayList<String> all_images;
        ArrayList<ObjEntity> myobj;
        Cursor c = dba.loadLocalTableFromRawQuery("SELECT id, code, name FROM dbmdepartment");
        if ((c != null ) && (c.moveToFirst())) {
            do {
                all_images = new ArrayList<>();
                FragmentItem myfragment = new FragmentItem();
                myobj = new ArrayList<>();

                cItem = dba.loadLocalTableFromRawQuery("SELECT id, code, name, image, type FROM dbmitem where status = 0 and depId = " + c.getInt(0));
                if ((cItem != null ) && (cItem.moveToFirst())) {
                    do {
                        /*int id = getResources().getIdentifier(cItem.getString(3), "drawable", getPackageName());
                        Drawable drawable = getResources().getDrawable(id);

                        all_images.add(drawable);*/

                        all_images.add(cItem.getString(3));
                        myobj.add(new ObjEntity(cItem.getInt(0), cItem.getString(1), cItem.getString(2), cItem.getString(4)));
                    } while (cItem.moveToNext());
                }
                cItem.close();

                GridViewAdapter gridViewAdapter = new GridViewAdapter(getApplicationContext(), all_images, myobj);
                myfragment.setAdapter(gridViewAdapter);
                //myfragment.setItemAdapter(dtlAdapter);
                adapter.addFrag(myfragment, c.getString(1));

            } while (c.moveToNext());
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void opendialogboxmenupost(){
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialogbox_pos, null);
        btnentryitem = (Button) dialogView.findViewById(R.id.btnitemsearch);
        btnpayment = (Button) dialogView.findViewById(R.id.btnpayment);
        btnscanbarcode = (Button) dialogView.findViewById(R.id.btnscanbarcodeentry);
        btncancel = (Button) dialogView.findViewById(R.id.btncancel);
        dialog = adb.create();
        dialog.setView(dialogView);

        btnentryitem.setOnClickListener(this);
        btnpayment.setOnClickListener(this);
        btnscanbarcode.setOnClickListener(this);
        btncancel.setOnClickListener(this);
    }

    private void setStatType(Integer layts, Integer laypay){
        if (layts == View.VISIBLE){
            btnentryitem.setBackgroundResource(R.drawable.rb_grey);
            btnpayment.setBackgroundResource(R.drawable.rb_grey1);
        }
        else {
            btnentryitem.setBackgroundResource(R.drawable.rb_grey1);
            btnpayment.setBackgroundResource(R.drawable.rb_grey);
        }

        layoutmenuitem.setVisibility(layts);
        layoutmenupayment.setVisibility(laypay);

        layoutnc.setVisibility(laypay);
        layoutec.setVisibility(laypay);
        layoutmain.setVisibility(layts);
    }

    private void calcSubTotal() {
        subtotal = 0;
        for (int i = 0; i < dtlAdapter.detailList.size(); i++) {
            subtotal = subtotal + dtlAdapter.detailList.get(i).getSubtotal();
        }
        tsubtotal.setText(ug.convertToStrCurr(subtotal));

        calcDisc();
    }

    private void calcDisc() {
        Log.d("PosActivity", "Masuk ke calcDisc");
        tdisc.removeTextChangedListener(disctextwatcher);
        tpctdisc.removeTextChangedListener(pctdisctextwatcher);
        if (pctflagdisc) {
            //hitung disc dari pct;

            disc = pctdisc /100 * subtotal;
            tdisc.setText(ug.convertToStrCurr(disc));
        } else {
            //hitung pct dari nominal;

            if (subtotal > 0) {
                pctdisc = (disc / subtotal) * 100;
            } else {
                disc = 0;
                pctdisc = 0;
                tdisc.setText(String.valueOf(disc));
            }
            tpctdisc.setText(String.valueOf(pctdisc));
        }
        tdisc.addTextChangedListener(disctextwatcher);
        tpctdisc.addTextChangedListener(pctdisctextwatcher);

        calcTax();
    }

    private void calcTax() {
        Log.d("PosActivity", "Masuk ke calcTax");
        ttax.removeTextChangedListener(taxtextwatcher);
        tpcttax.removeTextChangedListener(pcttaxtextwatcher);
        if (pctflagtax) {
            //hitung tax dari pct;

            tax = pcttax /100 * (subtotal - disc);
            ttax.setText(ug.convertToStrCurr(tax));
        } else {
            //hitung pct tax dari nominal;

            if ((subtotal - disc) > 0) {
                pcttax = (tax / (subtotal - disc)) * 100;

            } else {
                tax = 0;
                pcttax = 0;
                ttax.setText(ug.convertToStrCurr(tax));
            }
            tpcttax.setText(String.valueOf(pcttax));
        }
        ttax.addTextChangedListener(taxtextwatcher);
        tpcttax.addTextChangedListener(pcttaxtextwatcher);

        calcService();
    }

    private void calcService() {
        Log.d("PosActivity", "Masuk ke calcService");
        tservice.removeTextChangedListener(servicetextwatcher);
        tpctservice.removeTextChangedListener(pctservicetextwatcher);
        if (pctflagservice) {
            //hitung service dari pct;

            service = pctservice /100 * (subtotal - disc);
            tservice.setText(ug.convertToStrCurr(service));
        } else {
            //hitung pct service dari nominal;

            if ((subtotal - disc) > 0) {
                pctservice = (service / (subtotal - disc)) * 100;
            } else {
                service = 0;
                pctservice = 0;
                tservice.setText(ug.convertToStrCurr(service));
            }
            tpctservice.setText(String.valueOf(pctservice));
        }
        tservice.addTextChangedListener(servicetextwatcher);
        tpctservice.addTextChangedListener(pctservicetextwatcher);

        calcGrandTotal();
    }

    private void calcGrandTotal() {
        grandtotal = subtotal - disc + tax + service + rounded;
        tgrandtotal.setText(ug.convertToStrCurr(grandtotal));
        etotal.setText(ug.convertToStrCurr(grandtotal));
        txtTotal.setText(ug.convertToStrCurr(grandtotal));
    }

    //tombol2 pada tab payment, seperti tax, discount, etc

    private class myTextWatcher implements TextWatcher {
        String type;

        public myTextWatcher(String parmtype) {
            this.type = parmtype;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d("PosActivity", "Masuk ke onTextChanged : " + type);
            switch (type) {
                case "disc" :
                    pctflagdisc = false;
                    disc = ug.getFloatValue(tdisc);
                    calcDisc();
                    break;
                case "pctdisc" :
                    pctflagdisc = true;
                    pctdisc = ug.getFloatValue(tpctdisc);
                    calcDisc();
                    break;
                case "tax" :
                    pctflagtax = false;
                    tax = ug.getFloatValue(ttax);
                    calcTax();
                    break;
                case "pcttax" :
                    pctflagtax = true;
                    pcttax = ug.getFloatValue(tpcttax);
                    calcTax();
                    break;
                case "service" :
                    pctflagservice = false;
                    service = ug.getFloatValue(tservice);
                    calcService();
                    break;
                case "pctservice" :
                    pctflagservice = true;
                    pctservice = ug.getFloatValue(tpctservice);
                    calcService();
                    break;
                case "rounded" :
                    rounded = ug.getFloatValue(trounded);
                    calcGrandTotal();
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void addNewItem(ObjEntity myobj) {
        //Toast.makeText(getApplicationContext(),"item has been clicked", Toast.LENGTH_SHORT).show();

        Log.d("FragmentItem", "Item name : " + myobj.getName());

        String mysql = "SELECT price FROM dbmprice where ctpId = " + ctpid + " and itemId = " + String.valueOf(myobj.getId());
        Log.d("FragmentItem", "query : " + mysql);
        Cursor c = dba.loadLocalTableFromRawQuery(mysql);

        float itemPrice = 0;
        if ((c != null ) && (c.moveToFirst())) {
            Log.d("FragmentItem", "masuk ke isi itemprice");
            itemPrice = c.getFloat(0);
        }
        c.close();

        dtlAdapter.add(new ObjEntityItem(myobj.getId(), myobj.getCode(), myobj.getName(), "", 1, itemPrice, "0", myobj.getType()));
        dtlAdapter.notifyDataSetChanged();
        Log.d("FragmentItem", "item has been clicked");

        calcSubTotal();

        //auto-scroll
        itemListView.setSelection(dtlAdapter.detailList.size() - 1);
    }

    public void saveData() {

        boolean valid = true;

        if (dtlAdapter.detailList.size() <= 0) {
            valid = false;
            Toast.makeText(getBaseContext(), "trans tdk bisa disimpan karna detail kosong, ", Toast.LENGTH_SHORT).show();
        }

        if ((valid) && (partnerid <= 0)) {
            valid = false;
            Toast.makeText(getBaseContext(), "trans tdk bisa disimpan karna cust belum diisi. ", Toast.LENGTH_SHORT).show();
        }

        if ((valid) && (cashbankid <= 0)) {
            valid = false;
            Toast.makeText(getBaseContext(), "trans tdk bisa disimpan karna Kas/Bank belum diisi. ", Toast.LENGTH_SHORT).show();
        }

        if ((valid) && (tcardnumber.getText().toString().equalsIgnoreCase("")) && (paymentmethod > 0)) {
            valid = false;
            Toast.makeText(getBaseContext(), "trans tdk bisa disimpan karna untuk pembayaran non cash, nomer kartu harus diisi. ", Toast.LENGTH_SHORT).show();
        }

        float paidTotal = UnitGlobal.getFloatValue(tpaidtotal);
        if ((valid) && (paidTotal < grandtotal)) {
            valid = false;
            Toast.makeText(getBaseContext(), "trans tdk bisa disimpan karna pembayaran lebih kecil dari Grand Total. ", Toast.LENGTH_SHORT).show();
        }

        if (valid) {
            saveTrans();
        }

    }

    public void saveTrans() {
        try{
            boolean issaved = false;
            boolean isdocsaved = false;
            int tempdocid = 0;
            String docdate = "";
            String docnumberpayment = "";

            final ContentValues data = new ContentValues();
            String time = ug.dateFormat("HH:mm:ss", Calendar.getInstance().getTime());
            String trialdate = ug.convertStrToDate("yyyy-MM-dd HH:mm:ss", docdate+" "+time, "yyyy-MM-dd HH:mm:ss");
            //dateFormat(, Calendar.getInstance().getTime())
            //Log.d("MB Android", "trialdate : " + trialdate);
            data.put("docDate", trialdate);

            String docnumber = ug.generateDocNumber(dba, moduletype);
            Log.d("MBKIOS ", "docnumber : " + docnumber);
            data.put("docNo", docnumber);
            data.put("docType", moduletype);
            data.put("promoId", 0);

            data.put("partnerId", partnerid);

            data.put("subTotal", subtotal);
            data.put("discountPercent", String.valueOf(pctdisc));
            data.put("discountAmount", disc);
            data.put("taxDPP", subtotal);
            data.put("taxPercent", String.valueOf(pcttax));
            data.put("taxAmount", tax);
            data.put("grandTotal", grandtotal);
            data.put("paidTotal", grandtotal);

            data.put("sessionId", sessionId);

            if (paymentmethod == 0) {
                data.put("isCash", 1);
                data.put("paidCash", grandtotal);
            } else {
                data.put("isCash", 0);
                data.put("paidCash", 0);
            }

            data.put("docNotes", "");
            data.put("void", 0);
            data.put("printTime", 0);
            data.put("userCreateId", userid);

            if (dba.saveToDB("dbtsalesdoc", data) > 0) {
                isdocsaved = true;
                tempdocid = Integer.parseInt(dba.getlabeldata("dbtsalesdoc where docno = '" + docnumber + "'", "ID").get(0));
            }

            if (isdocsaved) {
                ObjEntityItem objitemdtl;

                Log.d("MBKIOS", "jumlah baris : " + String.valueOf(dtlAdapter.detailList.size()));
                for (int i = 0; i < dtlAdapter.detailList.size(); i++) {

                    if (i == 0) {
                        issaved = true;
                    }


                    objitemdtl = dtlAdapter.detailList.get(i);

                    ContentValues datatrans = new ContentValues();

                    datatrans.put("docId", tempdocid);
                    datatrans.put("itemId", objitemdtl.getId());
                    datatrans.put("line", i + 1);
                    datatrans.put("itemName", objitemdtl.getName());
                    datatrans.put("qty", objitemdtl.getQty());

                    datatrans.put("price", objitemdtl.getPrice());
                    datatrans.put("discountAmount", objitemdtl.getDisc());

                    if ((issaved) && (dba.saveToDB("dbtsalestrans", datatrans)  > 0)) {

                    } else {
                        issaved = false;
                    }
                }

                if (issaved) {
                    //saving the payment
                    ContentValues datapayment = new ContentValues();

                    docnumberpayment = ug.generateDocNumber(dba, UnitConstant.TT_AR_PAYMENT);

                    float paidTotal = UnitGlobal.getFloatValue(tpaidtotal);

                    datapayment.put("partnerId", partnerid);
                    datapayment.put("accId", cashbankid);
                    datapayment.put("docDate", trialdate);
                    datapayment.put("docNo", docnumberpayment);
                    datapayment.put("docType", UnitConstant.TT_AR_PAYMENT);
                    datapayment.put("term", "0");
                    datapayment.put("paymentType", paymentmethod);
                    datapayment.put("paymentRef", tcardnumber.getText().toString());
                    datapayment.put("grandTotal", grandtotal);
                    datapayment.put("paidTotal", paidTotal);
                    datapayment.put("overpaid", paidTotal - grandtotal);
                    datapayment.put("isPos", "1");
                    datapayment.put("sessionId", sessionId);
                    datapayment.put("docNotes", "");
                    datapayment.put("void", "0");
                    datapayment.put("userCreateId", userid);

                    if ((issaved) && (dba.saveToDB("dbtamtpaymentdoc", datapayment)  > 0)) {
                        int paymentdocid = Integer.parseInt(dba.getlabeldata("dbtamtpaymentdoc where docno = '" + docnumberpayment + "'", "ID").get(0));

                        ContentValues datapaymentdtl = new ContentValues();

                        datapaymentdtl.put("docId",paymentdocid);
                        datapaymentdtl.put("paidDocId",tempdocid);
                        datapaymentdtl.put("paidDocType", moduletype);
                        datapaymentdtl.put("line","1");
                        datapaymentdtl.put("discountStr","");
                        datapaymentdtl.put("discountAmount",0);
                        datapaymentdtl.put("amount",grandtotal);

                        if ((issaved) && (dba.saveToDB("dbtamtpaymenttrans", datapaymentdtl)  > 0)) {

                        } else {
                            issaved = false;
                        }

                    } else {
                        issaved = false;
                    }


                }




            }

            if (issaved) {
                initiateSummaryPopupWindow();
                Toast.makeText(getBaseContext(), "Save trans succeed with number : " + docnumber + ", payment with number : " + docnumberpayment, Toast.LENGTH_SHORT).show();
                //commit
                //inittrans();
            } else {
                //rollback
            }

        } catch(Exception e) {
            Log.d("MBKIOS", "ga masuk");
            e.printStackTrace();
        }finally {
            //database.endtrans();
        }
    }

    public static class FragmentItem extends Fragment {

        private PosActivity activity; // outer Activity
        private GridView gridView;
        GridViewAdapter gridViewAdapter;

        public FragmentItem() {
            // Required empty public constructor
        }

        public void setAdapter(GridViewAdapter parmgridViewAdapter) {
            this.gridViewAdapter = parmgridViewAdapter;
        }


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activity = (PosActivity) getActivity();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment

            View rootView = inflater.inflate(R.layout.fragment_item, container, false);
            gridView =(GridView)rootView.findViewById(R.id.gridViewItem);
            gridView.setAdapter(this.gridViewAdapter);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(getApplicationContext(),"item has been clicked", Toast.LENGTH_SHORT).show();

                    activity.addNewItem(gridViewAdapter.getObj(i));
                }
            });

            return rootView;
        }

    }

}
