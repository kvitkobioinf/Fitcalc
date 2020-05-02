package pl.fitcalc.fitcalc;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button przycisk = findViewById(R.id.policz);
        przycisk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int wzrost;
                float waga;
                float BMI;

                wzrost = Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.wzrost)).getText()));

                waga = Float.parseFloat(String.valueOf(((EditText) findViewById(R.id.waga)).getText()));

                float wzrost_w_metrach = wzrost / 100f;

                BMI = waga / (wzrost_w_metrach * wzrost_w_metrach);

                TextView miejsce_dla_wyniku = findViewById(R.id.pole_tekstowe);
                miejsce_dla_wyniku.setText("Twój BMI to " + String.valueOf(BMI));
            }

        });
    }

}
