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

        db = new DBAdapter(this);

        // przyciski z posiłkami do wyboru docelowo generowane automatycznie na bazie danych z tabeli "meals"
    }

    public void otworzPosilek(View v) {
        int posilek = 0;
        String posilek_nazwa = "";
        switch (v.getId()) {
            case R.id.buttonsiadanie:
                posilek = 0;
                posilek_nazwa = "Śniadanie";
                break;
            case R.id.przekaska1button:
                posilek = 1;
                posilek_nazwa = "Przekąska I";
                break;
            case R.id.buttonobiad:
                posilek = 2;
                posilek_nazwa = "Obiad";
                break;
            case R.id.buttonprzekaska2:
                posilek = 3;
                posilek_nazwa = "Przekąska II";
                break;
            case R.id.buttonkolacja:
                posilek = 4;
                posilek_nazwa = "Kolacja";
                break;
        }

        Intent posilek_intent = new Intent(WyborPosilkuActivity.this, PosilkiActivity.class);
        posilek_intent.putExtra("user_id", user_id);
        posilek_intent.putExtra("meal", posilek);
        posilek_intent.putExtra("meal_name", posilek_nazwa);
        startActivity(posilek_intent);
    }

}

