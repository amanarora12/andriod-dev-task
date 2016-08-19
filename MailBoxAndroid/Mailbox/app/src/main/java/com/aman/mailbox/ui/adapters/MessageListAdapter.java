package com.aman.mailbox.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aman.mailbox.Constants;
import com.aman.mailbox.R;
import com.aman.mailbox.data.managers.MessagesManager;
import com.aman.mailbox.data.model.Message;
import com.aman.mailbox.ui.MessageActivity;
import com.aman.mailbox.utility.CircularImageView;
import com.aman.mailbox.utility.CreateInitials;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 19-08-2016.
 */
public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageViewHolder> {
    private List<Message> messageList;
    private Context context;
    private LayoutInflater inflater;
    public MessageListAdapter(Context context){
        this.context=context;
        messageList=new ArrayList<>();
        inflater=LayoutInflater.from(context);
    }

    public void setMessageList(List<Message> messageList) {
        if(messageList!=null){
            this.messageList.clear();
            this.messageList.addAll(messageList);
            notifyDataSetChanged();
        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_msg,null);
        MessageViewHolder viewHolder=new MessageViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message=messageList.get(position);
        holder.txtPreview.setText(message.getPreview());
        holder.txtSubject.setText(message.getSubject());
        holder.participants.setText(message.getParticipantNames());
        setImage(null,message.getParticipantNames(),holder);
        holder.txtTime.setText(message.getDate());
        holder.setUnreadMsgIndicator(message.getIsRead());
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    private void setImage(String imgUrl,String name,MessageViewHolder viewHolder){
        CircularImageView imageView= CreateInitials.createCircle(context,imgUrl,name);
        CreateInitials.createInitialsImage(imgUrl==null,name,viewHolder.userInitials);
        viewHolder.imgParticipants.setImageDrawable(imageView);
        viewHolder.imgParticipants.setBackgroundColor(Color.TRANSPARENT);
    }

    public Message delete(final int pos) {
        Message message=messageList.get(pos);
        messageList.remove(pos);
        MessagesManager.getInstance().getMessageList().remove(pos);
        notifyItemRemoved(pos);
        return message;
    }

    public boolean add(Message message, int pos){
        if(pos>messageList.size())
            return false;
        messageList.add(pos,message);
        MessagesManager.getInstance().getMessageList().add(pos,message);
        notifyItemInserted(pos);
        return true;
    }
    class MessageViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgParticipants;
        private TextView userInitials;
        private TextView txtUnreadMsgs;
        private TextView participants;
        private TextView txtTime;
        private TextView txtSubject;
        private TextView txtPreview;

        public MessageViewHolder(View itemView) {
            super(itemView);
            imgParticipants= (ImageView) itemView.findViewById(R.id.img_partcipant);
            userInitials= (TextView) itemView.findViewById(R.id.txt_user_initials);
            txtUnreadMsgs= (TextView) itemView.findViewById(R.id.txt_unread_msgs);
            participants= (TextView) itemView.findViewById(R.id.txt_participants);
            txtTime= (TextView) itemView.findViewById(R.id.txt_time);
            txtSubject= (TextView) itemView.findViewById(R.id.txt_subject);
            txtPreview= (TextView) itemView.findViewById(R.id.txt_preview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setUnreadMsgIndicator(true);
                    Intent i = new Intent(v.getContext(), MessageActivity.class);
                    i.putExtra(Constants.EXTRA_POS, getAdapterPosition());
                    i.putExtra(Constants.EXTRA_ID,messageList.get(getAdapterPosition()).getId());
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    v.getContext().startActivity(i);
                }
            });
        }
        public void setUnreadMsgIndicator(boolean isRead) {
            int pos=getAdapterPosition();
            if (isRead) {
                txtUnreadMsgs.setVisibility(View.GONE);
                messageList.get(pos).setIsRead(true);
                MessagesManager.getInstance().getMessageList().get(pos).setIsRead(true);
            } else {
                txtUnreadMsgs.setVisibility(View.VISIBLE);
            }
        }
    }
}
