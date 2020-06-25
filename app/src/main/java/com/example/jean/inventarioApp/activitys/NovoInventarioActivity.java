package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.services.Preferences;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NovoInventarioActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private Preferences preferences;
    private EditText nomeInventario, descrInventario;
    private Button btnCadastro;
    private Inventario inventario;
    private static final String TAG = "CadastroInventario";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_inventario);
        preferences = new Preferences(getApplicationContext());
        db = Firebase.getFirebaseDatabase();
        nomeInventario = findViewById(R.id.nome_inventario);
        descrInventario = findViewById(R.id.descr_inventario);
        btnCadastro = findViewById(R.id.btn_cadastrar_inventario);
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nomeInventario.getText().toString().equals("") && nomeInventario.getText().toString().equals("")){
                    Toast.makeText(NovoInventarioActivity.this, "Nome e descrição são obrigatórios!", Toast.LENGTH_LONG ).show();
                }else{
                    cadastrarInventario();
                }
            }
        });
    }

    private void cadastrarInventario() {
    this.inventario = montaInventario();
        db = Firebase.getFirebaseDatabase();
        Map<String, Object> inv = new HashMap<>();
        inv.put("identificadorUsuarioResponsavel", this.inventario.getIdentificadorUsuarioResponsavel());
        inv.put("nome", this.inventario.getNome());
        inv.put("data", this.inventario.getData());
        inv.put("descricao", this.inventario.getDescricao());
        db.collection("Inventarios")
                .add(inv)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        Toast.makeText(NovoInventarioActivity.this, "cadastro efetuado com sucesso!", Toast.LENGTH_LONG ).show();

                        carregaInventariosCadastrados();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(NovoInventarioActivity.this, "Erro ao cadastrar Inventário!", Toast.LENGTH_LONG ).show();

                    }
                });

    }

    private void carregaInventariosCadastrados() {
        Intent i = new Intent(NovoInventarioActivity.this, InventarioActivity.class);
        startActivity(i);
        finish();
    }

    private Inventario montaInventario() {
        Inventario i = new Inventario();
        i.setNome(nomeInventario.getText().toString());
        i.setData(new Date());
        i.setDescricao(nomeInventario.getText().toString());
        i.setIdentificadorUsuarioResponsavel(preferences.getIdentificador());
        return i;
    }


}