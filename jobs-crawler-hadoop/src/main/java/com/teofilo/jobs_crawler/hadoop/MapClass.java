package com.teofilo.jobs_crawler.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
 
public class MapClass extends Mapper<Object, Text, Text, IntWritable> {
 
    private final IntWritable ONE = new IntWritable(1);
    private Text word = new Text();
    
    private Text keyWord = new Text();
    private Float salary;
 
    public void map(Object key, Text value, Context context)
            throws IOException, InterruptedException {
 
		String line = value.toString();
		StringTokenizer st = new StringTokenizer(line," ");
		
		while(st.hasMoreTokens()){
			word.set(st.nextToken());
			context.write(word,ONE);
		}
		
//		StringTokenizer st = new StringTokenizer(line,"|");
//		
//		keyWord.set(st.nextToken());
//		salary = (Float.parseFloat(st.nextToken()));
//		context.write(keyWord,salary);
				
    }
}