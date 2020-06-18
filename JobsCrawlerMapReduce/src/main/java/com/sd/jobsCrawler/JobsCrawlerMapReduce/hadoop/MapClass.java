package com.sd.jobsCrawler.JobsCrawlerMapReduce.hadoop;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;

public class MapClass extends Mapper<Object, Text, Text, FloatWritable> {

	private Text keyWord = new Text();
	private FloatWritable salary = new FloatWritable();

	public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

		String line = value.toString();
		StringTokenizer st = new StringTokenizer(line, "|");

		try {
			this.keyWord.set(st.nextToken());
			this.salary.set(Float.parseFloat(st.nextToken().replace(',', '.')));
			System.out.println(this.keyWord + " - "+this.salary.get());
			context.write(this.keyWord, this.salary);
		} catch (Exception e) {
			System.out.println("Error Parsing on map: " + e);
		}
	}
}