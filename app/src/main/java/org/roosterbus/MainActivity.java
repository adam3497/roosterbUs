package org.roosterbus;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;
import org.roosterbus.utils.OSMUpdateLocation;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<OverlayItem> puntos = new ArrayList<>();
    private MapView myOpenMapView;
    private MapController myMapController;
    private GeoPoint posicionActual;
    private GeoPoint posicionBus;
    private GeoPoint posicionParada;
    public static final int NOTIFICACION_ID = 122;
    public static final int NOTIFICACION_ID2 = 12;
    public static final int NOTIFICACION_ID3 = 1;
    private NotificationManager notificationManager;

    private Boolean isSignInFace;
    private Boolean isSignInGoogle;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            isSignInFace = extras.getBoolean("accountSignedFacebook", false);
            isSignInGoogle = extras.getBoolean("accountSignedGoogle", false);
        }else{
            Toast.makeText(this, "Se produjo un error en la carga de la aplicación", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (tengoPermisoEscritura()) {
            cargarMapas();
        }
        else {
        }
    }

    private void cargarMapas() {
        GeoPoint sede = new GeoPoint(10.01951,-84.19719);

        myOpenMapView = findViewById(R.id.openmapview);
        myOpenMapView.setBuiltInZoomControls(true);
        myMapController = (MapController) myOpenMapView.getController();
        myMapController.setCenter(sede);
        myMapController.setZoom(20);

        myOpenMapView.setMultiTouchControls(true);

        //Se centra la ubicación
        final MyLocationNewOverlay myLocationoverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(getApplicationContext()), myOpenMapView);
        myOpenMapView.getOverlays().add(myLocationoverlay);
        myLocationoverlay.enableMyLocation();
        myLocationoverlay.runOnFirstFix(new Runnable() {
            public void run() {
                myMapController.animateTo(myLocationoverlay.getMyLocation());
            }
        });


        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        OSMUpdateLocation detectaPosicion = new OSMUpdateLocation(this);
        if (tengoPermisoUbicacion()) {
            Location ultimaPosicionConocida = null;
            for (String provider : locationManager.getProviders(true)) {
                if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    ultimaPosicionConocida = locationManager.getLastKnownLocation(provider);
                if (ultimaPosicionConocida != null) {
                    actualizaPosicionActual(ultimaPosicionConocida);
                }
                locationManager.requestLocationUpdates(provider, 0, 0, detectaPosicion);
                break;
            }
        } else {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setClass(this, this.getClass());
            startActivity(intent);
            finish();
        } else {
        }
    }

    public boolean tengoPermisoEscritura() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            return true;
        }
    }

    public boolean tengoPermisoUbicacion() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                return false;
            }
        } else {
            return true;
        }
    }

    public void actualizaPosicionActual(Location location) {
        posicionActual = new GeoPoint(location.getLatitude(), location.getLongitude());
        myMapController.setCenter(posicionActual);
        if (puntos.size() > 1)
            puntos.remove(1);

        OverlayItem marcador = new OverlayItem("Estás aquí", "Posición actual", posicionActual);
        marcador.setMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pos_24dp, null));
        puntos.add(marcador);
        refrescaPuntos();
    }

    private void refrescaPuntos() {
        myOpenMapView.getOverlays().clear();

        posicionBus = new GeoPoint(10.0186011,-84.1971541);
        OverlayItem marcadorBus = new OverlayItem("Bus", "Ubicación del bus", posicionBus);
        marcadorBus.setMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bus, null));
        puntos.add(marcadorBus);

        posicionParada = new GeoPoint(10.0188136,-84.1943173);
        OverlayItem marcadorParada = new OverlayItem("Bus", "Ubicación de la parada", posicionParada);
        marcadorParada.setMarker(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_bus_stop, null));
        puntos.add(marcadorParada);

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> tap = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            @Override
            public boolean onItemLongPress(int arg0, OverlayItem arg1) {
                return false;
            }

            @Override
            public boolean onItemSingleTapUp(int index, OverlayItem item) {
                return true;
            }
        };

        ItemizedOverlayWithFocus<OverlayItem> capa = new ItemizedOverlayWithFocus<>(this, puntos, tap);
        capa.setFocusItemsOnTap(true);
        myOpenMapView.getOverlays().add(capa);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_atraso) {
            notificacionAtraso();
        }else if(id == R.id.action_precio){
            notificacionPrecio();
        } else if(id == R.id.action_salir){
            notificacionBusCerca();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account_settings) {
            Intent accountIntent = new Intent(MainActivity.this, AccountActivity.class);
            startActivity(accountIntent);
        } else if (id == R.id.nav_schedule) {
            Intent scheduleIntent = new Intent(MainActivity.this, ScheduleActivity.class);
            startActivity(scheduleIntent);
        } else if (id == R.id.nav_logout) {
            logout();
        } else if (id == R.id.nav_rate) {
            Intent rateIntent = new Intent(MainActivity.this, RateActivity.class);
            startActivity(rateIntent);
        } else if (id == R.id.nav_routes) {
            Intent routesIntent = new Intent(MainActivity.this, BusRoutesActivity.class);
            startActivity(routesIntent);
    }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }

    private void logout() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        final Intent loginRequestIntent = new Intent(MainActivity.this, LoginActivity.class);

        if(isSignInFace){
            progressDialog.setMessage("Cerrando sesión de Facebook");
            progressDialog.show();

            //obtain the current token, if true the user is logging
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

            if(isLoggedIn){
                //the user is logging, so we need to logout this session
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions", null, HttpMethod.DELETE, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                        LoginManager.getInstance().logOut();
                        progressDialog.dismiss();
                        startActivity(loginRequestIntent);
                        finish();
                    }
                }).executeAsync();
            }
            else{
                //the user is currently logout
                progressDialog.dismiss();
                startActivity(loginRequestIntent);
                finish();
            }
        }
        else if(isSignInGoogle){
            progressDialog.setMessage("Cerrando sesión de google");
            progressDialog.show();
            //make the request to logout from Google Account
            mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    //when the request is complete, finish this activity and start the Login Activity
                    progressDialog.dismiss();
                    startActivity(loginRequestIntent);
                    finish();
                }
            });
        }
        else{
            progressDialog.setMessage("Cerrando sesión de la cuenta de aplicación");
            progressDialog.show();
            startActivity(loginRequestIntent);
            progressDialog.dismiss();
            finish();
        }
    }

    public void notificacionAtraso(){
        //Patrón de vibración
        long vibrate[] = {0,100,100};

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("¡Atraso en ruta!")
                .setAutoCancel(true)
                .setContentText("Ha sucedido un atraso en la ruta Alajuela - Santa Bárbara")
                .setVibrate(vibrate);

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICACION_ID, builder.build());
    }
    public void notificacionPrecio(){
        //Patrón de vibración
        long vibrate[] = {0,100,100};

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("¡Cambio en el precio!")
                .setAutoCancel(true)
                .setContentText("El precio en la ruta Alajuela - Santa Bárbara ha cambiado")
                .setVibrate(vibrate);

        Intent intent = new Intent(MainActivity.this, RateActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICACION_ID2, builder.build());
    }
    public void notificacionBusCerca(){
        //Patrón de vibración
        long vibrate[] = {0,100,100};

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("¡El bus está cerca!")
                .setAutoCancel(true)
                .setContentText("¡Debes salir a tomar en bus!")
                .setVibrate(vibrate);

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(NOTIFICACION_ID3, builder.build());
    }
}
