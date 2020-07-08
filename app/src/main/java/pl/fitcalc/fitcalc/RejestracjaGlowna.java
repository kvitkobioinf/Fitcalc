package pl.fitcalc.fitcalc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Objects;

public class RejestracjaGlowna extends AppCompatActivity {
    private TextView mDisplayDate;
    private static final String TAG = "Rejestracjaglowna";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    DBAdapter db; // inicjujemy zmienną do przechowywania odnośnika do klasy pomocniczej (DBAdapter.class) obsługi bazy danych

    EditText imie_et;
    EditText nazwisko_et;
    String data_urodzenia = "";  // domyślna, zerowa, wartość przed wyborem użytkownika
    EditText email_et;
    EditText haslo_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracja_glowna);

        db = new DBAdapter(this); // tworzymy i przypisujemy nowy obiekt
        db.open();

        imie_et = (EditText) findViewById(R.id.textimie);
        nazwisko_et = (EditText) findViewById(R.id.textnazwisko);
        email_et = (EditText) findViewById(R.id.textemail);
        haslo_et = (EditText) findViewById(R.id.textpassword);

//        Obsługa kalendarza
        mDisplayDate = (TextView) findViewById(R.id.kalendarz);
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(RejestracjaGlowna.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/rrrr" + dayOfMonth + "/" + month + "/" + year);
                data_urodzenia = dayOfMonth + "/" + month + "/" + year;
                mDisplayDate.setText(data_urodzenia);
            }
        };

        // przycisk kontynuacji rejestracji - ustalenie celu
        Button dalej = (Button) findViewById(R.id.buttonDalej);
        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String imie = imie_et.getText().toString().trim();
                if (imie.length() > 0) {
                    String nazwisko = nazwisko_et.getText().toString().trim();
                    if (nazwisko.length() > 0) {
                        String email = email_et.getText().toString().trim();
                        if (email.length() > 0) { // TODO sprawdzić poprawność adresu e-mail, wysłać email do potwierdzenia konta (gdy baza danych na serwerze)
                            String haslo = haslo_et.getText().toString().trim();
                            if (haslo.length() > 0) { // TODO sprawdzić czy hasło spełnia określone wymagania (ilość znaków, znaki specjalne, itd.), przygotować drugie pole (do powtórzenia hasła), przygotować przycisk do podglądu znaków hasła
                                if (data_urodzenia.length() > 0) {
                                    long[] user = db.registerUser(imie, nazwisko, data_urodzenia, email, haslo);
                                    long user_status = user[1]; // 1 - użytkownik zarejestrowany
                                    long user_id = user[0];

                                    if (user_status == 1) {
                                        if (user_id == -1) {
                                            Toast.makeText(RejestracjaGlowna.this, "Niepowodzenie rejestracji użytkownia", Toast.LENGTH_SHORT).show();
                                        } else {
                                            db.logUserIn(email, haslo); // zaloguj nowoutworzonego użytkownika

                                            Intent kontynuuj_rejestracje_intent = new Intent(RejestracjaGlowna.this, CelActivity.class);
                                            startActivity(kontynuuj_rejestracje_intent);
                                        }
                                    } else {
                                        Toast.makeText(RejestracjaGlowna.this, "Użytkownik istnieje. Zaloguj się swoim adresem email", Toast.LENGTH_SHORT).show();
                                        Intent logowanie_intent = new Intent(RejestracjaGlowna.this, LogowanieActivity.class);
                                        logowanie_intent.putExtra("email", email);
                                        startActivity(logowanie_intent);
                                    }

                                } else
                                    Toast.makeText(RejestracjaGlowna.this, "Podaj datę urodzenia", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(RejestracjaGlowna.this, "Podaj hasło", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(RejestracjaGlowna.this, "Podaj poprawny adres e-mail", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(RejestracjaGlowna.this, "Podaj nazwisko", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(RejestracjaGlowna.this, "Podaj imię", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
