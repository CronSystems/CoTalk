package es.esy.cronsystems.cotalk;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class CoTalkService extends Service {

    public CoTalkService() {
    }

    public String service_log = "ServiceLog";

    private final IBinder binder = new XMPPServiceBinder();
    public static XMPPManager xmppManager;
    public static DatabaseHelper databaseManager;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(service_log, "onBind");
        return binder;
    }

    public class XMPPServiceBinder extends Binder {
        public CoTalkService getService() {
            return CoTalkService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseManager = new DatabaseHelper(getBaseContext(), "database.db", null, 1);
        Log.d(service_log, "onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags, final int startId) {
        Log.d(service_log, "onStartCommand");
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (databaseManager != null) databaseManager.close();
        if (xmppManager != null) xmppManager.connection.disconnect();
        Log.d(service_log, "onDestroy");
    }

    public Long sendMessage(String from, String to, String text, Long chatId) {
        Long messageId = null;
        messageId = databaseManager.insertMessage(from, text, chatId);
        xmppManager.sendMessage(text, to);
        return messageId;
    }

    public Long sendMessage(String from,String to, String text) {
        Long messageId = null;
        UserModel user = databaseManager.getUserByAddress(to);
        if(user != null) {
            messageId = sendMessage(from, to, text, user.getUserId());
        }
        return messageId;
    }
}
