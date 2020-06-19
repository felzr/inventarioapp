package com.example.jean.inventarioApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.jean.inventarioApp.adapters.InventarioAdapter;
import com.example.jean.inventarioApp.model.Inventario;

import java.util.ArrayList;
import java.util.List;

public class InventarioActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        recyclerView = (RecyclerView) findViewById(R.id.lista_inventario);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        List myDataset = new ArrayList<Inventario>();
        Inventario inventario = new Inventario();
        inventario.setNome("teste");
        myDataset.add(inventario);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        // specify an adapter (see also next example)
        mAdapter = new InventarioAdapter((ArrayList) myDataset, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }
}