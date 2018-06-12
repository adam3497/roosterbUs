package org.roosterbus;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.roosterbus.utils.CardCourse;
import org.roosterbus.utils.CourseAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                addNewItem();
            }
        });

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_check);
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddCoursesActivity.this, MainActivity.class));
                finish();
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

    @SuppressLint("InflateParams")
    private void addNewItem() {
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View view = inflater.inflate(R.layout.add_new_item, null);
        Spinner spinnerStart = (Spinner) view.findViewById(R.id.spiner_start);
        Spinner spinnerEnd = (Spinner) view.findViewById(R.id.spiner_end);
        MultiSelectionSpinner spinnerDays = (MultiSelectionSpinner) view.findViewById(R.id.mulspinner_days);

        String[] hours = {"7:00", "8:00", "9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, hours);
        spinnerStart.setAdapter(arrayAdapter);
        spinnerEnd.setAdapter(arrayAdapter);

        String[] days = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        List<String> daysArray = new ArrayList<String>(Arrays.asList(days));
        spinnerDays.setItems(daysArray);

        new AlertDialog.Builder(this)
                .setTitle("Agregar curso")
                .setView(view)
                .setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AddCoursesActivity.this, "Curso agregado", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .setCancelable(true)
                .create().show();
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
