package com.kreators.mybizkios.report;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;

import com.evrencoskun.tableview.TableView;
import com.kreators.mybizkios.DBAdapter;
import com.kreators.mybizkios.ObjEntity;
import com.kreators.mybizkios.R;
import com.kreators.mybizkios.UnitGlobal;
import com.kreators.mybizkios.report.model.BestProductModel;
import com.kreators.mybizkios.report.model.RevenueModel;

import java.util.ArrayList;
import java.util.List;

public class ReportDetail extends AppCompatActivity
{
    private TableView reportContainer;
    private TableViewAdapter tableViewAdapter;
    private Cursor c;
    private DBAdapter dba;
    private String query;
    //private List<RowHeader> mRowHeaderList;
    //private List<ColumnHeader> mColumnHeaderList;
    //private List<List<Cell>> mCellList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);

        Intent intent = getIntent();
        int modul = intent.getIntExtra("Modul", 1);
        String start = intent.getStringExtra("Start");
        String end = intent.getStringExtra("End");

        initView();

        List<Object> data = new ArrayList<>();
        List<String> header = new ArrayList<>();
        List<Integer> aligns = new ArrayList<>();

        if(modul == 0) //Daily Report
        {
            query = "SELECT a.docdate, sum(a.subtotal), sum(a.grandtotal), sum(a.paidtotal), b.code, b.name, e.code, d.paymentref " +
                    "FROM dbtsalesdoc a " +
                    "JOIN dbmpartner b on b.id = a.partnerid " +
                    "JOIN dbtamtpaymenttrans c on c.paidDocId = a.id " +
                    "JOIN dbtamtpaymentdoc d on d.id = c.docid " +
                    "JOIN dbmaccount e on e.id = d.accid " +
                    "WHERE DATE(a.docdate) = DATE('now')" +
                    "GROUP BY a.docdate, b.code, b.name, e.code, d.paymentref";

            c = dba.loadLocalTableFromRawQuery(query);

            if ((c != null) && (c.moveToFirst()))
            {
                do {
                    RevenueModel model = new RevenueModel();
                    model.setDocdate(c.getString(0));
                    model.setSubtotal(UnitGlobal.convertToStrCurr(c.getFloat(1)));
                    model.setGrandTotal(UnitGlobal.convertToStrCurr(c.getFloat(2)));
                    model.setPaidTotal(UnitGlobal.convertToStrCurr(c.getFloat(3)));
                    model.setCode(c.getString(4));
                    model.setName(c.getString(5));
                    model.setPaymentType(c.getString(6));
                    model.setPaymentMethod(c.getString(7));
                    data.add(model);
                } while (c.moveToNext());
                c.close();
                header.add("Date"); header.add("Cust Code"); header.add("Cust Name");
                header.add("Payment Type"); header.add("Card Number");
                header.add("Subtotal"); header.add("Grand Total"); header.add("Paid Total");
                aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT);
                aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT);
                aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT);
            }
            else
            {

            }
        }
        else if (modul == 1) //Best Product
        {
            float totalTrans;
            query = "SELECT SUM(b.qty) FROM dbtsalesdoc a JOIN dbtsalestrans b ON b.docid = a.id WHERE DATE(a.docdate) BETWEEN '" + start + "' AND '" + end + "' ";
            c = dba.loadLocalTableFromRawQuery(query);

            if(c != null && c.moveToFirst())
            {
                totalTrans = (float)c.getInt(0);
                c.close();
                query = "SELECT a.itemname, sum(a.qty), sum(a.price * a.qty), a.price " +
                        "FROM dbtsalestrans a " +
                        "JOIN dbtsalesdoc b on a.docid = b.id and DATE(b.docdate) BETWEEN '" + start + "' AND '" + end + "' " +
                        "GROUP BY a.itemname, a.price";
                c = dba.loadLocalTableFromRawQuery(query);

                if ((c != null) && (c.moveToFirst())) //ada transaksi
                {
                    do {
                        BestProductModel model = new BestProductModel();
                        model.setItemName(c.getString(0));
                        model.setQty(c.getInt(1));
                        model.setPrice(UnitGlobal.convertToStrCurr(c.getFloat(3)));
                        model.setTotalPrice(UnitGlobal.convertToStrCurr(c.getFloat(2)));
                        model.setTotalTrans(Math.round(c.getInt(1) / totalTrans * 10000) / 100f + "%");
                        data.add(model);
                    } while (c.moveToNext());
                    header.add("Item Name"); header.add("Qty"); header.add("%Contrib");
                    header.add("Price");header.add("Total");
                    aligns.add(Gravity.LEFT); aligns.add(Gravity.RIGHT);
                    aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT);
                    c.close();
                }
            }
            else //tidak ada transaksi
            {

            }
        }
        else if (modul == 2) //Revenue
        {
            query = "SELECT a.docdate, sum(a.subtotal), sum(a.grandtotal), sum(a.paidtotal), b.code, b.name, e.code, d.paymentref " +
                    "FROM dbtsalesdoc a " +
                    "JOIN dbmpartner b on b.id = a.partnerid " +
                    "JOIN dbtamtpaymenttrans c on c.paidDocId = a.id " +
                    "JOIN dbtamtpaymentdoc d on d.id = c.docid " +
                    "JOIN dbmaccount e on e.id = d.accid " +
                    "WHERE DATE(a.docdate) BETWEEN '" + start + "' and '" + end + "' " +
                    "GROUP BY a.docdate, b.code, b.name, e.code, d.paymentref";

            c = dba.loadLocalTableFromRawQuery(query);

            if ((c != null) && (c.moveToFirst()))
            {
                do {
                    RevenueModel model = new RevenueModel();
                    model.setDocdate(c.getString(0));
                    model.setSubtotal(UnitGlobal.convertToStrCurr(c.getFloat(1)));
                    model.setGrandTotal(UnitGlobal.convertToStrCurr(c.getFloat(2)));
                    model.setPaidTotal(UnitGlobal.convertToStrCurr(c.getFloat(3)));
                    model.setCode(c.getString(4));
                    model.setName(c.getString(5));
                    model.setPaymentType(c.getString(6));
                    model.setPaymentMethod(c.getString(7));
                    data.add(model);
                } while (c.moveToNext());
                c.close();
                header.add("Date"); header.add("Cust Code"); header.add("Cust Name");
                header.add("Payment Type"); header.add("Card Number");
                header.add("Subtotal"); header.add("Grand Total"); header.add("Paid Total");
                aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT);
                aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT);
                aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT);
            }
        }
        else if (modul == 3) //Revenue per Customer
        {
            query = "SELECT sum(a.subtotal), sum(a.grandtotal), sum(a.paidtotal), b.code, b.name, e.code, d.paymentref " +
                    "FROM dbtsalesdoc a " +
                    "JOIN dbmpartner b on b.id = a.partnerid " +
                    "JOIN dbtamtpaymenttrans c on c.paidDocId = a.id " +
                    "JOIN dbtamtpaymentdoc d on d.id = c.docid " +
                    "JOIN dbmaccount e on e.id = d.accid " +
                    "WHERE DATE(a.docdate) BETWEEN '" + start + "' and '" + end + "' " +
                    "GROUP BY b.code, b.name, e.code, d.paymentref";

            c = dba.loadLocalTableFromRawQuery(query);

            if ((c != null) && (c.moveToFirst()))
            {
                do {
                    RevenueModel model = new RevenueModel();
                    model.setSubtotal(UnitGlobal.convertToStrCurr(c.getFloat(0)));
                    model.setGrandTotal(UnitGlobal.convertToStrCurr(c.getFloat(1)));
                    model.setPaidTotal(UnitGlobal.convertToStrCurr(c.getFloat(2)));
                    model.setCode(c.getString(3));
                    model.setName(c.getString(4));
                    model.setPaymentType(c.getString(5));
                    model.setPaymentMethod(c.getString(6));
                    data.add(model);
                } while (c.moveToNext());
                c.close();
                header.add("Cust Code"); header.add("Cust Name");
                header.add("Payment Type"); header.add("Card Number");
                header.add("Subtotal"); header.add("Grand Total"); header.add("Paid Total");
                aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT);
                aligns.add(Gravity.LEFT); aligns.add(Gravity.LEFT);
                aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT); aligns.add(Gravity.RIGHT);
            }
        }

        tableViewAdapter.setUserList(data, header, aligns);
    }

    private void initView()
    {
        reportContainer = findViewById(R.id.reportContainer);
        initializeTableView(reportContainer);

        dba = DBAdapter.getInstance(this);
    }

    private void initializeTableView(TableView tableView){

        // Create TableView Adapter
        tableViewAdapter = new TableViewAdapter(this);
        tableView.setAdapter(tableViewAdapter);

        // Create listener
        tableView.setTableViewListener(new TableViewListener(tableView));
    }
}
