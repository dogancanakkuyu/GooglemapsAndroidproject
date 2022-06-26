package com.example.project;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity<onMapReady> extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    Database myDb;
    public static GoogleMap mMap;
    boolean add_button_clicked=false;
    boolean clicked = false;
    boolean clicked_2=false;
    Context context = this;
    FloatingActionButton fab;
    ArrayList<Info> arrayList = new ArrayList<>();
    ArrayList<String> latlist=new ArrayList<>();
    ArrayList<String> lonlist=new ArrayList<>();
    Cursor c;
    String name_1, address_1, address_type_1, id,latitude_1,longitude_1;
    public static DrawerLayout drawer;
    CustomAdapter customAdapter;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        customAdapter = new CustomAdapter(arrayList, context);
        recyclerView.setAdapter(customAdapter);
        myDb = new Database(this);
        getAllData();
        LayoutInflater layoutInflater1=LayoutInflater.from(this);
        View mView=layoutInflater1.inflate(R.layout.dialog_2,null);
        final EditText edit_name=mView.findViewById(R.id.edit_name);
        final EditText edit_address=mView.findViewById(R.id.edit_address);
        final Button edit_save=mView.findViewById(R.id.edit_save);
        Button edit_cancel=mView.findViewById(R.id.edit_cancel);
        Spinner edit_spinner=mView.findViewById(R.id.edit_spinner);
        ArrayAdapter adapter = (ArrayAdapter) ArrayAdapter.createFromResource(this, R.array.Types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        edit_spinner.setAdapter(adapter);



    }

    private void getAllData() {
        c = myDb.getAllData();
        arrayList.clear();
        if (c.getCount() > 0) {
            if (c.moveToFirst()) {
                do {
                    id = c.getString(0);
                    name_1 = c.getString(1);
                    address_1 = c.getString(2);
                    address_type_1 = c.getString(3);
                    latitude_1=c.getString(4);
                    longitude_1=c.getString(5);
                    Info info = new Info(id, name_1, address_1, address_type_1,latitude_1,longitude_1);
                    arrayList.add(info);

                } while (c.moveToNext());
            }
        } else {

        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        add_button_clicked=false;
        fab = findViewById(R.id.fab);
        mMap = googleMap;
        LatLng tc=new LatLng(39.901497,32.775502);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tc, 6));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_button_clicked=true;
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        if(add_button_clicked){
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(latLng);
                            markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                            mMap.clear();
                            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            mMap.addMarker(markerOptions);
                            fab.setImageResource(R.drawable.baseline_done_white_18);
                            clicked = true;
                            double temp_double_lat=latLng.latitude;
                            double temp_double_long=latLng.longitude;

                            latlist.add(Double.toString(temp_double_lat));
                            lonlist.add(Double.toString(temp_double_long));
                        }


                    }

                });
                if (clicked) {
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                    View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                    final EditText name_2 = mView.findViewById(R.id.editText3);
                    final EditText address_2 = mView.findViewById(R.id.editText7);
                    final Spinner address_type_2 = mView.findViewById(R.id.spinner);
                    ArrayAdapter adapter = (ArrayAdapter) ArrayAdapter.createFromResource(MainActivity.this, R.array.Types, android.R.layout.simple_spinner_item);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    address_type_2.setAdapter(adapter);
                    Button save = mView.findViewById(R.id.button2);
                    Button cancel = mView.findViewById(R.id.button3);
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();
                    address_type_2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            String[] address_types = getResources().getStringArray(R.array.Types);
                            address_type_1 = address_types[i];
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                    save.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            boolean isInserted = myDb.insert_data(name_2.getText().toString(), address_2.getText().toString(), address_type_1,latlist.get(latlist.size()-1),lonlist.get(lonlist.size()-1));
                            getAllData();
                            customAdapter.swap(arrayList);
                            clicked=false;
                            add_button_clicked=false;
                            fab.setImageResource(R.drawable.baseline_add_white_18);
                            mMap.clear();
                            if (isInserted) {
                                Toast.makeText(MainActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(MainActivity.this, "Data Not Inserted", Toast.LENGTH_LONG).show();
                            }
                            dialog.dismiss();
                        }
                    });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            clicked=false;
                            add_button_clicked=false;
                            fab.setImageResource(R.drawable.baseline_add_white_18);
                            mMap.clear();
                            dialog.dismiss();
                        }
                    });


                }

            }

        });

    }
}
