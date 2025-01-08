package com.alifia.mymaxxio;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ForgotPassword extends AppCompatActivity {

    private EditText etEmail;
    private Button btnReset;
    private TextView tvLogin, tvRegis;
    private FirebaseAuth mAuth;
    String strEmail, strEmailUser;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.forgotPassProgressBar);

        etEmail = findViewById(R.id.email);

        btnReset = findViewById(R.id.button_reset);
        btnReset.setOnClickListener(v -> {
            strEmail = etEmail.getText().toString().trim();
            strEmailUser = etEmail.getText().toString();

            // validasi jika email tidak kosong (terisi)
            if (!TextUtils.isEmpty(strEmail)){
                // validasi alamat email menggunakan domain gmail
                if (!isGmailAddress(etEmail.getText().toString())) {
                    Toast.makeText(this, "Alamat email harus Gmail dan ditulis dalam huruf kecil", Toast.LENGTH_SHORT).show();
                    return;
                }
                resetPassword();
            } else {
                etEmail.setError("Email tidak boleh kosong!");
            }
        });

        tvLogin = findViewById(R.id.masuk);
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPassword.this, Login.class);
            startActivity(intent);
        });

        tvRegis = findViewById(R.id.belum);
        tvRegis.setOnClickListener(v -> {
            Intent intent = new Intent(ForgotPassword.this, Register.class);
            startActivity(intent);
        });
    }

    private boolean isGmailAddress(String email) {
        String gmailRegex = "^[a-zA-Z0-9_]+@gmail\\.com$";
        return email.matches(gmailRegex);
    }

    private void resetPassword() {
        progressBar.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.INVISIBLE);
        tvLogin.setVisibility(View.INVISIBLE);
        tvRegis.setVisibility(View.INVISIBLE);

        // untuk mengecek apakah email inputan user terdaftar pada aplikasi
        CollectionReference usersCollectionRef = FirebaseFirestore.getInstance().collection("users");
        usersCollectionRef.whereEqualTo("emailUser", strEmailUser)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot!=null && !querySnapshot.isEmpty()) {
                                // email terdaftar, lanjut reset password
                                mAuth.sendPasswordResetEmail(strEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ForgotPassword.this, "Link reset password telah terkirim ke email Anda", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ForgotPassword.this, Login.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(ForgotPassword.this, "Error :- " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.INVISIBLE);
                                                btnReset.setVisibility(View.VISIBLE);
                                                tvLogin.setVisibility(View.VISIBLE);
                                                tvRegis.setVisibility(View.VISIBLE);
                                            }
                                        });
                            } else {
                                // email tidak terdaftar
                                Toast.makeText(ForgotPassword.this, "Email tidak terdaftar pada aplikasi", Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.INVISIBLE);
                                btnReset.setVisibility(View.VISIBLE);
                                tvLogin.setVisibility(View.VISIBLE);
                                tvRegis.setVisibility(View.VISIBLE);
                            }
                        } else {
                            // gagal memeriksa email di firebase
                            Toast.makeText(ForgotPassword.this, "Gagal memeriksa email", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            btnReset.setVisibility(View.VISIBLE);
                            tvLogin.setVisibility(View.VISIBLE);
                            tvRegis.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }
}