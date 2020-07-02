package com.example.jean.inventarioApp.activitys;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.inventarioApp.R;
import com.example.jean.inventarioApp.adapters.RelatorioAdapter;
import com.example.jean.inventarioApp.adapters.RelatorioViewClickListener;
import com.example.jean.inventarioApp.model.Inventario;
import com.example.jean.inventarioApp.model.Item;
import com.example.jean.inventarioApp.services.Firebase;
import com.example.jean.inventarioApp.services.Preferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RelatorioActivity extends AppCompatActivity {
    private String TAG = "Inventarios";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;
    private Preferences preferences;
    private EventListener valueEventListener;
    private TextView textoInventarioVazio;
    private List<Inventario> myDataset = new ArrayList<>();
    private Button btnhome, btnInventarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio);
        recyclerView = (RecyclerView) findViewById(R.id.lista_relatorio);
        preferences = new Preferences(getApplicationContext());
        recyclerView.setHasFixedSize(true);
        textoInventarioVazio = findViewById(R.id.texto_sem_registros);
        db = Firebase.getFirebaseDatabase();
        btnhome = findViewById(R.id.btn_home);
        btnhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaMain();

            }
        });
        btnInventarios = findViewById(R.id.btn_inventarios);
        btnInventarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chamaInventarios();
            }
        });
        mAdapter = new RelatorioAdapter((ArrayList) myDataset, getApplicationContext(), new RelatorioViewClickListener() {
            @Override
            public void clickItem(String id) {
                try {
                    gerarRelatorioDeIventario(id);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        atualizarListaInventario();
    }

    public void atualizarListaInventario() {
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
                        mAdapter.notifyDataSetChanged();
                    }
                    if (myDataset.isEmpty()) {
                        textoInventarioVazio.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void gerarRelatorioDeIventario(String id) throws IOException {
        List<Item> items = new ArrayList<Item>();
        Task<QuerySnapshot> docRef = db.collection("itens").whereEqualTo("identificadorInventario",
                id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Item item = new Item();
                        item = document.toObject(Item.class);
                        item.setId(document.getId());
                        items.add(item);
                    }
                    gerarPlanilha(id, items);
                }
            }
        });
    }

    private void gerarPlanilha(String id, List<Item> items) {
        String[] columns = {"Nome", "Data", "Descrição", "Qtd", "Categoria"};
        Workbook workbook = new HSSFWorkbook();
        CreationHelper createHelper = workbook.getCreationHelper();
        Sheet sheet = workbook.createSheet("Itens");
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.RED.getIndex());

        Row headerRow = sheet.createRow(0);

        // Create cells
        for (
                int i = 0;
                i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        int rowNum = 1;
        for (Item item : items) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
                    .setCellValue(item.getNome());

            row.createCell(1)
                    .setCellValue(format(item.getData(), "dd/MM/YYYY hh:mm:ss").toString());
            row.createCell(2)
                    .setCellValue(item.getDescricao().toString());
            row.createCell(3)
                    .setCellValue(item.getQtd());
            row.createCell(4)
                    .setCellValue(item.getCategoria().toString());

        }
        Date data = new Date();
        String dataC = format(data, "yyyy-MM-dd HH:mm");
        String fileName = id + dataC + "planilha.xls";
        File file = new File(getExternalFilesDir(null), fileName);
        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(file);
            workbook.write(outputStream);
            String caminho = file.getAbsolutePath().toString();

            Toast.makeText(getApplicationContext(), "A planilha foi salva em " + caminho, Toast.LENGTH_LONG).show();
        } catch (
                java.io.IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "NO OK", Toast.LENGTH_LONG).show();
            try {
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static String format(Date data, String parent) {
        return new SimpleDateFormat(parent).format(data);
    }

    private void chamaMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void chamaInventarios() {
        Intent intent = new Intent(this, InventarioActivity.class);
        startActivity(intent);
    }

    private void chamaRelatorio() {
        Intent intent = new Intent(this, RelatorioActivity.class);
        startActivity(intent);
    }

}