package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CEL extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c_e_l);

        Button utrata = (Button) findViewById(R.id.utrata);
        utrata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent utrata_intent = new Intent(CEL.this, obliczenie.class);
                startActivity(utrata_intent);
            }
        });

        Button zachowanie = (Button) findViewById(R.id.zachowanie);
        zachowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zachowanie_intent = new Intent(CEL.this, obliczenie.class);
                startActivity(zachowanie_intent);
            }
        });

        Button nabor = (Button) findViewById(R.id.nabor);
        nabor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nabor_intent = new Intent(CEL.this, Aktywnosc.class);
                startActivity(nabor_intent);
            }
        });
    }
}
