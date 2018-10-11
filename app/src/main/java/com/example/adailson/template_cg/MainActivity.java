package com.example.adailson.template_cg;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    //Variavel de referencia para o objeto de desenho
    GLSurfaceView superficieDesenho = null;
    Renderizador render = null;
    public static float sensorDirecaoX;
    public static float sensorDirecaoY;

    private Sensor accelerometer;
    private SensorManager sensorManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //valida a var de referencia para a superficie
        superficieDesenho = new GLSurfaceView(this);

        //Valida a var de referencia para o renderizador
        render = new Renderizador(this);

        //Associa o renderizador a superficie de desenho da tela.
        superficieDesenho.setRenderer(render);
        superficieDesenho.setOnTouchListener(render);

        //Publica a sup de desenho na tela do app
        setContentView(superficieDesenho);


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];


        sensorDirecaoX = sensorEvent.values[0];
        sensorDirecaoY = sensorEvent.values[1];

//        if (x < 0) {
//            Log.i("SENSOR", "DIREITA");
//            sensorDirecaoX = 100;
//
//        }
//        if (x > 0) {
//            Log.i("SENSOR", "ESQUERDA");
//            sensorDirecaoX = -100;
//        }
//        if (y < 0) {
//            Log.i("SENSOR", "frente baixa");
//        }
//        if (y > 0) {
//            Log.i("SENSOR", "frente alta");
//        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
