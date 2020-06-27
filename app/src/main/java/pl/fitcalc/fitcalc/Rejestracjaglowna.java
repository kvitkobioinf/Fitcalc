package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Rejestracjaglowna extends AppCompatActivity {
private TextView mDisplayDate;
private static final String TAG = "Rejestracjaglowna";
 private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rejestracjaglowna);

       mDisplayDate = (TextView) findViewById(R.id.kalendarz);
       mDisplayDate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Calendar cal = Calendar.getInstance();
               int year = cal.get(Calendar.YEAR);
               int month = cal.get(Calendar.MONTH);
               int day = cal.get(Calendar.DAY_OF_MONTH);


               DatePickerDialog dialog = new DatePickerDialog(Rejestracjaglowna.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
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
         String date = dayOfMonth + "/" + month + "/" + year;
         mDisplayDate.setText(date);
     }
     };

        Button dalej = (Button) findViewById(R.id.buttonDalej);
        dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dalej_intent = new Intent(Rejestracjaglowna.this, CEL.class);
                startActivity(dalej_intent);
            }
        });

        }


    }
