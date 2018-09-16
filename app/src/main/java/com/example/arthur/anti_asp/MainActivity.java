package com.example.arthur.anti_asp;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import static java.lang.Math.sqrt;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import com.example.arthur.anti_asp.R;

//TODO サンプリングした各状態のデータをまとめてarffを作成し, wekaのデータセットに設定する



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


    Button buttonSave_s = findViewById(R.id.button_l_stand);
    buttonSave_s.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //String text = editText.getText().toString();
        String text = "This is values of stand";
        saveFile("acc_stand", text);
      }
    });
    Button buttonSave_w = findViewById(R.id.button_l_walk);
    buttonSave_w.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = "This is values of stand";
        saveFile("acc_walk", text);
      }
    });
    Button buttonSave_r = findViewById(R.id.button_l_run);
    buttonSave_r.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String text = "This is values of stand";
        saveFile("acc_run", text);
      }
    });

      Button button_estimate = findViewById(R.id.button_estimate);
      button_estimate.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              //weka.main(XXXXX.csv);//wekaに現在の3軸加速度を引き渡し, 推定

          }
      });
  }



  //ファイル保存
  public void saveFile(String file, String str) {

    // try-with-resources
    try (FileOutputStream fileOutputstream = openFileOutput(file,
            Context.MODE_PRIVATE);){

      fileOutputstream.write(str.getBytes());
      System.out.println("csv出力が完了しました。");
      Toast.makeText(this, "出力が完了しました", Toast.LENGTH_SHORT).show();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  @Override
  protected void onStop() {
    super.onStop();
    // Listenerの登録解除
    manager.unregisterListener(this);
  }

  @Override
  protected void onResume() {
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

  }

  @Override
  public void onSensorChanged(SensorEvent event) {
    if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
      float accX = event.values[SensorManager.DATA_X];
      float accY = event.values[SensorManager.DATA_Y];
      float accZ = event.values[SensorManager.DATA_Z];
      float combine = (float)sqrt(accX*accX + accY*accY + accZ*accZ);

      String str = "加速度センサの値"
              + "\nX: " + accX
              + "\nY: " + accY
              + "\nZ: " + accZ
              + "\n合成加速度: " + combine;
      values.setText(str);
    }
  }

  }

