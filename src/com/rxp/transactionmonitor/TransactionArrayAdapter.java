package com.rxp.transactionmonitor;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rxp.realcontrol.api.Transactions;
import com.rxp.transactionmonitor.views.RTMListItemView;

public class TransactionArrayAdapter extends
		ArrayAdapter<Transactions.Transaction> {

	private Context context;
	private ArrayList<Transactions.Transaction> items;
	private int resourceId = -1;
	private NumberFormat nf = NumberFormat.getInstance();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private SimpleDateFormat sdf_parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	
	private String[] accountColours = { "#ffffff", "#ffffff", "#ffffff", "#ffffff" };
	private String[] accountBGColours = { "#ec7000", "#854f61", "#64992c", "#206cff" };
	
	private ArrayList<String> accountNames = new ArrayList<String>();
	
	
	public TransactionArrayAdapter(Context context, int textViewResourceId,
			ArrayList<Transactions.Transaction> items) {
		super(context, textViewResourceId, items);
		this.context = context;
		this.resourceId = textViewResourceId;
		this.items = items;
	
		nf.setMinimumFractionDigits(2);
		
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		int pos = 0;
		
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(this.resourceId, null);
		}
		try {
			Transactions.Transaction t = items.get(position);
			if (t != null) {
				pos = accountNames.indexOf(t.accountName);
				if (pos == -1) {
					accountNames.add(t.accountName);
					pos = accountNames.indexOf(t.accountName);
				}
				
				RTMListItemView liv = (RTMListItemView) v.findViewById(R.id.liv);
				if (liv != null) {
					liv.setTopLine(t.name);
					liv.setSubLine(t.orderid);
					liv.setTimestamp(sdf.format(sdf_parse.parse(t.timestamp)));
					liv.setCardtype(t.cardtype);
					liv.setAccount(t.accountName, accountColours[pos % accountColours.length], accountBGColours[pos % accountColours.length]);
					liv.setAmount(nf.format(((double)t.amount)/100), t.currency);
					liv.setResult(t.result);
					
				}
			} else {
				Log.i("TransactionAdapter", "object is NULL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return v;
	}
}
