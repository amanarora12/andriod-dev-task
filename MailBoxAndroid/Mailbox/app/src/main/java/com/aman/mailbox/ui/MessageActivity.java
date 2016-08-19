package com.aman.mailbox.ui;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.aman.mailbox.Constants;
import com.aman.mailbox.R;
import com.aman.mailbox.data.managers.MessagesManager;
import com.aman.mailbox.data.model.MessageDetails;
import com.aman.mailbox.utility.CircularImageView;
import com.aman.mailbox.utility.CreateInitials;
import com.aman.mailbox.utility.DateFormatter;

public class MessageActivity extends AppCompatActivity {
    private TextView subjectHeader;
    private TextView participants;
    private ImageView imgParticipants;
    private TextView txtInitials;
    private TextView date;
    private TextView msgBody;
    private MessageDetails messageDetails;
    private int pos;
    private ScrollView scrollView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        int id=intent.getIntExtra(Constants.EXTRA_ID,-1);
        pos=intent.getIntExtra(Constants.EXTRA_POS,-1);
        setContentView(R.layout.activity_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scrollView= (ScrollView) findViewById(R.id.scroll_view);
        subjectHeader= (TextView) findViewById(R.id.subject_header);
        participants= (TextView) findViewById(R.id.txt_participants);
        date= (TextView) findViewById(R.id.txt_date);
        msgBody= (TextView) findViewById(R.id.msg_body);
        imgParticipants= (ImageView) findViewById(R.id.img_partcipant);
        txtInitials= (TextView) findViewById(R.id.txt_initals);
        MessagesManager.getInstance().getMessageDetails(new MessagesManager.MessagesResponseCallback<MessageDetails>() {
            @Override
            public void onResult(MessageDetails response) {
                messageDetails=response;
                messageDetails.setParticipantNames();
                setViews();
            }
        },id);
    }
    private void setViews(){
        if(messageDetails!=null || pos!=-1){
            subjectHeader.setText(messageDetails.getSubject());
            date.setText(DateFormatter.getDateTime(messageDetails.getTs()));
            msgBody.setText(messageDetails.getBody());
            participants.setText(messageDetails.getParticipantNames());
            setImage(null,messageDetails.getParticipantNames());
        }else{
            Snackbar.make(scrollView,"Error Occurred. Try Again",Snackbar.LENGTH_SHORT).show();
            finish();
        }
    }
    private void setImage(String imgUrl,String name){
        CircularImageView imageView= CreateInitials.createCircle(this,imgUrl,name);
        CreateInitials.createInitialsImage(imgUrl==null,name,txtInitials);
        imgParticipants.setImageDrawable(imageView);
        imgParticipants.setBackgroundColor(Color.TRANSPARENT);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_delete) {
           MessagesManager.getInstance().deleteMessage(new MessagesManager.MessagesResponseCallback<Boolean>() {
               @Override
               public void onResult(Boolean response) {
                 if(response){
                     MessagesManager.getInstance().getMessageList().remove(pos);
                     Toast.makeText(getBaseContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                     finish();
                 }else {
                     Toast.makeText(getBaseContext(), "Message Not Deleted", Toast.LENGTH_SHORT).show();
                 }
               }
           }, (int) messageDetails.getId());
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
