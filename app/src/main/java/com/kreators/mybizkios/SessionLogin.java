package com.kreators.mybizkios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*public class SessionLogin extends ArrayAdapter<String>
{
    LayoutInflater inflater;
    Context context;
    List<ObjEntity> obj;
    int resource;

    public SessionLogin(Context context, int resource, List obj)
    {
        //super(context, resource, 0, obj);
        super(context, resource, obj);
        this.obj = obj;
        this.context = context;
        this.resource = resource;
        inflater = (LayoutInflater.from(context));
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent)
    {
        return createItemView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent)
    {
        View view = convertView;
        if(view == null)
            view = inflater.inflate(resource, parent, false);

        TextView code = (TextView) view.findViewById(R.id.spinnerLoginSessionCode);
        TextView desc = (TextView) view.findViewById(R.id.spinnerLoginSessionDesc);

        ObjEntity data = obj.get(position);

        code.setText(data.getCode());
        desc.setText(data.getName() + " - " + data.getType());

        return view;
    }
}
*/

public class SessionLogin extends BaseAdapter
{
    LayoutInflater inflater;
    Context context;
    List<ObjEntity> obj;
    int resource;

    public SessionLogin(Context context, List<ObjEntity> obj)
    {
        this.context = context;
        this.obj = obj;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return obj.size();
    }

    @Override
    public Object getItem(int i) {
        return obj.get(i);
    }

    @Override
    public long getItemId(int i) {
        return obj.get(i).id;
    }

    @Override
    public View getView(int position, View base, ViewGroup parent)
    {
        View view = base;
        if(view == null)
            view = inflater.inflate(R.layout.spinner_login_session, parent, false);

        TextView code = (TextView) view.findViewById(R.id.spinnerLoginSessionCode);
        TextView desc = (TextView) view.findViewById(R.id.spinnerLoginSessionDesc);

        ObjEntity data = obj.get(position);

        code.setText(data.getCode());
        desc.setText(data.getName() + " - " + data.getType());

        return view;
    }
}