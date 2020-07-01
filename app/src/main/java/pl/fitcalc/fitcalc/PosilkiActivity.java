package pl.fitcalc.fitcalc;

import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class PosilkiActivity extends AppCompatActivity {
    private RecyclerView dostepneDaniaRecyclerView;
    private DostepneDaniaAdapter dostepneDaniaAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageButton wyczysc_wyszukiwanie_btn;
    private View zjedzone_dania_placeholder;
    private PieChartView weglowodanyPieChart;
    private PieChartView bialkaPieChart;
    private PieChartView tluszczePieChart;

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
                TimePickerDialog godzinaPosilku = new TimePickerDialog(PosilkiActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int wybrana_godzina, int wybrana_minuta) {
                        wybierz_godzine.setText(String.valueOf(wybrana_godzina) + ':' + dodajWiodaceZero(wybrana_minuta));

                        // TODO zapisanie godziny posiłku w bazie danych
                    }
                }, godzina, minuta, true);
                godzinaPosilku.show();
            }
        });

        List<SliceValue> zjedzoneWeglowodany = new ArrayList<SliceValue>();
        zjedzoneWeglowodany.add(new SliceValue(0.3f, getColor(android.R.color.transparent)));
        zjedzoneWeglowodany.add(new SliceValue(0.7f, getColor(R.color.mediumPurple)));
        PieChartData zjedzoneWeglowodanyData = new PieChartData();
        zjedzoneWeglowodanyData.setValues(zjedzoneWeglowodany);

        weglowodanyPieChart = (PieChartView) findViewById(R.id.carbs_piechart);
        weglowodanyPieChart.setChartRotationEnabled(true);
        weglowodanyPieChart.setPieChartData(zjedzoneWeglowodanyData);
        weglowodanyPieChart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int arcIndex, SliceValue value) {
                Toast.makeText(PosilkiActivity.this, "Wybrano " + arcIndex, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {

            }
        });


        List<SliceValue> zjedzoneBialka = new ArrayList<SliceValue>();
        zjedzoneBialka.add(new SliceValue(0.3f, getColor(R.color.middleBlue)));
        zjedzoneBialka.add(new SliceValue(0.7f, getColor(R.color.skyBlueCrayola)));
        PieChartData zjedzoneBialkaData = new PieChartData();
        zjedzoneBialkaData.setValues(zjedzoneBialka);

        bialkaPieChart = (PieChartView) findViewById(R.id.proteins_piechart);
        bialkaPieChart.setChartRotationEnabled(true);
        bialkaPieChart.setPieChartData(zjedzoneBialkaData);


        List<SliceValue> zjedzoneTluszcze = new ArrayList<SliceValue>();
        zjedzoneTluszcze.add(new SliceValue(0.3f, getColor(android.R.color.transparent)));
        zjedzoneTluszcze.add(new SliceValue(0.7f, getColor(R.color.corn)));
        PieChartData zjedzoneTluszczeData = new PieChartData();
        zjedzoneTluszczeData.setValues(zjedzoneTluszcze);

        tluszczePieChart = (PieChartView) findViewById(R.id.fats_piechart);
        tluszczePieChart.setChartRotationEnabled(true);
        tluszczePieChart.setPieChartData(zjedzoneTluszczeData);

        final EditText wyszukajDanieEditText = (EditText) findViewById(R.id.wyszukaj_danie_edt);
        wyszukajDanieEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = charSequence.toString();

                // Testowo
                String[] values;
                try {
                    int repeats = Integer.parseInt(text);
                    values = new String[repeats];
                    Arrays.fill(values, "Snickers");
                    wyczysc_wyszukiwanie_btn.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    values = new String[]{};
                    wyczysc_wyszukiwanie_btn.setVisibility(View.GONE);
                }
                dostepneDaniaAdapter.update(values);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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
        dostepneDaniaAdapter = new DostepneDaniaAdapter(new String[]{}, dostepneDaniaRecyclerView, maxHeight);
        dostepneDaniaRecyclerView.setAdapter(dostepneDaniaAdapter);
    }

    private String dodajWiodaceZero(int liczba) {
        return (liczba < 10 ? "0" : "") + String.valueOf(liczba);
    }


    public static class DostepneDaniaAdapter extends RecyclerView.Adapter<DostepneDaniaAdapter.DostepneDaniaVH> {
        private String[] dania;
        private RecyclerView dostepneDaniaRecyclerView;
        private int maxHeight;

        public static class DostepneDaniaVH extends RecyclerView.ViewHolder {
            public TextView textView;

            public DostepneDaniaVH(TextView v) {
                super(v);
                textView = v;
            }
        }

        public DostepneDaniaAdapter(String[] myDataset, RecyclerView recyclerView, int maxHeight) {
            dania = myDataset;
            dostepneDaniaRecyclerView = recyclerView;
            this.maxHeight = maxHeight;
        }


        public void update(String[] dataset) {
            dania = dataset;
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
        public void onBindViewHolder(DostepneDaniaVH holder, int position) {
            holder.textView.setText(dania[position]);
        }

        @Override
        public int getItemCount() {
            return dania.length;
        }
    }
}
