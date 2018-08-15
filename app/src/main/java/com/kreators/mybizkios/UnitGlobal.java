package com.kreators.mybizkios;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.StrictMode;
import android.telephony.TelephonyManager;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.nio.channels.FileChannel;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class UnitGlobal {
    static NumberFormat nfcurr = null;
    static NumberFormat nfqty = null;
    //static DBAdapter database;
    static Cursor c;
    static String[] tableName = new String[1000];
    static UnitConstant uc;

    public UnitGlobal(){

        tableName[uc.TT_MASTER_DEPT] = "dbmdepartment";
        tableName[uc.TT_MASTER_CUST_TYPE] = "dbmcusttype";
        tableName[uc.TT_MASTER_PARTNER] = "dbmpartner";
        tableName[uc.TT_MASTER_ITEM] = "dbmitem";
        tableName[uc.TT_MASTER_ACCOUNTS] = "dbmaccount";
        tableName[uc.TT_MASTER_SESSION] = "dbmsession";
        tableName[uc.TT_MASTER_USER] = "dbmuser";
    }

    private static void init() {
        if ((nfcurr == null) || (nfqty == null)) {
            nfcurr = NumberFormat.getInstance();
            nfcurr.setMinimumFractionDigits(2);
            nfcurr.setMaximumFractionDigits(2);

            nfqty = NumberFormat.getInstance();
            nfqty.setMinimumFractionDigits(1);
            nfqty.setMaximumFractionDigits(1);
        }
    }

    public static float getFloatValue(EditText pedt) {
        /*
            method ini berfungsi utk mendapatkan qty dalam bentuk float(desimal 2 angka blk koma) dari var string
         */
        float result = 0;

        try {
            result = (pedt.getText().toString().equalsIgnoreCase("")) ? 0 : Float.parseFloat(pedt.getText().toString().replace(".00", "").replace(",", ""));
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    public static int getIntValue(EditText pedt) {
        /*
            method ini berfungsi utk mendapatkan qty dalam bentuk float(desimal 2 angka blk koma) dari var string
         */
        int result = 0;

        try {
            result = (pedt.getText().toString().equalsIgnoreCase("")) ? 0 : Integer.parseInt(pedt.getText().toString().replace(".00", "").replace(".0", "").replace(",", ""));
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    public static String convertToStrCurr(float parmnumber) {
        /*
            method ini berfungsi utk mengubah number ke string sudah dalam keadaan terformat sbg currency
            dan siap utk ditampilkan
         */
        init();
        String tmpresult = nfcurr.format(parmnumber);

        return tmpresult;
    }

    public static void setNumber(EditText pedt, boolean hasFocus, TextWatcher ptextwather) {
        /*
            method ini berfungsi utk set number dalam bentuk desimal atau dlm bentuk nominal ketika text edit focus atau tdk focus
         */
        init();
        if (ptextwather != null) {
            pedt.removeTextChangedListener(ptextwather);
        }
        Number numbertemp = null;
        String strvalue = pedt.getText().toString();
        if(hasFocus){
            try {
                numbertemp = nfcurr.parse(strvalue);
                pedt.setText(String.valueOf(numbertemp));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else {
            if (pedt.getText().toString().equalsIgnoreCase("")) {
                strvalue = "0";
            }
            pedt.setText(nfcurr.format(Float.parseFloat(strvalue)));

        }

        if (ptextwather != null) {
            pedt.addTextChangedListener(ptextwather);
        }

    }

    static public float getDisc(String parmdisc, float parmprice, Context ctx) {
        float result = 0;

        String mydisc = parmdisc;

        try {
            if (mydisc.equalsIgnoreCase("")) {
                //do nothing
            } else if (mydisc.indexOf("%") >= 0) {
                //persentase
                mydisc = mydisc.replace("%", "");
                Float tmpdisc = Float.parseFloat(mydisc);

                if (parmprice > 0) {
                    result = tmpdisc / 100 * parmprice;
                }

            } else {
                //nominal
                result = Float.parseFloat(mydisc);
            }
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(ctx, "Disc is not valid, please check it again", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    public static float calcSubTotal(int pqty, float pprice, float ppdisc) {
        /*
            method ini berfungsi utk menghitung sub total
        */
        return (pqty * (pprice - ppdisc));
    }

    public static String dateFormat(String formatstr, Date parmdate) {
        /*
            method ini berfungsi utk mengubah number ke string sudah dalam keadaan terformat sbg currency
            dan siap utk ditampilkan
         */

        //init();
        //String tmpresult = nfcurr.format(parmnumber);
        SimpleDateFormat mydateformat = new SimpleDateFormat(formatstr);
        String tmpresult = mydateformat.format(parmdate);

        return tmpresult;
    }

    public static String convertStrToDate(String formatstr, String parmdate) {
        /*
            method ini berfungsi utk mengubah string ke datetime, misalkan dari sql diubah ke date
            dgn format yg berbeda
         */

        //init();
        //String tmpresult = nfcurr.format(parmnumber);
        //SimpleDateFormat mydateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat mydateformat = new SimpleDateFormat("yyyy-MM-dd");
        Date tmpdate = Calendar.getInstance().getTime();

        Log.d("MB Kiosk", "parmdate : " + parmdate);

        try {
            tmpdate = mydateformat.parse(parmdate);
        } catch (ParseException ex) {
            Log.d("MB Android","error parse date");
        }

        String tmpresult = dateFormat(formatstr, tmpdate);

        return tmpresult;
    }

    public static String convertStrToDate(String formatstr, String parmdate, String formatsource) {
        /*
            method ini berfungsi utk mengubah string ke datetime, misalkan dari sql diubah ke date
            dgn format yg berbeda
         */

        //init();
        //String tmpresult = nfcurr.format(parmnumber);
        //SimpleDateFormat mydateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat mydateformat = new SimpleDateFormat(formatsource);
        Date tmpdate = Calendar.getInstance().getTime();

        Log.d("MB Android", "parmdate : " + parmdate);

        try {
            tmpdate = mydateformat.parse(parmdate);
        } catch (ParseException ex) {
            Log.d("MB Android","error parse date");
        }

        String tmpresult = dateFormat(formatstr, tmpdate);

        return tmpresult;
    }

    public static String generateDocNumber(DBAdapter database, Integer module){
        Integer ctrdocno = 0;
        String tempdocno = database.getlabeldata(" dbscount where DocType = " + String.valueOf(module),
                "ifnull(Counter, 0) as lastcounter").get(0);

        ContentValues dataupdate = new ContentValues();
        if ((tempdocno.equalsIgnoreCase(""))  ||  (tempdocno.equalsIgnoreCase("null"))) {
            tempdocno = "0";
            dataupdate.put("Counter", String.valueOf(ctrdocno));
            dataupdate.put("Prefix", UnitGlobal.getPrefix(module));
            dataupdate.put("DocType", String.valueOf(module));
            if (database.saveToDB("dbscount", dataupdate) > 0) {
                Log.d("MobileApp", "Creating record in dbscount");
            }
            dataupdate.clear();
        }

        ctrdocno = Integer.parseInt(tempdocno) + 1;
        dataupdate.put("Counter", String.valueOf(ctrdocno));
        String tempdocnumber = String.valueOf(ctrdocno);
        int ctr;
        for (ctr = 0; ctr < 7 - String.valueOf(ctrdocno).length(); ctr++) {
            tempdocnumber = '0' + tempdocnumber;
        }

        if (database.updateToDB("dbscount", dataupdate, "DocType = " + String.valueOf(module)) > 0) {
            Log.d("MobileApp", "Update counter in dbscount");
        }
        return getPrefix(module) + "-" + tempdocnumber;
    }
    public static String getPrefix(int parmmodule) {
        /*
            method ini berfungsi utk mendapatkan prefix trans
         */

        String tmpresult = "";

        switch (parmmodule) {
            case UnitConstant.TT_SALES_INVOICE:
                tmpresult = "JL";
                break;
            case UnitConstant.TT_SALES_RETURN:
                tmpresult = "RJ";
                break;
            case UnitConstant.TT_AR_PAYMENT:
                tmpresult = "PH";
                break;
        }


        return tmpresult;
    }




    public static String getImei(Context ctx){
        TelephonyManager mngr = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);

        return mngr.getDeviceId();
    }

    //=======================================//
    //parmMustList = field yang harus diisi
    //parmFieldList = field yang ingin diisi
    //parmValueList = isi nilai dari field (waktu valuelist.add urutannya harus sama dengan fieldlist)
    //=======================================//
    public void OpenIntent(Context ctx, Class cls, List<String> parmFieldList, List<String> parmValueList ){
        int i, countFL;
        Intent o = new Intent(ctx, cls);
        if (parmFieldList != null){
            countFL = parmFieldList.size();
            for(i=0; i < countFL; i++){
                o.putExtra(parmFieldList.get(i), parmValueList.get(i));
            }

        }
        ctx.startActivity(o);
    }



    public Boolean checkEmptyData(Context ctx, List<String> parmMustList, List<String> parmFieldList, List<String> parmValueList){
        int i = 0, ifield, countML;
        if (parmMustList != null) {
            countML = parmMustList.size();
            for (i = 0; i < countML; i++) {
                ifield = parmFieldList.indexOf(parmMustList.get(i));
                if (parmValueList.get(ifield).isEmpty()) {
                    Toast.makeText(ctx, parmMustList.get(i) + " cannot be blank", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }

    public Boolean checkDuplicateCode(Context ctx, Integer parmModule, List<String> parmFieldList, List<String> parmValueList, DBAdapter dba){

        int code = parmFieldList.indexOf("code");
        Log.d("isi", code+" | "+tableName[parmModule]+" | "+parmValueList.get(code));
        c = dba.loadLocalTableFromRawQuery("SELECT count(code) as code FROM "+tableName[parmModule]+" WHERE code = '"+parmValueList.get(code)+"'");
        if ((c != null ) && (c.moveToFirst())) {
            do {
                if(c.getInt(0) > 0 ){
                    Toast.makeText(ctx, parmValueList.get(0)+" has been used.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            } while (c.moveToNext());
        }

        return true;
    }

    public Integer insertMasterData(String tablename, List<String> parmFieldList, List<String> parmValueList, DBAdapter dba){
        int i, countFL, id;

        countFL = parmFieldList.size();
        ContentValues data = new ContentValues();
        for(i=0; i < countFL; i++) {
            Log.d("insertMasterData", "field: " + parmFieldList.get(i));
            Log.d("insertMasterData", "value: " + parmValueList.get(i));
            data.put(parmFieldList.get(i), parmValueList.get(i));
        }
        id = (int) dba.saveToDB(tablename, data);

        return id;
    }

    public Integer updateMasterData(String tablename, List<String> parmFieldList, List<String> parmValueList, DBAdapter dba, Integer parmid){
        int i, countFL, id;

        countFL = parmFieldList.size();
        ContentValues data = new ContentValues();
        for(i=0; i < countFL; i++) {
            data.put(parmFieldList.get(i), parmValueList.get(i));
        }

        id = (int) dba.updateToDB(tablename, data, "id = " + String.valueOf(parmid));

        return id;
    }

    public Boolean checkValidData(Context ctx, Integer parmModule, List<Integer> parmTypeList, List<String> parmMustList, List<String> parmFieldList, List<String> parmValueList, DBAdapter dba ){
        int i = 0, countTL;
        Log.d("checkvaliddata", "masuk");
        Boolean result = true;
        countTL = parmTypeList.size();
        while ((result) && (i < countTL)) {
            Log.d("masuk sini ", ""+i);
            switch(parmTypeList.get(i)){
                case UnitConstant.CHECK_CODE_DOUBLE:
                    result = checkDuplicateCode(ctx, parmModule, parmFieldList, parmValueList, dba);
                    break;
                case UnitConstant.CHECK_EMPTY_FIELD:
                    result = checkEmptyData(ctx, parmMustList, parmFieldList, parmValueList);
                    break;
                default:
                    break;
            }
            i++;
        }
        return result;
    }


    public void clearForm(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                ((EditText)view).setText("");
            }

            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                clearForm((ViewGroup)view);
        }
    }


    public int getIndexByString(Spinner spinner, String string) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)) {
                index = i;
                break;
            }
        }
        return index;
    }


}