package com.youran.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.MAC;
import com.sun.deploy.net.URLEncoder;
import com.youran.entiy.Data;
import com.youran.service.DataService;
import com.youran.service.Impl.DataServiceImpl;
import com.youran.utils.IOUtils;
import com.youran.utils.WordCount;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "data", urlPatterns = "/data")
public class DataControll extends HttpServlet {
    JSONObject result =new JSONObject();
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
//        req.setCharacterEncoding("utf-8");
        String method = req.getParameter("method");
        if ("del".equals(method)) {
            delData(req, resp);
        }
        if ("query".equals(method)) {
            queryData(req, resp);
        }
        if ("toResult".equals(method)) {
            toResult(req, resp);
        }
        if ("start".equals(method)) {
            try {
                starmr(req, resp);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if ("downloadFile".equals(method)) {
            downloadFile(req, resp);
        }
    }

    private void downloadFile(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String filename = new Date().getTime() + ".txt";
        IOUtils.copy(req.getParameter("path") + "\\part-r-00000",
                "H:\\git\\BigData_Demo\\src\\main\\resources\\temp\\" + filename);
        String filePath = "H:\\git\\BigData_Demo\\src\\main\\resources\\temp\\" + filename;
        File file = new File(filePath);
        if (file.exists()) {
            //设置响应头
            resp.setHeader("content-disposition", "attachment;filename=" + filename);
            //创建文件输入流
            FileInputStream is = new FileInputStream(filePath);
            //创建输出流
            OutputStream os = resp.getOutputStream();
            //创建缓冲区
            byte buffer[] = new byte[1024];
            int len = 0;
            //写数据到浏览器
            while ((len = is.read(buffer)) > 0) {
                os.write(buffer, 0, len);
            }
            //关闭流
            is.close();
            os.close();
        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }

    }

    private void starmr(HttpServletRequest req, HttpServletResponse resp) throws IOException, ClassNotFoundException, InterruptedException, ServletException {
        System.out.println(req.getParameter("input") + req.getParameter("out"));
        startJob(req.getParameter("input"), req.getParameter("out"));
        Map<String, String> map = getSussessResult(req.getParameter("out"));
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            System.out.println(mapKey + ":" + mapValue);
        }
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
        System.out.println(json);
        resp.setContentType("application/json;charset=utf-8");
        System.out.println("result:"+result);
        resp.getWriter().write(result + "");

//        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }

    //添加数据
    private void delData(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String id = req.getParameter("jobid");
        System.out.println(req.getParameter("jobid"));
        DataService dataService = new DataServiceImpl();
        dataService.delData(id);
        req.getRequestDispatcher("index.jsp").forward(req, resp);


    }

    //    根据Id查询
    private void toResult(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        System.out.println(getURLDecoderString(req.getParameter("out")));
        Map<String, String> map = getSussessResult(getURLDecoderString(req.getParameter("out")));
        JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(json + "");

    }

    //    查询所有
    private void queryData(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        JSONObject jsonObject = new JSONObject();
        DataService dataService = new DataServiceImpl();
        jsonObject.put("status", 200);
        jsonObject.put("data", dataService.selectData());
        resp.setContentType("application/json;charset=utf-8");
        resp.getWriter().write(jsonObject + "");


    }

    public void startJob(final String inputpath, final String outputpath) throws InterruptedException, IOException, ClassNotFoundException {
        // 设置windows中的环境变量
        System.out.println(inputpath + "," + outputpath);
        System.setProperty("hadoop.home.dir", "C:/hadoop2.7.2");
        Configuration conf = new Configuration();
        conf.set("mapreduce.cluster.local.dir", "C:/hadoop2.7.2");
        final String in = inputpath;
        final String out = outputpath;
        // 实例化一个job
        final Job job = Job.getInstance(conf);
        new Thread(new Runnable() {

            public void run() {
                while (true) {
                    try {

//                        System.out.println(job.getStatus().getJobID());
//                        System.out.println(job.getStatus().getReduceProgress());
//                        System.out.println(job.getJobState());

                        if (job.getJobState().toString().equals("SUCCEEDED")) {
                            System.out.println("统计结束成功");
                            //插入数据库中，状态为成功，务必要保存出参路径
                            DataService dataService = new DataServiceImpl();
                            Data data = new Data(job.getStatus().getJobID() + "", job.getJobState() + "", in, out);
                            System.out.println(data);
                            result.put("jobid",data.getJobId());
                            result.put("inputpath",data.getInput());
                            result.put("jobstatus",data.getJobStatus());
                            result.put("outputpath",data.getOut());
                            dataService.addData(data);
                            break;
                        } else if (job.getJobState().toString().equals("ERROR") || job.getJobState().toString().equals("FAILED")) {
                            DataService dataService = new DataServiceImpl();
                            Data data = new Data(job.getStatus().getJobID() + "", job.getJobState() + "", in, out);
                            System.out.println(data);
                            dataService.addData(data);
                            System.out.println("统计结束失败");
                            //插入数据库中，状态为失败
                            break;
                        }
                        Thread.sleep(1000);
                    } catch (Exception e) {

                    }
                }

            }
        }).start();
        // 指定本次mr job jar包运行主类
        job.setJarByClass(WordCount.class);
        job.setMapperClass(WordCount.TokenizerMapper.class);
        job.setCombinerClass(WordCount.IntSumReducer.class);
        job.setReducerClass(WordCount.IntSumReducer.class);

        // 指定本次mr 最终输出的 k v类型
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        // 设置输入路径和输出路径，都是接受命令行参数
        FileInputFormat.addInputPath(job, new Path(inputpath));
        FileOutputFormat.setOutputPath(job, new Path(outputpath));

//            FileInputFormat.addInputPath(job, new Path(args[0]));
//            FileOutputFormat.setOutputPath(job, new Path(args[1]));
        // 提交程序
        job.waitForCompletion(true);
    }

    public Map<String, String> getSussessResult(String path) {
        Map<String, String> map = new HashMap<>();
        String filePath = path + "/part-r-00000";
        File file = new File(filePath);
        BufferedReader reader = null;
        String tempString = null;
        int line = 1;
        try {
            // 以行为单位读取文件内容，一次读一行
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));

            while ((tempString = reader.readLine()) != null) {
                String[] tmp = tempString.split("\t");
                map.put(tmp[0], tmp[1]);
//                JSONObject json = JSONObject.parseObject(JSON.toJSONString(map));
//                jsonRsult.add(json);
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        return map;
    }

    public static String getURLDecoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
