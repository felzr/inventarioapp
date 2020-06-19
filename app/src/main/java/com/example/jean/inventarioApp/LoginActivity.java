package com.example.jean.inventarioApp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogar, btnRegsitar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnLogar = findViewById(R.id.btn_logar);
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar();
            }
        });
        btnRegsitar = findViewById(R.id.btn_registrar);
        btnRegsitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }

    private void logar() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void registrar() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}