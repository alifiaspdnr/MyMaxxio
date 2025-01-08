package com.alifia.mymaxxio;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DetailUnggahanSaya extends AppCompatActivity {
    private MaterialToolbar btnKembali;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    //private CollectionReference unggahanSayaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_unggahan_saya);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        //unggahanSayaRef = db.collection("posts");

        btnKembali = findViewById(R.id.btn_kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // ambil nama lengkap dari firestore
        String email = mAuth.getCurrentUser().getEmail();
        Log.d("EMAILnya : ", email);
        db.collection("member").whereEqualTo("email", email).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String strNamaLengkap = document.getString("namaLengkap");
                            setupFirestore(strNamaLengkap);
                        } else {
                            Log.d("MAAF GABISA NAMA", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void setupFirestore(String strNamaLengkap) {
        db.collection("posts")
                .whereEqualTo("nama", strNamaLengkap)
                .whereEqualTo("isPosted", 1)
                .orderBy("created", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshot) {
                        if (snapshot != null) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = auth.getUid();
                            assert uid != null;
                            db.collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User user = documentSnapshot.toObject(User.class);
                                            adapter = new PostAdapter(snapshot.toObjects(Post.class), user);
                                            recyclerView.setAdapter(adapter);
                                        }
                                    });
                        } else {
                            adapter = new PostAdapter(new ArrayList<>());
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
}