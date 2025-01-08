package com.alifia.mymaxxio;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class DetailUangKas extends AppCompatActivity {

    private MaterialToolbar btnKembali;
    private ImageView fotoBuktiTransfer;
    private TextView alasan;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_uang_kas);

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fotoBuktiTransfer = findViewById(R.id.foto_bukti_tf);
        alasan = findViewById(R.id.alasan);

        db = FirebaseFirestore.getInstance();

        String idKas = getIntent().getStringExtra("idKas");
        if (idKas != null) {
            //Log.d("namaMember nyaaa : ", namaMember);
            setupFirestore(idKas);
        }
    }

    private void setupFirestore(String idKas) {
        db.collection("kas_saya")
                .whereEqualTo("id", idKas)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if (!documents.isEmpty()) {
                            DocumentSnapshot document = documents.get(0);
                            alasan.setText(document.getString("alasan"));
                        } else {
                            Toast.makeText(this, "Data not found for: " + idKas, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}