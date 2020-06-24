package com.example.jean.inventarioApp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.jean.inventarioApp.R;

public class MainActivity extends AppCompatActivity {
    private Button btnNovoInventario, btnInventarios,
            btnRelatorio, btnMiniNovoInventario,btnMiniInventarios, btnMiniRelatorio;
    private ImageButton btnDeslogar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDeslogar = findViewById(R.id.btn_logout);
        btnDeslogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deslogar();
            }
        });
        btnInventarios = findViewById(R.id.btn_iventarios);
        btnInventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaInventarios();
            }
        });
        btnNovoInventario = findViewById(R.id.btn_novo_inventario);
        btnNovoInventario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                novoInventario();
            }
        });
        btnMiniInventarios = findViewById(R.id.btn_mini_inventario);
//        btnMiniInventarios.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                chamaInventarios();
//            }
//        });
    }

    private void deslogar() {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private void novoInventario() {
        Intent intent = new Intent(this, NovoInventarioActivity.class);
        startActivity(intent);
    }

    private void chamaInventarios() {
        Intent intent = new Intent(this, InventarioActivity.class);
        startActivity(intent);
    }
}