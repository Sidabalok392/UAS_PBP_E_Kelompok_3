package com.example.easy_learning.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.example.easy_learning.AddEditLiveActivity;
import com.example.easy_learning.MainActivity;
import com.example.easy_learning.R;
import com.example.easy_learning.model.Live;

import java.util.ArrayList;
import java.util.List;

public class LiveAdapter extends RecyclerView.Adapter<LiveAdapter.ViewHolder> implements Filterable {
    private List<Live> liveList;
    private List<Live> filteredLiveList;
    private Context context;

    public LiveAdapter(List<Live> liveList, Context context) {
        this.liveList = liveList;
        this.context = context;
        filteredLiveList = new ArrayList<>(liveList);
    }

    @NonNull
    @Override
    public LiveAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.live_item, parent, false);
        return new LiveAdapter.ViewHolder(view);
    }

    //menampilkan perlist data
    @Override
    public void onBindViewHolder(@NonNull LiveAdapter.ViewHolder holder, int position) {
        Live live = filteredLiveList.get(position);

        Glide.with(context).load(live.getUrl()).into(holder.iv_gambar_live);

        holder.tv_nama_live.setText(live.getNama_modul());
        holder.tv_sesi_live.setText(live.getSesi());
        holder.tv_tanggal_live.setText(live.getTanggal());
        holder.btn_edit_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddEditLiveActivity.class);
                intent.putExtra("id", live.getId());
                intent.putExtra("nama",live.getNama_modul());
                intent.putExtra("sesi",live.getSesi());
                intent.putExtra("tanggal",live.getTanggal());
                intent.putExtra("url",live.getUrl());
                context.startActivity(intent);
            }
        });
        holder.cv_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddEditLiveActivity.class);
                intent.putExtra("id", live.getId());
                intent.putExtra("nama",live.getNama_modul());
                intent.putExtra("sesi",live.getSesi());
                intent.putExtra("tanggal",live.getTanggal());
                intent.putExtra("url",live.getUrl());
                context.startActivity(intent);
            }
        });

        holder.btn_delete_live.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);

                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus data live ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (context instanceof MainActivity)
                                    ((MainActivity) context).deleteLive(live.getId());
                            }
                        }).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredLiveList.size();
    }

    public void setLiveList(List<Live> liveList) {
        this.liveList = liveList;
        filteredLiveList = new ArrayList<>(liveList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Live> filtered = new ArrayList<>();
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(liveList);
                } else {
                    for (Live live : liveList) {
                        if (live.getNama_modul().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filtered.add(live);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredLiveList.clear();
                filteredLiveList.addAll((List<Live>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
    //konstruktor
    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_live;
        private ImageView iv_gambar_live;
        private TextView tv_nama_live;
        private TextView tv_sesi_live;
        private TextView tv_tanggal_live;
        private ImageButton btn_delete_live;
        private ImageButton btn_edit_live;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cv_live = itemView.findViewById(R.id.cv_live);
            iv_gambar_live = itemView.findViewById(R.id.iv_gambar_live_item);
            tv_nama_live = itemView.findViewById(R.id.tv_nama_live);
            tv_sesi_live = itemView.findViewById(R.id.tv_sesi_live);
            tv_tanggal_live = itemView.findViewById(R.id.tv_tanggal_live);
            btn_delete_live = itemView.findViewById(R.id.btn_delete_live);
            btn_edit_live = itemView.findViewById(R.id.btn_edit_live);
        }
    }
}