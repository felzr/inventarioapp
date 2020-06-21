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
import com.example.jean.inventarioApp.model.Usuario;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.services.Preferences;
import com.example.jean.inventarioApp.utils.Base64Custom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class CadastroActivity extends AppCompatActivity {
    private EditText campoEmail, campoSenha, campoNome;
    private Button btnVoltar, btnRegistrar;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private static final String TAG = "CadastroUsuario";
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        btnVoltar = findViewById(R.id.btn_voltar);
        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        this.campoNome = findViewById(R.id.campo_nome);
        this.campoEmail = findViewById(R.id.campo_email);
        this.campoSenha = findViewById(R.id.campo_senha);
        btnRegistrar = findViewById(R.id.btn_registrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usuario = constroiUsuario();
                cadastrarUsuario(usuario);
            }
        });
    }

    private Usuario constroiUsuario() {
        Usuario usuario = new Usuario();
        usuario.setEmail(this.campoEmail.getText().toString());
        usuario.setSenha(this.campoSenha.getText().toString());
        usuario.setNome(this.campoNome.getText().toString());
        return usuario;
    }

    private void cadastrarUsuario(Usuario usuario) {
        auth = Firebase.getFirebaseAutenticacao();
        if (usuario.getNome().equals("")) {
            Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário: " + "O nome do usuário é obrigatório", Toast.LENGTH_LONG).show();
        }
        auth.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha()).addOnCompleteListener(CadastroActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Toast.makeText(CadastroActivity.this, "Sucesso ao cadastrar usuário", Toast.LENGTH_LONG).show();

                    String identificadorUsuario = Base64Custom.codificarBase64(usuario.getEmail());
                    usuario.setId(identificadorUsuario);
                    salvar(usuario);
                } else {

                    String erro = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erro = "Escolha uma senha que contenha, letras e números.";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erro = "Email indicado não é válido.";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erro = "Já existe uma conta com esse e-mail.";
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário: " + erro, Toast.LENGTH_LONG).show();
                }

            }
        });


    }
    private void carregaUsuarioLogado(Usuario usuario) {
        Preferences preferencias = new Preferences(CadastroActivity.this);
        preferencias.salvarDados( usuario.getId(), usuario.getNome() );
        Intent intent = new Intent(CadastroActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void salvar(Usuario usuario){
        db = Firebase.getFirebaseDatabase();
        Map<String, Object> user = new HashMap<>();
        user.put("email", usuario.getEmail());
        user.put("usuario", usuario.getNome());
        user.put("id", usuario.getId());
        db.collection("usuarios")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                        carregaUsuarioLogado(usuario);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }
}