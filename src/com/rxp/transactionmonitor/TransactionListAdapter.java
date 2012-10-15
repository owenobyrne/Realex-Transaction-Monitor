package com.rxp.transactionmonitor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.oauth.OAuthAccessor;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.rxp.realcontrol.api.Filter;
import com.rxp.realcontrol.api.Transactions;
import com.rxp.transactionmonitor.listeners.TransactionsListener;
import com.rxp.transactionmonitor.tasks.GetTransactionsTask;

public class TransactionListAdapter extends EndlessAdapter implements
		TransactionsListener, OnRefreshListener<ListView> {
	private RotateAnimation rotate = null;
	private View pendingView = null;
	private OAuthAccessor accessor;
	private int nextOffset = 0;
	private int totalNumTransactions = 0;
	private Filter filter;
	private PullToRefreshBase<ListView> refreshView;
	
	private SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
	private SimpleDateFormat sdf_time = new SimpleDateFormat("HH:mm");

	public TransactionListAdapter(Context ctxt, OAuthAccessor accessor) {
		super(new TransactionArrayAdapter(ctxt, R.layout.row, new ArrayList<Transactions.Transaction>()));

		setRunInBackground(false);

		filter = new Filter();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);
		
		Filter.DateTime dateTime = new Filter.DateTime();
		dateTime.dateFrom = sdf_date.format(c.getTime());
		dateTime.timeFrom = "00:00";
		
		dateTime.dateTo = sdf_date.format(new Date());
		dateTime.timeTo = sdf_time.format(new Date());
		filter.dateTime = dateTime;
		
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

	/** 
	 * Called if the user reaches the end of the list.
	 */
	@Override
	protected boolean cacheInBackground() {
		filter.offset = nextOffset;
		filter.timestamp = "" + System.currentTimeMillis();

		new GetTransactionsTask().getTransactions(accessor, filter, false, this);

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

	/**
	 * Called by the GetTransactionsTask if the refresh flag was false. This would be the
	 * case if the user reached the end of the endless list. More transactions will be 
	 * added to the list.
	 */
	@Override
	public void onMoreTransactions(Transactions t) {
		nextOffset = t.nextRange.offset;
		totalNumTransactions = t.totalNumTransactions;
		
		@SuppressWarnings("unchecked")
		ArrayAdapter<Transactions.Transaction> a = (ArrayAdapter<Transactions.Transaction>) getWrappedAdapter();
		a.addAll(t.transaction);

		onDataReady();
	}

	/**
	 * Clear down the list and add new transactions. Called by the GetTransactionTask if the 
	 * refresh flag was set to true (which it would be if the user pulls-to-refresh.)
	 * 
	 */
	@Override
	public void onRefreshTransactions(Transactions t) {
		nextOffset = t.nextRange.offset;
		totalNumTransactions = t.totalNumTransactions;
		
		@SuppressWarnings("unchecked")
		ArrayAdapter<Transactions.Transaction> a = (ArrayAdapter<Transactions.Transaction>) getWrappedAdapter();
		a.clear();
		a.addAll(t.transaction);

		refreshView.onRefreshComplete();
		onDataReady();
	}
	
	/**
	 * This is from the onRefreshListener interface. When the user pulls-to-refresh, this is called. 
	 */

	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		this.refreshView = refreshView;
		
		filter = new Filter();
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, -1);

		Filter.DateTime dateTime = new Filter.DateTime();
		dateTime.dateFrom = sdf_date.format(c.getTime());
		dateTime.timeFrom = "00:00";
		dateTime.dateTo = sdf_date.format(new Date());
		dateTime.timeTo = sdf_time.format(new Date());
		filter.dateTime = dateTime;
		
		filter.offset = 0;
		filter.timestamp = "" + System.currentTimeMillis();
		
		new GetTransactionsTask().getTransactions(accessor, filter, true, this);
		
	}


}