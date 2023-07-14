package com.example.iaservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Crea un objeto MediaPlayer con el sonido que quieres reproducir
        MediaPlayer mp = MediaPlayer.create(this, R.raw.sonido_cargando);
        // Inicia la reproducción del sonido
        mp.start();
        TimerTask tarea=new TimerTask() {
            @Override
            public void run() {
                Intent intent=new Intent(MainActivity.this, menu.class);
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo=new Timer();
        tiempo.schedule(tarea,5000);
    }
}
//oral y escrito