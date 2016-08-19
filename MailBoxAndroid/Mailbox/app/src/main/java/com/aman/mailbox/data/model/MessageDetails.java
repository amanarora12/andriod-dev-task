package com.aman.mailbox.data.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aman on 19-08-2016.
 */
public class MessageDetails {
    private String subject;
    private List<Participant> participants = new ArrayList<Participant>();
    private String preview;
    private boolean isRead;
    private boolean isStarred;
    private long id;
    private String body;
    private long ts;
    private String participantNames;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public boolean isIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTs() {
        return ts;
    }
    public void setTs(long ts) {
        this.ts = ts;
    }

    public void setParticipantNames(){
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(participants.get(0).getName());
        int i=1;
        while (i < participants.size()) {
            stringBuilder.append(", ").append(participants.get(i).getName());
            i++;
        }
        participantNames=stringBuilder.toString();
    }

    public String getParticipantNames() {
        return participantNames;
    }
}
