package pl.fitcalc.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button zaloguj = (Button) findViewById(R.id.button_zaloguj); //znalezienie przycisku ( przepisanie do zmiennej)

        zaloguj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zaloguj_intent = new Intent(MainActivity.this, LogowanieActivity.class); // skąd -> dokąd (przejscie)
                startActivity(zaloguj_intent);

            }
        });

        Button zaloz = (Button) findViewById (R.id.button_zaloz);
        zaloz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zaloz_intent = new Intent(MainActivity.this, RejestracjaGlowna.class);
                startActivity(zaloz_intent);
            }
        });
    }

}
