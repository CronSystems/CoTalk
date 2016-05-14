package es.esy.cronsystems.cotalk;

/**
 * Created by anton on 13.03.2016.
 */
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;


public class DialogListAdapter extends RecyclerView.Adapter<DialogListAdapter.MyViewHolder> {

    private ArrayList<UserModel> mData;
    public static String username;
    private String name = "name";
    private String xmppadress = "xmppadress";
    private String presence = "presence";
    private String id = "chatId";
    public DialogListAdapter(ArrayList<UserModel> persons) {
        mData = persons;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.position = position;
        holder.name.setText(mData.get(position).getName());
        holder.xmppadress.setText(mData.get(position).getUser());
        holder.imageView.setImageBitmap(BitmapFactory.decodeResource(holder.view.getResources()
                , R.drawable.man));
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = holder.name.getText().toString();
                Intent intent = new Intent(holder.view.getContext(), DialogActivity.class);
                intent.putExtra(name, holder.name.getText().toString());
                intent.putExtra(id, mData.get(holder.position).getUserId());
                intent.putExtra(xmppadress, holder.xmppadress.getText().toString());
                holder.view.getContext().startActivity(intent);
            }
        });
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
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
        public TextView name;
        public TextView xmppadress;
        public ImageView imageView;
        public int position;

        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            name = (TextView) itemView.findViewById(R.id.text_name);
            xmppadress = (TextView) itemView.findViewById(R.id.text_xmppadress);
            imageView = (ImageView) itemView.findViewById(R.id.person_view);
        }
    }
}
