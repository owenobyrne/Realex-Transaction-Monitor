package com.rxp.transactionmonitor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {
	private static final String SERVICE_URL = "http://owenob.dtdns.net/transactionmonitorserver/device";

	
	@Override
	protected void onError(Context context, String errorId) {
		// TODO Auto-generated method stub
		
	}

	@TargetApi(16)
	@Override
	protected void onMessage(Context context, Intent intent) {
		Bitmap bm = Bitmap
				.createScaledBitmap(
						BitmapFactory.decodeResource(getResources(),
								R.drawable.owen),
						getResources().getDimensionPixelSize(
								android.R.dimen.notification_large_icon_width),
						getResources().getDimensionPixelSize(
								android.R.dimen.notification_large_icon_height),
						true);
		//Intent i = new Intent(this, Main.class);
		//PendingIntent pendingIntent = PendingIntent.getActivity(this, 01,
		//		i, Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		Notification notification = new Notification.InboxStyle(
			      new Notification.Builder(getApplicationContext())
			         .setContentTitle("New Transactions")
			         .setContentText("")
			         .setSmallIcon(R.drawable.ic_launcher)
			         .setLargeIcon(bm)
			         .setNumber(2)
					 //.setContentIntent(pendingIntent)
					 .setTicker("New Transactions\nOwen O Byrne paid 39.99 EUR\nColm Lyon paid 45.00 GBP")
					 .setAutoCancel(true)
					 .setPriority(0)
			      )
			      .addLine("Owen O Byrne paid 39.99 EUR")
			      .addLine("Colm Lyon paid 45.00 GBP")
			      .setSummaryText("+3 more")
			      .build();
		
		NotificationManager notificationManger = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManger.notify(01, notification);
		
	}

	@Override
	protected void onRegistered(Context context, String regId) {
		WebServiceTask wst = new WebServiceTask(WebServiceTask.POST_TASK, this, "Registering Device...");
		 
        wst.addNameValuePair("deviceId", regId);
 
        // the passed String is the URL we will POST to
        wst.execute(new String[] { SERVICE_URL });
		
	}

	@Override
	protected void onUnregistered(Context context, String regId) {
		// TODO Auto-generated method stub
		// remove the device from the service.
		
	}

    public void handleResponse(String response) {
        
        Log.i("Realex Trans Monitor", "Recieved from Server: "+ response);     
    }
	
	private class WebServiceTask extends AsyncTask<String, Integer, String> {
		 
        public static final int POST_TASK = 1;
        public static final int GET_TASK = 2;
         
        private static final String TAG = "WebServiceTask";
 
        // connection timeout, in milliseconds (waiting to connect)
        private static final int CONN_TIMEOUT = 3000;
         
        // socket timeout, in milliseconds (waiting for data)
        private static final int SOCKET_TIMEOUT = 5000;
         
        private int taskType = GET_TASK;
        private Context mContext = null;
        private String processMessage = "Processing...";
 
        private ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
 
        public WebServiceTask(int taskType, Context mContext, String processMessage) {
 
            this.taskType = taskType;
            this.mContext = mContext;
            this.processMessage = processMessage;
        }
 
        public void addNameValuePair(String name, String value) {
 
            params.add(new BasicNameValuePair(name, value));
        }
 
 
        @Override
        protected void onPreExecute() {
 
        }
 
        protected String doInBackground(String... urls) {
 
            String url = urls[0];
            String result = "";
 
            HttpResponse response = doResponse(url);
 
            if (response == null) {
                return result;
            } else {
 
                try {
 
                    result = inputStreamToString(response.getEntity().getContent());
 
                } catch (IllegalStateException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
 
                } catch (IOException e) {
                    Log.e(TAG, e.getLocalizedMessage(), e);
                }
 
            }
 
            return result;
        }
 
        @Override
        protected void onPostExecute(String response) {
             
            handleResponse(response);
            
        }
         
        // Establish connection and socket (data retrieval) timeouts
        private HttpParams getHttpParams() {
             
            HttpParams htpp = new BasicHttpParams();
             
            HttpConnectionParams.setConnectionTimeout(htpp, CONN_TIMEOUT);
            HttpConnectionParams.setSoTimeout(htpp, SOCKET_TIMEOUT);
             
            return htpp;
        }
         
        private HttpResponse doResponse(String url) {
             
            // Use our connection and data timeouts as parameters for our
            // DefaultHttpClient
            HttpClient httpclient = new DefaultHttpClient(getHttpParams());
 
            HttpResponse response = null;
 
            try {
                switch (taskType) {
 
                case POST_TASK:
                    HttpPost httppost = new HttpPost(url);
                    // Add parameters
                    httppost.setEntity(new UrlEncodedFormEntity(params));
 
                    response = httpclient.execute(httppost);
                    break;
                case GET_TASK:
                    HttpGet httpget = new HttpGet(url);
                    response = httpclient.execute(httpget);
                    break;
                }
            } catch (Exception e) {
 
                Log.e(TAG, e.getLocalizedMessage(), e);
 
            }
 
            return response;
        }
         
        private String inputStreamToString(InputStream is) {
 
            String line = "";
            StringBuilder total = new StringBuilder();
 
            // Wrap a BufferedReader around the InputStream
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
 
            try {
                // Read response until the end
                while ((line = rd.readLine()) != null) {
                    total.append(line);
                }
            } catch (IOException e) {
                Log.e(TAG, e.getLocalizedMessage(), e);
            }
 
            // Return full string
            return total.toString();
        }
 
    }
}
