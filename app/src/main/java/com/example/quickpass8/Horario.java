package com.example.quickpass8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Horario extends AppCompatActivity {

    ImageButton btnInicio, btnAgregarFoto;
    ImageView imgHorario;
    TextView tvSinFoto;
    String boleta;
    SharedPreferences prefs;

    ActivityResultLauncher<Intent> galeriaLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri imagenUri = result.getData().getData();

                    // Persistir permisos de lectura
                    getContentResolver().takePersistableUriPermission(
                            imagenUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    // Guardar URI en SharedPreferences con la boleta como clave
                    prefs.edit()
                            .putString("horario_" + boleta, imagenUri.toString())
                            .apply();

                    // Mostrar imagen
                    mostrarImagen(imagenUri.toString());
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_horario);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        boleta = getIntent().getStringExtra("boleta");

        btnInicio = findViewById(R.id.btnInicio);
        btnAgregarFoto = findViewById(R.id.btnAgregarFoto);
        imgHorario = findViewById(R.id.imgHorario);
        tvSinFoto = findViewById(R.id.tvSinFoto);

        prefs = getSharedPreferences("QuickPass8Prefs", MODE_PRIVATE);

        // Cargar foto guardada si existe
        String uriGuardada = prefs.getString("horario_" + boleta, null);
        if (uriGuardada != null) {
            mostrarImagen(uriGuardada);
        }

        // Botón agregar foto
        btnAgregarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            galeriaLauncher.launch(intent);
        });

        // Botón inicio
        btnInicio.setOnClickListener(v -> {
            Intent intent = new Intent(Horario.this, paginicio.class);
            intent.putExtra("boleta", boleta);
            startActivity(intent);
        });
    }

    private void mostrarImagen(String uriString) {
        imgHorario.setImageURI(Uri.parse(uriString));
        imgHorario.setVisibility(View.VISIBLE);
        tvSinFoto.setVisibility(View.GONE);
    }
}