package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Usuario;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.services.Preferences;
import com.example.jean.inventarioApp.utils.Base64Custom;
import com.example.jean.inventarioApp.utils.Permissao;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class LoginActivity extends AppCompatActivity {
    private Button btnLogar, btnRegistra;
    private EditText campoEmail, campoSenha;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private Preferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        preferences = new Preferences(getApplicationContext());
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };
        Permissao.validaPermissoes(PERMISSION_ALL, LoginActivity.this, PERMISSIONS);
        campoEmail = (EditText) findViewById(R.id.campo_email);
        campoSenha = (EditText) findViewById(R.id.campo_senha);
        verificaLoginAnterior();
        this.usuario = new Usuario();
        btnLogar = findViewById(R.id.btn_logar);
        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logar();
            }
        });
        btnRegistra = findViewById(R.id.btn_registrar);
        btnRegistra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrar();
            }
        });
    }

    private void logar() {
        this.usuario.setEmail(campoEmail.getText().toString());
        this.usuario.setSenha(campoSenha.getText().toString());
        autenticacao = Firebase.getFirebaseAutenticacao();
        if (this.usuario.getEmail().equals("") && this.usuario.getSenha().equals("")){
            Toast.makeText(LoginActivity.this, "E-mail e senha são obrigatórios", Toast.LENGTH_LONG ).show();
        }else{
            autenticacao.signInWithEmailAndPassword(this.usuario.getEmail(), this.usuario.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        if (preferences.getIdentificador() == null){
                            String id = Base64Custom.codificarBase64(autenticacao.getCurrentUser().getEmail().toString());
                            preferences.salvarDados(id,campoEmail.getText().toString());
                        }
                        abrirMain();
                        Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();
                    }else{
                        Toast.makeText(LoginActivity.this, "Erro ao fazer login!", Toast.LENGTH_LONG ).show();
                    }
                }
            });

        }
    }

    private void verificaLoginAnterior() {
        autenticacao = Firebase.getFirebaseAutenticacao();
        if(autenticacao.getCurrentUser() != null){
            this.campoEmail.setText(autenticacao.getCurrentUser().getEmail().toString());
        }
    }

    private void abrirMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void registrar() {
        Intent intent = new Intent(this, CadastroActivity.class);
        startActivity(intent);
    }
}