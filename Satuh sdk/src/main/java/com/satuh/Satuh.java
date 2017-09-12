package com.satuh;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieSyncManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by User on 9/11/2017.
 */


public class Satuh {

    // Strings used in the authorization flow
    public static final String REDIRECT_URI = "https://account.satuh.com/response";
    public static final String CANCEL_URI = "cancel";

    private String mAppId;
    private String mAccessToken = null;
    private DialogListener mAuthDialogListener;
    protected static String DIALOG_BASE_URL =
            "https://account.satuh.com/oauth/authorize";
    protected static String API =
            "https://account.satuh.com/api/user";
    public Satuh(String appId) {
        if (appId == null) {
            throw new IllegalArgumentException(
                    "You must specify your application ID when instantiating " +
                            "a Satuh object. See README for details.");
        }
        mAppId = appId;
    }

    public void authorize(Activity activity, final DialogListener listener) {

        boolean singleSignOnStarted = false;

        mAuthDialogListener = listener;


        startDialogAuth(activity);

    }


    private void startDialogAuth(Activity activity) {
        final AccessTokenCache a = new AccessTokenCache(activity.getApplicationContext());
        if(a.load()!=null){
            mAuthDialogListener.onComplete(a.load());
        }else {
            Bundle params = new Bundle();

            CookieSyncManager.createInstance(activity);
            dialog(activity, params, new DialogListener() {

                public void onComplete(String values) {
                    // ensure any cookies set by the dialog are saved

                    a.save(values);
                    setAccessToken(values);

                    Util.logd("Satuh-authorize", "Login Success! access_token="
                            + getAccessToken() + " expires="
                    );
                    mAuthDialogListener.onComplete(values);


                }

                public void onError(DialogError error) {
                    Util.logd("Satuh-authorize", "Login failed: " + error);
                    mAuthDialogListener.onError(error);
                }

                //
                public void onSatuhError(SatuhError error) {
                    Util.logd("Satuh-authorize", "Login failed: " + error);
                    mAuthDialogListener.onSatuhError(error);
                }

                public void onCancel() {
                    Util.logd("Satuh-authorize", "Login canceled");
                    mAuthDialogListener.onCancel();
                }
            });
        }
    }

    public String getAccessToken() {
        return mAccessToken;
    }

    public void setAccessToken(String token) {
        mAccessToken = token;
    }

    public void dialog(Context context, Bundle parameters,
                       final DialogListener listener) {

        String endpoint = DIALOG_BASE_URL;

        parameters.putString("redirect_uri", REDIRECT_URI);


        parameters.putString("response_type", "token");
        parameters.putString("client_id", mAppId);
        parameters.putString("scope","");


        String url = endpoint + "?" + Util.encodeUrl(parameters);
        if (context.checkCallingOrSelfPermission(Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            Util.showAlert(context, "Error",
                    "Application requires permission to access the Internet");
        } else {
//
            new SatuhDialog(context, url, listener).show();
        }
    }

    public String request(Bundle params, String httpMethod)
            throws FileNotFoundException, MalformedURLException, IOException {

        String url = API;
        String auth = "Bearer " +getAccessToken();
        return Util.openUrl(url, httpMethod, auth);
    }


    public static interface DialogListener {

        /**
         * Called when a dialog completes.
         *
         * Executed by the thread that initiated the dialog.
         *
         * @param values
         *            Key-value string pairs extracted from the response.
         */
        public void onComplete(String values);

        /**
         * Called when a Satuh responds to a dialog with an error.
         *
         * Executed by the thread that initiated the dialog.
         *
         */
        public void onSatuhError(SatuhError e);

        /**
         * Called when a dialog has an error.
         *
         * Executed by the thread that initiated the dialog.
         *
         */
        public void onError(DialogError e);

        /**
         * Called when a dialog is canceled by the user.
         *
         * Executed by the thread that initiated the dialog.
         *
         */
        public void onCancel();

    }

    /**
     * Callback interface for service requests.
     */
    public static interface ServiceListener {

        /**
         * Called when a service request completes.
         *
         * @param values
         *            Key-value string pairs extracted from the response.
         */
        public void onComplete(Bundle values);

        /**
         * Called when a Satuh server responds to the request with an error.
         */
        public void onSatuhError(SatuhError e);

        /**
         * Called when a Satuh Service responds to the request with an error.
         */
        public void onError(Error e);

    }

    public void logout(Context context)
            throws MalformedURLException, IOException {
        Util.clearCookiesForDomain(context, "account.satuh.com");
        Util.clearCookiesForDomain(context, "https://account.satuh.com/");
        AccessTokenCache a = new AccessTokenCache(context);
        a.clear();
        setAccessToken(null);

    }

}
