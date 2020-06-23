package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Logowanie extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logowanie);

        Button zaloguj2 = (Button) findViewById(R.id.button_zaloguj);
        zaloguj2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zaloguj2_intent = new Intent(Logowanie.this, wybor_posilku.class);
                startActivity(zaloguj2_intent);
            }
        });
    }
}
