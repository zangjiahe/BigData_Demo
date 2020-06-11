package com.youran.utils;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WordCount {
	public static void main(String[] args) throws Exception {
		try {
			System.setProperty("hadoop.home.dir", "C:/hadoop2.7.2");
			Configuration conf = new Configuration();
			conf.set("mapreduce.cluster.local.dir", "C:/hadoop2.7.2");

			final Job job = Job.getInstance(conf);
			new Thread(new Runnable() {

				public void run() {
					while (true) {
						try {
							
							System.out.println(job.getStatus().getJobID());
							System.out.println(job.getStatus().getReduceProgress());
							System.out.println(job.getJobState());
							if(job.getJobState().toString().equals("SUCCEEDED"))
							{
								break;
							}
							else  if(job.getJobState().toString().equals("ERROR")||job.getJobState().toString().equals("FAILED"))
							{
								
								break;
							}
							Thread.sleep(1000);
						} catch (Exception e) {

						}
					}

				}
			}).start();
			job.setJarByClass(WordCount.class);

			job.setMapperClass(TokenizerMapper.class);
			job.setCombinerClass(IntSumReducer.class);
			job.setReducerClass(IntSumReducer.class);

			job.setOutputKeyClass(Text.class);
			job.setOutputValueClass(IntWritable.class);
			FileInputFormat.addInputPath(job, new Path("D:\\ali home\\��ʱ\\seq10w"));
			FileOutputFormat.setOutputPath(job, new Path("C:\\Users\\zhangli\\Desktop\\50w10"));

//            FileInputFormat.addInputPath(job, new Path(args[0]));
//            FileOutputFormat.setOutputPath(job, new Path(args[1]));
			job.waitForCompletion(true);


		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values,
				Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			this.result.set(sum);
			context.write(key, this.result);
		}
	}

	public static class TokenizerMapper extends Mapper<Object, Text, Text, IntWritable> {
		private static final IntWritable one = new IntWritable(1);

		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
//			// 分割这条记录
//			String[] words = line.split(" ");
//			for (String word : words) {
//				// 使用mr程序的上下文context 把mapper阶段处理的数据发送出去,作为reduce节点的输入数据
//				context.write(new Text(word), one);
//			}
			String[] words = line.split("\\|");

			int id = Integer.parseInt(words[0]);

			String keyid = "";
			if (id > 0 && id <= 10000) {
				keyid = "0~10000";
			} else if (id > 10000 && id <= 20000) {
				keyid = "10001~20000";
			} else if (id > 20000 && id <= 30000) {
				keyid = "20001~30000";
			} else if (id > 30000 && id <= 40000) {
				keyid = "30001~40000";
			} else if (id > 40000 && id <= 50000) {
				keyid = "40001~50000";
			} else {
				keyid = "50001~";
			}

			context.write(new Text(keyid), one);
		}
	}
}
