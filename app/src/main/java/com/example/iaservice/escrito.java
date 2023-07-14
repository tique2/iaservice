package com.example.iaservice;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class escrito extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private Button playButton;
    private Button menuButton; // Nuevo botón

    private MediaPlayer mediaPlayer;

    // Arreglo que contiene los identificadores de los recursos de imagen
    private int[] imageResources = {R.drawable.perro, R.drawable.gato, R.drawable.pajaro};

    // Arreglo que contiene los textos asociados a cada imagen
    private String[] textResources = {"Perro", "Gato", "Pájaro"};

    // Arreglo que contiene los identificadores de los recursos de sonido
    private int[] soundResources = {R.raw.sound_perro, R.raw.sound_gato, R.raw.sound_pajaro};

    private int currentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrito);

        // Inicialización de vistas
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);
        playButton = findViewById(R.id.playButton);
        menuButton = findViewById(R.id.menuButton); // Nuevo botón

        updateUI();

        // Manejador de clics para el botón de reproducción
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
            }
        });

        // Manejador de clics para el nuevo botón de menú
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
    }

    private void updateUI() {
        // Actualizar la imagen y el texto según el índice actual
        imageView.setImageResource(imageResources[currentIndex]);
        textView.setText(textResources[currentIndex]);
    }

    private void playSound() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        // Crear y reproducir el sonido asociado al índice actual
        mediaPlayer = MediaPlayer.create(this, soundResources[currentIndex]);
        mediaPlayer.start();
    }

    private void openMenu() {
        // Iniciar la actividad del menú
        startActivity(new Intent(this, menu.class));
    }

    public void onNextButtonClick(View view) {
        // Incrementar el índice actual y actualizar la UI
        currentIndex = (currentIndex + 1) % imageResources.length;
        updateUI();
    }

    public void onPreviousButtonClick(View view) {
        // Decrementar el índice actual y actualizar la UI
        currentIndex = (currentIndex - 1 + imageResources.length) % imageResources.length;
        updateUI();
    }
}
