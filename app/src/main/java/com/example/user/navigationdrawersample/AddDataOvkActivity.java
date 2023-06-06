package com.example.user.navigationdrawersample;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.navigationdrawersample.Auth.ApiServices;
import com.example.user.navigationdrawersample.Model.DataAyam;
import com.example.user.navigationdrawersample.Model.DataOvk;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddDataOvkActivity extends AppCompatActivity {
    TextView tv_tanggalovk,tv_jumlah_ayam, tv_jenis_ovk,tv_next_ovk, tv_biaya_ovk;
    Button reset,save;
    private DatePickerDialog.OnDateSetListener mDate;
    private DatePickerDialog.OnDateSetListener mDate2;
    String formattedDate = "";
    String formattedDate2 = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data_ovk);
        tv_tanggalovk = findViewById(R.id.tanggalovk);
        tv_jenis_ovk = findViewById(R.id.jenisovk);
        tv_jumlah_ayam = findViewById(R.id.jumlahayam);
        tv_next_ovk = findViewById(R.id.nextovk);
        tv_biaya_ovk = findViewById(R.id.biayaovk);
        save = findViewById(R.id.save);
        reset = findViewById(R.id.reset);

        tv_tanggalovk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddDataOvkActivity.this,android.R.style.Theme_Material_Light_Dialog_MinWidth,
                        mDate, year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        tv_next_ovk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddDataOvkActivity.this,android.R.style.Theme_Material_Light_Dialog_MinWidth,
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

                tv_tanggalovk.setText(formattedDate);
            }
        };
        mDate2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                formattedDate2 = dateFormat.format(selectedDate.getTime());

                tv_next_ovk.setText(formattedDate2);
            }
        };
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_tanggalovk.setText("");
                tv_jenis_ovk.setText("");
                tv_jumlah_ayam.setText("");
                tv_biaya_ovk.setText("");
                tv_next_ovk.setText("");
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
                try {
                    Date date = dateFormat.parse(formattedDate);
                    Date date2 = dateFormat.parse(formattedDate2);
                    SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String new_tanggalovk = outputFormat.format(date);
                    String new_jenisovk = tv_jenis_ovk.getText().toString().trim();
                    String new_jumlahayam = tv_jumlah_ayam.getText().toString().trim();
                    String new_nextovk = outputFormat.format(date2);
                    String new_biayaovk = tv_biaya_ovk.getText().toString().trim();

                    ApiServices.createDataOvk(getApplicationContext(), new_tanggalovk, new_jenisovk, new_jumlahayam, new_nextovk, new_biayaovk, new ApiServices.CreateDataOvkResponseListener() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Toast.makeText(AddDataOvkActivity.this, "Berhasil menambah data ovk", Toast.LENGTH_SHORT).show();
                            tv_tanggalovk.setText("");
                            tv_jenis_ovk.setText("");
                            tv_jumlah_ayam.setText("");
                            tv_biaya_ovk.setText("");
                            tv_next_ovk.setText("");
                            ApiServices.readDataOvk(getApplicationContext(), new ApiServices.DataOvkResponseListener() {
                                @Override
                                public void onSuccess(List<DataOvk> dataOvkList) {
                                    onBackPressed();
                                }

                                @Override
                                public void onError(String message) {
                                    Log.e("error", message);
                                }
                            });

                        }

                        @Override
                        public void onError(String message) {
                            Toast.makeText(AddDataOvkActivity.this, "Gagal menambah data ayam", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}