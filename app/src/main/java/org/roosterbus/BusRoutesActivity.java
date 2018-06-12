package org.roosterbus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.roosterbus.utils.CardRoutes;
import org.roosterbus.utils.RoutesAdapter;

import java.util.ArrayList;

public class BusRoutesActivity extends AppCompatActivity {

    private ArrayList<CardRoutes> routesArray;
    private RoutesAdapter adapter;
    private RecyclerView recyclerView;

    private String[] titles = {"Santa Bárbara - Alajuela", "Poás - Alajuela"};
    private String[] rate = {"₡340", "₡590"};
    private int[] colors = {Color.BLUE, Color.CYAN};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_routes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_route);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here is the intent to add a new course
            }
        });

        routesArray = new ArrayList<>();

        CardRoutes route1 = new CardRoutes(titles[0], rate[0], colors[0]);
        CardRoutes route2 = new CardRoutes(titles[1], rate[1], colors[1]);
        routesArray.add(route1);
        routesArray.add(route2);

        adapter = new RoutesAdapter(getApplicationContext(), routesArray);
        recyclerView = (RecyclerView) findViewById(R.id.rcv_routes);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(BusRoutesActivity.this, MainActivity.class));
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
