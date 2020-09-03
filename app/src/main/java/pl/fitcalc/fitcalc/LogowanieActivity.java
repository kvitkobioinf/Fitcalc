package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LogowanieActivity extends AppCompatActivity {
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        db = new DBAdapter(this);

        final EditText email = (EditText) findViewById(R.id.email);
        final EditText haslo = (EditText) findViewById(R.id.haslo);
        Button zaloguj = (Button) findViewById(R.id.button_zaloguj);

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long user_id = db.logUserIn(email.getText().toString(), haslo.getText().toString());

                if (user_id == -1) {
                    Toast.makeText(LogowanieActivity.this, "Nieprawidłowe dane logowania", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent wybor_posilku_intent = new Intent(LogowanieActivity.this, WyborPosilkuActivity.class);
                wybor_posilku_intent.putExtra("user_id", user_id);
                startActivity(wybor_posilku_intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.open();
    }

    @Override
    protected void onPause() {
        db.close();

        super.onPause();
    }
}
