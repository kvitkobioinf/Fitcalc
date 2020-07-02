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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cel);

        final EditText textwaga = (EditText) findViewById(R.id.textwaga);
        textwaga.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2 && !s.toString().equals("10") && start != 2 && before != 1 && !s.toString().contains(".")) {
                    textwaga.append(".");
                }

                if (s.length() == 3 && before != 1 && start != 3 && s.subSequence(0,1).toString().equals("1") && s.subSequence(1,2).toString().equals("0") && !s.toString().contains(("."))){
                    textwaga.append(".");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
