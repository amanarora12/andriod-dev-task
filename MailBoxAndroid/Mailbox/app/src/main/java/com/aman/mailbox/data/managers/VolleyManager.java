package com.aman.mailbox.data.managers;

import com.aman.mailbox.MailboxApp;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Aman on 19-08-2016.
 */
public class VolleyManager {
    private static VolleyManager volleyManager;
    private static RequestQueue requestQueue;

    private VolleyManager(){
        requestQueue= Volley.newRequestQueue(MailboxApp.getAppContext());
    }
    public static VolleyManager getInstance(){
        if(volleyManager==null){
            synchronized (VolleyManager.class){
                if(volleyManager==null){
                    volleyManager=new VolleyManager();
                }
            }
        }
        return volleyManager;
    }

    public static RequestQueue getRequestQueue() {
        return requestQueue;
    }
    public <T> boolean addToRequestQueue(Request<T> request) {
        if (requestQueue == null)
            return false;
        requestQueue.add(request);
        return true;
    }
}
