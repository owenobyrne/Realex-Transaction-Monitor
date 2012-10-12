package com.rxp.transactionmonitor;

import java.util.ArrayList;

import net.oauth.OAuthAccessor;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;

import com.rxp.realcontrol.api.Filter;
import com.rxp.realcontrol.api.Transactions;
import com.rxp.transactionmonitor.listeners.TransactionsListener;
import com.rxp.transactionmonitor.tasks.GetTransactionsTask;

public class TransactionListAdapter extends EndlessAdapter implements
		TransactionsListener {
	private RotateAnimation rotate = null;
	private View pendingView = null;
	private OAuthAccessor accessor;
	private int nextOffset = 0;
	private int totalNumTransactions = 0;

	public TransactionListAdapter(Context ctxt,
			ArrayList<Transactions.Transaction> list, OAuthAccessor accessor) {
		super(new TransactionArrayAdapter(ctxt, R.layout.row, list));

		setRunInBackground(false);

		this.accessor = accessor;

		rotate = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		rotate.setDuration(600);
		rotate.setRepeatMode(Animation.RESTART);
		rotate.setRepeatCount(Animation.INFINITE);
	}

	@Override
	protected View getPendingView(ViewGroup parent) {
		View row = LayoutInflater.from(parent.getContext()).inflate(
				R.layout.row, null);

		pendingView = row.findViewById(R.id.liv);
		pendingView.setVisibility(View.GONE);
		pendingView = row.findViewById(R.id.throbber);
		pendingView.setVisibility(View.VISIBLE);
		startProgressAnimation();

		return (row);
	}

	@Override
	protected boolean cacheInBackground() {
		Filter filter = new Filter();
		filter.offset = nextOffset;

		Filter.DateTime dateTime = new Filter.DateTime();
		dateTime.dateFrom = "01/10/2012";
		dateTime.dateTo = "02/10/2012";
		dateTime.timeFrom = "00:00";
		dateTime.timeTo = "00:00";
		filter.dateTime = dateTime;

		filter.timestamp = "" + System.currentTimeMillis();

		new GetTransactionsTask().getTransactions(accessor, filter, this);

		return (nextOffset <= totalNumTransactions);
	}

	@Override
	protected void appendCachedData() {

	}

	public void startProgressAnimation() {
		if (pendingView != null) {
			pendingView.startAnimation(rotate);
		}
	}

	@Override
	public void setTransaction(Transactions t) {
		ArrayList<Transactions.Transaction> rt = (ArrayList<Transactions.Transaction>) t.transaction;
		nextOffset = t.nextRange.offset;

		if (totalNumTransactions == 0) {
			totalNumTransactions = t.totalNumTransactions;
		}

		@SuppressWarnings("unchecked")
		ArrayAdapter<Transactions.Transaction> a = (ArrayAdapter<Transactions.Transaction>) getWrappedAdapter();
		a.addAll(t.transaction);

		onDataReady();
	}



}