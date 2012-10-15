package com.rxp.transactionmonitor.tasks;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.ParameterStyle;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.os.AsyncTask;
import android.util.Log;

import com.rxp.realcontrol.api.Filter;
import com.rxp.realcontrol.api.Transactions;
import com.rxp.transactionmonitor.listeners.TransactionsListener;

public class GetTransactionsTask extends AsyncTask<Object, Void, String> {
	TransactionsListener tl; 
	boolean refreshList = false;
	
	/**
	 * Helper method to aid code completion mostly. Better than guessing which Objects need
	 * to be passed to execute()
	 * 
	 * @param accessor The OAuthAccessor with valid access_token and secret
	 * @param filter A filter to apply to the transaction search
	 * @param tl A TransactionsListener to return the results to. 
	 */
	public void getTransactions(OAuthAccessor accessor, Filter filter, boolean refreshList, TransactionsListener tl) {
		this.refreshList = refreshList;
		this.execute(accessor, filter, tl);
	}
	
	protected String doInBackground(Object... params) {
		OAuthAccessor accessor = (OAuthAccessor)params[0];
		Filter filter = (Filter) params[1];
		this.tl = (TransactionsListener)params[2];
		
		OAuthClient oclient = new OAuthClient(new HttpClient4());
		
		
		Serializer serializer = new Persister();
		StringWriter sw = new StringWriter();

		try {
			serializer.write(filter, sw);

			Log.d("TMS", sw.toString());

			OAuthMessage request = accessor
					.newRequestMessage(
							"POST",
							"https://api.realexpayments.com/IPS-Reporting/api/v1.0/~/search/transactions",
							null, new ByteArrayInputStream(sw.toString()
									.getBytes()));

			List<Map.Entry<String, String>> headers = request.getHeaders();
			headers.add(new AbstractMap.SimpleEntry<String, String>(
					"Content-Type", "text/xml"));

			OAuthMessage omessage = oclient.invoke(request,
					ParameterStyle.AUTHORIZATION_HEADER);

			String xml = omessage.readBodyAsString(); // can only read once

			return xml;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	protected void onPostExecute(String transactionsXML) {
		Log.d("TMS", "In Transactions.onPostExecute: " + transactionsXML);

		Serializer serializer = new Persister();
		Reader reader = new StringReader(transactionsXML);
		Transactions t = null;
		try {
			t = serializer.read(Transactions.class, reader, false);
		} catch (Exception e) {
			Log.e("TMS", "Crud! " + e.getLocalizedMessage());
		}
		
		if (refreshList) {
			tl.onRefreshTransactions(t);
		} else {
			tl.onMoreTransactions(t);
		}
	}
}
