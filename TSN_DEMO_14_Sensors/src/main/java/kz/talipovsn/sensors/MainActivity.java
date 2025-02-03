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
    private Sensor compass; // Датчик компаса
    private Sensor light; // Датчик освещенности
    private float lightValue; // Значение освещенности
    private int compassValue;   // Значение компаса
    private TextView textView;

    // Задаем метод, вызываемый при создании активности
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
        // Подключаемся к датчику компаса
        compass = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
    }

    // Обработчик событий датчиков
    private SensorEventListener sensorlistener = new SensorEventListener() {
        // Метод, вызываемый при изменении значений датчика
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT ) {
                lightValue = event.values[0]; // Получаем значение освещенности
            }
            if (event.sensor.getType() == Sensor.TYPE_ORIENTATION ) {
                compassValue = Math.round(event.values[0]); // Получаем значение компаса
            }
            textView.setText(String.format("%s %s %s \n %s %s", getString(R.string.compass),
                    compassValue, getString(R.string.degrees), "LIGHT:", lightValue));
        }

        // Метод, вызываемый при изменении точности датчика
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    // Задаем метод, вызываемый при возвращении программы в активный режим
    // @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorlistener, light, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(sensorlistener, compass, SensorManager.SENSOR_DELAY_NORMAL);
    }

    // Задаем метод, вызываемый при приостановке работы программы
    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorlistener);
    }

}
