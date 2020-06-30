package pl.fitcalc.fitcalc;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class PosilkiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posilki);

        Calendar teraz = Calendar.getInstance();
        final int godzina = teraz.get(Calendar.HOUR_OF_DAY);
        final int minuta = teraz.get(Calendar.MINUTE);

        final Button wybierz_godzine = (Button) findViewById(R.id.godzina_posilku_btn);
        wybierz_godzine.setText(String.valueOf(godzina) + ':' + dodajWiodaceZero(minuta));
        wybierz_godzine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog godzinaPosilku = new TimePickerDialog(PosilkiActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int wybrana_godzina, int wybrana_minuta) {
                        wybierz_godzine.setText(String.valueOf(wybrana_godzina) + ':' + dodajWiodaceZero(wybrana_minuta));

                        // TODO zapisanie godziny posiłku w bazie danych
                    }
                }, godzina, minuta, true);
                godzinaPosilku.show();
            }
        });

    }

    private String dodajWiodaceZero(int liczba) {
        return (liczba < 10 ? "0" : "") + String.valueOf(liczba);
    }


}
