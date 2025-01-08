package com.alifia.mymaxxio;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NotifChapterFragment extends Fragment {

    private RecyclerView notifChapList;
    private NotifAdapter adapter;
    private FirebaseFirestore db;
    private CollectionReference notifRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notif_chapter, container, false);

        db = FirebaseFirestore.getInstance();
        notifRef = db.collection("pengumuman");

        notifChapList = view.findViewById(R.id.notifChapList);
        notifChapList.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NotifAdapter(new ArrayList<>());
        notifChapList.setAdapter(adapter);

        setupFirestore();

        return view;
    }

    private void setupFirestore() {
        notifRef.whereEqualTo("namaTingkatan", "Chapter").
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DocumentSnapshot> notifList = task.getResult().getDocuments();
                            adapter.setPengumumanList(notifList);
                        }
                    }
                });
    }

}