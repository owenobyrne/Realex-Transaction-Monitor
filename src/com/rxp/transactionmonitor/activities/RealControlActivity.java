package com.rxp.transactionmonitor.activities;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;
import net.oauth.OAuthProblemException;
import net.oauth.client.OAuthClient;
import net.oauth.client.httpclient4.HttpClient4;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.rxp.realcontrol.api.Client;
import com.rxp.realcontrol.api.ClientAccounts;
import com.rxp.realcontrol.api.Filter;
import com.rxp.realcontrol.api.Transactions;
import com.rxp.transactionmonitor.OAuthHelper;
import com.rxp.transactionmonitor.R;
import com.rxp.transactionmonitor.TransactionListAdapter;

public class RealControlActivity extends Activity {
	SharedPreferences preferences;
	OAuthAccessor accessor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		

		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		String access_token = preferences.getString("access_token", "");
		String access_secret = preferences.getString("access_secret", "");

		Log.d("TMS", "In onCreate: " + access_token + "/" + access_secret);

		if (access_token.equals("")) {
			Log.d("TMS", "Don't have any stored creds - going to get some.");
			startActivity(new Intent(getBaseContext(), OAuthLoginActivity.class));

		} else {
			Log.d("TMS", "Got stored creds - using them.");
			accessor = OAuthHelper.defaultAccessor();

			accessor.accessToken = access_token;
			accessor.tokenSecret = access_secret;

			TransactionListAdapter adapter = new TransactionListAdapter(getBaseContext(), accessor);
			
			PullToRefreshListView pullToRefreshView = (PullToRefreshListView) findViewById(R.id.list);
			pullToRefreshView.getRefreshableView().setAdapter(adapter);
			pullToRefreshView.setOnRefreshListener(adapter);
			
			new ClientTask().execute("");

		}

		/*
		 * GCMRegistrar.checkDevice(this); GCMRegistrar.checkManifest(this);
		 * final String regId = GCMRegistrar.getRegistrationId(this); if
		 * (regId.equals("")) { GCMRegistrar.register(this, "541821719407"); }
		 * else { Log.v("Realex Transaction Monitor", "Already registered"); }
		 */

	}

	public void onResume() {
		super.onResume();
		Log.d("TMS", "in onResume");

	}

	class ClientTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			OAuthClient oclient = new OAuthClient(new HttpClient4());
			Log.d("TMS", "ClientTask: " + accessor.accessToken + "/"
					+ accessor.tokenSecret);

			try {
				OAuthMessage omessage = oclient
						.invoke(accessor,
								"GET",
								"https://api.realexpayments.com/IPS-Reporting/api/v1.0/~/client",
								null);

				String xml = omessage.readBodyAsString(); // can only read once
				Log.d("TMS", "Client: " + xml);

				return xml;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (OAuthProblemException e) {
				Log.w("TMS",
						"Credentials no longer valid - reauthenticate and authorise...");
				Editor edit = preferences.edit();
				edit.clear(); // wipe the stored credentials
				edit.commit();

				Log.d("TMS",
						"No longer have and stored creds - going to get some.");
				startActivity(new Intent(getBaseContext(),
						OAuthLoginActivity.class));

			} catch (OAuthException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String clientXML) {
			Log.d("TMS", "In onPoseExecute: " + clientXML);

			if (clientXML == null) {
				return;
			}

			Serializer serializer = new Persister();
			Reader reader = new StringReader(clientXML);
			Client client = null;
			try {
				client = serializer.read(Client.class, reader, false);
			} catch (Exception e) {
				Log.e("TMS", "Crud! " + e.getLocalizedMessage());
			}
			Log.d("TMS", "Got Client: " + client.companyName);

			TextView tvClientId = (TextView) findViewById(R.id.clientId);
			tvClientId.setText(client.companyName);
		}
	}

	class AccountsTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			OAuthClient oclient = new OAuthClient(new HttpClient4());
			// Log.d("TMS", "ClientTask: "+ accessor.accessToken + "/" +
			// accessor.tokenSecret);

			try {
				OAuthMessage omessage = oclient
						.invoke(accessor,
								"GET",
								"https://api.realexpayments.com/IPS-Reporting/api/v1.0/~/accounts",
								null);

				String xml = omessage.readBodyAsString(); // can only read once
				Log.d("TMS", "Accounts: " + xml);

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
			}
			return null;
		}

		protected void onPostExecute(String accountsXML) {
			// Log.d("TMS", "In onPostExecute: " + accountsXML);

			Serializer serializer = new Persister();
			Reader reader = new StringReader(accountsXML);
			ClientAccounts ca = null;
			try {
				ca = serializer.read(ClientAccounts.class, reader, false);
			} catch (Exception e) {
				Log.e("TMS", "Crud! " + e.getLocalizedMessage());
			}
			Log.d("TMS", "Got Client: " + ca.account.get(0).accountName);

			TextView tvAccounts = (TextView) findViewById(R.id.transactions);
			tvAccounts.setText(ca.account.get(0).accountName);
		}
	}

}
