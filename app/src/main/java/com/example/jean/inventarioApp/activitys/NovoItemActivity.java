package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.model.Item;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.utils.Permissao;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NovoItemActivity extends AppCompatActivity {
    private static final String TAG = "cadastroItem";
    private String identificadorInventario;
    private FirebaseFirestore db;
    private FirebaseStorage galeriaFirebase;
    private Item item;
    private EditText campoNomeProduto, campoQtdProduto, campoCategoria, campodescrProduto;
    private Button cadastrarItem, udploadFoto, camera, abrirGaleira, btnEditar;
    private View viewUpdaload;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView imageItem;
    private Uri filePath;
    private Bitmap fotoItem;
    private Boolean modoEditar;
    private String indentificadorIemEdicao;
    private Button btn_home, btn_inventarios, btn_relatorio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);
        udploadFoto = findViewById(R.id.btn_abrir_opcao_foto);
        imageItem = findViewById(R.id.view_image);
        btn_home = findViewById(R.id.btn_home);
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaMain();
            }
        });
        btn_inventarios = findViewById(R.id.btn_inventarios);
        btn_inventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaInventarios();
            }
        });
        btn_relatorio = findViewById(R.id.btn_relatorio);
        btn_relatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaRelatorio();
            }
        });

        udploadFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewUpdaload = findViewById(R.id.view_upload_foto);
                viewUpdaload.setVisibility(View.VISIBLE);
            }
        });
        camera = findViewById(R.id.btn_camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tirarForo();
            }
        });
        abrirGaleira = findViewById(R.id.btn_galeria);
        abrirGaleira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selecionarImagem();
            }
        });
        campoNomeProduto = findViewById(R.id.campo_nome_produto);
        campoQtdProduto = findViewById(R.id.campo_quantidade);
        campoCategoria = findViewById(R.id.campo_categoria);
        campodescrProduto = findViewById(R.id.campo_descr);
        cadastrarItem = findViewById(R.id.btn_cadastrar);
        cadastrarItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (campoNomeProduto.getText().toString().equals("") && campoQtdProduto.getText().toString().equals("") &&
                        campoCategoria.getText().toString().equals("") && campodescrProduto.getText().toString().equals("")) {
                    Toast.makeText(NovoItemActivity.this, "Nome, Quantidade, Categoria e descrição são obrigatórios!", Toast.LENGTH_LONG).show();
                } else {
                    cadastrarFoto();
                }
            }
        });
        btnEditar = findViewById(R.id.btn_edit);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        validaCadastroOuEdicao();
        this.identificadorInventario = getChaveInventario();
    }

    private void validaCadastroOuEdicao() {
        this.modoEditar = getIntent().getBooleanExtra("modoEditar", false);
        if (modoEditar) {
            carregaViewEditar();
        }
    }

    private void carregaViewEditar() {
        db = Firebase.getFirebaseDatabase();
        Item itemEditado = new Item();
        this.cadastrarItem.setVisibility(View.INVISIBLE);
        this.btnEditar.setVisibility(View.VISIBLE);
        indentificadorIemEdicao = getIntent().getSerializableExtra("indentificadorIemEdicao").toString();
        itemEditado.setId(indentificadorIemEdicao);
        DocumentReference docRef = db.collection("itens").document(indentificadorIemEdicao);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Item itemSnapshot = documentSnapshot.toObject(Item.class);
                campoNomeProduto.setText(itemSnapshot.getNome());
                campoQtdProduto.setText(itemSnapshot.getQtd().toString());
                campoCategoria.setText(itemSnapshot.getCategoria());
                campodescrProduto.setText(itemSnapshot.getDescricao());
                itemEditado.setIdentificadorInventario(itemSnapshot.getIdentificadorInventario());
                itemEditado.setId(documentSnapshot.getId());
                itemEditado.setData(itemSnapshot.getData());
                itemEditado.setUrlFoto(itemSnapshot.getUrlFoto());
            }
        });
        this.btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemEditado.setNome(campoNomeProduto.getText().toString());
                itemEditado.setQtd(Integer.parseInt(campoQtdProduto.getText().toString()));
                itemEditado.setCategoria(campoCategoria.getText().toString());
                itemEditado.setDescricao(campodescrProduto.getText().toString());
                Map<String, Object> item = new HashMap<>();
                item.put("identificadorInventario", itemEditado.getIdentificadorInventario());
                item.put("nome", itemEditado.getNome());
                item.put("data", itemEditado.getData());
                item.put("descricao", itemEditado.getDescricao());
                item.put("qtd", itemEditado.getQtd());
                item.put("categoria", itemEditado.getCategoria());
                item.put("urlFoto", itemEditado.getUrlFoto());
                db.collection("itens").document(itemEditado.getId()).set(item).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent i = new Intent(NovoItemActivity.this, ItensActivity.class);
                        i.putExtra("idInventario", itemEditado.getIdentificadorInventario());
                        startActivity(i);
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });
    }

    private void cadastrarItem(String url) {
        Item i = new Item();
        i.setData(new Date());
        i.setNome(campoNomeProduto.getText().toString());
        i.setQtd(Integer.parseInt(campoQtdProduto.getText().toString()));
        i.setIdentificadorInventario(this.identificadorInventario);
        i.setDescricao(campodescrProduto.getText().toString());
        i.setCategoria(campoCategoria.getText().toString());
        FirebaseFirestore db = Firebase.getFirebaseDatabase();
        Map<String, Object> item = new HashMap<>();
        item.put("identificadorInventario", i.getIdentificadorInventario());
        item.put("nome", i.getNome());
        item.put("data", i.getData());
        item.put("descricao", i.getDescricao());
        item.put("qtd", i.getQtd());
        item.put("categoria", i.getCategoria());
        if (url.equals("")) {
            item.put("urlFoto", "SEMFOTO");
        } else {
            item.put("urlFoto", url);
        }
        db.collection("itens").add(item).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                carregaActivityItems();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
                Toast.makeText(NovoItemActivity.this, "Erro ao cadastrar Item!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void carregaActivityItems() {
        Intent i = new Intent(NovoItemActivity.this, ItensActivity.class);
        i.putExtra("idInventario", identificadorInventario);
        startActivity(i);
        finish();
    }

    private void cadastrarFoto() {
        galeriaFirebase = Firebase.getFirebaseStorage();
        StorageReference storageRef = galeriaFirebase.getReference();
        if (this.filePath == null) {
            this.cadastrarItem("");
        } else {
            storageRef.child("item.jpg");
            imageItem.setDrawingCacheEnabled(true);
            imageItem.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) imageItem.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageRef.putBytes(data);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        cadastrarItem(downloadUri.toString());
                    } else {
                        Toast.makeText(NovoItemActivity.this, "Erro ao fazer upload da imagem do item!", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }


    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode,
                resultCode,
                data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            filePath = data.getData();
            try {

                this.fotoItem = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageItem.setImageBitmap(this.fotoItem);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            this.fotoItem = (Bitmap) data.getExtras().get("data");
            imageItem.setImageBitmap(this.fotoItem);
        }
    }

    public void selecionarImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Selecione uma imagem..."),
                PICK_IMAGE_REQUEST);
    }

    public void tirarForo() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }

    private String getChaveInventario() {
        Intent i = getIntent();
        if (modoEditar) {
            return "";
        } else {
            return i.getSerializableExtra("idInventario").toString();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void chamaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void chamaRelatorio() {
        Intent intent = new Intent(this, RelatorioActivity.class);
        startActivity(intent);
        finish();
    }

    private void chamaInventarios() {
        Intent intent = new Intent(this, InventarioActivity.class);
        startActivity(intent);
        finish();
    }
}