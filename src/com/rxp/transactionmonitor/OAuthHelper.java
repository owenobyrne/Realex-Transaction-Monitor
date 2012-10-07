package com.rxp.transactionmonitor;

import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthServiceProvider;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class OAuthHelper {
	private static final String requestToken = "https://api.realexpayments.com/IPS-Reporting/oauth/request_token";
	private static final String authorize = "https://api.realexpayments.com/IPS-Reporting/oauth/confirm_access";
	private static final String accessToken = "https://api.realexpayments.com/IPS-Reporting/oauth/access_token";
	public static final String callbackURL = "http://localhost";
	//String callbackUrl = "realex-android-app:///";
	
	// private static final String requestToken = "http://payb.in/oauth/request_token";
	// private static final String authorize = "http://payb.in/oauth/confirm_access";
	// private static final String accessToken = "http://payb.in/oauth/access_token";


	static public OAuthServiceProvider defaultProvider() {
		OAuthServiceProvider provider = new OAuthServiceProvider(requestToken, authorize, accessToken);
		return provider;
	}

	static public OAuthAccessor defaultAccessor() {
		OAuthServiceProvider provider = defaultProvider();
		// OAuthConsumer consumer = new OAuthConsumer(callbackUrl, "tonr-consumer-key", "SHHHHH!!!!!!!!!!", provider);
		OAuthConsumer consumer = new OAuthConsumer(callbackURL, "realcontrol", "mysecret", provider);
		OAuthAccessor accessor = new OAuthAccessor(consumer);

		return accessor;
	}

}
