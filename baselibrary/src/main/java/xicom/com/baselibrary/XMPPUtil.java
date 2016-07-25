package xicom.com.baselibrary;

import android.os.AsyncTask;
import android.util.Log;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.iqregister.AccountManager;

import java.io.IOException;
import java.util.Collection;

/**
 * Created by Sanidhya09 on 18/07/2016.
 * http://www.programcreek.com/java-api-examples/index.php?source_dir=LOL-Chat-master/app/src/main/java/com/github/theholywaffle/lolchatapi/wrapper/Friend.java
 */
public enum XMPPUtil {
    INSTANCE;
//    private static final String DOMAIN = "xmpp.jp";
//    private static final String HOST = "xmpp.jp";
//    private static final int PORT = 5222;
    AbstractXMPPConnection connection;
    ChatManager chatmanager;
    XMPPConnectionListener connectionListener = new XMPPConnectionListener();

    //Initialize
    public void init(String domain, String host, int port) {
        Log.i("XMPP", "Initializing!");

        SmackConfiguration.DEBUG = true;
        XMPPTCPConnectionConfiguration.Builder configBuilder = XMPPTCPConnectionConfiguration.builder();
        //configBuilder.setUsernameAndPassword(userName, passWord);
        configBuilder.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
        configBuilder.setResource("Android");
        configBuilder.setServiceName(domain);
        configBuilder.setHost(host);
        configBuilder.setPort(port);
        configBuilder.setSendPresence(true);
        configBuilder.setDebuggerEnabled(true);
        SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
        SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
        SASLAuthentication.unBlacklistSASLMechanism("PLAIN");

        connection = new XMPPTCPConnection(configBuilder.build());
        connection.addConnectionListener(connectionListener);

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {

            @Override
            protected Boolean doInBackground(Void... arg0) {
                try {
                    connection.connect();
                } catch (IOException e) {
                } catch (SmackException e) {

                } catch (XMPPException e) {
                }

                return null;
            }
        };
        connectionThread.execute();
    }

    public AbstractXMPPConnection getConnection() {
        return connection;
    }

    public void sendMsg(String message, String userId) {
        if (connection.isConnected()) {
            chatmanager = ChatManager.getInstanceFor(connection);
            Chat chat = chatmanager.createChat(userId, new ChatMessageListener() {
                @Override
                public void processMessage(Chat chat, Message message) {
                    Log.d("xmpp", "Message Received :: " + message);
                }
            });

            try {
                chat.sendMessage(message);
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
    }

    public void checkPresence() {
        Roster roster = Roster.getInstanceFor(connection);
        roster.addRosterListener(new RosterListener() {
            public void entriesDeleted(Collection<String> addresses) {
            }

            @Override
            public void entriesAdded(Collection<String> addresses) {

            }

            public void entriesUpdated(Collection<String> addresses) {
            }

            public void presenceChanged(Presence presence) {
                System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
            }
        });
    }

    public void login(String userName, String passWord) {
        try {
            connection.login(userName, passWord);
        } catch (XMPPException | SmackException | IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

    }

    public void registerUser(String userName, String passWord) {
        AccountManager accountManager = AccountManager.getInstance(connection);
        try {
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(userName, passWord);
        } catch (XMPPException e1) {
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }

    // Disconnect Function
    public void disconnectConnection() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();
    }

    //Connection Listener to check connection state
    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {
            Log.d("xmpp", "Connected!");
        }

        @Override
        public void connectionClosed() {
            Log.d("xmpp", "ConnectionClosed!");
        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            Log.d("xmpp", "ConnectionClosedOn Error!");
        }

        @Override
        public void reconnectingIn(int arg0) {
            Log.d("xmpp", "Reconnectingin " + arg0);
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            Log.d("xmpp", "ReconnectionFailed!");
        }

        @Override
        public void reconnectionSuccessful() {
            Log.d("xmpp", "ReconnectionSuccessful");
        }

        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.d("xmpp", "Authenticated!");

        }
    }


}