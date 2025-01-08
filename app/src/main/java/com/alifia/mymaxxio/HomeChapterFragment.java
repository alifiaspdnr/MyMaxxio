package com.alifia.mymaxxio;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alifia.mymaxxio.model.Post;
import com.alifia.mymaxxio.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeChapterFragment extends Fragment {
    private RecyclerView chapterPostList;
    private PostAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_chapter, container, false);
        chapterPostList = view.findViewById(R.id.chapterPostList);
        chapterPostList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // menampilkan data di collection 'posts' dengan parameter namaTingkatan == "Chapter"
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        store.collection("posts").whereEqualTo("namaTingkatan", "Chapter")
                .orderBy("created", Query.Direction.DESCENDING)
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
                                            chapterPostList.setAdapter(adapter);
                                        }
                                    });
                        } else {
                            adapter = new PostAdapter(new ArrayList<>());
                            chapterPostList.setAdapter(adapter);
                        }
                    }
                });


        return view;
    }
}