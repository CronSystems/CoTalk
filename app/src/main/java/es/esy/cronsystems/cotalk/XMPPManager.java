package es.esy.cronsystems.cotalk;
import android.util.Log;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.*;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.filter.StanzaFilter;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class XMPPManager {

    public static XMPPTCPConnection connection;

    private InboxMessageListener messageListener;
    private XMPPChatManagerListener chatListener;
    CoTalkService context;
    public static String username;
    public static String password;
    public static String domain;
    public org.jivesoftware.smack.chat.Chat chat;

    private static XMPPManager instance = null;

    public static XMPPManager getInstance(CoTalkService context, String serverAddress, String user, String password) {
        if (instance == null) {
            instance = new XMPPManager(context, serverAddress, user, password);
        }
        return instance;
    }

    private XMPPManager(CoTalkService context, String serverAddress, String user, String password) {
        this.context = context;
        this.domain = serverAddress;
        this.username = user;
        this.password = password;
        chatListener = new XMPPChatManagerListener();
        messageListener = new InboxMessageListener(context);
        connectionInit();
    }

    private void connectionInit() {
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        configBuilder
                .setUsernameAndPassword(username, password)
                .setServiceName(domain).setHost(domain)
                .setDebuggerEnabled(true);

        connection = new XMPPTCPConnection(configBuilder.build());
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
        connection = new XMPPTCPConnection(configBuilder.build());
    }

    public boolean connect() {
        try {
            connection.connect();
            Log.d("Connect", "Success!");
        } catch (SmackException e) {
            Log.e("Connect", e.toString());
        } catch (IOException e) {
            Log.e("Connect", e.toString());
        } catch (XMPPException e) {
            Log.e("Connect", e.toString());
        }
        if (!connection.isAuthenticated())
        {
            login();
        }
        return connection.isAuthenticated();
    }

    public void login() {
        try {
            connection.login();
            Log.d("Login", "Success!");
        } catch (XMPPException e) {
            Log.e("Login", e.toString());
        } catch (SmackException e) {
            Log.e("Login", e.toString());
        } catch (IOException e) {
            Log.e("Login", e.toString());
        }

        if (connection.isAuthenticated()) {
            ChatManager.getInstanceFor(connection).addChatListener(chatListener);
            connection.addAsyncStanzaListener(stanzaListener, new StanzaTypeFilter(Message.class));
        }
    }

    private class XMPPChatManagerListener implements ChatManagerListener {
        @Override
        public void chatCreated(Chat chat, boolean createdLocally) {
            if (!createdLocally) chat.addMessageListener(messageListener);
        }
    }

    public void sendMessage(String text, String receiverAddress) {
        chat = ChatManager.getInstanceFor(connection).createChat(receiverAddress, messageListener);
        Message message = new Message();
        message.setBody(text);
        message.setTo(receiverAddress);
        try {
            if (connection.isAuthenticated()) {
                chat.sendMessage(message);
            } else {
                login();
            }
        } catch (SmackException.NotConnectedException e) {
            Log.e("SenderMessage", "Not connected");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private StanzaListener stanzaListener = new StanzaListener() {
        @Override
        public void processPacket(Stanza packet) throws SmackException.NotConnectedException {
            Message message = (Message) packet;
            String body = message.getBody();
            String from = message.getFrom();
        }

    };

    public boolean isConnected() {
        return connection.isAuthenticated();
    }

    public String getCurrentUserAddress() {
        return splitterFrom(connection.getUser());
    }

    private String splitterFrom (String address) {
        String result = address.split("/")[0];
        return result;
    }
}
