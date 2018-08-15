package com.kreators.mybizkios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class CheckboxAdapter extends ArrayAdapter<ObjEntity>{

	private Context ctx;
	List<ObjEntity> detailList;
	int layoutResourceId;

	public CheckboxAdapter(Context ctx, int textViewResourceId, List<ObjEntity> objects) {
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
		ObjEntity myobj = detailList.get(position);
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(layoutResourceId, null);
			TextView txtcode = (TextView) v.findViewById(R.id.txtcode);
			holder.CodeView = txtcode;
			TextView txtname = (TextView) v.findViewById(R.id.txtname);
			holder.NameView = txtname;
			TextView txtid = (TextView) v.findViewById(R.id.txtid);
			holder.IdView = txtid;
			TextView txttype = (TextView) v.findViewById(R.id.txttype);
			holder.TypeView = txttype;
			//Log.d("Master Adapter", String.valueOf(txtid.getText()));

			CheckBox cbauth = (CheckBox) v.findViewById(R.id.CBAuth);
			holder.CBView  = cbauth;
			holder.CBView.setTag(myobj.getId());

			v.setTag(holder);
		}
		else
			holder = (SearchHolder) v.getTag();

		holder.CodeView.setText(myobj.getCode());
		holder.NameView.setText(myobj.getName());
		holder.IdView.setText(String.valueOf(myobj.getId()));
		if (layoutResourceId == R.layout.listview_account && (myobj.getType() != "" && myobj.getType() != null)){
			holder.CBView.setChecked(true);
		}

		return v;
	}

	private static class SearchHolder {
		public TextView CodeView;
		public TextView NameView;
		public TextView IdView;
		public CheckBox CBView;
		public TextView TypeView;
	}

}
