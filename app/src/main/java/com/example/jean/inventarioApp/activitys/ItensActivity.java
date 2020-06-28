package com.example.jean.inventarioApp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.services.Firebase;
import com.google.firebase.firestore.FirebaseFirestore;

public class ItensActivity extends AppCompatActivity {
    private String chaveIventario;
    private Button btnCadastrarItem;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);
        this.chaveIventario = getChaveInventario();
        btnCadastrarItem = findViewById(R.id.btn_cadastrar_item);
        btnCadastrarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrarItem(chaveIventario);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private String getChaveInventario() {
        Intent i = getIntent();
        return i.getStringExtra("idInventario").toString();
    }

    private void cadastrarItem(String chaveIventario) {
        Intent intent = new Intent(getApplicationContext(), NovoItemActivity.class);
        intent.putExtra("idInventario", chaveIventario);
        startActivity(intent);

    }
}