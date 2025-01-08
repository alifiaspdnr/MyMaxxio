package com.alifia.mymaxxio;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class GantiPassword extends AppCompatActivity {

    private EditText etPasswordSaatini, etPasswordBaru, etPasswordUlangi;
    private Button btnGanti;
    private MaterialToolbar btnKembali;
    private ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ganti_password);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etPasswordBaru = findViewById(R.id.password_baru);
        etPasswordSaatini = findViewById(R.id.password_saatini);
        etPasswordUlangi = findViewById(R.id.password_ulangi);
        progressBar = findViewById(R.id.forgotPassProgressBar);

        btnGanti = findViewById(R.id.button_ganti);
        btnGanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gantiPassword();
            }
        });
    }

    private void gantiPassword() {
        String passwordSaatIni = etPasswordSaatini.getText().toString();
        String passwordBaru = etPasswordBaru.getText().toString();
        String passwordUlangi = etPasswordUlangi.getText().toString();

        if (TextUtils.isEmpty(passwordSaatIni)) {
            etPasswordSaatini.setError("Password saat ini harus diisi");
            return;
        }

        if (TextUtils.isEmpty(passwordBaru) || passwordBaru.length() < 6) {
            etPasswordBaru.setError("Password baru harus minimal 6 karakter");
            return;
        }

        if (!passwordBaru.equals(passwordUlangi)) {
            etPasswordUlangi.setError("Password tidak cocok");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Re-autentikasi user
        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), passwordSaatIni);
        user.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Mengganti password
                user.updatePassword(passwordBaru).addOnCompleteListener(updateTask -> {
                    progressBar.setVisibility(View.GONE);
                    if (updateTask.isSuccessful()) {
                        Toast.makeText(GantiPassword.this, "Password berhasil diganti", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(GantiPassword.this, "Gagal mengganti password", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(GantiPassword.this, "Password saat ini salah", Toast.LENGTH_SHORT).show();
            }
        });
    }
}