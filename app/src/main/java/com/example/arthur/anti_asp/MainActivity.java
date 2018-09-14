package com.example.arthur.anti_asp;

import java.util.List;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.arthur.anti_asp.R;

public class MainActivity extends Activity implements SensorEventListener {
    private SensorManager manager;
  private TextView values;
  /** Called when the activity is first created. */
          @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    values = (TextView)findViewById(R.id.text_view);
    manager = (SensorManager)getSystemService(SENSOR_SERVICE);
  }

  @Override
  protected void onStop() {
    // TODO Auto-generated method stub
    super.onStop();
    // Listenerの登録解除
    manager.unregisterListener(this);
  }

  @Override
  protected void onResume() {
    // TODO Auto-generated method stub
    super.onResume();
    // Listenerの登録
    List<Sensor> sensors = manager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    if(sensors.size() > 0) {
      Sensor s = sensors.get(0);
      manager.registerListener(this, s, SensorManager.SENSOR_DELAY_UI);
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    // TODO Auto-generated method stub
    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      String str = "加速度センサの値"
          + "\nX: " + event.values[SensorManager.DATA_X]
          + "\nY: " + event.values[SensorManager.DATA_Y]
          + "\nZ: " + event.values[SensorManager.DATA_Z];
      values.setText(str);
    }
  }
}