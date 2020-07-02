package com.example.jean.inventarioApp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Inventario;

import java.util.ArrayList;
import java.util.List;

public class RelatorioAdapter extends RecyclerView.Adapter<RelatorioHolder> {
    private List<Inventario> inventarios;
    private Context context;
    private RelatorioViewClickListener listener;

    public RelatorioAdapter(ArrayList inventarios, Context context, RelatorioViewClickListener listener) {
        this.inventarios = inventarios;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RelatorioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RelatorioHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relatorio_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RelatorioHolder holder, int position) {
        holder.nome.setText(inventarios.get(position).getNome());
        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clickItem(inventarios.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return inventarios.size();
    }
}
