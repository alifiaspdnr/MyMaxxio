package com.alifia.mymaxxio;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.ListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class UangKasAdapter extends RecyclerView.Adapter<UangKasAdapter.UangKasViewHolder> {
    private List<DocumentSnapshot> uangkasList;
    private Context context;
    private static final int PICK_IMAGE_REQUEST = 1;

    public UangKasAdapter(List<DocumentSnapshot> uangkasList, Context context) {
        this.uangkasList = uangkasList;
        this.context = context;
    }

    @NonNull
    @Override
    public UangKasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_uangkas_row, parent, false);
        return new UangKasViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UangKasViewHolder holder, int position) {
        DocumentSnapshot uangkas = uangkasList.get(position);
        holder.bulan.setText(uangkas.getString("bulan"));
        holder.tahun.setText(uangkas.getString("tahun"));
        holder.status.setText(uangkas.getString("status"));
        //holder.jumlah.setText(uangkas.get("jumlah_uang_kas").toString());

        // mengambil jumlah_uang_kas dan memformatnya jadi 10.000
        Number jumlahUangKas = uangkas.getDouble("jumlah_uang_kas");
        String formattedJumlah = NumberFormat.getNumberInstance(Locale.US).format(jumlahUangKas);

        holder.jumlah.setText(formattedJumlah);

        // bikin yang statusnya blm lunas & ditolak bisa dipencet
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strStatus = holder.status.getText().toString();
                if (strStatus.equals("Belum Lunas")) { // untuk upload bukti tf
                    Intent intent = new Intent(v.getContext(), UploadUangKas.class);
                    intent.putExtra("idKas", uangkas.getId());
                    intent.putExtra("namaMember", uangkas.getString("namaMember"));
                    intent.putExtra("status", strStatus);
                    v.getContext().startActivity(intent);

                } else if (strStatus.equals("Ditolak")) { // untuk intent ke detail uang kas (melihat alasan ditolak)
                    Intent intent = new Intent(v.getContext(), DetailUangKas.class);
                    intent.putExtra("idKas", uangkas.getId());
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return uangkasList.size();
    }

    public void setUangkasList(List<DocumentSnapshot> uangkasList) {
        this.uangkasList = uangkasList;
        notifyDataSetChanged();
    }

    static class UangKasViewHolder extends RecyclerView.ViewHolder {
        TextView bulan, tahun, jumlah, status;

        public UangKasViewHolder(@NonNull View itemView) {
            super(itemView);
            bulan = itemView.findViewById(R.id.bulan);
            jumlah = itemView.findViewById(R.id.jumlah);
            tahun = itemView.findViewById(R.id.tahun);
            status = itemView.findViewById(R.id.status);
        }
    }


}
