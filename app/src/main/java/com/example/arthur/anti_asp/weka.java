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

           /*
            FastVector out = new FastVector(3);
            out.addElement("stand");
            out.addElement("overcast");
            out.addElement("rainy");
           */
            Attribute acc = new Attribute("acc", 0);

            //分類したいデータを与える
            Instance instance = new DenseInstance(2);
            instance.setValue(acc, c_acc);//acc:MainActivityから引き渡す現在の3軸合成加速度
            instance.setDataset(instances);

            double result = classifier.classifyInstance(instance);
            System.out.println(result);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}