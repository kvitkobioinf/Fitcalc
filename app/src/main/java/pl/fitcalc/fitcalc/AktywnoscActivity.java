package pl.fitcalc.fitcalc;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class AktywnoscActivity extends AppCompatActivity {
    private long user_id;
    private long measurement_id;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aktywnosc);

        Bundle extras = getIntent().getExtras();
        user_id = Objects.requireNonNull(extras).getLong("user_id");
        measurement_id = Objects.requireNonNull(extras).getLong("measurement_id");

        db = new DBAdapter(this);
    }

    public void WybierzPosilek(View view) {
        float poziom_aktywnosci = 0;
        switch (view.getId()) {
            case R.id.buttonact1:
                poziom_aktywnosci = 1;
                break;
            case R.id.buttonact2:
                poziom_aktywnosci = 2;
                break;
            case R.id.buttonact3:
                poziom_aktywnosci = 3;
                break;
            case R.id.buttonact4:
                poziom_aktywnosci = 4;
                break;
            case R.id.buttonact5:
                poziom_aktywnosci = 5;
                break;
        }

        db.open();
        ContentValues values = new ContentValues();
        values.put("activity_level", poziom_aktywnosci);
        db.update("user_measurements", values, "id = '" + String.valueOf(measurement_id) + "'");
        db.close();

        Intent wybor_posilku_intent = new Intent(AktywnoscActivity.this, WyborPosilkuActivity.class);
        wybor_posilku_intent.putExtra("user_id", user_id);
        startActivity(wybor_posilku_intent);
    }
}
