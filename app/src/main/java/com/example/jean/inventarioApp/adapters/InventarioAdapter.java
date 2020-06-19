package com.example.jean.inventarioApp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.inventarioApp.NovoItemActivity;
import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Inventario;

import java.util.ArrayList;
import java.util.List;

public class InventarioAdapter extends RecyclerView.Adapter<InventarioHolder>  {
    private List<Inventario> inventarios;
    private Context context;

    public InventarioAdapter(ArrayList inventarios,Context context) {
        this.inventarios = inventarios;
        this.context = context;
    }

    @NonNull
    @Override
    public InventarioHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InventarioHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InventarioHolder holder, int position) {
        holder.nome.setText(inventarios.get(position).getNome());
        holder.nome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NovoItemActivity.class);
                view.getContext().startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return inventarios.size();
    }
}
