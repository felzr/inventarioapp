package com.example.jean.inventarioApp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.activitys.ItensActivity;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItensAdapter extends RecyclerView.Adapter<ItensHolder> {

    private List<Item> itens;
    private Context context;

    public ItensAdapter(ArrayList itens, Context context) {
        this.itens = itens;
        this.context = context;
    }

    @NonNull
    @Override
    public ItensHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItensHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItensHolder holder, int position) {
        holder.nome.setText(itens.get(position).getNome());
//        holder.nome.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), ItensActivity.class);
//                view.getContext().startActivity(intent);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
