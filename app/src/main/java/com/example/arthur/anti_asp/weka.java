package com.example.arthur.anti_asp;
import android.widget.Toast;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.core.converters.CSVLoader;
import com.example.arthur.anti_asp.R;


public class weka {

    public static void main(String[] args, float c_acc) {
        try {
            DataSource source = new DataSource("acc.arff");
            Instances instances = source.getDataSet();
            instances.setClassIndex(1);
            Classifier classifier = new J48();
            classifier.buildClassifier(instances);

            Evaluation eval = new Evaluation(instances);
            eval.evaluateModel(classifier, instances);
            System.out.println(eval.toSummaryString());


            FastVector out = new FastVector(2);
            out.addElement("stand");
            out.addElement("walk");
            out.addElement("run");
            Attribute acc = new Attribute("acc", 0);
            Attribute state = new Attribute("state",out, 1);
            FastVector win = new FastVector(2);

            //分類したいデータを与える
            Instance instance = new DenseInstance(3);
            instance.setValue(acc, c_acc);//acc:MainActivityから引き渡す現在の3軸合成加速度
            instance.setDataset(instances);

            double result = classifier.classifyInstance(instance);
            System.out.println(result);

            if (result == 0){
                //stand
            }else if(result == 1){
                //walk
            }else if(result == 2){
                //run
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}