package com.example.jean.inventarioApp.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Item;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button btnNovoInventario, btnInventarios,
            btnRelatorio, btnMiniNovoInventario, btnMiniInventarios, btnMiniRelatorio;
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
        btnRelatorio = findViewById(R.id.btn_relatorios);
        btnRelatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RelatorioActivity.class);
                startActivity(i);
            }
        });
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