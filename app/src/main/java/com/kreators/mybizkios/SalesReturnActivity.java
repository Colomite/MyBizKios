package com.kreators.mybizkios;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SalesReturnActivity extends AppCompatActivity {
    DetailAdapter detailAdpt;
    DBAdapter database;
    List<ObjEntityItem> detailList;

    EditText edtitem, edtqty;
    TextView vsubtot, vcust;
    int moduletype, userid, sessionId, action, docid, custid, spinnerPos, ctpid, paymentmethod;
    int cashbankid;
    String tablename, tabledtlname, docnumber, docdate, accode, cardNo;

    float subtotal, paidtotal, disc, grandtotal;
    float tax, rounded;
    float pctdisc, pcttax;

    boolean pctflagdisc, pctflagtax;

    EditText edtdiscnotastr;
    EditText edtdiscnota;
    EditText edtjumlah;
    EditText edtaccode, edtcardno;
    private myTextWatcher disctextwatcher, pctdisctextwatcher;
    private myTextWatcher taxtextwatcher, pcttaxtextwatcher;
    private myTextWatcher servicetextwatcher, pctservicetextwatcher;
    private myTextWatcher roundedtextwatcher;

    AlertDialog alertDialogPaymentPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesreturn);

        database = DBAdapter.getInstance(this);

        edtqty = (EditText) findViewById(R.id.edtqtyitem);
        edtitem = (EditText) findViewById(R.id.edtitem);
        vsubtot = (TextView) findViewById(R.id.edttotal);

        userid = 1;

        tablename = "dbtsalesdoc";
        tabledtlname = "dbtsalestrans";
        moduletype = UnitConstant.TT_SALES_RETURN;
        action = getIntent().getExtras().getInt("action");
        docid = getIntent().getExtras().getInt("id");

        detailList = new ArrayList<ObjEntityItem>();
        //detailAdpt = new DetailAdapter(getApplicationContext(), R.layout.simple_list_item_1, detailList);
        detailAdpt = new DetailAdapter(getApplicationContext(), R.layout.listview_item, detailList);
        final ListView lvdetail = (ListView) findViewById(R.id.itemlist);
        lvdetail.setAdapter(detailAdpt);

        inittrans(tablename, tabledtlname, action, docid);

        ctpid = 0;
        Cursor cpartnertype = database.loadLocalTableFromRawQuery("select id, code from dbmcusttype where status = 0 order by id desc");
        Log.d("PosActivity", "sebelum ngisi partner type");

        if ((cpartnertype != null ) && (cpartnertype.moveToFirst())) {
            do {
                ctpid = cpartnertype.getInt(0);
            } while (cpartnertype.moveToNext());
        }
        cpartnertype.close();

        custid = 0;
        String querypartner = "select id, code from dbmpartner where status = 0 and ctpId = " + String.valueOf(ctpid) + " order by id desc";
        Cursor cpartner = database.loadLocalTableFromRawQuery(querypartner);
        if ((cpartner != null ) && (cpartner.moveToFirst())) {
            do {
                custid = cpartner.getInt(0);
            }while (cpartner.moveToNext());
        }
        cpartner.close();

        Button btnadd = (Button) findViewById(R.id.btnaddnewitem);
        btnadd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Log.d("MB Android", "masuk ke btnadd click listener");

                if (!additem()) {
                    //tdk ditemukan
                    Bundle bundle = new Bundle();
                    bundle.putInt("Module", 1);
                    bundle.putString("InitialSearch", edtitem.getText().toString());

                    Intent i = new Intent(SalesReturnActivity.this, SearchScreen.class);
                    i.putExtras(bundle);
                    startActivityForResult(i, 1);
                }

            }
        });

        Button btnsave = (Button) findViewById(R.id.btnsave);
        btnsave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePaymentPopupWindow(tablename);
            }
        });

        Button btncancel = (Button) findViewById(R.id.btncancel);
        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (moduletype == UnitConstant.TT_SALES_INVOICE) {
                    inittrans(tablename, tabledtlname, UnitConstant.M_NEW, 0);
                } else {
                    doFinish();
                }
            }
        });

        lvdetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                initiateDetailPopupWindow(arg2);
            }

        });



    }

    protected boolean additem() {
        boolean result = false;
        Cursor c=null;

        String itemcode = edtitem.getText().toString();

        String stritemquery =   "select a.id, a.code, a.name, b.price as price, a.type " +
                                "from dbmitem a left join dbmprice b on b.itemId = a.id " +
                                "where a.code = '" + itemcode.toUpperCase() + "' and b.ctpId = " + ctpid;
        //c = database.loadLocalTableFromRawQuery("select id, code, name, 0 as price, type from dbmitem " + extrafilter);
        c = database.loadLocalTableFromRawQuery(stritemquery);

        if ((c != null ) && (c.moveToFirst())) {
            int itemid = c.getInt(0);
            itemcode = c.getString(1);
            String itemname = c.getString(2);
            String itemtype = c.getString(4);
            float itemprice = c.getFloat(3);

            int qty = 1;

            if (!edtqty.getText().toString().equalsIgnoreCase("")) {
                qty = Integer.parseInt(edtqty.getText().toString());
            }

            if (qty <= 0) {
                Toast.makeText(getBaseContext(), "Qty harus lebih besar dari nol.", Toast.LENGTH_SHORT).show();
            } else {
                detailAdpt.add(new ObjEntityItem(itemid, itemcode, "[" + itemcode + "] - " + itemname, "", qty,
                        itemprice, "", itemtype));

                detailAdpt.notifyDataSetChanged();
                subtotal = subtotal + UnitGlobal.calcSubTotal(qty, itemprice, 0);
                vsubtot.setText(UnitGlobal.convertToStrCurr(subtotal));

                calcDisc();

                edtitem.setText("");
                edtqty.setText("1");
                result = true;
            }
        }
        c.close();

        return result;

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Log.d("MBTOUCH", "POSITION : " + String.valueOf(resultCode));

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                edtitem.setText(data.getStringExtra("code"));
                additem();
            } else if (requestCode == 2) {
                vcust.setText("[" + data.getStringExtra("code") + "] - " + data.getStringExtra("name"));
                custid = data.getIntExtra("id", 0);
            } else if (requestCode == 4) {
                edtaccode.setText(data.getStringExtra("code"));
                cashbankid = data.getIntExtra("id", 0);
                accode = data.getStringExtra("code");
            }
        }
    }

    protected void inittrans(String tablename, String tabledtlname, int parmaction, int parmid) {

        edtitem.setText("");
        detailAdpt.detailList.clear();
        detailAdpt.notifyDataSetChanged();
        docnumber = "";
        custid = 0;
        cashbankid = 0;
        accode = "";
        subtotal = (float) 0;
        paidtotal = (float) 0;

        subtotal = 0;
        vsubtot.setText(UnitGlobal.convertToStrCurr(subtotal));

        disc = 0;
        tax = 0;
        rounded = 0;
        grandtotal = 0;

        pctdisc = 0;
        pcttax = 0;


        pctflagdisc = false;
        pctflagtax = false;

        edtqty.setText("1");
        grandtotal = (float) 0;
        spinnerPos = 0;
        int qty = 1;
        docdate = UnitGlobal.dateFormat("yyyy-MM-dd HH:mm:ss", Calendar.getInstance().getTime());

        if (parmaction == UnitConstant.M_EDIT) {
            String strquery;

                strquery =  "select a.id, a.partnerid, b.code, a.name, a.discountPercent, a.discountAmount, " +
                                "a.docDate, a.accId, a.paidTotal, a.accountBuyer, a.cardNo" +
                            "from " + tablename + " a left join dbmpartner b on b.id = a.partnerid " +
                            "where a.id = " + String.valueOf(parmid);

                Cursor doc = database.loadLocalTableFromRawQuery(strquery);
                if (doc.moveToFirst()) {
                    disc = doc.getFloat(5);
                    pctdisc = doc.getFloat(4);
                    paidtotal = doc.getFloat(8);
                    accode = doc.getString(9);
                    cardNo = doc.getString(10);

                    custid = doc.getInt(1);
                    docdate = doc.getString(6);
                    spinnerPos = doc.getInt(7);
                    if (doc.getInt(7) > 0){
                        spinnerPos--;
                    }
                }
                doc.close();

                strquery =  "SELECT b.id, b.code, b.name, a.qty, a.price, a.discountAmount, a.discountStr  " +
                            "FROM " + tabledtlname + " a LEFT JOIN dbmitem b on b.id = a.itemid " +
                            "WHERE a.docid = " + String.valueOf(parmid)+" AND a.bundledId = 0";


                Cursor c = database.loadLocalTableFromRawQuery(strquery);
                if (c.moveToFirst()) {
                    do {
                        qty = c.getInt(3);
                        float itemprice = c.getFloat(4);

                        detailAdpt.add(new ObjEntityItem(c.getInt(0), c.getString(1), "[" + c.getString(1) + "] - " + c.getString(2), "", qty,
                                itemprice, c.getString(5), c.getString(6)));

                        detailAdpt.notifyDataSetChanged();
                        subtotal = subtotal + UnitGlobal.calcSubTotal(qty, itemprice-c.getFloat(5), 0) ;
                    } while (c.moveToNext());
                }
                c.close();

                vsubtot.setText(UnitGlobal.convertToStrCurr(subtotal));
                calcGrandTotal();
                edtitem.setText("");
                edtqty.setText("1");
        }

    }

    private void calcSubTotal() {
        subtotal = 0;
        for (int i=0;i<detailAdpt.detailList.size(); i++) {
            subtotal = subtotal + detailAdpt.detailList.get(i).getSubtotal();
        }
        vsubtot.setText(UnitGlobal.convertToStrCurr(subtotal));

        calcDisc();
    }

    private void calcDisc() {
        Log.d("PosActivity", "Masuk ke calcDisc");
        //edtdiscnota.removeTextChangedListener(disctextwatcher);
        //edtdiscnotastr.removeTextChangedListener(pctdisctextwatcher);
        if (pctflagdisc) {
            //hitung disc dari pct;
            Log.d("PosActivity", "Masuk ke pctflagdisc");
            Log.d("PosActivity", "pctdisc : " + pctdisc);
            Log.d("PosActivity", "subtotal : " + subtotal);
            disc = pctdisc /100 * subtotal;
            Log.d("PosActivity", "disc : " + disc);
            //edtdiscnota.setText(UnitGlobal.convertToStrCurr(disc));
        } else {
            //hitung pct dari nominal;
            Log.d("SalesReturnActivity", "Masuk ke disc");
            Log.d("SalesReturnActivity", "disc : " + disc);
            Log.d("SalesReturnActivity", "subtotal : " + subtotal);

            if (subtotal > 0) {
                pctdisc = (disc / subtotal) * 100;
            } else {
                disc = 0;
                pctdisc = 0;
                //edtdiscnota.setText(String.valueOf(disc));
            }
            Log.d("SalesReturnActivity", "pctdisc : " + pctdisc);
            Log.d("SalesReturnActivity", "disc : " + disc);
            //edtdiscnotastr.setText(String.valueOf(pctdisc));
        }
        //edtdiscnota.addTextChangedListener(disctextwatcher);
        //edtdiscnotastr.addTextChangedListener(pctdisctextwatcher);

        calcTax();
    }

    private void calcTax() {
        Log.d("PosActivity", "Masuk ke calcTax");

        /*
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
        */

        calcGrandTotal();
    }

    private void calcGrandTotal() {
        grandtotal = subtotal - disc + tax + rounded;
        //tgrandtotal.setText(UnitGlobal.convertToStrCurr(grandtotal));
        //etotal.setText(UnitGlobal.convertToStrCurr(grandtotal));
    }

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
                    disc = UnitGlobal.getFloatValue(edtdiscnota);
                    edtdiscnota.removeTextChangedListener(disctextwatcher);
                    edtdiscnotastr.removeTextChangedListener(pctdisctextwatcher);

                    calcDisc();
                    if (subtotal <= 0) {
                        edtdiscnota.setText(String.valueOf(disc));
                    }

                    edtdiscnotastr.setText(String.valueOf(pctdisc));

                    edtdiscnota.addTextChangedListener(disctextwatcher);
                    edtdiscnotastr.addTextChangedListener(pctdisctextwatcher);
                    break;
                case "pctdisc" :
                    pctflagdisc = true;
                    pctdisc = UnitGlobal.getFloatValue(edtdiscnotastr);
                    edtdiscnota.removeTextChangedListener(disctextwatcher);
                    edtdiscnotastr.removeTextChangedListener(pctdisctextwatcher);

                    calcDisc();
                    edtdiscnota.setText(UnitGlobal.convertToStrCurr(disc));

                    edtdiscnota.addTextChangedListener(disctextwatcher);
                    edtdiscnotastr.addTextChangedListener(pctdisctextwatcher);
                    break;
                /*
                case "tax" :
                    pctflagtax = false;
                    tax = UnitGlobal.getFloatValue(ttax);
                    calcTax();
                    break;
                case "pcttax" :
                    pctflagtax = true;
                    pcttax = UnitGlobal.getFloatValue(tpcttax);
                    calcTax();
                    break;
                case "rounded" :
                    rounded = UnitGlobal.getFloatValue(trounded);
                    calcGrandTotal();
                    break;
                */
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    protected  void doFinish() {
        Intent output = new Intent();
        setResult(RESULT_OK, output);
        finish();
    }

    protected void doSave() {
        boolean valid = true;

        if (detailAdpt.detailList.size() <= 0) {
            Toast.makeText(getBaseContext(), "Detail barang tidak boleh kosong", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid && custid <= 0) {
            Toast.makeText(getBaseContext(), "Customer harus diisi", Toast.LENGTH_SHORT).show();
            valid = false;
        }


        if (valid && cashbankid == 0){
            Toast.makeText(getApplicationContext(),"Kas/Bank harus diisi", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid && paymentmethod >= 1 && edtcardno.getText().toString().equalsIgnoreCase("")){
            Toast.makeText(getApplicationContext(),"untuk kartu debit/kredit, nomer kartu harus diisi", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        /*
        if(valid && UnitConstant.M_VERSI.equals("DEMO") && !UnitGlobal.checkDemoTrans(moduletype, database)){
            Toast.makeText(getApplicationContext(),"Maaf. Masa demo telah berakhir", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        */

        if(valid && paymentmethod == 0 && paidtotal < grandtotal){
            Toast.makeText(getApplicationContext(),"Jumlah tidak boleh kurang dari Grand Total", Toast.LENGTH_SHORT).show();
            valid = false;
        }

        if (valid) {
            savetrans();
        }
    }

    private void goEnd(){
        final AlertDialog alertDialog;
        LayoutInflater inflater = (LayoutInflater) SalesReturnActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDev = inflater.inflate(R.layout.finishpaymentpopup, null);

        TextView tggrandtotal, tgjumlah, tgkembalian;
        Button btnback = (Button) viewDev.findViewById(R.id.btnback);

        tggrandtotal = (TextView) viewDev.findViewById(R.id.tgrandtotal);
        tgjumlah = (TextView) viewDev.findViewById(R.id.tjumlah);
        tgkembalian = (TextView) viewDev.findViewById(R.id.tkembalian);

        tggrandtotal.setText(UnitGlobal.convertToStrCurr(grandtotal));
        tgjumlah.setText(UnitGlobal.convertToStrCurr(paidtotal));
        tgkembalian.setText(UnitGlobal.convertToStrCurr(paidtotal - grandtotal));
        AlertDialog.Builder builder;// = new AlertDialog.Builder(this);

        builder = new AlertDialog.Builder(this);
        //builder.setTitle(false);
        builder.setView(viewDev);

        alertDialog = builder.show();

        alertDialog.getWindow().setLayout(750, ViewGroup.LayoutParams.WRAP_CONTENT);

        btnback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                alertDialog.dismiss();
            }
        });
    }

    private void savetrans() {
        try{
            Log.d("MB Android", "masuk ke savetrans");

            boolean issaved = false;
            String docnumberpayment;

            final ContentValues data = new ContentValues();
            String time = UnitGlobal.dateFormat("HH:mm:ss", Calendar.getInstance().getTime());
            String trialdate = UnitGlobal.convertStrToDate("yyyy-MM-dd HH:mm:ss", docdate+" "+time, "yyyy-MM-dd HH:mm:ss");

            data.put("docDate", trialdate);


            docnumber = UnitGlobal.generateDocNumber(database, moduletype);
            Log.d("docnumbernya ", docnumber);
            data.put("docNo", docnumber);
            data.put("docType", moduletype);
            data.put("promoId", 0);

            Log.d("MB Android", "masuk ke savetrans 2");
            data.put("subTotal", subtotal);
            data.put("grandTotal", grandtotal);

            data.put("docNotes", "");
            data.put("void", 0);
            data.put("printTime", 0);
            data.put("userCreateId", userid);

            data.put("partnerId", custid);
            data.put("discountPercent", pctdisc);
            data.put("discountAmount", disc);
            data.put("taxDPP", subtotal);
            data.put("taxPercent", 0);
            data.put("taxAmount", 0);
            data.put("paidTotal", grandtotal);
            data.put("isCash", paymentmethod);
            data.put("sessionId", sessionId);

            if (paymentmethod == 0) {
                data.put("isCash", 1);
                data.put("paidCash", grandtotal);
            } else {
                data.put("isCash", 0);
                data.put("paidCash", 0);
            }

            boolean isdocsaved = false;
            int tempdocid = docid;

            Log.d("MB Android", "docid : " + String.valueOf(docid));

            Log.d("MB Android", "masuk ke savetrans 3");
            if (database.saveToDB(tablename, data) > 0) {
                isdocsaved = true;
                tempdocid = Integer.parseInt(database.getlabeldata(tablename + " where docno = '" + docnumber + "'", "ID").get(0));
            }

            Log.d("MB Android", "masuk ke savetrans 4");
            issaved = true;
            if (isdocsaved) {
                Log.d("MB Android", "masuk");

                int dtlid, dtlbundledid;
                ObjEntityItem objitem;

                Log.d("MB Android", "jumlah baris : " + String.valueOf(detailAdpt.detailList.size()));
                for (int i = 0; i < detailAdpt.detailList.size(); i++) {

                    if (i == 0) {
                        issaved = true;
                    }

                    objitem = detailAdpt.detailList.get(i);
                    Log.d("MB Android", "baris ke : " + String.valueOf(i));
                    ContentValues datatrans = new ContentValues();

                    int dtlItemId = objitem.getId();
                    datatrans.put("docId", tempdocid);
                    datatrans.put("itemId", dtlItemId);
                    datatrans.put("line", i + 1);
                    datatrans.put("itemName", objitem.getName());
                    datatrans.put("qty", objitem.getQty());
                    datatrans.put("price", objitem.getPrice());
                    datatrans.put("discountAmount", objitem.getDisc());

                    dtlid = (int) database.saveToDB(tabledtlname, datatrans);

                    if (dtlid > 0) {
                        System.out.println("masuk dtlid > 0");

                        int dtlItemType = Integer.parseInt(objitem.getType());

                        if(dtlItemType == UnitConstant.ITE_TYPE_BUNDLE){
                            /*
                            int ib = -1;
                            String query = "SELECT a.id, a.code, a.name, a.unit, b.qty FROM dbmitemmaterial b LEFT JOIN dbmitem a ON a.id = b.compId WHERE b.itemId = " + dtlItemId;
                            Log.d("POS ANDROID", "item id yg akan disimpan : " + String.valueOf(dtlItemId));
                            Log.d("POS ANDROID", "query cari item material : " + query);
                            Cursor d = database.loadLocalTableFromRawQuery(query);
                            if ((d != null) && (d.moveToFirst())) {
                                do {
                                    System.out.println("masuk bundle querynya " + query);
                                    ContentValues databundle = new ContentValues();
                                    databundle.put("docId", tempdocid);
                                    databundle.put("itemId", d.getInt(0));
                                    databundle.put("unitId", 0);
                                    databundle.put("line", ib);
                                    databundle.put("itemName", d.getString(2));
                                    databundle.put("qty", objitem.getQty() * d.getFloat(4));
                                    databundle.put("qtyDef", objitem.getQty() * d.getFloat(4));

                                    databundle.put("price", 0);

                                    databundle.put("transid", 0);
                                    databundle.put("qtySettled", 0);
                                    databundle.put("discountStr", 0);
                                    databundle.put("discountAmount", 0);
                                    databundle.put("isTaxed", 1);

                                    databundle.put("bundledId", dtlid);
                                    databundle.put("qtyM", d.getFloat(4));


                                    dtlbundledid = (int) database.saveToDB(tabledtlname, databundle);
                                    ib--;
                                } while (d.moveToNext());
                            }
                            */
                        }
                        Log.d("MobileApp", "Save detail succeed with id : " + String.valueOf(dtlid));

                        //issaved = true;
                    } else {
                        issaved = false;
                    }



                }

                if (issaved) {
                    ContentValues datapayment = new ContentValues();

                    docnumberpayment = UnitGlobal.generateDocNumber(database, UnitConstant.TT_AR_PAYMENT);

                    float paidTotal = UnitGlobal.getFloatValue(edtjumlah);

                    datapayment.put("partnerId", custid);
                    datapayment.put("accId", cashbankid);
                    datapayment.put("accId", 0);
                    datapayment.put("docDate", trialdate);
                    datapayment.put("docNo", docnumberpayment);
                    datapayment.put("docType", UnitConstant.TT_AP_PAYMENT);
                    datapayment.put("term", "0");
                    datapayment.put("paymentType", paymentmethod);
                    datapayment.put("paymentRef", edtcardno.getText().toString());
                    datapayment.put("grandTotal", grandtotal);
                    datapayment.put("paidTotal", paidTotal);
                    datapayment.put("overpaid", paidTotal - grandtotal);
                    datapayment.put("isPos", "0");
                    datapayment.put("sessionId", sessionId);
                    datapayment.put("docNotes", "");
                    datapayment.put("void", "0");
                    datapayment.put("userCreateId", userid);

                    if ((issaved) && (database.saveToDB("dbtamtpaymentdoc", datapayment)  > 0)) {
                        int paymentdocid = Integer.parseInt(database.getlabeldata("dbtamtpaymentdoc where docno = '" + docnumberpayment + "'", "ID").get(0));

                        ContentValues datapaymentdtl = new ContentValues();

                        datapaymentdtl.put("docId",paymentdocid);
                        datapaymentdtl.put("paidDocId",tempdocid);
                        datapaymentdtl.put("paidDocType", moduletype);
                        datapaymentdtl.put("line","1");
                        datapaymentdtl.put("discountStr","");
                        datapaymentdtl.put("discountAmount",0);
                        datapaymentdtl.put("amount",grandtotal);

                        if ((issaved) && (database.saveToDB("dbtamtpaymenttrans", datapaymentdtl)  > 0)) {

                        } else {
                            issaved = false;
                        }

                    } else {
                        issaved = false;
                    }
                }



                if (issaved) {
                    Toast.makeText(getBaseContext(), "Save trans succeed with number : " + docnumber, Toast.LENGTH_SHORT).show();
                    alertDialogPaymentPopup.dismiss();
                    goEnd();
                    inittrans(tablename, tabledtlname, UnitConstant.M_NEW, 0);
                } else {
                    //roll back
                }

            }



        } catch(Exception e) {
            Log.d("MB Android", "ga masuk");
            e.printStackTrace();
        }finally {
            //database.endtrans();
        }

    }

    private void initiateDetailPopupWindow(int pos) {

        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) SalesReturnActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.detailpopup, (ViewGroup) findViewById(R.id.itempopuplayout));

            final AlertDialog alertDialog;
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setView(layout);

            alertDialog = builder.show();
            alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

            final ObjEntityItem objdetail = detailAdpt.detailList.get(pos);
            TextView txtitem = (TextView) layout.findViewById(R.id.txtitem);
            final EditText edtnotes = (EditText) layout.findViewById(R.id.edtnotes);
            final EditText myedtqty = (EditText) layout.findViewById(R.id.edtqty);

            final EditText edtprice = (EditText) layout.findViewById(R.id.edtprice);
            final EditText edtdiscamt = (EditText) layout.findViewById(R.id.edtdiscamt);

            edtnotes.setVisibility(View.GONE);
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
                Cursor citem = database.loadLocalTableFromRawQuery(strqueryitem);

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
            myedtqty.setText(String.valueOf(objdetail.getQty()));

            edtprice.setText(UnitGlobal.convertToStrCurr(objdetail.getPrice()));
            edtdiscamt.setText(objdetail.getDisc());

            final float itemtotalold = objdetail.getSubtotal();
            txttotal.setText(UnitGlobal.convertToStrCurr(itemtotalold));

            final TextWatcher dtltextwatcher = new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                    /*float tempdiscamt = UnitGlobal.getFloatValue(edtdiscamt);
                    txttotal.setText(UnitGlobal.convertToStrCurr(UnitGlobal.calcSubTotal(UnitGlobal.getIntValue(myedtqty), UnitGlobal.getFloatValue(edtprice), tempdiscamt)));*/

                    float tmpprice = UnitGlobal.getFloatValue(edtprice);
                    float tmpdisc = UnitGlobal.getDisc(edtdiscamt.getText().toString(), tmpprice, getApplicationContext());
                    txttotal.setText(UnitGlobal.convertToStrCurr(UnitGlobal.calcSubTotal(UnitGlobal.getIntValue(myedtqty), tmpprice, tmpdisc)));

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

            myedtqty.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
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
                        vsubtot.setText(UnitGlobal.convertToStrCurr(totaldetail));

                        float tempprice = Float.parseFloat(edtprice.getText().toString().replace(".00", "").replace(",", ""));

                        objdetail.update("", Integer.parseInt(myedtqty.getText().toString()),
                                tempprice, edtdiscamt.getText().toString());

                        detailAdpt.notifyDataSetChanged();
                        //detaileditpopup.dismiss();
                        calcSubTotal();
                        alertDialog.dismiss();
                    }


                }
            });

            Button btndelete = (Button) layout.findViewById(R.id.btndelete);
            if (getIntent().hasExtra("status")){
                btndelete.setEnabled(false);
            }
            btndelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub

                    new AlertDialog.Builder(SalesReturnActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Delete Confirmation")
                            .setMessage("Do you really want to delete this item ?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    subtotal = subtotal - itemtotalold;
                                    vsubtot.setText(UnitGlobal.convertToStrCurr(subtotal));

                                    detailAdpt.detailList.remove(objdetail);
                                    detailAdpt.notifyDataSetChanged();
                                    //detaileditpopup.dismiss();
                                    alertDialog.dismiss();
                                }

                            })
                            .setNegativeButton("NO", null)
                            .show();
                }
            });

            edtprice.setOnFocusChangeListener(new myFocusListener(dtltextwatcher, getApplicationContext()));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initiatePaymentPopupWindow(String tablename) {
        try {
            boolean success = true;
            LayoutInflater inflater = (LayoutInflater) SalesReturnActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.paymentpopup, (ViewGroup) findViewById(R.id.paymentpopuplayout));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.95);
//            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.90);
            builder.setView(layout);
            alertDialogPaymentPopup = builder.show();
            alertDialogPaymentPopup.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            final EditText vtgl = (EditText) layout.findViewById(R.id.vtgl);
            final TextView vgrandtotal = (TextView) layout.findViewById(R.id.vgrandtotal);
            final LinearLayout layoutdataatas = (LinearLayout) layout.findViewById(R.id.layoutdataatas);
            final LinearLayout layoutterm = (LinearLayout) layout.findViewById(R.id.layoutterm);
            final LinearLayout vnoncash = (LinearLayout) layout.findViewById(R.id.layoutnoncash);
            vnoncash.setVisibility(View.GONE);
            TextView vtotal = (TextView) layout.findViewById(R.id.vtotal);

            Log.d("SalesReturnActivity", "disc : " + disc);

            edtdiscnotastr = (EditText) layout.findViewById(R.id.edtdiscstr);

            edtdiscnota = (EditText) layout.findViewById(R.id.edtdisc);

            edtaccode = (EditText) layout.findViewById(R.id.edtaccust);
            edtcardno = (EditText) layout.findViewById(R.id.edtcardno);
            edtjumlah = (EditText) layout.findViewById(R.id.edtjumlah);
            final LinearLayout layoutbayar = (LinearLayout) layout.findViewById(R.id.layoutbayar);
            final LinearLayout layoutkembalian = (LinearLayout) layout.findViewById(R.id.layoutkembalian);
            final TextView vkembalian = (TextView) layout.findViewById(R.id.vkembalian);
            vtgl.setText(UnitGlobal.convertStrToDate("dd/MM/yyyy", docdate));

            edtaccode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //if (paymentmethod > 0) {
                    Log.d("PosActivity", "Kas Bank has been clicked, cashbankid : " + cashbankid);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Module", 4);

                    bundle.putString("InitialSearch", String.valueOf(paymentmethod));

                    Intent i = new Intent(SalesReturnActivity.this, SearchScreen.class);
                    i.putExtras(bundle);
                    startActivityForResult(i, 4);
                    //}
                }
            });

            vtgl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DateDialog(vtgl, Integer.parseInt(UnitGlobal.convertStrToDate("dd", docdate)),
                            Integer.parseInt(UnitGlobal.convertStrToDate("MM", docdate)),
                            Integer.parseInt(UnitGlobal.convertStrToDate("yyyy", docdate)));
                }
            });

            vcust = (TextView) layout.findViewById(R.id.vcust);

            String strquery = "";
            if(custid > 0 && docid > 0) {
                strquery = "select a.id, a.partnerId, b.code, a.name " +
                        "from dbtsalesdoc a left join dbmpartner b on b.id = a.partnerid " +
                        "where a.id = " + String.valueOf(docid);
            } else if(custid > 0) {
                strquery = "SELECT a.id, 0 as partnerId, a.code, a.name FROM dbmpartner a WHERE a.id = "+String.valueOf(custid);
            }

            if(!strquery.isEmpty()){
                System.out.println("masuk initiatepaymentpopup, quernya " + strquery);
                Cursor doc = database.loadLocalTableFromRawQuery(strquery);
                if (doc.moveToFirst()) {
                    if(doc.getString(2).isEmpty()){
                        vcust.setText("");
                    } else {
                        vcust.setText("[" + doc.getString(2) + "] - " + doc.getString(3));
                    }
                    System.out.println("masuk initiatepaymentpopup, custnya " + doc.getString(2));
                }
                doc.close();
            }

            edtaccode.setText(accode);
            edtcardno.setText(cardNo);
            edtjumlah.setText(UnitGlobal.convertToStrCurr(paidtotal));

            vtotal.setText(UnitGlobal.convertToStrCurr(subtotal));
            edtdiscnotastr.setText(UnitGlobal.convertToStrCurr(pctdisc));
            //disc = pctdisc * subtotal;

            edtdiscnota.setText(UnitGlobal.convertToStrCurr(disc));

            //grandtotal = calcGrandTotal();
            vgrandtotal.setText(UnitGlobal.convertToStrCurr(grandtotal));


            disctextwatcher = new myTextWatcher("disc");
            edtdiscnota.addTextChangedListener(disctextwatcher);
            edtdiscnota.setOnFocusChangeListener(new myFocusListener(disctextwatcher, getApplicationContext()));

            pctdisctextwatcher = new myTextWatcher("pctdisc");
            edtdiscnotastr.addTextChangedListener(pctdisctextwatcher);
            edtdiscnotastr.setOnFocusChangeListener(new myFocusListener(pctdisctextwatcher, getApplicationContext()));





            Spinner spinnerpayment = (Spinner) layout.findViewById(R.id.spinnerpayment);
            List<SpinnerWithTag> spinType = new ArrayList<SpinnerWithTag>();
            spinType.add(new SpinnerWithTag(UnitConstant.PAYMENT_TYPE_NAME[UnitConstant.PAYMENT_TYPE_CASH], UnitConstant.PAYMENT_TYPE_CASH));
            spinType.add(new SpinnerWithTag(UnitConstant.PAYMENT_TYPE_NAME[UnitConstant.ACCOUNT_TYPE_DEBIT_CARD], UnitConstant.ACCOUNT_TYPE_DEBIT_CARD));
            spinType.add(new SpinnerWithTag(UnitConstant.PAYMENT_TYPE_NAME[UnitConstant.ACCOUNT_TYPE_CREDIT_CARD], UnitConstant.ACCOUNT_TYPE_CREDIT_CARD));

            final ArrayAdapter<SpinnerWithTag> paymentadapter = new ArrayAdapter<SpinnerWithTag>(this,
                    R.layout.spinner_style_black, spinType);
            paymentadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerpayment.setAdapter(paymentadapter);
            spinnerpayment.setSelection(spinnerPos);

            spinnerpayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    SpinnerWithTag s = (SpinnerWithTag) parent.getItemAtPosition(position);
                    paymentmethod = s.tag;

                    edtaccode.setText("");
                    cashbankid = 0;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            Button btncancel = (Button) layout.findViewById(R.id.btncancel);
            btncancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //paymentpopup.dismiss();
                    alertDialogPaymentPopup.dismiss();
                }
            });


            Button btnsave = (Button) layout.findViewById(R.id.btnsave);
            btnsave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    //accode = String.valueOf(edtaccode.getText());
                    //cardNo = String.valueOf(edtcardno.getText());
                    paidtotal = UnitGlobal.getFloatValue(edtjumlah);
                    doSave();
                }
            });


            Button btncust = (Button) layout.findViewById(R.id.btncust);
            btncust.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    Bundle bundle = new Bundle();
                    bundle.putInt("Module", 2);
                    bundle.putString("InitialSearch", "");

                    Intent i = new Intent(SalesReturnActivity.this, SearchScreen.class);
                    i.putExtras(bundle);
                    startActivityForResult(i, 2);
                }
            });

            vcust.setEnabled(false);

            edtjumlah.setOnFocusChangeListener(new myFocusListener(null, getApplicationContext()));
            layoutkembalian.setVisibility(View.GONE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void DateDialog(final EditText parmdatedialog, int parmday, int parmmonth, int parmyear) {

        DatePickerDialog.OnDateSetListener listener=new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
            {
                parmdatedialog.setText(dayOfMonth+"/"+(monthOfYear+1)+"/"+year);
                docdate = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
            }};

        DatePickerDialog dpDialog=new DatePickerDialog(this, listener, parmyear, parmmonth-1, parmday);
        dpDialog.show();

    }

}
