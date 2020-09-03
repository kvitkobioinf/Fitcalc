package pl.fitcalc.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class WyborPosilkuActivity extends AppCompatActivity {
    private long user_id;
    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybor_posilku);

        Bundle extras = getIntent().getExtras();
        user_id = Objects.requireNonNull(extras).getLong("user_id");

        Toast.makeText(this, String.valueOf(user_id), Toast.LENGTH_SHORT).show();

        db = new DBAdapter(this);
    }

    public void otworzPosilek(View v) {
        int posilek;
        if (v.getId() == R.id.buttonsiadanie) {
            posilek = 0;
            Log.d("FitCalc", "Otworze posilek - sniadanie");
            startActivity(new Intent(WyborPosilkuActivity.this, PosilkiActivity.class));
        } else if (v.getId() == R.id.przekaska1button) {
            posilek = 1;
            Log.d("FitCalc", "Otworze posilek - przekaska 1");
        }


    }

}

