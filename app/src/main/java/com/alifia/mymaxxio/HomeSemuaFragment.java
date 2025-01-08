package com.alifia.mymaxxio;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeSemuaFragment extends Fragment {
    private RecyclerView allPostList;
    private PostAdapter adapter;

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
        if (adapter != null) {
            setupFirestore();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_semua, container, false);
        allPostList = view.findViewById(R.id.allPostList);
        allPostList.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupFirestore();
        // Inflate the layout for this fragment
        return view;

    }

    public void setupFirestore() {
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        store.collection("posts")
                .orderBy("created", Query.Direction.DESCENDING)
                .whereEqualTo("idKegiatan", "")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot snapshot) {
                        if (snapshot != null) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = auth.getUid();
                            assert uid != null;
                            store.collection("users")
                                    .document(uid)
                                    .get()
                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            User user = documentSnapshot.toObject(User.class);
                                            adapter = new PostAdapter(snapshot.toObjects(Post.class), user);
                                            allPostList.setAdapter(adapter);
                                        }
                                    });
                        } else {
                            adapter = new PostAdapter(new ArrayList<>());
                            allPostList.setAdapter(adapter);
                        }
                    }
                });
    }
}