package com.alifia.mymaxxio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class UangKas extends AppCompatActivity {

    private RecyclerView listUangKas;
    private UangKasAdapter adapter;
    private UangKasChapterAdapter adapterChapter;
    private Spinner btnTingkatanUangKas;
    private MaterialToolbar btnKembali;
    private FirebaseFirestore db;
    private CollectionReference uangkasRef;
    private FirebaseAuth mAuth;
    private String strNamaLengkap = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_uang_kas);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        uangkasRef = db.collection("kas_saya");

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // untuk menampilkan list uang kas, digunakan recyclerview
        listUangKas = findViewById(R.id.listUangKas);
        listUangKas.setLayoutManager(new LinearLayoutManager(this));

        btnTingkatanUangKas = findViewById(R.id.dropdown_uangkas);
        ArrayAdapter<CharSequence> adapterTingkatanUangKas = ArrayAdapter.createFromResource(
                this,
                R.array.uangkas_array,
                R.layout.custom_spinner_item
        );
        adapterTingkatanUangKas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnTingkatanUangKas.setAdapter(adapterTingkatanUangKas);
        btnTingkatanUangKas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedTingkatan = btnTingkatanUangKas.getSelectedItem().toString();
                setupFirestore(selectedTingkatan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupFirestore(String tingkatan) {
        if (tingkatan.equals("Uang Kas Saya")) {
            // get nama
            String email = mAuth.getCurrentUser().getEmail();
            Log.d("EMAILnya : ", email);

            db.collection("member").whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                strNamaLengkap = document.getString("namaLengkap");

                                uangkasRef.
                                        whereEqualTo("namaMember", strNamaLengkap).
                                        orderBy("bulan_urutan", Query.Direction.DESCENDING).
                                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            List<DocumentSnapshot> uangkasList = task.getResult().getDocuments();
                                            adapter = new UangKasAdapter(new ArrayList<>(), getApplicationContext());
                                            listUangKas.setAdapter(adapter);
                                            adapter.setUangkasList(uangkasList);
                                        }
                                    }
                                });
                            } else {
                                Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else if (tingkatan.equals("Tagihan Saya")) {
            // get nama
            String email = mAuth.getCurrentUser().getEmail();
            Log.d("EMAILnya : ", email);
            db.collection("member").whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                strNamaLengkap = document.getString("namaLengkap");

                                uangkasRef.
                                        whereEqualTo("namaMember", strNamaLengkap).
                                        whereEqualTo("status", "Belum Lunas").
                                        orderBy("bulan_urutan", Query.Direction.DESCENDING).
                                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<DocumentSnapshot> uangkasList = task.getResult().getDocuments();
                                                    adapter = new UangKasAdapter(new ArrayList<>(), getApplicationContext());
                                                    listUangKas.setAdapter(adapter);
                                                    adapter.setUangkasList(uangkasList);
                                                }
                                            }
                                        });
                            } else {
                                Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else if (tingkatan.equals("Dalam Review")) {
            // get nama
            String email = mAuth.getCurrentUser().getEmail();
            Log.d("EMAILnya : ", email);
            db.collection("member").whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                strNamaLengkap = document.getString("namaLengkap");

                                uangkasRef.
                                        whereEqualTo("namaMember", strNamaLengkap).
                                        whereEqualTo("status", "Dalam Review").
                                        orderBy("bulan_urutan", Query.Direction.DESCENDING).
                                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<DocumentSnapshot> uangkasList = task.getResult().getDocuments();
                                                    adapter = new UangKasAdapter(new ArrayList<>(), getApplicationContext());
                                                    listUangKas.setAdapter(adapter);
                                                    adapter.setUangkasList(uangkasList);
                                                }
                                            }
                                        });
                            } else {
                                Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else if (tingkatan.equals("Lunas")) {
            // get nama
            String email = mAuth.getCurrentUser().getEmail();
            Log.d("EMAILnya : ", email);
            db.collection("member").whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                strNamaLengkap = document.getString("namaLengkap");

                                uangkasRef.
                                        whereEqualTo("namaMember", strNamaLengkap).
                                        whereEqualTo("status", "Lunas").
                                        orderBy("bulan_urutan", Query.Direction.DESCENDING).
                                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<DocumentSnapshot> uangkasList = task.getResult().getDocuments();
                                                    adapter = new UangKasAdapter(new ArrayList<>(), getApplicationContext());
                                                    listUangKas.setAdapter(adapter);
                                                    adapter.setUangkasList(uangkasList);
                                                }
                                            }
                                        });
                            } else {
                                Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else if (tingkatan.equals("Ditolak")) {
            // get chapter
            String email = mAuth.getCurrentUser().getEmail();
            Log.d("EMAILnya : ", email);
            db.collection("member").whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                strNamaLengkap = document.getString("namaLengkap");

                                uangkasRef.
                                        whereEqualTo("namaMember", strNamaLengkap).
                                        whereEqualTo("status", "Ditolak").
                                        orderBy("bulan_urutan", Query.Direction.DESCENDING).
                                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<DocumentSnapshot> uangkasList = task.getResult().getDocuments();
                                                    adapter = new UangKasAdapter(new ArrayList<>(), getApplicationContext());
                                                    listUangKas.setAdapter(adapter);
                                                    adapter.setUangkasList(uangkasList);
                                                }
                                            }
                                        });
                            } else {
                                Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        } else { // Uang Kas Chapter
            // get nama
            String email = mAuth.getCurrentUser().getEmail();
            Log.d("EMAILnya : ", email);
            db.collection("member").whereEqualTo("email", email).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                String strChapter = document.getString("namaChapter");

                                CollectionReference kasChapterRef = db.collection("kas_chapter");
                                kasChapterRef.
                                        whereEqualTo("chapter", strChapter).
                                        orderBy("bulan_urutan", Query.Direction.DESCENDING).
                                        get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<DocumentSnapshot> kasChapterList = task.getResult().getDocuments();
                                                    adapterChapter = new UangKasChapterAdapter(new ArrayList<>(), UangKas.this);
                                                    listUangKas.setAdapter(adapterChapter);
                                                    adapterChapter.setKasChapterList(kasChapterList);
                                                }
                                            }
                                        });
                            } else {
                                Log.d("MAAAFFF ", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }


}