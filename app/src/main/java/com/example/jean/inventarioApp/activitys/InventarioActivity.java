package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.adapters.InventarioAdapter;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.services.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventario);
        recyclerView = (RecyclerView) findViewById(R.id.lista_inventario);
        preferences = new Preferences(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        textoInventarioVazio = findViewById(R.id.texto_sem_registros);

        // use a linear layout manager
        List<Inventario> myDataset = new ArrayList<>();
        db = Firebase.getFirebaseDatabase();
        mAdapter = new InventarioAdapter((ArrayList) myDataset, getApplicationContext());
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
}