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

public class HomeNasionalFragment extends Fragment {
    private RecyclerView nasionalPostList;
    private PostAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_nasional, container, false);

        nasionalPostList = view.findViewById(R.id.nasionalPostList);
        nasionalPostList.setLayoutManager(new LinearLayoutManager(getActivity()));

        // menampilkan data di collection 'posts' dengan parameter namaTingkatan == "Nasional"
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        //store.collection("posts").whereEqualTo("chapter", "Nasional")
        store.collection("posts").whereEqualTo("namaTingkatan", "Nasional")
                .orderBy("created", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(
                            @Nullable QuerySnapshot snapshot,
                            @Nullable FirebaseFirestoreException e
                    ) {
                        if (e != null) {
                            adapter = new PostAdapter(new ArrayList<>());
                            nasionalPostList.setAdapter(adapter);
                            return;
                        }

                        if (snapshot != null) {
                            FirebaseAuth auth = FirebaseAuth.getInstance();
                            String uid = auth.getUid();
                            assert uid != null;
                            store.collection("users")
                                    .document(uid)
                                    .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                            User user = value.toObject(User.class);
                                            adapter = new PostAdapter(snapshot.toObjects(Post.class), user);
                                            nasionalPostList.setAdapter(adapter);
                                        }
                                    });
                        } else {
                            adapter = new PostAdapter(new ArrayList<>());
                            nasionalPostList.setAdapter(adapter);
                        }
                    }
                });


        return view;
    }
}