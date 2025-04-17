//package com.pplus.prnumberuser.apps.common.ui.base;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.os.Build;
//import android.widget.ArrayAdapter;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public abstract class BaseArrayAdapter<T> extends ArrayAdapter<T> {
//
//	public BaseArrayAdapter(Context context, int resource) {
//		super(context, resource);
//	}
//
//	public BaseArrayAdapter(Context context, int resource, ArrayList<T> data) {
//		super(context, resource, data);
//	}
//
//	@SuppressLint("NewApi")
//	@Override
//	public void addAll(Collection<? extends T> collection) {
//		if (collection != null) {
//			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//				super.addAll(collection);
//			} else {
//				for(T item: collection) {
//					add(item);
//				}
//			}
//		}
//	}
//
//}
