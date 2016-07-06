package com.thushear.mahout;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;

import java.io.*;
import java.util.List;

/**
 * Created by kongming on 2016/7/6.
 */
public class SlopeOneRecommenderTest {

    private static String inputPath = "data/movies/ratings.dat";

    private static String outputPath = "data/movies/ratings.csv";

    private static void createCSVFile(String inputPath,String outputPath,String splitter) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(inputPath));
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));

        String line ;
        String lineWrite;
        String temp[];

        int i = 0;
        while ((line = reader.readLine())!=null && i <= 1000){
            i++;
            temp = line.split(splitter);
            lineWrite = temp[0] + "," + temp[1];
            writer.write(lineWrite);
            writer.newLine();
            writer.flush();
        }

        reader.close();
        writer.close();

    }




    public static void main(String[] args) throws IOException, TasteException {
        createCSVFile(inputPath,outputPath,"::");

        // create data source (model) - from the csv file
        File ratingsFile = new File(outputPath);
        DataModel model = new FileDataModel(ratingsFile);

        // create a simple recommender on our data
        CachingRecommender cachingRecommender = new CachingRecommender(new SlopeOneRecommender(model));
        // for all users
        for (LongPrimitiveIterator it = model.getUserIDs();it.hasNext();){
            long userId = it.nextLong();
            // get the recommendations for the user
            List<RecommendedItem> recommenations = cachingRecommender.recommend(userId,10);
            // if empty write something
            if (recommenations.size() == 0){
                System.out.print("user ");
                System.out.print(userId);
                System.out.println(": no recommendations");
            }
            // print the list of recommendations for each
            for (RecommendedItem recommenation : recommenations) {
                System.out.print("user ");
                System.out.print(userId);

                System.out.print(":");
                System.out.println(recommenation);
            }

        }

//
//        System.out.println(new File("").getAbsolutePath());
//        System.out.println(new File("/").getAbsolutePath());
//        System.out.println(SlopeOneRecommenderTest.class.getResource("/").getPath());
    }






}
