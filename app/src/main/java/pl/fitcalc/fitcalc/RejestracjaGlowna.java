package pl.fitcalc.fitcalc;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RejestracjaGlowna extends AppCompatActivity {
    private TextView mDisplayDate;
    private static final String TAG = "RejestracjaGlowna";
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    DBAdapter db; // inicjujemy zmienną do przechowywania odnośnika do klasy pomocniczej (DBAdapter.class) obsługi bazy danych

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja_glowna);

        db = new DBAdapter(this); // tworzymy i przypisujemy nowy obiekt
        mDisplayDate = (TextView) findViewById(R.id.kalendarz);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RejestracjaGlowna.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mDateSetListener, year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/rrrr" + dayOfMonth + "/" + month + "/" + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        final RadioButton kobieta_rb = (RadioButton) findViewById(R.id.radio_kobieta);
        final RadioButton mezczyzna_rb = (RadioButton) findViewById(R.id.radio_mezczyzna);
        final EditText imie_ed = (EditText) findViewById(R.id.textimie);
        imie_ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().endsWith("a"))
                    kobieta_rb.setChecked(true);
                else
                    mezczyzna_rb.setChecked(true);
            }
        });

        Button dalej = (Button) findViewById(R.id.buttonDalej);
        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dalej_intent = new Intent(RejestracjaGlowna.this, CelActivity.class);

                String imie = imie_ed.getText().toString();
                if (IsEmptyString(imie)) {
                    Toast.makeText(RejestracjaGlowna.this, "Uzupełnij imię", Toast.LENGTH_SHORT).show();
                    return;
                }

                String nazwisko = ((EditText) findViewById(R.id.textnazwisko)).getText().toString();
                if (IsEmptyString(nazwisko)) {
                    Toast.makeText(RejestracjaGlowna.this, "Uzupełnij nazwisko", Toast.LENGTH_SHORT).show();
                    return;
                }

                String urodziny = mDisplayDate.getText().toString();
                if (IsEmptyString(urodziny)) {
                    Toast.makeText(RejestracjaGlowna.this, "Uzupełnij datę urodzin", Toast.LENGTH_SHORT).show();
                    return;
                }

                String email = ((EditText) findViewById(R.id.textemail)).getText().toString();
                if (IsEmptyString(email)) {
                    Toast.makeText(RejestracjaGlowna.this, "Podaj swój adres e-mail", Toast.LENGTH_SHORT).show();
                    return;
                }

                String haslo = ((EditText) findViewById(R.id.texthaslo)).getText().toString();
                if (IsEmptyString(haslo)) {
                    Toast.makeText(RejestracjaGlowna.this, "Wybierz hasło", Toast.LENGTH_SHORT).show();
                    return;
                }

                String potwierdzHaslo = ((EditText) findViewById(R.id.textpotwierdzhaslo)).getText().toString();
                if (IsEmptyString(potwierdzHaslo)) {
                    Toast.makeText(RejestracjaGlowna.this, "Potwierdź hasło", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!potwierdzHaslo.equals(haslo)) {
                    Toast.makeText(RejestracjaGlowna.this, "Hasło i potwierdzenie hasła różnią się", Toast.LENGTH_SHORT).show();
                    return;
                }

                String plec = kobieta_rb.isChecked() ? "f" : "m";

                db.open();
                ContentValues values = new ContentValues();
                values.put("first_name", imie);
                values.put("last_name", nazwisko);
                values.put("email", email);
                values.put("password", haslo);
                values.put("birthday", urodziny);
                values.put("sex", plec);
                long user_id = db.insert("users", values);

                if (user_id == -1) {
                    if (db.getUserId(email) != -1)
                        Toast.makeText(RejestracjaGlowna.this, "Użytkownik istnieje - zaloguj się", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(RejestracjaGlowna.this, "Błąd rejestracji", Toast.LENGTH_SHORT).show();

                    return;
                }

                db.close();

                dalej_intent.putExtra("user_id", user_id);
                startActivity(dalej_intent);
            }
        });
    }

    public static boolean IsEmptyString(String string) {
        return string == null || string.isEmpty();
    }
}
