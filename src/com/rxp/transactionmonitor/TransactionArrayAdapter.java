package com.rxp.transactionmonitor;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rxp.realcontrol.api.Transactions;

public class TransactionArrayAdapter extends
		ArrayAdapter<Transactions.Transaction> {

	private Context context;
	private ArrayList<Transactions.Transaction> items;
	private int resourceId = -1;
	
	public TransactionArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Transactions.Transaction> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.resourceId = textViewResourceId;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(this.resourceId, null);
		}
		Transactions.Transaction t = items.get(position);
		if (t != null) {
			TextView t1 = (TextView) v.findViewById(R.id.text1);
			TextView t2 = (TextView) v.findViewById(R.id.text2);
			if (t1 != null) {
				t1.setText("Name: " + t.name);
			}
			if (t2 != null) {
				t2.setText("Amount: " + t.amount);
			}
		} else {
			Log.i("TransactionAdapter", "object is NULL");
		}
		return v;
	}
}
