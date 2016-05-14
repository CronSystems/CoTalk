package es.esy.cronsystems.cotalk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class DatabaseHelper extends SQLiteOpenHelper implements BaseColumns {

    public static Context context = null;
    public static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    public static final String NAME_COLUMN = "name";
    public static final String CHAT_ID = "chatId";
    public static final String XMPPADDRESS_COLUMN = "address";
    public static final String TEXTMESSAGE_COLUMN = "message";
    public static final String DATETIME_COLUMN = "created";
    private static final String TABLE_USERS_DATABASE_CREATE_SCRIPT = "create table if not exists"
            + " users" + " (" + BaseColumns._ID
            + " integer primary key autoincrement, " + NAME_COLUMN
            + " text not null, " + XMPPADDRESS_COLUMN + " text not null unique); ";

    private static final String TABLE_MESSAGES_DATABASE_CREATE_SCRIPT = "create table if not exists messages ("
            + BaseColumns._ID + " integer primary key autoincrement, "
            + CHAT_ID + " integer references users (" + BaseColumns._ID + "), "
            + XMPPADDRESS_COLUMN + " text not null, "
            + TEXTMESSAGE_COLUMN + " text not null, "
            + DATETIME_COLUMN + " datetime default CURRENT_TIMESTAMP" + "); ";

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
        super(context, name, factory, version);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_USERS_DATABASE_CREATE_SCRIPT);
        db.execSQL(TABLE_MESSAGES_DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Update from " + oldVersion + " on version " + newVersion);
        db.execSQL("DROP TABLE IF IT EXISTS " + "users");
        db.execSQL("DROP TABLE IF IT EXISTS " + "messages");
        onCreate(db);

    }

    public UserModel getUserByAddress(String address) {
        UserModel userModel = null;
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM users WHERE address = '" + address + "'";
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);
        if (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN));
            String user = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.XMPPADDRESS_COLUMN));
            Long chatId = cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID));
            userModel = new UserModel(name, user, chatId);
        }
        cursor.close();
        mSqLiteDatabase.close();
        return userModel;
    }

    public Long insertMessage(String from, String text, Long chatId) {
        Long messageId = null;
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("address", from);
        values.put("message", text);
        values.put("chatId", chatId);
        messageId = mSqLiteDatabase.insert("messages", null, values);
        mSqLiteDatabase.close();
        return messageId;
    }

    public ArrayList<Chat> getMessages(Long chatId) {
        ArrayList<Chat> messages = new ArrayList<Chat>();
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM messages WHERE chatId=" + chatId.toString() + ";";
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            long messageId = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
            String address = cursor.getString(cursor.getColumnIndex(DatabaseHelper.XMPPADDRESS_COLUMN));
            String textmessage = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TEXTMESSAGE_COLUMN));
            String datetime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.DATETIME_COLUMN));
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            formatter.setTimeZone(TimeZone.getTimeZone("gmt"));
            try {
                Date DateTime = formatter.parse(datetime);
                messages.add(new Chat(messageId, DateTime, textmessage, address));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        mSqLiteDatabase.close();
        return messages;
    }

    public Long insertUser(String name, String address) {
        Long userId = null;
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.NAME_COLUMN, name);
        contentValues.put(DatabaseHelper.XMPPADDRESS_COLUMN, address);
        userId = mSqLiteDatabase.insert("users", null, contentValues);
        mSqLiteDatabase.close();
        return userId;
    }

    public ArrayList<UserModel> getUserModels() {
        ArrayList<UserModel> users = new ArrayList<UserModel>();
        SQLiteDatabase mSqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM users;";
        Cursor cursor = mSqLiteDatabase.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.NAME_COLUMN));
            String user = cursor.getString(cursor
                    .getColumnIndex(DatabaseHelper.XMPPADDRESS_COLUMN));
            Long id = cursor.getLong(cursor.getColumnIndex(DatabaseHelper._ID));
            users.add(new UserModel(name, user, id));
        }
        mSqLiteDatabase.close();
        return users;
    }

    public void deleteMessage(Long messageId) {
        SQLiteDatabase mSqLiteDatabase = getWritableDatabase();
        mSqLiteDatabase.execSQL("DELETE FROM messages WHERE _ID=" + messageId + ";");
        mSqLiteDatabase.close();

    }
}
