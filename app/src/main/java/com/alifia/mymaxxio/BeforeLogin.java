package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class BeforeLogin extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_before_login);

        // ngeset tampilan fragment (default) jadi HomeFragment
        // yang mana di dalam HomeFragment itu layoutnya yang fragment_home.xml
        loadFragment(new HomeBeforeLoginFragment());

        // naro bottom navigation view untuk navbar
        BottomNavigationView navigationView = findViewById(R.id.navbar);
        // pakein listener untuk penanganan gimana kalo salah satu item diklik
        navigationView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        int itemId = item.getItemId();

        if (itemId == R.id.fr_home) {
            fragment = new HomeBeforeLoginFragment();
        } else if (itemId == R.id.fr_notif) {
            //fragment = new NotifFragment();
            showConfirmationDialog();
        } else if (itemId == R.id.fr_profile) {
            //fragment = new ProfileFragment();
            showConfirmationDialog();
        }
        return loadFragment(fragment);
    }

    private void showConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Anda harus login terlebih dahulu!")
                .setPositiveButton("Login", (dialog, which) -> {
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);
                })
                .setNeutralButton("Batal", null)
                .show();
    }

    private boolean loadFragment(Fragment fragment) {
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}