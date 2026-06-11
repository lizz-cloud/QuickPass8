package com.example.quickpass8;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class Horario extends AppCompatActivity {


    // ya funciona para todos los dispositivos mooviles
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

                    // Copiar imagen al almacenamiento interno de la app, especialmente apara Xiaomi :(
                    String rutaLocal = copiarImagenInterna(imagenUri);

                    if (rutaLocal != null) {
                        // Guardar ruta local (no URI externa) en SharedPreferences
                        prefs.edit()
                                .putString("horario_" + boleta, rutaLocal)
                                .apply();

                        mostrarImagenDesdeRuta(rutaLocal);
                    }
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
        String rutaGuardada = prefs.getString("horario_" + boleta, null);
        if (rutaGuardada != null) {
            mostrarImagenDesdeRuta(rutaGuardada);
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

    // Copia la imagen seleccionada al almacenamiento interno y devuelve su ruta
    private String copiarImagenInterna(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            // Nombre de archivo único por boleta
            File archivo = new File(getFilesDir(), "horario_" + boleta + ".jpg");

            FileOutputStream outputStream = new FileOutputStream(archivo);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

            outputStream.flush();
            outputStream.close();
            inputStream.close();

            return archivo.getAbsolutePath();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Muestra la imagen desde una ruta local del almacenamiento interno, para MIUI
    private void mostrarImagenDesdeRuta(String ruta) {
        File archivo = new File(ruta);
        if (archivo.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(ruta);
            imgHorario.setImageBitmap(bitmap);
            imgHorario.setVisibility(View.VISIBLE);
            tvSinFoto.setVisibility(View.GONE);
        }
    }
}