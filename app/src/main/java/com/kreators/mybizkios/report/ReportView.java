package com.kreators.mybizkios.report;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kreators.mybizkios.DBAdapter;
import com.kreators.mybizkios.Helper;
import com.kreators.mybizkios.MainActivity;
import com.kreators.mybizkios.R;
import com.kreators.mybizkios.UnitGlobal;
import com.kreators.mybizkios.report.model.BestProductModel;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ReportView extends AppCompatActivity
{
    private ListView listview_report;
    private AlertDialog dialog;
    private Calendar calendar;
    private String dateFormat;
    private SimpleDateFormat sdf;
    private TextView currentlyEdited;
    private DBAdapter dba;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);
        Helper.getInstance().scaleImage(this);

        calendar = Calendar.getInstance();
        dateFormat = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dba = DBAdapter.getInstance(this);

        initView();
        setAdapter();
        bindListener();
    }

    private void initView()
    {
        listview_report = (ListView)findViewById(R.id.listview_report);
    }

    private void initDialog(final int index)
    {
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.report_detail_dialog, null, false);

        Button viewReport = view.findViewById(R.id.btnViewReport);
        Button exportReport = view.findViewById(R.id.btnExportReport);
        final EditText startDate = view.findViewById(R.id.startDate);
        final EditText endDate = view.findViewById(R.id.endDate);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(year, month, day);
                if(calendar.getTime().getTime() <= datePicker.getMaxDate())
                    currentlyEdited.setText(sdf.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        if(index == 0) {
            view.findViewById(R.id.startTxtView).setVisibility(View.GONE);
            view.findViewById(R.id.endTxtView).setVisibility(View.GONE);
            startDate.setVisibility(View.GONE);
            endDate.setVisibility(View.GONE);
        }

        adb.setView(view);
        dialog = adb.create();
        dialog.show();

        viewReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = startDate.getText().toString();
                String end = endDate.getText().toString();
                Intent intent = null;

                if(index == 0)
                {
                    intent = new Intent(getApplicationContext(), ReportDetail.class);
                    intent.putExtra("Modul", index);
                    intent.putExtra("Start", "");
                    intent.putExtra("End", "");

                }
                else
                {
                    if (start.equals("") || end.equals(""))
                        Toast.makeText(ReportView.this, "Mohon isikan tanggal terlebih dahulu", Toast.LENGTH_SHORT).show();
                    else
                    {
                        Date dateStart = sdf.parse(start, new ParsePosition(0));
                        Date dateEnd = sdf.parse(end, new ParsePosition(0));
                        if (dateStart.after(dateEnd))
                            Toast.makeText(ReportView.this, "Pastikan tanggal valid", Toast.LENGTH_SHORT).show();
                        else //tampilkan report
                        {
                            intent = new Intent(getApplicationContext(), ReportDetail.class);
                            intent.putExtra("Modul", index);
                            intent.putExtra("Start", start);
                            intent.putExtra("End", end);
                        }
                    }
                }
                dialog.dismiss();
                startDate.setText("");
                endDate.setText("");
                if(intent != null)
                    startActivity(intent);
            }
        });

        exportReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start = startDate.getText().toString();
                String end = endDate.getText().toString();

                if(index == 0) exportToExcel(index, "", "");
                else
                {
                    if (start.equals("") || end.equals(""))
                        Toast.makeText(ReportView.this, "Mohon isikan tanggal terlebih dahulu", Toast.LENGTH_SHORT).show();
                    else
                    {
                        Date dateStart = sdf.parse(startDate.getText().toString(), new ParsePosition(0));
                        Date dateEnd = sdf.parse(endDate.getText().toString(), new ParsePosition(0));

                        if (dateStart.after(dateEnd))
                            Toast.makeText(ReportView.this, "Pastikan tanggal valid", Toast.LENGTH_SHORT).show();
                        else //expor
                            exportToExcel(index, startDate.getText().toString(), endDate.getText().toString());
                    }
                }
                dialog.dismiss();
                startDate.setText("");
                endDate.setText("");
            }
        });

        startDate.setInputType(InputType.TYPE_NULL);
        startDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                currentlyEdited = (TextView)view;
                datePickerDialog.show();
                return false;
            }
        });

        endDate.setInputType(0);
        endDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                currentlyEdited = (TextView)view;
                datePickerDialog.show();
                return false;
            }
        });
    }

    private boolean checkSDCard()
    {
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            Toast.makeText(this, "SD card tidak ditemukan", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return false;
        }

        return true;
    }

    private void exportToExcel(final int index, String start, String end)
    {
        if(!checkSDCard()) return;

        Cursor c;
        String query;
        String csvFile;
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook;
        WritableSheet sheet;
        int row = 1;
        File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "MyBizKios");

        //create directory if not exist
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Toast.makeText(this, "Gagal membuat folder", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if(index == 0)
            csvFile = "Daily revenue.xls";
        else if(index == 1)
            csvFile = "Top product.xls";
        else if (index == 2)
            csvFile= "Revenue.xls";
        else if (index == 3)
            csvFile = "Revenue per customer.xls";
        else
            csvFile = "unknown.xls";

        try
        {
            File file = new File(directory, csvFile);
            if(file.exists()) file.delete();
            workbook = Workbook.createWorkbook(file, wbSettings);
            sheet = workbook.createSheet("Data", 0);
        }
        catch (IOException ex)
        {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        float totalTrans;
        query = "SELECT SUM(b.qty) FROM dbtsalesdoc a JOIN dbtsalestrans b ON b.docid = a.id ";
        if(index != 0) query += "WHERE DATE(a.docdate) BETWEEN '" + start + "' AND '" + end + "' ";
        else query += "WHERE DATE(a.docdate) = DATE('now') ";

        c = dba.loadLocalTableFromRawQuery(query);
        c.moveToFirst();
        totalTrans = (float) c.getInt(0);
        c.close();

        if(totalTrans < 1) {
            Toast.makeText(this, "Tidak ada transaksi untuk diekspor", Toast.LENGTH_SHORT).show();
            return;
        }

        if(index == 0) //daily report
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
                try {
                    sheet.addCell(new Label(0, 0, "Date"));
                    sheet.addCell(new Label(1, 0, "Cust Code"));
                    sheet.addCell(new Label(2, 0, "Cust Name"));
                    sheet.addCell(new Label(3, 0, "Payment Type"));
                    sheet.addCell(new Label(4, 0, "Card Number"));
                    sheet.addCell(new Label(5, 0, "Subtotal"));
                    sheet.addCell(new Label(6, 0, "Grand Total"));
                    sheet.addCell(new Label(7, 0, "Paid Total"));
                    do {
                        sheet.addCell(new Label(0, row, c.getString(0)));
                        sheet.addCell(new Label(1, row, c.getString(4)));
                        sheet.addCell(new Label(2, row, c.getString(5)));
                        sheet.addCell(new Label(3, row, c.getString(6)));
                        sheet.addCell(new Label(4, row, c.getString(7)));
                        sheet.addCell(new Label(5, row, c.getFloat(1) + ""));
                        sheet.addCell(new Label(6, row, c.getFloat(2) + ""));
                        sheet.addCell(new Label(7, row, c.getFloat(3) + ""));
                        row++;
                    } while (c.moveToNext());
                    c.close();
                } catch (WriteException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    c.close();
                    return;
                }
            }
        }
        else if(index == 1) //best product
        {
            query = "SELECT a.itemname, sum(a.qty), sum(a.price * a.qty), a.price " +
                    "FROM dbtsalestrans a " +
                    "JOIN dbtsalesdoc b on a.docid = b.id and DATE(b.docdate) BETWEEN '" + start + "' AND '" + end + "' " +
                    "GROUP BY a.itemname, a.price";

            c = dba.loadLocalTableFromRawQuery(query);

            if ((c != null) && (c.moveToFirst()))
            {
                try {
                    sheet.addCell(new Label(0, 0, "Item Name"));
                    sheet.addCell(new Label(1, 0, "Qty"));
                    sheet.addCell(new Label(2, 0, "%Contrib"));
                    sheet.addCell(new Label(3, 0, "Price"));
                    sheet.addCell(new Label(4, 0, "Total"));
                    do {
                        sheet.addCell(new Label(0, row, c.getString(0)));
                        sheet.addCell(new Label(1, row, c.getInt(1) + ""));
                        sheet.addCell(new Label(2, row, Math.round(c.getInt(1) / totalTrans * 10000) / 100f + "%"));
                        sheet.addCell(new Label(3, row, UnitGlobal.convertToStrCurr(c.getFloat(3))));
                        sheet.addCell(new Label(4, row, UnitGlobal.convertToStrCurr(c.getFloat(2))));
                        row++;
                    } while (c.moveToNext());
                    c.close();
                } catch (WriteException ex) {
                    Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    c.close();
                    return;
                }
            }
        }

        try {
            workbook.write();
            workbook.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } catch (Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 101:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Mohon izinkan menulis di kartu sd", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void setAdapter()
    {
        List<String> menus = new ArrayList<>();
        menus.add("Daily Report");
        menus.add("Best Product");
        menus.add("Revenue");
        menus.add("Revenue per Customer");

        ReportViewAdapter reportViewAdapter = new ReportViewAdapter(this, menus);
        listview_report.setAdapter(reportViewAdapter);
    }

    private void bindListener()
    {
        listview_report.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*if(i == 0)
                {
                    Intent intent = new Intent(getApplicationContext(), ReportDetail.class);
                    intent.putExtra("Modul", i);
                    intent.putExtra("Start", "");
                    intent.putExtra("End", "");
                    startActivity(intent);
                }
                else*/
                    initDialog(i);
            }
        });
    }
}