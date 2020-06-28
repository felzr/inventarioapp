package com.example.jean.inventarioApp.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.inventarioApp.R;

public class ItensHolder extends RecyclerView.ViewHolder {
    public TextView nome;
    public ItensHolder(View itemView) {
        super(itemView);
        nome = (TextView) itemView.findViewById(R.id.texto_lista_inventario);
    }
}
