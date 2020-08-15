package com.example.arthur.pmml_evaluator_Android;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.dmg.pmml.FieldName;
import org.jpmml.android.EvaluatorUtil;
import org.jpmml.evaluator.Evaluator;
import org.jpmml.evaluator.ModelField;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
//import org.jpmml.evaluator.EvaluatorUtil;



public class MainActivity extends Activity {

    //外部ストレージに保存するためのパーミッション確認
    private static final int REQUEST_EXTERNAL_STORAGE_CODE = 0x01;
    private static String[] mPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(".ser viewer");
        Button loadButton = (Button)findViewById(R.id.loadButton);

        View.OnClickListener onClickListener = new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Evaluator evaluator;

                try {
                    evaluator = createEvaluator();
                } catch(Exception e){
                    throw new RuntimeException(e);
                }

                showEvaluatorDialog(evaluator);
            }
        };
        loadButton.setOnClickListener(onClickListener);
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    private void showEvaluatorDialog(Evaluator evaluator){
        List<FieldName> activeFields = getNames(evaluator.getActiveFields());
        List<FieldName> targetFields = getNames(evaluator.getTargetFields());
        List<FieldName> outputFields = getNames(evaluator.getOutputFields());

        StringBuilder sb = new StringBuilder();

        sb.append("Active fields: ").append(activeFields);
        sb.append("\n");
        sb.append("Target fields: ").append(targetFields);
        sb.append("\n");
        sb.append("Output fields: ").append(outputFields);

        TextView textView = new TextView(this);
        textView.setText(sb.toString());

        Dialog dialog = new Dialog(this);
        dialog.setContentView(textView);
        dialog.show();
    }

    private Evaluator createEvaluator() throws Exception {
        AssetManager assetManager = getAssets();

        try(InputStream is = new FileInputStream(Environment.getExternalStorageDirectory().getPath() + "/Download/pmml_model/ser/scikit_learn_models_test_trigger075s_newFeatures.pmml.ser")){
            return EvaluatorUtil.createEvaluator(is);

        }
    }

    static
    private List<FieldName> getNames(List<? extends ModelField> modelFields){
        List<FieldName> names = new ArrayList<>(modelFields.size());

        for(ModelField modelField : modelFields){
            FieldName name = modelField.getName();

            names.add(name);
        }

        return names;
    }

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