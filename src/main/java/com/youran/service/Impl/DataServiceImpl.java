package com.youran.service.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youran.dao.DataDao;
import com.youran.dao.Impl.DataDaoImpl;
import com.youran.entiy.Data;
import com.youran.service.DataService;

public class DataServiceImpl implements DataService {
    @Override
    public void addData(Data data) {
        DataDao dataDao=new DataDaoImpl();
        dataDao.addData(data);
    }

    @Override
    public JSONArray selectData() {
        DataDao dataDao=new DataDaoImpl();
        return dataDao.selectData();
    }

    @Override
    public JSONObject selectDataById(String id) {
        DataDao dataDao=new DataDaoImpl();
        return dataDao.selectDataById(id);
    }

    @Override
    public void delData(String id) {
        DataDao dataDao=new DataDaoImpl();
        dataDao.delData(id);
    }

    @Override
    public long checkTime(String timestmap) {
        DataDao dataDao=new DataDaoImpl();

        return dataDao.checkTime(timestmap);
    }
}
