package com.alifia.mymaxxio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class UangKasChapterAdapter extends RecyclerView.Adapter<UangKasChapterAdapter.UangKasChapterViewHolder> {

    private List<DocumentSnapshot> kasChapterList;
    private Context context;

    public UangKasChapterAdapter(List<DocumentSnapshot> kasChapterList, Context context) {
        this.kasChapterList = kasChapterList;
        this.context = context;
    }

    @NonNull
    @Override
    public UangKasChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kas_chapter, parent, false);
        return new UangKasChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UangKasChapterViewHolder holder, int position) {
        DocumentSnapshot kasChapter = kasChapterList.get(position);
        holder.bulan.setText(kasChapter.getString("bulan"));
        holder.tahun.setText(kasChapter.getString("tahun"));

        holder.itemView.setOnClickListener(v -> {
            String fileUrl = kasChapter.getString("fileUrl");
            openExcelFile(context, fileUrl);
        });
    }

    private void openExcelFile(Context context, String fileUrl) {
        Log.d("excelllllll", "Opening Excel file: " + fileUrl);

        // Untuk sementara, buka URL file Excel di browser
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(fileUrl));
        context.startActivity(intent);

        /*// Buka file Excel menggunakan Intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(fileUrl), "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        // Verifikasi apakah ada aplikasi yang bisa membuka file Excel
        Intent chooser = Intent.createChooser(intent, "Open File");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(chooser);
        } else {
            // Handle jika tidak ada aplikasi yang bisa membuka file Excel
            Toast.makeText(context, "No application to open Excel file", Toast.LENGTH_SHORT).show();
            System.out.println("GABISA MAAFFFF");
        }*/
    }

    @Override
    public int getItemCount() {
        return kasChapterList.size();
    }

    public void setKasChapterList(List<DocumentSnapshot> kasChapterList) {
        this.kasChapterList = kasChapterList;
        notifyDataSetChanged();
    }

    static class UangKasChapterViewHolder extends RecyclerView.ViewHolder {
        TextView bulan, tahun;

        public UangKasChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            bulan = itemView.findViewById(R.id.bulan);
            tahun = itemView.findViewById(R.id.tahun);
        }
    }
}
