package com.youran.entiy;

import org.apache.hadoop.mapreduce.JobID;

import java.io.Serializable;

public class Data implements Serializable {
    private String jobId;//id
    private String jobStatus;//状态
    private String input;//入参路径
    private String out;//出参路径

    public Data() {
    }

    public Data(String jobId, String jobStatus, String input, String out) {
        this.jobId = jobId;
        this.jobStatus = jobStatus;
        this.input = input;
        this.out = out;
    }

    @Override
    public String toString() {
        return "Data{" +
                "jobId='" + jobId + '\'' +
                ", jobStatus='" + jobStatus + '\'' +
                ", input='" + input + '\'' +
                ", out='" + out + '\'' +
                '}';
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public String getInput() {
        return input;
    }

    public String getOut() {
        return out;
    }
}
