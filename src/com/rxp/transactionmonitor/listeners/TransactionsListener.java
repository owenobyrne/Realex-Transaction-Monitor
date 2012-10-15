package com.rxp.transactionmonitor.listeners;

import com.rxp.realcontrol.api.Transactions;

public interface TransactionsListener {

	public void onMoreTransactions(Transactions t);
	public void onRefreshTransactions(Transactions t);
}
