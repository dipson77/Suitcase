package com.example.suitcase;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class Manage_Items extends AppCompatActivity {
    ImageView icon_hamburger;
    CardView card_clothing, card_bookings, card_healthcare, card_documents, card_devices;

    DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_items);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        icon_hamburger=findViewById(R.id.hamburger_icon);
        drawerLayout = findViewById(R.id.drawer_layout);
        card_clothing=findViewById(R.id.clothing);
        card_bookings=findViewById(R.id.booking);
        card_documents=findViewById(R.id.documents);
        card_devices = findViewById(R.id.devices);
        card_healthcare=findViewById(R.id.healthcare);

        icon_hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        card_clothing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Manage_Items.this,Add_Items.class);
                startActivity(intent);

            }
        });

        card_bookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookintent = new Intent(Manage_Items.this, BookingList.class);
                startActivity(bookintent);

            }
        });
        card_devices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Manage_Items", "Navigating to Gadget Activity");
                Intent deviceintent = new Intent(Manage_Items.this, GadgetList.class);
                startActivity(deviceintent);

            }
        });

        card_documents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Manage_Items", "Navigating to Documents Activity");
                Intent documentintent = new Intent(Manage_Items.this, Document_List.class);
                startActivity(documentintent);

            }
        });
        card_healthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Manage_Items", "Navigating to health Activity");
                Intent healthintent = new Intent(Manage_Items.this, HealthcareList.class);
                startActivity(healthintent);

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                Intent intent = null;

                if(id == R.id.nav_home){
                    intent = new Intent(Manage_Items.this, Manage_Items.class);
                } else if (id == R.id.nav_about) {
                    intent = new Intent(Manage_Items.this, Manage_Items.class);

                } else if (id == R.id.nav_items) {
                    intent = new Intent(Manage_Items.this, ItemsList.class);

                } else if (id == R.id.nav_logout) {
                    intent = new Intent(Manage_Items.this, MainActivity.class);

                }


                if(intent !=null){
                    startActivity(intent);
                    finish();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

}