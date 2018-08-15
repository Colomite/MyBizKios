package com.kreators.mybizkios;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivn on 6/25/2018.
 */

public class SearchAdapter extends ArrayAdapter<ObjEntity> implements Filterable {

    private Context ctx;
    List<ObjEntity> searchList;
    List<ObjEntity> orisearchList;
    private Filter searchFilter;

    public SearchAdapter(Context ctx, int textViewResourceId, List<ObjEntity> objects) {
        super(ctx, textViewResourceId, objects);

        this.ctx = ctx;
        this.searchList = objects;
        this.orisearchList = objects;
    }

    public int getCount() {
        return searchList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        SearchHolder holder = new SearchHolder();

        // First let's verify the convertView is not null
        if (convertView == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.search_list_view, null);
            TextView txtcode = (TextView) v.findViewById(R.id.txtcode);
            holder.CodeView = txtcode;
            TextView txtname = (TextView) v.findViewById(R.id.txtname);
            holder.NameView = txtname;

            v.setTag(holder);
        }
        else
            holder = (SearchHolder) v.getTag();

        //Log.d("MBTOUCH SEARCH", "POSITION : " + String.valueOf(position));
        ObjEntity myobj = searchList.get(position);
        holder.CodeView.setText(myobj.getCode());
        holder.NameView.setText(myobj.getName());


        return v;
    }

    private static class SearchHolder {
        public TextView CodeView;
        public TextView NameView;
    }

    public void resetData() {
        searchList = orisearchList;
        notifyDataSetChanged();
    }

    /*
     * We create our filter
     */
    @Override
    public Filter getFilter() {
        if (searchFilter == null)
            searchFilter = new SearchFilter();


        return searchFilter;
    }

    @SuppressLint("DefaultLocale")
    private class SearchFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.d("MBTOUCH SEARCH", "PERFORM FILTERING");
            FilterResults results = new FilterResults();
            // We implement here the filter logic
//			if (searchList.size() == 0){
//				resetData();
//				Log.d("MBTOUCH SEARCH", "SEARCH ADAPTER"+String.valueOf(searchList.size()));
//			}
            if (constraint == null || constraint.length() == 0) {
                // No filter implemented we return all the list

                results.values = orisearchList;
                results.count = orisearchList.size();

            } else {
                // We perform filtering operation
//				if (searchList.size() == 0){
//					searchList = orisearchList;
//					notifyDataSetChanged();
//					Log.d("MBTOUCH SEARCH", "SEARCH ADAPTER"+String.valueOf(searchList.size()));
//				}
                List<ObjEntity> nSearchList = new ArrayList<ObjEntity>();
                Log.d("Search ADAP","MASUK constraint > 1");
                for (ObjEntity myobj : searchList) {
                    if (myobj.getName().toUpperCase().contains(constraint.toString().toUpperCase()) ||
                            myobj.getCode().toUpperCase().contains(constraint.toString().toUpperCase())){
                        nSearchList.add(myobj);

                    }

                }
                results.values = nSearchList;
                results.count = nSearchList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {

            // Now we have to inform the adapter about the new list filtered
            Log.d("MBTOUCH SEARCH", "PUBLISH RESULTS");
            if (results.count == 0) {
                Log.d("MBTOUCH SEARCH", "PUBLISH RESULTS 1");
                searchList.clear();
                notifyDataSetInvalidated();

            } else {
                Log.d("MBTOUCH SEARCH", "RESULT COUNT : " + String.valueOf(results.count));
                searchList = (List<ObjEntity>) results.values;
                Log.d("MBTOUCH SEARCH", "PUBLISH RESULTS 2");
                notifyDataSetChanged();
            }
        }

    }
}
