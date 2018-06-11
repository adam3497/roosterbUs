package org.roosterbus;

import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;


public class AccountActivity extends AppCompatActivity {
    private EditText extNombreUsuario;
    private EditText extCorreoUsuario;
    private ImageButton btnEditarNombre;
    private ImageButton btnEditarCorreo;
    private Button btnGuardar;
    private Spinner spinnerModalidad;
    ArrayAdapter<CharSequence> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setViews();

    }
    private void setViews(){
        extNombreUsuario = (EditText) findViewById(R.id.extNombreUsuario);
        btnEditarCorreo = (ImageButton) findViewById(R.id.btnCorreo);
        btnEditarNombre = (ImageButton) findViewById(R.id.btnNombre);
        extCorreoUsuario = (EditText) findViewById(R.id.extCorreoUsuario);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        spinnerModalidad = (Spinner) findViewById(R.id.spinner);

        adapter = ArrayAdapter.createFromResource(this, R.array.modalidades, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerModalidad.setAdapter(adapter);



        btnEditarNombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extNombreUsuario.setEnabled(true);
            }
        });
        btnEditarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extCorreoUsuario.setEnabled(true);
            }
        });
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                extCorreoUsuario.setEnabled(false);
                extNombreUsuario.setEnabled(false);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
