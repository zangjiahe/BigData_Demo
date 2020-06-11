package com.youran.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youran.entiy.Data;

public interface DataService {
    void addData(Data data);
    JSONArray selectData();
    JSONObject selectDataById(String id);
    void delData(String id);
}
