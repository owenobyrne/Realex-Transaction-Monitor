package com.rxp.transactionmonitor.activities;

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
import net.oauth.OAuthProblemException;
import net.oauth.ParameterStyle;
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
import android.widget.TextView;

import com.rxp.realcontrol.api.Client;
import com.rxp.realcontrol.api.ClientAccounts;
import com.rxp.realcontrol.api.Filter;
import com.rxp.realcontrol.api.Transactions;
import com.rxp.transactionmonitor.OAuthHelper;
import com.rxp.transactionmonitor.R;

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

			new ClientTask().execute("");
			new TransactionsTask().execute("");

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
			// Log.d("TMS", "ClientTask: "+ accessor.accessToken + "/" +
			// accessor.tokenSecret);

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

	class TransactionsTask extends AsyncTask<String, Void, String> {

		protected String doInBackground(String... urls) {
			OAuthClient oclient = new OAuthClient(new HttpClient4());
			// Log.d("TMS", "ClientTask: "+ accessor.accessToken + "/" +
			// accessor.tokenSecret);

			Filter filter = new Filter();
			filter.offset = 0;

			Filter.DateTime dateTime = new Filter.DateTime();
			dateTime.dateFrom = "01/10/2012";
			dateTime.dateTo = "02/10/2012";
			dateTime.timeFrom = "00:00";
			dateTime.timeTo = "00:00";
			filter.dateTime = dateTime;

			filter.timestamp = "" + System.currentTimeMillis();

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
				Log.d("TMS", "Transactions: " + xml);

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
			Log.d("TMS", "Got Client: " + t.totalNumTransactions + ": "
					+ t.transaction.get(0).orderid);

			TextView tvAccounts = (TextView) findViewById(R.id.transactions);
			tvAccounts.setText(t.totalNumTransactions + ": "
					+ t.transaction.get(0).orderid);
		}
	}

}
