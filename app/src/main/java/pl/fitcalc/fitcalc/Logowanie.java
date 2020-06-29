package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Logowanie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        final DBAdapter db = new DBAdapter(this);
        db.open();
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText haslo = (EditText) findViewById(R.id.haslo);
        Button zaloguj2 = (Button) findViewById(R.id.button_zaloguj);
        zaloguj2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int user_id = db.logUserIn(email.getText().toString(), haslo.getText().toString());

                if(user_id != 0) {
                    Intent zaloguj2_intent = new Intent(Logowanie.this, wybor_posilku.class);
                    startActivity(zaloguj2_intent);
                }
                else {
                    //Log
                }
            }
        });
    }
}
