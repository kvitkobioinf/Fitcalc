package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CelActivity extends AppCompatActivity {
    DBAdapter db; // inicjujemy zmienną do przechowywania odnośnika do klasy pomocniczej (DBAdapter.class) obsługi bazy danych
    long user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cel);

        user_id = Ustawienia.getUserId(CelActivity.this);

        db = new DBAdapter(this); // tworzymy i przypisujemy nowy obiekt
        db.open();

        EditText waga_docelowa_et = (EditText) findViewById(R.id.textwaga);
        waga_docelowa_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                float waga = Float.parseFloat(s.toString());
            }
        });

        Button utrata = (Button) findViewById(R.id.utrata);
        utrata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent utrata_intent = new Intent(CelActivity.this, AktywnoscActivity.class);
                startActivity(utrata_intent);
            }
        });

        Button zachowanie = (Button) findViewById(R.id.zachowanie);
        zachowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent zachowanie_intent = new Intent(CelActivity.this, AktywnoscActivity.class);
                startActivity(zachowanie_intent);
            }
        });

        Button nabor = (Button) findViewById(R.id.nabor);
        nabor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nabor_intent = new Intent(CelActivity.this, AktywnoscActivity.class);
                startActivity(nabor_intent);
            }
        });
    }
}
