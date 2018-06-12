package org.roosterbus.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.roosterbus.R;

import java.util.ArrayList;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CardCourse> courseArray;

    public CourseAdapter(Context context, ArrayList<CardCourse> courseArray){
        this.context = context;
        this.courseArray = courseArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(courseArray.get(position).getTitle());
        holder.txtInitialName.setText(Character.toString(courseArray.get(position).getTitle().charAt(0)));
        holder.txtSchedule.setText(courseArray.get(position).getSchedule());
        holder.txtTeacher.setText(courseArray.get(position).getTeacher());
    }

    @Override
    public int getItemCount() {
        if(courseArray.isEmpty()){
            return 0;
        }
        else {
            return courseArray.size();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtInitialName, txtTitle, txtSchedule, txtTeacher;

        public ViewHolder(View view) {
            super(view);
            txtInitialName = (TextView) view.findViewById(R.id.txt_initial_name_course);
            txtTitle = (TextView) view.findViewById(R.id.txt_course_name);
            txtSchedule = (TextView) view.findViewById(R.id.txt_schedule);
            txtTeacher = (TextView) view.findViewById(R.id.txt_teacher_name);
        }
    }
}
