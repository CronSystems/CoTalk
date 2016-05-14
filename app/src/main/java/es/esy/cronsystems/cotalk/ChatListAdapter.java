package es.esy.cronsystems.cotalk;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nikky on 03.04.16.
 */
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder>{
    private ArrayList<Chat> mData;
    public ChatListAdapter(ArrayList<Chat> chatrooms) {
        mData = chatrooms;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.message.setText(mData.get(position).getMessages());
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        Date date = mData.get(position).getTimeOfSent();
        holder.timeofsent.setText(dateFormat.format(date));
        if((mData.get(position).getFrom()).equals(DialogActivity.current_user)) {
            holder.card.setCardBackgroundColor(Color.WHITE);
        } else {
            holder.card.setCardBackgroundColor(Color.LTGRAY);
        }
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.coTalkService.databaseManager.deleteMessage(mData.get(holder.getAdapterPosition()).getMessageId());
                mData.remove(mData.get(holder.getAdapterPosition()));
                DialogActivity.rv.getAdapter().notifyDataSetChanged();
                Snackbar.make(v, "Message deleted", Snackbar.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView message;
        public TextView timeofsent;
        public CardView card;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            message = (TextView) itemView.findViewById(R.id.text_message);
            timeofsent = (TextView) itemView.findViewById(R.id.time_message);
            card = (CardView) itemView.findViewById(R.id.card);
        }
    }
}
