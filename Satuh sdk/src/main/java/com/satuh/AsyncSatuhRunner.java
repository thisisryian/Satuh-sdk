package com.satuh;

import android.content.Context;
import android.os.Bundle;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by User on 9/11/2017.
 */


public class AsyncSatuhRunner {

    Satuh st;

    public AsyncSatuhRunner(Satuh st){
        this.st = st;
    }




    public void request(RequestListener listener) {
        request(new Bundle(), "GET", listener, /* state */ null);
    }


    public void request(final Bundle parameters,
                        final String httpMethod,
                        final RequestListener listener,
                        final Object state) {
        new Thread() {
            @Override public void run() {
                try {
                    String resp = st.request(parameters, httpMethod);
                    listener.onComplete(resp, state);
                } catch (FileNotFoundException e) {
                    listener.onFileNotFoundException(e, state);
                } catch (MalformedURLException e) {
                    listener.onMalformedURLException(e, state);
                } catch (IOException e) {
                    listener.onIOException(e, state);
                }
            }
        }.start();
    }


    public void logout(Context context)  {

        try {
            st.logout(context);
        }catch (IOException io){
            io.printStackTrace();
        }

    }

    public static interface RequestListener {

        /**
         * Called when a request completes with the given response.
         *
         * Executed by a background thread: do not update the UI in this method.
         */
        public void onComplete(String response, Object state);

        /**
         * Called when a request has a network or request error.
         *
         * Executed by a background thread: do not update the UI in this method.
         */
        public void onIOException(IOException e, Object state);

        /**
         * Called when a request fails because the requested resource is
         * invalid or does not exist.
         *
         * Executed by a background thread: do not update the UI in this method.
         */
        public void onFileNotFoundException(FileNotFoundException e,
                                            Object state);

        /**
         * Called if an invalid graph path is provided (which may result in a
         * malformed URL).
         *
         * Executed by a background thread: do not update the UI in this method.
         */
        public void onMalformedURLException(MalformedURLException e,
                                            Object state);

        /**
         * Called when the server-side Satuh method fails.
         *
         * Executed by a background thread: do not update the UI in this method.
         */
        public void onSatuhError(SatuhError e, Object state);

    }
}
