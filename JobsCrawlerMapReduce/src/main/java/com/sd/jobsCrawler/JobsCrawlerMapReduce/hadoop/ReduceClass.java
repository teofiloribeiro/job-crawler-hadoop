package com.sd.jobsCrawler.JobsCrawlerMapReduce.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReduceClass extends Reducer<Text, FloatWritable, Text, FloatWritable> {

	public void reduce(Text text, Iterable<FloatWritable> values, Context context)
			throws IOException, InterruptedException {
		
		Float median = 0.0f;
		for (FloatWritable value : values) {
			median += (value.get() + median) / 2;
		}
		context.write(text, new FloatWritable(median));
	}
}
