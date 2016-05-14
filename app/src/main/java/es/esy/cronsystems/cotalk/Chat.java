package es.esy.cronsystems.cotalk;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Date;

/**
 * Created by nikky on 03.04.16.
 */
public class Chat {
    private Date timeofsent;
    private String messages = "";
    private String from = "";
    private Long messageId;

    public Chat(Long messageId, Date timeofsent, String messages, String from) {
        this.timeofsent = timeofsent;
        this.messages = messages;
        this.from  = from;
        this.messageId = messageId;
    }

    public Date getTimeOfSent() {
        return timeofsent;
    }

    public void setTimeOfSent(Date timeofsent) {
        this.timeofsent = timeofsent;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String result) {
        this.messages = result;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }
}
