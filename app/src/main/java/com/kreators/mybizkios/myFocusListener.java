package com.kreators.mybizkios;

import android.content.Context;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Ivn on 7/4/2018.
 */

class myFocusListener implements View.OnFocusChangeListener {

    private TextWatcher vTextWatcher;
    private Context ctx;

    public myFocusListener(TextWatcher parmTextWatcher, Context ctx) {
        // TODO Auto-generated constructor stub
        this.vTextWatcher = parmTextWatcher;
        this.ctx = ctx;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        // TODO Auto-generated method stub
        try {
            UnitGlobal.setNumber(((EditText) v), hasFocus, this.vTextWatcher);
        } catch (Exception e) {
            // TODO: handle exception
            Toast.makeText(this.ctx, "Format angka tidak valid, mohon dicek kembali.", Toast.LENGTH_SHORT).show();
            ((EditText) v).setText("");
            UnitGlobal.setNumber(((EditText) v), hasFocus, this.vTextWatcher);
        }


        if (hasFocus) {
            ((EditText) v).selectAll();
        }
    }

}
