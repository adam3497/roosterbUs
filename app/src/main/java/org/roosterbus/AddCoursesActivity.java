package org.roosterbus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.roosterbus.utils.CardCourse;
import org.roosterbus.utils.CourseAdapter;

import java.util.ArrayList;

public class AddCoursesActivity extends AppCompatActivity {

    private ArrayList<CardCourse> courseArray;
    private CourseAdapter adapter;
    private RecyclerView recyclerView;

    private String[] titles = {"Cálculo", "Matemática General", "Compiladores", "Requerimientos de Software"};
    private String[] schedules = {"M 13:00 - 15:00, V 13:00 - 15:00", "K 13:00 - 15:00, J 13:00 - 15:00",
            "K 10:00 - 12:00, J 13:00 - 15:00", "M 7:00 - 9:00, J 7:00 - 9:00"};
    private String[] teachers = {"Jorge Prendas", "Randall Azofeifa", "Jaime Gutiérrez", "Ándres Víquez"};
    private int[] colors = {Color.BLUE, Color.CYAN, Color.GREEN, Color.YELLOW};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_courses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_course);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here is the intent to add a new course
            }
        });

        courseArray = new ArrayList<>();

        for(int i = 0; i < titles.length; i++){
            CardCourse course = new CardCourse(titles[i], schedules[i], teachers[i], colors[i]);
            courseArray.add(course);
        }

        adapter = new CourseAdapter(getApplicationContext(), courseArray);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_courses);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddCoursesActivity.this, RegisterActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
