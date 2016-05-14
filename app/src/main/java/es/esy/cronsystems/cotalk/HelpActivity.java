package es.esy.cronsystems.cotalk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HelpActivity extends AppCompatActivity {

    RecyclerView rv;
    ArrayList<Help> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        arrayList = new ArrayList<Help>();
        for(int i = 0; i < 5; i++) {
            arrayList.add(0, new Help());
        }
        rv = (RecyclerView) findViewById(R.id.recycler_view_help);
        if (rv != null) {
            rv.setHasFixedSize(true);
            rv.setLayoutManager(new LinearLayoutManager(this));
            HelpAdapter adapter = new HelpAdapter(arrayList);
            rv.setAdapter(adapter);
        }
    }
}
