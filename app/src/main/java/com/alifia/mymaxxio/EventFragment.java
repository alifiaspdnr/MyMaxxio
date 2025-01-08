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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.alifia.mymaxxio.model.Event;
import com.alifia.mymaxxio.model.EventItem;
import com.alifia.mymaxxio.model.ListItem;
import com.alifia.mymaxxio.model.TitleItem;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class EventFragment extends Fragment {
    private RecyclerView eventList;
    private EventAdapter adapter;
    private Spinner btnTingkatanPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        eventList = view.findViewById(R.id.eventList);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnTingkatanPost = view.findViewById(R.id.dropdown_tingkatan_post);
        ArrayAdapter<CharSequence> adapterTingkatanPost = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.event_array,
                R.layout.custom_spinner_item
        );
        adapterTingkatanPost.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        btnTingkatanPost.setAdapter(adapterTingkatanPost);
        btnTingkatanPost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setupFirestore();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        setupFirestore();

        return view;
    }

    public void setupFirestore() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore store = FirebaseFirestore.getInstance();
        Query snapshot = store.collection("kegiatan");

        switch (btnTingkatanPost.getSelectedItem().toString()) {
            case "Nasional":
                snapshot = store.collection("kegiatan").whereEqualTo("namaTingkatan", "Nasional");
                break;
            case "Regional Saya":
                snapshot = store.collection("kegiatan").whereEqualTo("namaTingkatan", "Regional");
                break;
            case "Chapter Saya":
                snapshot = store.collection("kegiatan").whereEqualTo("namaTingkatan", "Chapter");
                break;
            case "Kegiatan Saya":
                snapshot = store.collection("kegiatan").whereEqualTo("creatorEmail", auth.getCurrentUser().getEmail());
                break;
        }
        Log.d("TAG", btnTingkatanPost.getSelectedItem().toString());
        snapshot.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(
                    @Nullable QuerySnapshot snapshot,
                    @Nullable FirebaseFirestoreException error
            ) {
                if (error != null) {
                    adapter = new EventAdapter(new ArrayList<>());
                    eventList.setAdapter(adapter);
                    return;
                }

                if (snapshot != null) {
                    List<Event> events = snapshot.toObjects(Event.class);
                    HashMap<String, List<Event>> groups = groupEvents(events);
                    List<ListItem> consolidatedList = new ArrayList<>();
                    for (String date : groups.keySet()) {
                        TitleItem titleItem = new TitleItem();
                        titleItem.setDate(date);
                        consolidatedList.add(titleItem);


                        for (Event event : groups.get(date)) {
                            EventItem eventItem = new EventItem();
                            eventItem.setEvent(event);
                            consolidatedList.add(eventItem);
                        }
                    }
                    adapter = new EventAdapter(consolidatedList);
                    eventList.setAdapter(adapter);
                } else {
                    adapter = new EventAdapter(new ArrayList<>());
                    eventList.setAdapter(adapter);
                }
            }
        });
    }

    public HashMap<String, List<Event>> groupEvents(List<Event> events) {
        HashMap<String, List<Event>> groupedHashMap = new HashMap<>();

        for (Event event : events) {
            Timestamp timestamp = event.getTgl();
            Date date = timestamp.toDate();
            SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.forLanguageTag("ID"));
            String monthYear = sdf.format(date);

            if (groupedHashMap.containsKey(monthYear)) {
                // The key is already in the HashMap; add the pojo object
                // against the existing key.
                groupedHashMap.get(monthYear).add(event);
            } else {
                // The key is not there in the HashMap; create a new key-value pair
                List<Event> list = new ArrayList<>();
                list.add(event);
                groupedHashMap.put(monthYear, list);
            }
        }

        return groupedHashMap;
    }
}