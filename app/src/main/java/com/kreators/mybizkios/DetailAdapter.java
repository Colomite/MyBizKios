package com.kreators.mybizkios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class DetailAdapter extends ArrayAdapter<ObjEntityItem>{
	
	private Context ctx;
	List<ObjEntityItem> detailList;
	int layoutResourceId;
	UnitConstant uc;
	UnitGlobal ug = new UnitGlobal();

	public DetailAdapter(Context ctx, int textViewResourceId, List<ObjEntityItem> objects) {
		super(ctx, textViewResourceId, objects);
		
		this.ctx = ctx;
		this.detailList = objects;
		this.layoutResourceId = textViewResourceId;
	}
	
	public int getCount() {
		return detailList.size();
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		SearchHolder holder = new SearchHolder();

		NumberFormat nf = NumberFormat.getInstance();
		nf.setMinimumFractionDigits(2);
		nf.setMaximumFractionDigits(2);
		
		// First let's verify the convertView is not null
		ObjEntityItem myobj = detailList.get(position);
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(layoutResourceId, null);


			TextView tname = (TextView) v.findViewById(R.id.tname);
			holder.NameView = tname;
			TextView tid = (TextView) v.findViewById(R.id.tid);
			TextView tnotes = (TextView) v.findViewById(R.id.tnotes);
			holder.NotesView = tnotes;


			TextView tqty = (TextView) v.findViewById(R.id.tqty);
			holder.QtyView = tqty;

			if (layoutResourceId != R.layout.simple_list_itembundle) {
				TextView tprice = (TextView) v.findViewById(R.id.tprice);
				holder.PriceView = tprice;
				TextView tdisc = (TextView) v.findViewById(R.id.tdisc);
				holder.DiscView = tdisc;
				TextView tsubtotal = (TextView) v.findViewById(R.id.tsubtotal);
				holder.SubtotalView = tsubtotal;
			}

			holder.IdView = tid;
			v.setTag(holder);
		}
		else
			holder = (SearchHolder) v.getTag();


		holder.NameView.setText(myobj.getName());
		holder.NotesView.setText(myobj.getNotes());
		holder.IdView.setText(String.valueOf(myobj.getId()));

		holder.QtyView.setText(String.valueOf(myobj.getQty()));

        if (layoutResourceId != R.layout.simple_list_itembundle) {
            holder.PriceView.setText(ug.convertToStrCurr(myobj.getPrice()));
            holder.DiscView.setText(myobj.getDisc());
            holder.SubtotalView.setText(ug.convertToStrCurr(myobj.getSubtotal()));
        }

		return v;
	}
	
	private static class SearchHolder {
		public TextView NameView;
		public TextView NotesView;
		public TextView IdView;

		public TextView QtyView;
		public TextView PriceView;
		public TextView DiscView;
		public TextView SubtotalView;
	}
	
}
