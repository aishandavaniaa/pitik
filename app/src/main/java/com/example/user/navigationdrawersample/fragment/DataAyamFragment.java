package com.example.user.navigationdrawersample.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.navigationdrawersample.AddDataAyamActivity;
import com.example.user.navigationdrawersample.Auth.ApiServices;
import com.example.user.navigationdrawersample.EditDataAyamActivity;
import com.example.user.navigationdrawersample.Model.DataAyam;
import com.example.user.navigationdrawersample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataAyamFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataAyamFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DataAyamFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataAyamFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataAyamFragment newInstance(String param1, String param2) {
        DataAyamFragment fragment = new DataAyamFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    RecyclerView rv_data;
    List<DataAyam> data = new ArrayList<>();
    Button add;
    CustomAdapterDataAyam adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_data_ayam, container, false);
        rv_data = view.findViewById(R.id.rv_dataayam);
        add = view.findViewById(R.id.add);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(layoutManager);
        adapter = new CustomAdapterDataAyam( data, getContext());
        rv_data.setAdapter(adapter);
        loadData();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddDataAyamActivity.class);
                startActivity(i);
            }
        });
        return view;

    }
    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        ApiServices.readDataAyam(getContext(), new ApiServices.DataAyamResponseListener() {
            @Override
            public void onSuccess(List<DataAyam> dataAyamList) {
                adapter = new CustomAdapterDataAyam( dataAyamList, getContext());
                rv_data.setAdapter(adapter);
                data = dataAyamList;
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onError(String message) {
                Log.e("Failed to load", message);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

    }
    public static class CustomAdapterDataAyam extends RecyclerView.Adapter<CustomAdapterDataAyam.ViewHolder> {
        private List<DataAyam> dataAyamList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapterDataAyam(List<DataAyam> dataAyamList, Context context) {
            this.dataAyamList = dataAyamList;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_list_ayam, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tglmasuk.setText(dataAyamList.get(position).getTanggalMasuk());
            holder.jumlahmasuk.setText(dataAyamList.get(position).getJumlahMasuk());
            holder.hargasatuan.setText(dataAyamList.get(position).getHargaSatuan());
            holder.mati.setText(dataAyamList.get(position).getMati());
            holder.totalharga.setText(dataAyamList.get(position).getTotalHarga());
            holder.totalayam.setText(dataAyamList.get(position).getTotalAyam());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, EditDataAyamActivity.class);
                        intent.putExtra("dataayam", dataAyamList.get(pos));
                        context.startActivity(intent);
                    }
                }
            });
            holder.hapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        AlertDialog.Builder alert =new AlertDialog.Builder(context);
                        View alertView =LayoutInflater.from(context).inflate(R.layout.popup_delete,
                                (LinearLayout) view.findViewById(R.id.popup_box));
                        alert.setView(alertView);
                        final AlertDialog dialog = alert.create();
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                        dialog.show();
                        Button ok, batal;
                        ok = alertView.findViewById(R.id.btl_ok);
                        batal = alertView.findViewById(R.id.btl_batal);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ApiServices.deleteDataAyam(context, dataAyamList.get(pos).getId(), new ApiServices.DeleteDataAyamResponseListener() {
                                    @Override
                                    public void onSuccess(String response) {
                                        // Notify the adapter
                                        Toast.makeText(alertView.getContext(), response, Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }

                                    @Override
                                    public void onError(String message) {
                                        Toast.makeText(alertView.getContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show();
                                        dialog.cancel();
                                    }
                                });
                            }
                        });
                        batal.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.cancel();
                            }
                        });
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataAyamList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tglmasuk, jumlahmasuk, hargasatuan, mati, totalharga, totalayam;
            FrameLayout edit, hapus;
            public ViewHolder(View itemView) {
                super(itemView);
                tglmasuk = itemView.findViewById(R.id.tglmasuk);
                jumlahmasuk = itemView.findViewById(R.id.jumlahmasuk);
                hargasatuan = itemView.findViewById(R.id.hrgsatuan);
                mati = itemView.findViewById(R.id.mati);
                totalharga = itemView.findViewById(R.id.total);
                totalayam = itemView.findViewById(R.id.totalayam);
                edit = itemView.findViewById(R.id.editdata);
                hapus = itemView.findViewById(R.id.hapusdata);
            }
        }
    }

}
