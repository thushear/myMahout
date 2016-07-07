package com.thushear.mahout;

import org.apache.hadoop.fs.Path;
import org.apache.mahout.classifier.ClassifierResult;
import org.apache.mahout.classifier.bayes.*;
import org.apache.mahout.classifier.naivebayes.BayesUtils;
import org.apache.mahout.common.nlp.NGrams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Created by kongming on 2016/7/7.
 */
public class NaiveBayesClassifier {


    public static void main(String[] args) {

        final BayesParameters params = new BayesParameters();
        params.setGramSize(1);
        params.set("verbose","true");
        params.set("classifierType","bayes");
        params.set("defaultCat","OTHER");
        params.set("encoding","UTF-8");
        params.set("alpha_i","1.0");
        params.set("datasource","hdfs");
        params.set("basePath","/tmp/output");

        Path input = new Path("/tmp/input");
        Path output = new Path("/tmp/output");

        try {
            TrainClassifier.trainNaiveBayes(input,output,params);
            Algorithm algorithm = new BayesAlgorithm();
            Datastore datastore = new InMemoryBayesDatastore(params);
            ClassifierContext classifierContext = new ClassifierContext(algorithm,datastore);
            classifierContext.initialize();

            final BufferedReader reader = new BufferedReader(new FileReader(args[0]));
            String entry = reader.readLine();

            while (entry!=null){
                List<String> document = new NGrams(entry,Integer.parseInt(params.get("gramSize")))
                        .generateNGramsWithoutLabel();
                ClassifierResult result = classifierContext.classifyDocument(
                        document.toArray( new String[ document.size() ] ),
                        params.get( "defaultCat" ) );

                entry = reader.readLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidDatastoreException e) {
            e.printStackTrace();
        }


    }









}
