package com.example.jean.inventarioApp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.inventarioApp.R;

public class InventarioHolder extends RecyclerView.ViewHolder {
    public TextView nome;

    public InventarioHolder(View itemView) {
        super(itemView);
        nome = (TextView) itemView.findViewById(R.id.texto_lista_inventario);
    }
}
