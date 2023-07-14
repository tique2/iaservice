package com.example.iaservice;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;

public class menu extends Activity {
    Button oral,escrito;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        oral= findViewById(R.id.oral);
        // Crea un objeto MediaPlayer para el botón audio con el sonido que quieres reproducir
        MediaPlayer mp_oral = MediaPlayer.create(this, R.raw.sonido_oral);
        oral.setOnClickListener(view -> {
            // Reproduce el sonido del botón audio
            mp_oral.start();
            Intent intent=new Intent(menu.this, oral.class);
            startActivity(intent);
        });
        escrito= findViewById(R.id.escrito);
        // Crea un objeto MediaPlayer para el botón audio con el sonido que quieres reproducir
        MediaPlayer mp_escrito = MediaPlayer.create(this, R.raw.sonido_escrito);
        escrito.setOnClickListener(view -> {
            // Reproduce el sonido del botón audio
            mp_escrito.start();
            Intent intent=new Intent(menu.this, escrito.class);
            startActivity(intent);
        });
    }
}