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
//library glide
import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.example.easy_learning.AddEditCourseActivity;
import com.example.easy_learning.MainAdminActivity;
import com.example.easy_learning.R;
import com.example.easy_learning.model.Course;

import java.util.ArrayList;
import java.util.List;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> implements Filterable {

    private List<Course> courseList;
    private List<Course> filteredCourseList;
    private Context context;

    //konstruktor spy  lain
    public AdminAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
        filteredCourseList = new ArrayList<>(courseList);
    }

    // seperti ugd
    @NonNull
    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.admin_item, parent, false);
        return new AdminAdapter.ViewHolder(view);
    }

    //menampilkan list
    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.ViewHolder holder, int position) {
        Course course = filteredCourseList.get(position);

        Glide.with(context).load(course.getUrl()).into(holder.iv_gambar_course);
        holder.tv_nama_course.setText(course.getNama_modul());
        holder.tv_kode_course.setText(course.getKode());
        holder.tv_desc_course.setText(course.getDesc());

        holder.btn_edit_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddEditCourseActivity.class);
                intent.putExtra("id", course.getId());
                intent.putExtra("nama",course.getNama_modul());
                intent.putExtra("kode",course.getKode());
                intent.putExtra("desc",course.getDesc());
                intent.putExtra("url",course.getUrl());
                context.startActivity(intent);
            }
        });
        holder.cv_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddEditCourseActivity.class);
                intent.putExtra("id", course.getId());
                intent.putExtra("nama",course.getNama_modul());
                intent.putExtra("kode",course.getKode());
                intent.putExtra("desc",course.getDesc());
                intent.putExtra("url",course.getUrl());
                context.startActivity(intent);
            }
        });
        holder.btn_delete_course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);

                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus data course ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                if (context instanceof MainAdminActivity)
                                    ((MainAdminActivity) context).deleteCourse(course.getId());
                            }
                        }).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return filteredCourseList.size();
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        filteredCourseList = new ArrayList<>(courseList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Course> filtered = new ArrayList<>();
                if (charSequenceString.isEmpty()) {
                    filtered.addAll(courseList);
                } else {
                    for (Course course : courseList) {
                        if (course.getNama_modul().toLowerCase().contains(charSequenceString.toLowerCase())) {
                            filtered.add(course);
                        }
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredCourseList.clear();
                filteredCourseList.addAll((List<Course>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }
//konstruktor
    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cv_course;
        private ImageView iv_gambar_course;
        private TextView tv_nama_course;
        private TextView tv_kode_course;
        private TextView tv_desc_course;
        private ImageButton btn_delete_course;
        private ImageButton btn_edit_course;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cv_course = itemView.findViewById(R.id.cv_admin);
            iv_gambar_course = itemView.findViewById(R.id.iv_gambar_admin);
            tv_nama_course = itemView.findViewById(R.id.tv_nama_admin);
            tv_kode_course = itemView.findViewById(R.id.tv_kode_admin);
            tv_desc_course = itemView.findViewById(R.id.tv_desc_admin);
            btn_delete_course = itemView.findViewById(R.id.btn_delete_admin);
            btn_edit_course = itemView.findViewById(R.id.btn_edit_admin);
        }
    }
}