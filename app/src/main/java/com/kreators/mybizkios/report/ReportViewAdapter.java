package com.kreators.mybizkios.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kreators.mybizkios.R;

import java.util.List;

class ReportViewAdapter extends BaseAdapter
{
    List<String> menus;
    Context context;
    LayoutInflater inflater;

    public ReportViewAdapter(Context context, List<String> menus)
    {
        this.context = context;
        this.menus = menus;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int i) {
        return menus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if(v == null)
            v = inflater.inflate(com.kreators.mybizkios.R.layout.reportview_adapter, viewGroup, false);

        TextView text = (TextView) v.findViewById(R.id.textview_report_view);
        text.setText(menus.get(i));
        return v;
    }
}
