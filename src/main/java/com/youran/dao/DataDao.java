package com.youran.dao;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youran.entiy.Data;

public interface DataDao {
    void addData(Data data);
    JSONArray selectData();
    JSONObject selectDataById(String id);
    void delData(String id);
}
