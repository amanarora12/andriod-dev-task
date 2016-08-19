package com.aman.mailbox.data.model;

import com.aman.mailbox.utility.DateFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 19-08-2016.
 */
public class Message {
    private String subject;
    private List<String> participants = new ArrayList<String>();
    private String preview;
    private boolean isRead;
    private boolean isStarred;
    private long ts;
    private int id;
    private String participantNames;
    private String date;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public long getTs() {
        return ts;
    }

    public void setTs(long ts) {
        this.ts = ts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate() {
        date= DateFormatter.getDate(ts);
    }

    public String getDate() {
        return date;
    }

    public void setParticipantNames(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(participants.get(0));
        int i=1;
        while (i < participants.size()) {
            stringBuilder.append(", ").append(participants.get(i));
            i++;
        }
        participantNames=stringBuilder.toString();
    }

    public String getParticipantNames() {
        return participantNames;
    }
}
