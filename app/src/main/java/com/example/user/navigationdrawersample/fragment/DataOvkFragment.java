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
import com.example.user.navigationdrawersample.AddDataOvkActivity;
import com.example.user.navigationdrawersample.Auth.ApiServices;
import com.example.user.navigationdrawersample.EditDataAyamActivity;
import com.example.user.navigationdrawersample.EditDataOvkActivity;
import com.example.user.navigationdrawersample.Model.DataAyam;
import com.example.user.navigationdrawersample.Model.DataOvk;
import com.example.user.navigationdrawersample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataOvkFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataOvkFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DataOvkFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataOvkFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataOvkFragment newInstance(String param1, String param2) {
        DataOvkFragment fragment = new DataOvkFragment();
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
    List<DataOvk> data = new ArrayList<>();
    Button add;
    CustomAdapterDataOvk adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_ovk, container, false);
        rv_data = view.findViewById(R.id.rv_dataovk);
        add = view.findViewById(R.id.add);
        swipeRefreshLayout = view.findViewById(R.id.refresh);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(layoutManager);
        adapter = new CustomAdapterDataOvk( data, getContext());
        rv_data.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddDataOvkActivity.class);
                startActivity(i);
            }
        });
        loadData();
        return view;
    }

    private void loadData(){
        swipeRefreshLayout.setRefreshing(true);
        ApiServices.readDataOvk(getContext(), new ApiServices.DataOvkResponseListener() {
            @Override
            public void onSuccess(List<DataOvk> dataOvkList) {
                adapter = new CustomAdapterDataOvk( dataOvkList, getContext());
                rv_data.setAdapter(adapter);
                data = dataOvkList;
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


    public static class CustomAdapterDataOvk extends RecyclerView.Adapter<CustomAdapterDataOvk.ViewHolder> {
        private List<DataOvk> dataOvkList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapterDataOvk(List<DataOvk> dataOvkList, Context context) {
            this.dataOvkList = dataOvkList;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public CustomAdapterDataOvk.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_list_ovk, parent, false);
            return new CustomAdapterDataOvk.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapterDataOvk.ViewHolder holder, int position) {
            holder.tanggalovk.setText(dataOvkList.get(position).getTanggalOvk());
            holder.jumlahayam.setText(dataOvkList.get(position).getJumlahAyam());
            holder.jenisovk.setText(dataOvkList.get(position).getJenisOvk());
            holder.nextovk.setText(dataOvkList.get(position).getNextOvk());
            holder.biayaovk.setText(dataOvkList.get(position).getBiayaOvk());
            holder.totalbiaya.setText(dataOvkList.get(position).getTotalBiaya());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, EditDataOvkActivity.class);
                        intent.putExtra("dataovk", dataOvkList.get(pos));
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
                                ApiServices.deleteDataOvk(context, dataOvkList.get(pos).getId(), new ApiServices.DeleteDataOvkResponseListener() {
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
            return dataOvkList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tanggalovk, jenisovk, jumlahayam, nextovk, biayaovk, totalbiaya;
            FrameLayout edit, hapus;
            public ViewHolder(View itemView) {
                super(itemView);
                tanggalovk = itemView.findViewById(R.id.tglovk);
                jenisovk = itemView.findViewById(R.id.jenisovk);
                jumlahayam = itemView.findViewById(R.id.jumlahayam);
                nextovk = itemView.findViewById(R.id.nextovk);
                biayaovk = itemView.findViewById(R.id.biayaovk);
                totalbiaya = itemView.findViewById(R.id.totalbiaya);
                edit = itemView.findViewById(R.id.editdata);
                hapus = itemView.findViewById(R.id.hapusdata);
            }
        }
    }

}