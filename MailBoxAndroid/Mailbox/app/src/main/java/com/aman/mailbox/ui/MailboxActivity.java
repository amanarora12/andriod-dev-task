package com.aman.mailbox.ui;

import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.aman.mailbox.R;
import com.aman.mailbox.data.managers.MessagesManager;
import com.aman.mailbox.data.model.Message;
import com.aman.mailbox.data.model.MessageDetails;
import com.aman.mailbox.ui.adapters.MessageListAdapter;

import java.util.List;

public class MailboxActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private MessagesManager messagesManager;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView msgRecyclerView;
    private MessageListAdapter messageListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox);
        messagesManager=MessagesManager.getInstance();
        refreshLayout= (SwipeRefreshLayout) findViewById(R.id.refresh_layout);
        msgRecyclerView= (RecyclerView) findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        refreshLayout.setOnRefreshListener(this);
        messageListAdapter=new MessageListAdapter(getBaseContext());
        msgRecyclerView.setAdapter(messageListAdapter);
        ItemTouchHelper.SimpleCallback callback = new SwipeToDelete(messageListAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(msgRecyclerView);
        getData();
    }
    private void getData(){
        refreshLayout.setRefreshing(true);
        messagesManager.getMessages(new MessagesManager.MessagesResponseCallback<List<Message>>() {
            @Override
            public void onResult(List<Message> response) {
                refreshLayout.setRefreshing(false);
                if(response!=null){
                    messageListAdapter.setMessageList(response);
                }else {
                    Snackbar.make(refreshLayout,"Error Occurred. Try Again",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        messageListAdapter.setMessageList(messagesManager.getMessageList());
    }

    @Override
    public void onRefresh() {
        getData();
    }

    private class SwipeToDelete extends ItemTouchHelper.SimpleCallback {
        private MessageListAdapter messageListAdapter;
        private Message message;
        private int position;
        private CountDownTimer timer;
        public SwipeToDelete(MessageListAdapter messageListAdapter) {
            super(ItemTouchHelper.ACTION_STATE_IDLE, ItemTouchHelper.START | ItemTouchHelper.END);
            this.messageListAdapter=messageListAdapter;
            timer=new CountDownTimer(3000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    if(message!=null && position!=-1){
                        MessagesManager.getInstance().deleteMessage(new MessagesManager.MessagesResponseCallback<Boolean>() {
                            @Override
                            public void onResult(Boolean response) {
                                Log.d(getClass().getSimpleName(), "Delete " + response);
                            }
                        },message.getId());
                        MessagesManager.getInstance().setMessage(null);
                        message=null;
                        position=-1;
                    }else {

                    }
                }
            };
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            if(message!=null && position!=-1){
                MessagesManager.getInstance().deleteMessage(new MessagesManager.MessagesResponseCallback<Boolean>() {
                    @Override
                    public void onResult(Boolean response) {
                        Log.d(getClass().getSimpleName(), response+" ");
                    }
                },message.getId());
            }
            position = viewHolder.getAdapterPosition();
            Message message1 = messageListAdapter.delete(viewHolder.getAdapterPosition());
            message = message1;
            MessagesManager.getInstance().setMessage(message1);
            Snackbar snackbar=Snackbar.make(refreshLayout,"Message Deleted",3000).setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (message != null && position != -1) {
                        messageListAdapter.add(message, position);
                        timer.cancel();
                        message = null;
                        MessagesManager.getInstance().setMessage(null);
                        position = -1;
                    }
                }
            });
            snackbar.show();
            timer.cancel();
            timer.start();
        }
    }
}
