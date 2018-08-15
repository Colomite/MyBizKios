package com.kreators.mybizkios;

/**
 * Created by admin on 2/2/2018.
 */

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class TmpFragmentItem extends Fragment {

    private GridView gridView;
    GridViewAdapter gridViewAdapter;
    DetailAdapter dtlAdapter;
    DBAdapter dba;

    public TmpFragmentItem() {
        // Required empty public constructor
    }

    public void setAdapter(GridViewAdapter parmgridViewAdapter) {
        this.gridViewAdapter = parmgridViewAdapter;
    }

    public void setItemAdapter(DetailAdapter parmAdapter) {
        this.dtlAdapter = parmAdapter;
    }

    public void setDBAdapter(DBAdapter parmAdapter) {
        this.dba = parmAdapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

                ObjEntity myobj = gridViewAdapter.getObj(i);
                //int tmpitemId = gridViewAdapter.getObjItemId(i);
                Log.d("FragmentItem", "Item name : " + myobj.getName());

                String mysql = "SELECT price FROM dbmprice where ctpId = 1 and itemId = " + String.valueOf(myobj.getId());
                Log.d("FragmentItem", "query : " + mysql);
                Cursor c = dba.loadLocalTableFromRawQuery(mysql);

                float itemPrice = 0;
                if ((c != null ) && (c.moveToFirst())) {
                    Log.d("FragmentItem", "masuk ke isi itemprice");
                    itemPrice = c.getFloat(0);
                }
                c.close();



                dtlAdapter.add(new ObjEntityItem(myobj.getId(), myobj.getCode(), "", "", 1, itemPrice, "0"));
                dtlAdapter.notifyDataSetChanged();
                Log.d("FragmentItem", "item has been clicked");
            }
        });

        return rootView;
    }

}
