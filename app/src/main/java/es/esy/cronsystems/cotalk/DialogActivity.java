package es.esy.cronsystems.cotalk;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

public class DialogActivity extends AppCompatActivity {

    String result;
    static String new_name;
    static String new_xmpp_address;
    static Long chatId;
    static String current_user;
    static ArrayList<Chat>  messages;
    static RecyclerView rv;
    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!MainActivity.coTalkService.xmppManager.isConnected()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return;
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        chatId = getIntent().getLongExtra("chatId", 0);
        new_xmpp_address = getIntent().getStringExtra("xmppadress");
        new_name = getIntent().getStringExtra("name");
        current_user = MainActivity.coTalkService.xmppManager.getCurrentUserAddress();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(new_name);
        actionBar.setSubtitle(new_xmpp_address);
        messages = new ArrayList<>();
        context = getBaseContext();
        rv = (RecyclerView) findViewById(R.id.recycler_view);
        if (rv != null) {
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(this));
            ChatListAdapter adapter = new ChatListAdapter(messages);
            rv.setAdapter(adapter);
            RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
            rv.setItemAnimator(itemAnimator);
        }
        final TextView message_input = (TextView) findViewById(R.id.message_input);
         FloatingActionButton sendButton = (FloatingActionButton) findViewById(R.id.send_button);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            result = message_input.getText().toString();
            message_input.setText("");
            if (!result.equals("")) {
                Long messageId = MainActivity.coTalkService.sendMessage(current_user, new_xmpp_address, result, chatId);
                if (messageId != null) {
                    messages.add(new Chat(messageId, new Date(), result, current_user));
                    rv.getAdapter().notifyDataSetChanged();
                    rv.getLayoutManager().smoothScrollToPosition(rv, null, rv.getAdapter().getItemCount() - 1);
                }
            }
            }
        });
        ArrayList<Chat> msgs = MainActivity.coTalkService.databaseManager.getMessages(chatId);
        if (msgs.size() > 0) {
            messages.clear();
            messages.addAll(msgs);
            rv.getAdapter().notifyDataSetChanged();
        }

        if (rv.getAdapter().getItemCount() > 0)
            rv.getLayoutManager().smoothScrollToPosition(rv, null, rv.getAdapter().getItemCount() - 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(DialogActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_help) {
            Intent intent = new Intent(DialogActivity.this, HelpActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


}