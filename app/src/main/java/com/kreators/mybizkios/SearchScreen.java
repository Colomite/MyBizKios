package com.kreators.mybizkios;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 6/25/2018.
 */

public class SearchScreen extends Activity {

    DBAdapter database;
    LinearLayout custlayout;
    SearchAdapter aAdpt;

    List<ObjEntity> data;
    int module;

    String tablename = "dbmitem";
    String strInitialSearch = "";
    String fielddesc = "name";
    String sqlwhere = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen);

        database = DBAdapter.getInstance(this);
        custlayout = (LinearLayout) findViewById(R.id.custlayout);
        ListView lv = (ListView) findViewById(R.id.datalist);

        sqlwhere = " where status = 0 ";

        if(getIntent() != null) {
            Bundle extras = getIntent().getExtras();
            strInitialSearch = extras.getString("InitialSearch");
            module = extras.getInt("Module");

            switch(module) {
                case 1 :
                    //item
                    break;
                case 2 :
                    //partner => cust
                    //custlayout.setVisibility(View.VISIBLE);
                    tablename = "dbmpartner";
                    if (!strInitialSearch.equalsIgnoreCase("")) {
                        sqlwhere = sqlwhere + "and ctpId = " + strInitialSearch;
                    }
                    Log.d("SearchScreen", sqlwhere);
                    strInitialSearch = "";
                    break;
                case 3 :
                    //cust type
                    tablename = "dbmcusttype";
                    fielddesc = "'' as name";
                    break;
                case 4 :
                    //dbmaccount
                    tablename = "dbmaccount";

                    if (!strInitialSearch.equalsIgnoreCase("")) {

                        switch(Integer.parseInt(strInitialSearch)) {
                            case UnitConstant.PAYMENT_TYPE_CASH:
                                //cash
                                sqlwhere = sqlwhere + "and isBank = 0 ";
                                break;
                            case UnitConstant.ACCOUNT_TYPE_DEBIT_CARD:
                                //debit card
                                sqlwhere = sqlwhere + "and isBank = 1 and isDebit = 1 ";
                                break;
                            case UnitConstant.ACCOUNT_TYPE_CREDIT_CARD:
                                //credit card
                                sqlwhere = sqlwhere + "and isBank = 1 and isCredit = 1 ";
                                break;
                        }



                    }
                    strInitialSearch = "";

                    break;
                case 5 :
                    //item without bundle, hanya di pakai di master item ...
                    if (getIntent().hasExtra("janganbundle")){
                        int myid = extras.getInt("id");
                        sqlwhere = sqlwhere + "and id <> " + myid + " and type <> "+UnitConstant.ITE_TYPE_BUNDLE;
                    }

                    tablename = "dbmitem";
                    break;
            };
        }

        data = new ArrayList<ObjEntity>();
        getData();

        Log.d("MB Android", tablename + sqlwhere + " : " + fielddesc);

        aAdpt = new SearchAdapter(getApplicationContext(), R.layout.search_list_view, data);
        lv.setAdapter(aAdpt);

        if(!strInitialSearch.equalsIgnoreCase("")){
            aAdpt.getFilter().filter(strInitialSearch);
        }

        EditText edtsearch = (EditText) findViewById(R.id.edtsearch);
        edtsearch.setText(strInitialSearch);

        Button btncancel = (Button) findViewById(R.id.btncancel);
        btncancel.setFocusable(true);
        btncancel.setFocusableInTouchMode(true);
        btncancel.requestFocus();
        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        lv.setTextFilterEnabled(true);
        edtsearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    // We're deleting char so we need to reset the adapter data
                    aAdpt.resetData();
                }

                aAdpt.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                Intent output = new Intent();

                output.putExtra("id", aAdpt.searchList.get(arg2).getId());
                output.putExtra("code", aAdpt.searchList.get(arg2).getCode());
                output.putExtra("name", aAdpt.searchList.get(arg2).getName());
                setResult(RESULT_OK, output);
                finish();
            }

        });

        Button btnadd = (Button) findViewById(R.id.btnadd);
        btnadd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),PosActivity.class);
                i.putExtra("search", 1);
                startActivityForResult(i, 2);
            }
        });
    }

    public void getData(){
        data = database.getSearchList(tablename + sqlwhere, fielddesc);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent dataExtra) {
        Log.d("MBTOUCH", "POSITION : " + String.valueOf(resultCode));

        if (resultCode == RESULT_OK) {
            if (requestCode == 2) {

                getData();
                aAdpt.clear();
                aAdpt.addAll(data);
                aAdpt.notifyDataSetChanged();

            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.search_screen, menu);
        return true;
    }

}
