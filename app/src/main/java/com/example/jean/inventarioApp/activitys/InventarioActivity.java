package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.adapters.InventarioAdapter;
import com.example.jean.inventarioApp.adapters.InventarioViewClickListener;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.model.Item;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.services.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventarioActivity extends AppCompatActivity {
    private String TAG = "Inventarios";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private Preferences preferences;
    private EventListener valueEventListener;
    private TextView textoInventarioVazio;
    private List<Inventario> myDataset = new ArrayList<>();
    List<Item> listaItensFilhosRetorno = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        recyclerView = (RecyclerView) findViewById(R.id.lista_inventario);
        preferences = new Preferences(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        textoInventarioVazio = findViewById(R.id.texto_sem_registros);

        db = Firebase.getFirebaseDatabase();
        mAdapter = new InventarioAdapter((ArrayList) myDataset, getApplicationContext(), new InventarioViewClickListener() {
            @Override
            public void deleteClickItem(String id) {
                verificarPossiveldeletarInventario(id);

            }

            @Override
            public void editClickItem(String id) {
                Intent i = new Intent(InventarioActivity.this, NovoInventarioActivity.class);
                i.putExtra("modoEditar", true);
                i.putExtra("indentificadorInventarioEdicao", id);
                startActivity(i);
                finish();
            }
        });


        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        atualizarListaInventario();

    }

    private void verificarPossiveldeletarInventario(String id) {
        db.collection("itens")
                .whereEqualTo("identificadorInventario", id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = new Item();
                                item = document.toObject(Item.class);
                                item.setId(document.getId());
                                listaItensFilhosRetorno.add(item);
                            }
                            if (listaItensFilhosRetorno.size() == 0) {
                                deletarInventario(id);
                            } else {
                                Toast.makeText(InventarioActivity.this, "Antes de deletar esse inventario, deve-se deletar os itens contidos nele! ", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void deletarInventario(String id) {
        db.collection("Inventarios").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myDataset.clear();
                atualizarListaInventario();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(InventarioActivity.this, "Erro ao deletar Inventário", Toast.LENGTH_LONG).show();
            }
        });


    }


    public void atualizarListaInventario() {
        Task<QuerySnapshot> docRef = db.collection("Inventarios").whereEqualTo("identificadorUsuarioResponsavel",
                preferences.getIdentificador()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Inventario inventario = new Inventario();
                        inventario = document.toObject(Inventario.class);
                        inventario.setId(document.getId());
                        myDataset.add(inventario);
                        mAdapter.notifyDataSetChanged();
                    }
                    if (myDataset.isEmpty()) {
                        textoInventarioVazio.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

    }

}