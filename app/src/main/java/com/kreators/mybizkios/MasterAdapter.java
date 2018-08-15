package com.kreators.mybizkios;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.List;

public class MasterAdapter extends ArrayAdapter<ObjEntity>{
	
	private Context ctx;
	List<ObjEntity> detailList;
	int layoutResourceId, moduletype;
	UnitConstant uc;
	
	public MasterAdapter(Context ctx, int textViewResourceId, List<ObjEntity> objects, int moduletype) {
		super(ctx, textViewResourceId, objects);
		
		this.ctx = ctx;
		this.detailList = objects;
		this.layoutResourceId = textViewResourceId;
		this.moduletype = moduletype;
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

			TextView tcode = (TextView) v.findViewById(R.id.tcode);
			holder.CodeView = tcode;
			TextView tname = (TextView) v.findViewById(R.id.tname);
			holder.NameView = tname;
			TextView tid = (TextView) v.findViewById(R.id.tid);

			TextView tdepartment = (TextView) v.findViewById(R.id.tdepartment);
			TextView ttype = (TextView) v.findViewById(R.id.ttype);
			TextView titemunit = (TextView) v.findViewById(R.id.titemunit);
			TextView tminqty = (TextView) v.findViewById(R.id.tminqty);

			tdepartment.setVisibility(View.GONE);
			ttype.setVisibility(View.GONE);
			titemunit.setVisibility(View.GONE);
			tminqty.setVisibility(View.GONE);

			if (moduletype == uc.TT_MASTER_PARTNER || moduletype == uc.TT_MASTER_ACCOUNTS || moduletype == uc.TT_MASTER_SESSION){
				holder.TypeView = ttype;
				ttype.setVisibility(View.VISIBLE);
			} else if (moduletype == uc.TT_MASTER_ITEM){
				holder.TypeView = ttype;
				ttype.setVisibility(View.VISIBLE);
				holder.DepartmentView = tdepartment;
				tdepartment.setVisibility(View.VISIBLE);
			}

			holder.IdView = tid;
			v.setTag(holder);
		}
		else
			holder = (SearchHolder) v.getTag();


		holder.CodeView.setText(myobj.getCode());
		holder.NameView.setText(myobj.getName());
		holder.IdView.setText(String.valueOf(myobj.getId()));

		if (moduletype == uc.TT_MASTER_PARTNER || moduletype == uc.TT_MASTER_ACCOUNTS || moduletype == uc.TT_MASTER_SESSION ){
			holder.TypeView.setText(myobj.getType());
		} else if (moduletype == uc.TT_MASTER_ITEM){
			holder.DepartmentView.setText(myobj.getDep());
			holder.TypeView.setText(myobj.getType());
		}

		return v;
	}
	
	private static class SearchHolder {
		public TextView CodeView;
		public TextView NameView;
		public TextView TypeView;
		public TextView DepartmentView;
		public TextView IdView;
	}
	
}
