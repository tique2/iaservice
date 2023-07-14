package com.example.iaservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class oral extends Activity {
    // Declaración de variables de instancia para los botones y el EditText
    Button menu, compartir, escuchar;
    EditText traductor;

    // Variable de instancia para TextToSpeech
    private TextToSpeech tts;

    // Variables de instancia para MediaPlayer
    private MediaPlayer mediaPlayerMenu;
    private MediaPlayer mediaPlayerCompartir;
    private MediaPlayer mediaPlayerEscuchar;

    // Constante para el código de solicitud de reconocimiento de voz
    private static final int RECOGNIZE_SPEECH_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oral);

        // Inicialización de las variables de instancia para los botones y el EditText
        menu = findViewById(R.id.menu);
        compartir = findViewById(R.id.compartir);
        escuchar = findViewById(R.id.escuchar);
        traductor = findViewById(R.id.traductor);

        // Inicialización de las instancias de MediaPlayer para cada botón
        // Reemplaza "R.raw.sonido_menu", "R.raw.sonido_compartir" y "R.raw.sonido_escuchar" con los nombres de tus archivos de audio en formato MP3
        mediaPlayerMenu = MediaPlayer.create(this, R.raw.sonido_menu);
        mediaPlayerCompartir = MediaPlayer.create(this, R.raw.sonido_compartir);
        mediaPlayerEscuchar = MediaPlayer.create(this, R.raw.sonido_escuchar);

        // Establecimiento del OnClickListener para el botón "menu"
        menu.setOnClickListener(view -> {
            // Reproducción del sonido personalizado para el botón "menu"
            reproducirSonido(mediaPlayerMenu);

            Intent intent = new Intent(oral.this, menu.class);
            startActivity(intent);
        });

        // Establecimiento del OnClickListener para el botón "compartir"
        compartir.setOnClickListener(view -> {
            // Reproducción del sonido personalizado para el botón "compartir"
            reproducirSonido(mediaPlayerCompartir);

            // Creación del intent para compartir texto
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, traductor.getText().toString());
            sendIntent.setType("text/plain");

            // Inicio de la actividad para compartir texto
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        // Establecimiento del OnClickListener para el botón "escuchar"
        escuchar.setOnClickListener(view -> {
            // Reproducción del sonido personalizado para el botón "escuchar"
            reproducirSonido(mediaPlayerEscuchar);

            // Obtención del texto del EditText "traductor"
            String text = traductor.getText().toString();

            // Reproducción rápida del texto en voz alta usando TextToSpeech
            reproducirTexto(text);
        });

        // Inicialización de la variable de instancia para TextToSpeech
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                // Verificar si el paquete de voz está disponible
                int availability = tts.isLanguageAvailable(Locale.getDefault());
                if (availability == TextToSpeech.LANG_MISSING_DATA || availability == TextToSpeech.LANG_NOT_SUPPORTED) {
                    // Paquete de voz no soportado o faltante, mostrar error
                    Log.e("TTS", "Language not supported or missing data");
                } else {
                    // Establecer configuraciones adicionales para la voz
                    tts.setPitch(1.0f); // Ajustar el tono de la voz (rango de 0.5 a 2.0)
                    tts.setSpeechRate(1.0f); // Ajustar la velocidad de habla (rango de 0.5 a 2.0)
                }
            } else {
                // Error en la inicialización del TextToSpeech
                Log.e("TTS", "Initialization failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificación del código de solicitud y del resultado
        if (requestCode == RECOGNIZE_SPEECH_ACTIVITY) {
            if (resultCode == RESULT_OK && data != null) {
                // Obtención del texto reconocido por el reconocimiento de voz
                ArrayList<String> speech = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                String strSpeech2Text = speech.get(0);

                // Establecimiento del texto en el EditText "traductor"
                traductor.setText(strSpeech2Text);
            }
        }
    }

    public void voz(View view) {
        // Verificar la conectividad a internet
        boolean isConnected = isInternetConnected();
        if (isConnected) {
            // Si hay conexión a internet, usar reconocimiento de voz en línea
            iniciarReconocimientoVozEnLinea();
        } else {
            // Si no hay conexión a internet, usar reconocimiento de voz sin conexión
            iniciarReconocimientoVozSinConexion();
        }
    }

    private boolean isInternetConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    private void iniciarReconocimientoVozEnLinea() {
        Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        String language = Locale.getDefault().getLanguage();
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);

        try {
            startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), R.string.error_intent, Toast.LENGTH_LONG).show();
        }
    }

    private void iniciarReconocimientoVozSinConexion() {
        Intent intentActionRecognizeSpeech = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        String language = Locale.getDefault().getLanguage();
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, language);
        intentActionRecognizeSpeech.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);

        try {
            startActivityForResult(intentActionRecognizeSpeech, RECOGNIZE_SPEECH_ACTIVITY);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), R.string.error_intent, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }

        if (mediaPlayerMenu != null) {
            mediaPlayerMenu.release();
            mediaPlayerMenu = null;
        }

        if (mediaPlayerCompartir != null) {
            mediaPlayerCompartir.release();
            mediaPlayerCompartir = null;
        }

        if (mediaPlayerEscuchar != null) {
            mediaPlayerEscuchar.release();
            mediaPlayerEscuchar = null;
        }

        super.onDestroy();
    }

    private void reproducirSonido(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    private void reproducirTexto(String texto) {
        if (tts != null) {
            Bundle params = new Bundle();
            params.putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "uniqueId");

            // Establecer las marcas de puntuación en el texto
            StringBuilder modifiedText = new StringBuilder();
            for (char c : texto.toCharArray()) {
                if (c == '.' || c == ',' || c == '?' || c == '!') {
                    modifiedText.append(c).append(" ");
                } else {
                    modifiedText.append(c);
                }
            }

            // Configurar las pausas entre las frases
            String[] sentences = modifiedText.toString().split("[.!?]");
            StringBuilder utteranceId = new StringBuilder();
            for (int i = 0; i < sentences.length; i++) {
                utteranceId.append("sentence").append(i);
                if (i < sentences.length - 1) {
                    utteranceId.append(",");
                }
            }

            tts.speak(modifiedText.toString(), TextToSpeech.QUEUE_FLUSH, params, utteranceId.toString());
        }
    }
}