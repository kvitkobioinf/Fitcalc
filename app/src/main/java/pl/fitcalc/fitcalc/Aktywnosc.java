package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Aktywnosc extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktywnosc);

        Button siedzacy = (Button) findViewById(R.id.buttonact1);
        siedzacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siedzacy_intent = new Intent(Aktywnosc.this, obliczenie.class);
                startActivity(siedzacy_intent);
            }
        });
    }
}
