package com.example.user.navigationdrawersample;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.navigationdrawersample.Auth.ApiServices;
import com.example.user.navigationdrawersample.Model.DataPakan;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDataPakanActivity extends AppCompatActivity {

    TextView pembelian,jenispakan, hargakg,stok;
    Button reset,save;
    private DatePickerDialog.OnDateSetListener mDate;
    String formattedDate = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_pakan);

        pembelian = findViewById(R.id.pembelian);
        jenispakan = findViewById(R.id.jenispakan);
        hargakg = findViewById(R.id.hargakg);
        stok = findViewById(R.id.stokpakan);
        save = findViewById(R.id.save);
        reset = findViewById(R.id.reset);

        pembelian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddDataPakanActivity.this,android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDate, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                formattedDate = dateFormat.format(selectedDate.getTime());

                pembelian.setText(formattedDate);
            }
        };
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pembelian.setText("");
                jenispakan.setText("");
                hargakg.setText("");
                stok.setText("");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                try {
                    Date date = dateFormat.parse(formattedDate);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String ppembelian = outputFormat.format(date);
                    String pjenis = jenispakan.getText().toString().trim();
                    String pstok = stok.getText().toString().trim();
                    String pharga = hargakg.getText().toString().trim();

                    ApiServices.createDataPakan(getApplicationContext(), ppembelian, pjenis, pstok, pharga, new ApiServices.CreateDataPakanResponseListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(AddDataPakanActivity.this, "Berhasil menambah data pakan", Toast.LENGTH_SHORT).show();
                            pembelian.setText("");
                            jenispakan.setText("");
                            stok.setText("");
                            hargakg.setText("");
                            ApiServices.readDataPakan(getApplicationContext(), new ApiServices.DataPakanResponseListener() {
                                @Override
                                public void onSuccess(List<DataPakan> dataPakanList) {
                                    onBackPressed();
                                }

                                @Override
                                public void onError(String message) {
                                }
                            });

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(AddDataPakanActivity.this, "Gagal menambah data pakan", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}