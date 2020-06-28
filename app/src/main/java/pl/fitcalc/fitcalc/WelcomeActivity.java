package pl.fitcalc.fitcalc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        DBAdapter db = new DBAdapter(this);
        db.open();
        int numberRows = db.count("users");
        Log.d("probabazydanych", "numberRows" );
        db.insert("users", "first_name, last_name, email, password, birthday, sex, body_type", "'Darya', 'Boginskaya', 'boginskad@gmail.com', 'marcopolo', '27/11/1996', 'female', 'exomatic'");
        numberRows = db.count("users");
        Log.d("probabazydanych", "numberRows" );
        Button przejdz_dalej = (Button) findViewById(R.id.button);
        przejdz_dalej.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
            }
        });
    }
}