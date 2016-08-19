package com.aman.mailbox.data.managers;

import android.util.Log;

import com.aman.mailbox.data.model.Message;
import com.aman.mailbox.data.model.MessageDetails;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 19-08-2016.
 */
public class MessagesManager {
    public static final String URL="https://guarded-falls-64993.herokuapp.com/api/message/";

    private static MessagesManager messagesManager;
    private static VolleyManager volleyManager;
    private List<Message> messageList;
    private Message message;

    private MessagesManager(){
        volleyManager=VolleyManager.getInstance();
        messageList=new ArrayList<>();
    }

    public static MessagesManager getInstance(){
        if (messagesManager == null) {
            synchronized (MessagesManager.class) {
                if (messagesManager == null) {
                    messagesManager = new MessagesManager();
                }
            }
        }
        return messagesManager;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public void getMessages(final MessagesResponseCallback<List<Message>> callback){
        JsonArrayRequest request=new JsonArrayRequest(Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Message> messageArrayList=null;
                try {
                    Gson gson=new Gson();
                    Type type=new TypeToken<ArrayList<Message>>(){
                    }.getType();
                    messageArrayList=gson.fromJson(response.toString(),type);
                    messageList.clear();
                    for(Message msg:messageArrayList){
                        if(message!=null && msg.getId()==message.getId()){
                            continue;
                        }
                        msg.setParticipantNames();
                        msg.setDate();
                        messageList.add(msg);
                    }
                } catch (JsonSyntaxException e) {
                }
                callback.onResult(messageList);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResult(null);
            }
        });
        volleyManager.addToRequestQueue(request);
    }

    public void getMessageDetails(final MessagesResponseCallback<MessageDetails> callback, int id){
        JsonObjectRequest request=new JsonObjectRequest(Request.Method.GET, URL + id, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MessageDetails messageDetails=null;
                try {
                    Gson gson=new Gson();
                    messageDetails=gson.fromJson(response.toString(),MessageDetails.class);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                }
                callback.onResult(messageDetails);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResult(null);
                Log.e("ERROR",error.getLocalizedMessage());
            }
        });
        volleyManager.addToRequestQueue(request);
    }

    public void deleteMessage(final MessagesResponseCallback<Boolean> callback, int id){
        StringRequest request=new StringRequest(Request.Method.DELETE, URL + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onResult(true);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                callback.onResult(false);
            }
        });
        volleyManager.addToRequestQueue(request);
    }
    public interface MessagesResponseCallback<T>{
        void onResult(T response);
    }
}
