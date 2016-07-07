package com.thushear.mahout;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by kongming on 2016/7/6.
 */
public class CreateSequenceFileFromArtists {


    public static void main(String[] args) throws IOException {
        String inputPath = "data/lastfm/original/Lastfm-ArtistTags2007/artists.txt";
        String outputPath =  "data/lastfm/sequencesfiles/part-0000";
        Path path = new Path(outputPath);

        BufferedReader br = new BufferedReader(new FileReader(inputPath));
        //creating Sequence Writerr
        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);
        SequenceFile.Writer writer = new SequenceFile.Writer(fs,conf,path, LongWritable.class, Text.class);

        String line  = null;
        String[] temp;
        String tempValue = new String();
        String delimiter = " ";
        LongWritable key = new LongWritable();
        Text value = new Text();
        long tempkey = 0;
        while (line != null){

            line = br.readLine();
            temp = line.split(delimiter);
            value = new Text();
            tempValue = "";
            for (int i = 1;i<temp.length;i++) {
                tempValue += temp[i] + delimiter;
            }
            value = new Text(tempValue);
            System.out.println("writing key/value  " + key.toString() + "/" + value.toString());
            tempkey++;
            key = new LongWritable(tempkey);
            writer.append(key,value);
        }



        writer.close();





    }

}
