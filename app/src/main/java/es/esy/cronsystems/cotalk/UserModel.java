package es.esy.cronsystems.cotalk;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;

/**
 * Created by nikky on 01.04.16.
 */
public class UserModel {

    public String name = "";
    public String user = "";
    public Long userId;
    public Long unread;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public UserModel(RosterEntry rosterEntry) {
        name = rosterEntry.getName();
        user = rosterEntry.getUser();
    }

    public UserModel(String name, String user, Long userId, Long unread) {
        this.name = name;
        this.user = user;
        this.userId = userId;
        this.unread = unread;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Long getUnread() {
        return unread;
    }

    public void setUnread(Long unread) {
        this.unread = unread;
    }
}
