package com.example.easy_learning.adapter;

import static com.example.easy_learning.MyApplication.CHANNEL_1_ID;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.easy_learning.AddEditCourseActivity;
import com.example.easy_learning.R;
import com.example.easy_learning.model.Course;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> implements Filterable {
    private List<Course> courseList;
    private List<Course> filteredCourseList;
    private Context context;
    private NotificationManagerCompat notificationManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public CourseAdapter(List<Course> courseList, Context context) {
        this.courseList = courseList;
        this.context = context;
        filteredCourseList = new ArrayList<>(courseList);

        notificationManager = NotificationManagerCompat.from(context);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        //
        databaseReference = FirebaseDatabase.getInstance()
                .getReference("class/"+firebaseUser.getEmail()
                        .toString().replace('.','&'));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.course_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Course course = filteredCourseList.get(position);

        Glide.with(context).load(course.getUrl()).into(holder.iv_gambar_course);
        holder.tv_nama_course.setText(course.getNama_modul());
        holder.tv_kode_course.setText(course.getKode());
        holder.tv_desc_course.setText(course.getDesc());


        holder.cv_course.setOnClickListener(new View.OnClickListener() {
            //menambahkan course ke CLASS masing" individu berdasarkan ID dari db
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Anda ingin menambahkan modul pembelajaran ke kelas belajar?");
                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, AddEditCourseActivity.class);

                        intent.putExtra("id", course.getId());
                        intent.putExtra("nama",course.getNama_modul());
                        intent.putExtra("kode",course.getKode());
                        intent.putExtra("desc",course.getDesc());
                        intent.putExtra("url",course.getUrl());

                        Notification notification = new
                                NotificationCompat.
                                        Builder(context,
                                CHANNEL_1_ID)
                                .setSmallIcon(R.drawable.hi)
                                .setContentTitle("Easy-Learning")
                                .setContentText("Selamat Belajar !")
                                .setPriority(NotificationCompat
                                        .PRIORITY_HIGH)
                                .setCategory(NotificationCompat
                                        .CATEGORY_MESSAGE)
                                .setColor(Color.BLUE)
                                .setAutoCancel(true)
                                .setOnlyAlertOnce(true)
                                .build();
                        notificationManager.notify(1,notification);

                        ArrayList<String> list = new ArrayList<>();
                        list.add(course.getNama_modul());
                        list.add(course.getKode());
                        list.add(course.getDesc());
                        list.add(course.getUrl());
                        //menyimpan kelas" individu ke firebase
                        databaseReference.child(String.valueOf(
                                course.getId())).setValue(list);
                    }
                });
                builder.setNegativeButton("TIDAK",null);
                builder.show();
            }
        });

//        holder.btn_delete_course.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
//
//                materialAlertDialogBuilder.setTitle("Konfirmasi")
//                        .setMessage("Apakah anda yakin ingin menghapus data course ini?")
//                        .setNegativeButton("Batal", null)
//                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener()
//                        {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i)
//                            {
//                                if (context instanceof MainActivity)
//                                    ((MainActivity) context).deleteCourse(course.getId());
//                            }
//                        }).show();
//            }
//        });


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

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            cv_course = itemView.findViewById(R.id.cv_course);
            iv_gambar_course = itemView.findViewById(R.id.iv_gambar_course);
            tv_nama_course = itemView.findViewById(R.id.tv_nama_course);
            tv_kode_course = itemView.findViewById(R.id.tv_kode_course);
            tv_desc_course = itemView.findViewById(R.id.tv_desc_course);
//            btn_delete_course = itemView.findViewById(R.id.btn_delete_course);
        }
    }
}