package es.esy.cronsystems.cotalk;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nikky on 07.05.16.
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.MyViewHolder>{


    private ArrayList<Help> mData;
    public static String username;
    private String name = "name";
    private String xmppadress = "xmppadress";
    private String presence = "presence";
    public HelpAdapter(ArrayList<Help> help) {
        mData = help;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.help_item_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.help_header.setText(mData.get(position).getHelp_header());
        holder.help_text.setText(mData.get(position).getHelp_text());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public TextView help_header;
        public TextView help_text;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            help_header = (TextView) itemView.findViewById(R.id.help_header);
            help_text = (TextView) itemView.findViewById(R.id.help_text);
        }
    }
}
