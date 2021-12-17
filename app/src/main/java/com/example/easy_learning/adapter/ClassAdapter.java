package com.example.easy_learning.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easy_learning.AddClass;
import com.example.easy_learning.R;

import java.util.List;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder>{
    private List<AddClass> classList;
    private Context context;

    public ClassAdapter(List<AddClass> classList, Context context) {
        this.classList = classList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.class_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AddClass addClass = classList.get(position);

        Glide.with(context).load(addClass.getUrl_class()).into(holder.iv_gambar_class);
        holder.tv_title_class.setText(addClass.getNama_class());
        holder.tv_kode_class.setText(addClass.getKode_class());
        holder.tv_desc_class.setText(addClass.getDesc_class());

    }
//konstruktor
    @Override
    public int getItemCount() {
        return classList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_class;
        private ImageView iv_gambar_class;
        private TextView tv_title_class;
        private TextView tv_kode_class;
        private TextView tv_desc_class;
        private ImageButton btn_delete_class;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            iv_gambar_class = itemView.findViewById(R.id.foto_class);
            tv_title_class = itemView.findViewById(R.id.tv_title_class);
            tv_kode_class = itemView.findViewById(R.id.tv_kode_class);
            tv_desc_class = itemView.findViewById(R.id.tv_desc_class);
        }
    }
}
