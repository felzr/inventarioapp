package com.example.jean.inventarioApp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.model.Item;
import com.example.jean.inventarioApp.utils.Permissao;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;

public class NovoItemActivity extends AppCompatActivity {
    private String identificadorInventario;
    private FirebaseFirestore db;
    private FirebaseStorage galeriaFirebase;
    private Item item;
    private EditText campoNomeProduto, campoQtdProduto, categoriaProduto, descrProduto;
    private Button cadastrarItem, udploadFoto, camera, abrirGaleira;
    private View viewUpdaload;
    private final int PICK_IMAGE_REQUEST = 22;
    private final int REQUEST_IMAGE_CAPTURE = 111;
    private ImageView imageItem;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_item);
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.CAMERA
        };
        Permissao.validaPermissoes(PERMISSION_ALL, NovoItemActivity.this, PERMISSIONS);

        udploadFoto = findViewById(R.id.btn_abrir_opcao_foto);
        imageItem = findViewById(R.id.view_image);
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

                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageItem.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            filePath = data.getData();
            imageItem.setImageBitmap(photo);
        }
    }

    public void selecionarImagem() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    public void tirarForo() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
}