package es.esy.cronsystems.cotalk;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewDialogCreateActivity extends Activity {

    private String adress;
    private String name;
    private AutoCompleteTextView emailForTransfer;
    private AutoCompleteTextView nameForTransfer;
    public final static String AdressResult = "es.esy.cronsystems.cotalk.AdressResult";
    public final static String NameResult = "es.esy.cronsystems.cotalk.NameResult";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dialog_create);
        Button button = (Button) findViewById(R.id.email_sign_in_button);
        emailForTransfer = (AutoCompleteTextView) findViewById(R.id.email_new_name);
        nameForTransfer = (AutoCompleteTextView) findViewById(R.id.name_new_user);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adress = emailForTransfer.getText().toString();
                name = nameForTransfer.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(NameResult, name);
                intent.putExtra(AdressResult, adress);
                setResult(101, intent);
                finish();
            }
        });
    }
}
