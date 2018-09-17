package com.example.arthur.anti_asp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import static java.lang.Math.sqrt;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;

import com.example.arthur.anti_asp.R;

//TODO サンプリングした各状態のデータをまとめてarffを作成し, wekaのデータセットに設定する

public class MainActivity extends Activity implements SensorEventListener {
  private SensorManager manager;
  private TextView values;
  float combine;
  String input_csv;
  String STATUS;

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
        String val  = String.valueOf(combine + ",");
        saveFile("acc_stand.csv", val);
      }
    });
    Button buttonSave_w = findViewById(R.id.button_l_walk);
    buttonSave_w.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String val = String.valueOf(combine + ",");
        saveFile("acc_walk.csv", val);
      }
    });
    Button buttonSave_r = findViewById(R.id.button_l_run);
    buttonSave_r.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String val = String.valueOf(combine + ",");
        saveFile("acc_run.csv", val);
      }
    });

    //学習ボタン: csvからarff生成 & wekaの学習フェーズだけ実行(その部分のcodeだけこの中にべた貼りでいいかも)
    Button button_learn = findViewById(R.id.button_learn);
    button_learn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        CsvToArff("acc_stand.csv", "stand");

        /*weka識別器生成
        try {
          ConverterUtils.DataSource source = new ConverterUtils.DataSource("acc.arff");
          Instances instances = source.getDataSet();
          instances.setClassIndex(1);
          Classifier classifier = new J48();
          classifier.buildClassifier(instances);
        } catch (Exception e) {
          e.printStackTrace();
        }*/
      }

    });

    Button button_estimate = findViewById(R.id.button_estimate);
    button_estimate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //String file = readFile("acc_stand.csv");
        float combined_acc = combine;
        //weka.main(combined_acc);//wekaに現在の3軸加速度(csv?生の値？)を引き渡し, 推定

      }
    });

  }

  //ファイル保存
  public void saveFile(String file, String str/*float value*/) {

    // try-with-resources
    try {
      FileOutputStream fileOutputstream = openFileOutput(file, Context.MODE_APPEND);

      fileOutputstream.write(str.getBytes());
      System.out.println("csv出力が完了しました。");
      Toast.makeText(this, "出力が完了しました", Toast.LENGTH_SHORT).show();

    } catch (IOException e) {
      e.printStackTrace();
    }

  }
  //csv to arff converter
  public void  CsvToArff(String file, String STATUS) {

    // try-with-resources
    try {
      FileInputStream fileInputStream = openFileInput(file);
      BufferedReader reader = new BufferedReader(
              new InputStreamReader(fileInputStream, "UTF-8"));

      StringTokenizer st = new StringTokenizer(file, ",");
      int length = st.countTokens();
      String[] tokens = new String[length];
      for (int i = 0; i < length; i++) {
        tokens[i] = st.nextToken();
      }
      try {

        // 出力ファイルの作成
        FileWriter f = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/temp0917" + "/acc.arff", false);
        //FileOutputStream f = openFileOutput(file, Context.MODE_APPEND);
        PrintWriter p = new PrintWriter(new BufferedWriter(f));

        //FileOutputStream f = openFileOutput(file, Context.MODE_APPEND);
        //fileOutputstream.write(str.getBytes());

        // ヘッダーを指定する
        p.print("@RERATION acceleration");
        p.println();
        p.print("@ATTRIBUTE acc real");
        p.println();
        p.print("@ATTRIBUTE state  {stand,walk,run}");
        p.println();
        p.println();
        p.print("@DATA");
        p.println();

        // 内容をセットする
        for (int i = 0; i < length; i++) {
          p.print(tokens[i]);
          p.print(",");
          p.print(STATUS);
          p.println();//改行
        }

        // ファイルに書き出し閉じる
        p.close();

        System.out.println("ファイル出力完了！");

      } catch (IOException ex) {
        ex.printStackTrace();
        System.out.println("arff出力失敗1");
      }
      System.out.println("arff出力が完了しました");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println("arff出力失敗2");
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
      combine = (float)sqrt(accX*accX + accY*accY + accZ*accZ);

      String str = "加速度センサの値"
              + "\nX: " + accX
              + "\nY: " + accY
              + "\nZ: " + accZ
              + "\n合成加速度: " + combine;
      values.setText(str);
    }
  }

  //外部ストレージに保存するためのパーミッション確認
  private static final int REQUEST_EXTERNAL_STORAGE_CODE = 0x01;
  private static String[] mPermissions = {
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
  };

  private static void verifyStoragePermissions(Activity activity) {
    int readPermission = ContextCompat.checkSelfPermission(activity, mPermissions[0]);
    int writePermission = ContextCompat.checkSelfPermission(activity, mPermissions[1]);

    if (writePermission != PackageManager.PERMISSION_GRANTED ||
            readPermission != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(
              activity,
              mPermissions,
              REQUEST_EXTERNAL_STORAGE_CODE
      );
    }
  }
}

