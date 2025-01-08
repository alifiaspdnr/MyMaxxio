package com.alifia.mymaxxio;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class NotifAdapter extends RecyclerView.Adapter<NotifAdapter.NotifViewHolder>{
    //private List<Notif> notif;
    private User user;
    private List<DocumentSnapshot> notifList;

    public static class NotifViewHolder extends RecyclerView.ViewHolder {
        public TextView judulPengumuman, isiPengumuman;

        public NotifViewHolder(@NonNull View itemView) {
            super(itemView);
            judulPengumuman = itemView.findViewById(R.id.judul_pengumuman);
            isiPengumuman = itemView.findViewById(R.id.isi_pengumuman);
        }
    }

    public NotifAdapter(List<DocumentSnapshot> notifList) {
        //Log.d("TAG", notif.toString());
        this.notifList= notifList;
        //this.user = new User();
    }

    /*public NotifAdapter(List<Notif> notif, User user) {
        this.notif = notif;
        this.user = user;
        store = FirebaseFirestore.getInstance();
    }*/

    @Override
    public NotifAdapter.NotifViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notif_row, parent, false);
        return new NotifAdapter.NotifViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotifViewHolder holder, int position) {
        DocumentSnapshot notif = notifList.get(position);
        holder.judulPengumuman.setText(notif.getString("judulPengumuman"));
        holder.isiPengumuman.setText(notif.getString("isiPengumuman"));
    }

    @Override
    public int getItemCount() {
        return notifList.size();
    }

    public void setPengumumanList(List<DocumentSnapshot> notifList) {
        this.notifList = notifList;
        notifyDataSetChanged();
    }
}
