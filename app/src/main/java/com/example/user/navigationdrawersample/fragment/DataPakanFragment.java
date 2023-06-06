package com.example.user.navigationdrawersample.fragment;

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

import com.example.user.navigationdrawersample.AddDataPakanActivity;
import com.example.user.navigationdrawersample.Auth.ApiServices;
import com.example.user.navigationdrawersample.EditDataPakanActivity;
import com.example.user.navigationdrawersample.Model.DataPakan;
import com.example.user.navigationdrawersample.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataPakanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataPakanFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DataPakanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataPakanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataPakanFragment newInstance(String param1, String param2) {
        DataPakanFragment fragment = new DataPakanFragment();
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
    List<DataPakan> data = new ArrayList<>();
    Button add;
    CustomAdapterDataPakan adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_data_pakan, container, false);
        rv_data = view.findViewById(R.id.rv_datapakan);
        add = view.findViewById(R.id.add);
        swipeRefreshLayout = view.findViewById(R.id.refresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_data.setLayoutManager(layoutManager);
        adapter = new CustomAdapterDataPakan( data, getContext());
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
                Intent i = new Intent(getContext(), AddDataPakanActivity.class);
                startActivity(i);
            }
        });
        loadData();
        return view;
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(true);
        ApiServices.readDataPakan(getContext(), new ApiServices.DataPakanResponseListener() {
            @Override
            public void onSuccess(List<DataPakan> dataPakanList) {
                adapter = new CustomAdapterDataPakan( dataPakanList, getContext());
                rv_data.setAdapter(adapter);
                data = dataPakanList;
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

    public static class CustomAdapterDataPakan extends RecyclerView.Adapter<CustomAdapterDataPakan.ViewHolder> {
        private List<DataPakan> dataPakanList;
        private Context context;
        private LayoutInflater layoutInflater;

        public CustomAdapterDataPakan(List<DataPakan> dataPakanList, Context context) {
            this.dataPakanList = dataPakanList;
            this.context = context;
            this.layoutInflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public CustomAdapterDataPakan.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = layoutInflater.inflate(R.layout.item_list_pakan, parent, false);
            return new CustomAdapterDataPakan.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CustomAdapterDataPakan.ViewHolder holder, int position) {
            holder.pembelian.setText(dataPakanList.get(position).getPembelian());
            holder.jenis.setText(dataPakanList.get(position).getJenisPakan());
            holder.harga.setText(dataPakanList.get(position).getHarga());
            holder.stok.setText(dataPakanList.get(position).getStok());
            holder.total.setText(dataPakanList.get(position).getTotal());
            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, EditDataPakanActivity.class);
                        intent.putExtra("datapakan", dataPakanList.get(pos));
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
                                ApiServices.deleteDataPakan(context, dataPakanList.get(pos).getId(), new ApiServices.DeleteDataPakanResponseListener() {
                                    @Override
                                    public void onSuccess(String response) {
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
            return dataPakanList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView pembelian, jenis, stok, harga, total;
            FrameLayout edit, hapus;
            public ViewHolder(View itemView) {
                super(itemView);
                pembelian = itemView.findViewById(R.id.pembelian);
                jenis = itemView.findViewById(R.id.jenispakan);
                stok = itemView.findViewById(R.id.stok);
                harga = itemView.findViewById(R.id.harga);
                total = itemView.findViewById(R.id.totalayam);
                edit = itemView.findViewById(R.id.editdata);
                hapus = itemView.findViewById(R.id.hapusdata);
            }
        }
    }

}