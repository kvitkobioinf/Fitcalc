package pl.fitcalc.fitcalc;

import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PosilkiActivity extends AppCompatActivity {
    private RecyclerView dostepneDaniaRecyclerView;
    private DostepneDaniaAdapter dostepneDaniaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private LinearLayout foodContainer;
    private PieChartView elementyPieChart;
    private ImageButton wyczysc_wyszukiwanie_btn;
    private View zjedzone_dania_placeholder;

    private long user_id;
    private int meal;
    private int meal_id;
    private String meal_name;
    private ArrayList<Food> meal_food;

    DBAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posilki);

        db = new DBAdapter(this);
        db.open();

        Bundle extras = getIntent().getExtras();
        user_id = Objects.requireNonNull(extras).getLong("user_id");
        meal = Objects.requireNonNull(extras).getInt("meal");
        meal_name = Objects.requireNonNull(extras).getString("meal_name");
        ((TextView) findViewById(R.id.posilek_tv)).setText(meal_name);

        Calendar teraz = Calendar.getInstance();
        final int godzina = teraz.get(Calendar.HOUR_OF_DAY);
        final int minuta = teraz.get(Calendar.MINUTE);

        Date dzien = Calendar.getInstance().getTime();
        System.out.println("Current time => " + dzien);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = df.format(dzien);

        String[] id_time = db.getUserMealIdTime(user_id, formattedDate);
        String czas;
        if (id_time != null) {
            meal_id = Integer.parseInt(id_time[0]);
            czas = id_time[1];
        } else {
            // dodawanie nowego posiłku
            ContentValues values = new ContentValues();
            values.put("user_id", user_id);
            values.put("date", formattedDate);
            czas = DodajWiodaceZero(godzina) + ':' + DodajWiodaceZero(minuta);
            values.put("time", czas);
            values.put("meal_id", meal);
            meal_id = (int) db.insert("user_meals", values);
            if (meal_id == -1) {
                Toast.makeText(this, "Wystąpił błąd dodawania nowego posiłku", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        final Button wybierz_godzine = (Button) findViewById(R.id.godzina_posilku_btn);
        wybierz_godzine.setText(czas);
        wybierz_godzine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog godzinaPosilku = new TimePickerDialog(PosilkiActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int wybrana_godzina, int wybrana_minuta) {
                        String czas = DodajWiodaceZero(wybrana_godzina) + ':' + DodajWiodaceZero(wybrana_minuta);
                        wybierz_godzine.setText(czas);
                        ContentValues values = new ContentValues();
                        values.put("time", czas);
                        db.update("user_meals", values, "id = " + meal_id);
                    }
                }, godzina, minuta, true);
                godzinaPosilku.show();
            }
        });

        foodContainer = (LinearLayout) findViewById(R.id.food_container);
        elementyPieChart = (PieChartView) findViewById(R.id.elements_piechart);

        final EditText wyszukajDanieEditText = (EditText) findViewById(R.id.wyszukaj_danie_edt);
        wyszukajDanieEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();

                // Testowo
                ArrayList<Food> values = new ArrayList<>();
                try {
                    if (text.length() > 3) {
                        values = db.findFoods(text);
                        wyczysc_wyszukiwanie_btn.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    values = new ArrayList<>();
                    wyczysc_wyszukiwanie_btn.setVisibility(View.GONE);
                }
                dostepneDaniaAdapter.update(values);
            }
        });

        zjedzone_dania_placeholder = findViewById(R.id.zjedzone_dania_placeholder);
        zjedzone_dania_placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wyszukajDanieEditText.requestFocus()) {
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });

        wyczysc_wyszukiwanie_btn = (ImageButton) findViewById(R.id.wyczysc_wyszukiwanie_btn);
        wyczysc_wyszukiwanie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wyszukajDanieEditText.setText("");
            }
        });

        layoutManager = new LinearLayoutManager(this);
        dostepneDaniaRecyclerView = (RecyclerView) findViewById(R.id.dostepne_dania_rv);
        dostepneDaniaRecyclerView.setLayoutManager(layoutManager);
        int maxHeight = Math.round(getResources().getDimension(R.dimen.dostepne_dania_max_height));
        dostepneDaniaAdapter = new DostepneDaniaAdapter(new ArrayList<Food>(), dostepneDaniaRecyclerView, maxHeight, PosilkiActivity.this);
        dostepneDaniaRecyclerView.setAdapter(dostepneDaniaAdapter);

        OdswiezListeDan();
    }

    public void DodajDanie(Food danie, float porcja) {
        ContentValues values = new ContentValues();
        values.put("meal_id", meal_id);
        values.put("serving", porcja);
        values.put("food_id", danie.id);
        if (db.insert("meal_food", values) != -1)
            OdswiezListeDan();
    }

    private void AktualizujDanie(Food danie, float porcja) {
        ContentValues values = new ContentValues();
        values.put("serving", porcja);
        if (db.update("meal_food", values, "id = " + danie.food_meal_id) > 0)
            OdswiezListeDan();
    }

    private void OdswiezListeDan() {
        foodContainer.removeAllViews();

        meal_food = db.getUserMealFood(meal_id);

        if (meal_food.size() > 0)
            zjedzone_dania_placeholder.setVisibility(View.GONE);
        else
            zjedzone_dania_placeholder.setVisibility(View.VISIBLE);

        float fat = 0;
        float proteins = 0;
        float carbohydrates = 0;

        for (final Food food : meal_food) {
            View danie_layout = getLayoutInflater().inflate(R.layout.danie_posilku, null);
            ((TextView) danie_layout.findViewById(R.id.name)).setText(food.name + " (" + (int) Math.round(food.serving * food.weight) + food.unit + ")");
            float all = food.carbohydrates + food.proteins + food.fat;
            fat = fat + (food.fat * food.serving);
            proteins = proteins + (food.proteins * food.serving);
            carbohydrates = carbohydrates + (food.carbohydrates * food.serving);
            ((TextView) danie_layout.findViewById(R.id.nutrients)).setText("T " + (int) Math.round(100 * food.fat / all) + "% B " + (int) Math.round(100 * food.proteins / all) + "% W " + (int) Math.round(100 * food.carbohydrates / all));
            ((ImageButton) danie_layout.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (db.delete("meal_food", "id = " + food.food_meal_id) > 0)
                        OdswiezListeDan();
                }
            });
            ((ImageButton) danie_layout.findViewById(R.id.edit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(PosilkiActivity.this);
                    builder.setTitle("Porcja " + food.name + " (" + food.weight + food.unit + ")");
                    final EditText input = new EditText(PosilkiActivity.this);
                    input.setText(String.valueOf(food.serving));
                    builder.setView(input);
                    builder.setPositiveButton("Edytuj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            float porcja = Float.parseFloat(input.getText().toString());
                            AktualizujDanie(food, porcja);
                        }
                    });
                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
            foodContainer.addView(danie_layout);
        }

        float all = fat + proteins + carbohydrates;

        List<SliceValue> zjedzoneElementy = new ArrayList<SliceValue>();
        zjedzoneElementy.add(new SliceValue(carbohydrates / all, getColor(R.color.middleBlue)));
        zjedzoneElementy.add(new SliceValue(fat / all, getColor(R.color.corn)));
        zjedzoneElementy.add(new SliceValue(proteins / all, getColor(R.color.androidGreen)));
        PieChartData zjedzoneElementyData = new PieChartData();
        zjedzoneElementyData.setValues(zjedzoneElementy);

        elementyPieChart.setChartRotationEnabled(true);
        elementyPieChart.setPieChartData(zjedzoneElementyData);
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.open();
    }

    @Override
    protected void onPause() {
        db.close();

        super.onPause();
    }

    public static String DodajWiodaceZero(int liczba) {
        return (liczba < 10 ? "0" : "") + String.valueOf(liczba);
    }

    public static class DostepneDaniaAdapter extends RecyclerView.Adapter<DostepneDaniaAdapter.DostepneDaniaVH> {
        private ArrayList<Food> foods;
        private RecyclerView dostepneDaniaRecyclerView;
        private int maxHeight;
        private Context context;

        public static class DostepneDaniaVH extends RecyclerView.ViewHolder {
            public TextView textView;

            public DostepneDaniaVH(TextView v) {
                super(v);
                textView = v;
            }
        }

        public DostepneDaniaAdapter(ArrayList<Food> foods, RecyclerView recyclerView, int maxHeight, Context context) {
            this.foods = foods;
            dostepneDaniaRecyclerView = recyclerView;
            this.maxHeight = maxHeight;
            this.context = context;
        }

        public void update(ArrayList<Food> foods) {
            this.foods = foods;
            ViewGroup.LayoutParams params = dostepneDaniaRecyclerView.getLayoutParams();
            if (getItemCount() > 5) {
                params.height = this.maxHeight;
                dostepneDaniaRecyclerView.setLayoutParams(params);
            } else {
                params.height = RecyclerView.LayoutParams.WRAP_CONTENT;
                dostepneDaniaRecyclerView.setLayoutParams(params);
            }
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public DostepneDaniaAdapter.DostepneDaniaVH onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView v = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.danie_textview, parent, false);
            DostepneDaniaVH vh = new DostepneDaniaVH(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(DostepneDaniaVH holder, final int position) {
            holder.textView.setText(foods.get(position).name);
            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Porcja " + foods.get(position).name + " (" + foods.get(position).weight + foods.get(position).unit + ")");
                    final EditText input = new EditText(context);
                    builder.setView(input);
                    builder.setPositiveButton("Dodaj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            float porcja = Float.parseFloat(input.getText().toString());
                            ((PosilkiActivity) context).DodajDanie(foods.get(position), porcja);
                        }
                    });
                    builder.setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return foods.size();
        }
    }
}
