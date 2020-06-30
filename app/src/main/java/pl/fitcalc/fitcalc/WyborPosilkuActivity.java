package pl.fitcalc.fitcalc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class WyborPosilkuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wybor_posilku);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
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

