package es.esy.cronsystems.cotalk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;

import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.Date;

public class InboxMessageListener implements ChatMessageListener {

    private static final int NOTIFY_ID = 101;
    static String from;


    public InboxMessageListener(Context context) {
    }

    @Override
    public void processMessage(org.jivesoftware.smack.chat.Chat chat, final Message message) {
        String text = message.getBody();
        from = splitterFrom(message.getFrom());
        final Context contextApp = MainActivity.context;
        final Context contextDialog = DialogActivity.context;
        UserModel userModel = MainActivity.coTalkService.databaseManager.getUserByAddress(from);
        if (userModel != null) {
            Long messageId = MainActivity.coTalkService.databaseManager.insertMessage(from, text, userModel.getUserId());
            if (from.equals(DialogActivity.new_xmpp_address)) {
                DialogActivity.messages.add(new Chat(messageId, new Date(), text, from));
                DialogActivity.rv.post(new Runnable() {
                    @Override
                    public void run() {
                        DialogActivity.rv.getAdapter().notifyDataSetChanged();
                        DialogActivity.rv.getLayoutManager().smoothScrollToPosition(DialogActivity.rv, null,
                                DialogActivity.rv.getAdapter().getItemCount() - 1);
                    }
                });
            }
            /*Intent notificationIntent = new Intent(contextApp, DialogActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(contextApp,
                    0, notificationIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            Resources res = contextApp.getResources();
            Notification.Builder builder = new Notification.Builder(contextApp);

            builder.setContentIntent(contentIntent)
                    .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.man_online))
                    .setTicker(res.getString(R.string.new_message))
                    .setWhen(System.currentTimeMillis())
                    .setAutoCancel(true)
                    .setContentTitle(R.string.notify_new_message_title + message.getFrom())
                    .setContentText(message.getBody());
            Notification notification = builder.getNotification();

            NotificationManager notificationManager = (NotificationManager) contextApp
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(NOTIFY_ID, notification);*/
        }
    }

    public String splitterFrom (String address) {
        String result = address.split("/")[0];
        return result;
    }
}
