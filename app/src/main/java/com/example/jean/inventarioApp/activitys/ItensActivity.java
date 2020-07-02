package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.adapters.InventarioAdapter;
import com.example.jean.inventarioApp.adapters.ItensAdapter;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.model.Item;
import com.example.jean.inventarioApp.services.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ItensActivity extends AppCompatActivity {
    private String chaveIventario;
    private Button btnCadastrarItem;
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private TextView textoInventarioVazio;
    private String TAG = "itens";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);
        this.chaveIventario = getChaveInventario();
        recyclerView = (RecyclerView) findViewById(R.id.lista_itens);
        textoInventarioVazio = findViewById(R.id.texto_sem_registros);
        btnCadastrarItem = findViewById(R.id.btn_cadastrar_item);
        btnCadastrarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarItem(chaveIventario);
            }
        });
        List<Item> myDataset = new ArrayList<>();
        db = Firebase.getFirebaseDatabase();
        mAdapter = new ItensAdapter((ArrayList) myDataset, getApplicationContext());
        Task<QuerySnapshot> docRef = db.collection("itens").whereEqualTo("identificadorInventario",
                this.chaveIventario).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Item item = new Item();
                        item = document.toObject(Item.class);
                        item.setId(document.getId());
                        myDataset.add(item);
                        if (myDataset.size() != 0) {
                            mAdapter.notifyDataSetChanged();
                        } else {
                            textoInventarioVazio.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String getChaveInventario() {
        Intent i = getIntent();
        return i.getSerializableExtra("idInventario").toString();
    }

    private void cadastrarItem(String chaveIventario) {
        Intent intent = new Intent(getApplicationContext(), NovoItemActivity.class);
        intent.putExtra("idInventario", chaveIventario);
        startActivity(intent);

    }
}