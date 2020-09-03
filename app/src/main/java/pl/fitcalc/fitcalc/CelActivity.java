package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class CelActivity extends AppCompatActivity {
    private String cel;
    private long user_id;

    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cel);

        user_id = Objects.requireNonNull(getIntent().getExtras()).getLong("user_id");
        db = new DBAdapter(this);

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

                if (s.length() == 3 && before != 1 && start != 3 && s.subSequence(0, 1).toString().equals("1") && s.subSequence(1, 2).toString().equals("0") && !s.toString().contains(("."))) {
                    textwaga.append(".");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button utrata = (Button) findViewById(R.id.utrata);
        final Button zachowanie = (Button) findViewById(R.id.zachowanie);
        final Button nabor = (Button) findViewById(R.id.nabor);

        utrata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cel = "utrata";

                v.setSelected(false);
                nabor.setSelected(true);
                zachowanie.setSelected(true);
            }
        });

        zachowanie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cel = "zachowanie";

                v.setSelected(false);
                utrata.setSelected(true);
                nabor.setSelected(true);
            }
        });

        nabor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cel = "nabor";

                v.setSelected(false);
                utrata.setSelected(true);
                zachowanie.setSelected(true);
            }
        });

        ((Button) findViewById(R.id.kontynuuj)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RejestracjaGlowna.IsEmptyString(textwaga.getText().toString())) {
                    Toast.makeText(CelActivity.this, "Uzupełnij wagę docelową", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (RejestracjaGlowna.IsEmptyString(cel)) {
                    Toast.makeText(CelActivity.this, "Wybierz swój cel", Toast.LENGTH_SHORT).show();
                    return;
                }

                db.open();
                ContentValues values = new ContentValues();
                values.put("user_id", user_id);
                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String formattedDate = df.format(c);
                values.put("date", formattedDate);
                values.put("desired_weight", textwaga.getText().toString());
                values.put("goal", cel);
                long measurement_id = db.insert("user_measurements", values);
                db.close();
                if (measurement_id == -1) {
                    Toast.makeText(CelActivity.this, "Błąd dodawania danych", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent aktywnosc_intent = new Intent(CelActivity.this, AktywnoscActivity.class);
                aktywnosc_intent.putExtra("user_id", user_id);
                aktywnosc_intent.putExtra("measurement_id", measurement_id);
                startActivity(aktywnosc_intent);
            }
        });
    }
}
