package com.kreators.mybizkios;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjEntity implements Parcelable{
	protected int id;
	protected String code, name, type, dep, unit;
	
	public ObjEntity(int id, String code, String name) {
		//digunakan pada DB Adapter utk browse master
		this.id = id;
		this.code = code;
		this.name = name;
	}

	public ObjEntity(int id, String code, String name, String type) {
		//digunakan pada DB Adapter utk browse master
		this.id = id;
		this.code = code;
		this.name = name;
		this.type = type;
	}

	public ObjEntity(int id, String code, String name, String type, String dep) {
		//digunakan pada DB Adapter utk browse master
		this.id = id;
		this.code = code;
		this.name = name;
		this.type = type;
		this.dep = dep;
	}

	public ObjEntity(int id, String code, String name, String type, String dep, String unit) {
		//digunakan pada DB Adapter utk browse master
		this.id = id;
		this.code = code;
		this.name = name;
		this.type = type;
		this.dep = dep;
		this.unit = unit;
	}

	protected ObjEntity(Parcel in) {
		id = in.readInt();
		code = in.readString();
		name = in.readString();
	}

	public static final Creator<ObjEntity> CREATOR = new Creator<ObjEntity>() {
		@Override
		public ObjEntity createFromParcel(Parcel in) {
            return new ObjEntity(in);
		}

		@Override
		public ObjEntity[] newArray(int size) {
            return new ObjEntity[size];
		}
	};

	public int getId() {
		return this.id;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public String getName() {
		return this.name;
	}

	public String getType() {
		return this.type;
	}

	public String getDep() {
		return this.dep;
	}

	public String getUnit() {
		return this.unit;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

		dest.writeInt(id);
		dest.writeString(code);
		dest.writeString(name);
	}
}
