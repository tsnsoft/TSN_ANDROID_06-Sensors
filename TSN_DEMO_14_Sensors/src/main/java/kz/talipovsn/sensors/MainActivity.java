package kz.talipovsn.sensors;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;

import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager; // Менеджер датчиков
    private Sensor compass; // Датчик ориентации (компаса)
    private Sensor light; // Датчик освещенности
    private float lightValue; // Значение освещенности
    private int compassValue; // Значение компаса (0..360 градусов)
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Установка темы
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);

        textView = findViewById(R.id.textView); // Получаем ссылку на элемент TextView

        // Создаем менеджер датчиков
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // Подключаемся к датчику освещенности
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // Подключаемся к современному датчику вектора вращения вместо устаревшего TYPE_ORIENTATION
        compass = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    // Обработчик событий датчиков
    private final SensorEventListener sensorlistener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                lightValue = event.values[0]; // Получаем значение освещенности
            }
            if (event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
                float[] rotationMatrix = new float[9];
                float[] orientation = new float[3];
                // Переводим данные датчика в углы поворота
                SensorManager.getRotationMatrixFromVector(rotationMatrix, event.values);
                SensorManager.getOrientation(rotationMatrix, orientation);
                // Переводим радианы в градусы и приводим к диапазону 0..360
                float azimuthInDegrees = (float) Math.toDegrees(orientation[0]);
                compassValue = Math.round((azimuthInDegrees + 360) % 360);
            }
            textView.setText(String.format("%s %s %s \n %s %s", getString(R.string.compass),
                    compassValue, getString(R.string.degrees), "LIGHT:", lightValue));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (light != null) {
            mSensorManager.registerListener(sensorlistener, light, SensorManager.SENSOR_DELAY_NORMAL);
        }
        if (compass != null) {
            mSensorManager.registerListener(sensorlistener, compass, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorlistener);
    }
}