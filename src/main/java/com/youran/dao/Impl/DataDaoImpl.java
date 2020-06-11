package com.youran.dao.Impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.youran.dao.DataDao;
import com.youran.entiy.Data;
import com.youran.utils.DBCon;

import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

import static com.youran.utils.DBCon.MyClose;

public class DataDaoImpl implements DataDao {
    private DBCon db;
    private Connection con;
    private PreparedStatement ps;
    private ResultSet rs;
    public DataDaoImpl() {
        super();
        db = new DBCon();
        con = db.getConnection();
    }
    @Override
    public void addData(Data data) {
        try {
            ps = con.prepareStatement("insert into t_bigdata (id,jobid,jobstatus,input,outpath) values (null,?,?,?,?)");
            ps.setString(1, data.getJobId());
            ps.setString(2, data.getJobStatus());
            ps.setString(3, data.getInput());
            ps.setString(4, data.getOut());

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyClose(rs, ps, con);
        }
    }

    @Override
    public JSONArray selectData() {
        JSONArray jsonArray=new JSONArray();
        try {
            ps = con.prepareStatement("select * from t_bigdata");
            rs = ps.executeQuery();
            while (rs.next()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("jobid",rs.getString("jobid"));
                jsonObject.put("jobstatus",rs.getString("jobstatus"));
                jsonObject.put("inputpath",rs.getString("input"));
                jsonObject.put("outputpath",rs.getString("outpath"));
                String out=rs.getString("outpath")+"\\part-r-00000";
                jsonObject.put("url", URLEncoder.encode( out, "gbk" ));
                jsonArray.add(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyClose(rs, ps, con);
        }

        return jsonArray;
    }

    @Override
    public JSONObject selectDataById(String id) {
        return null;
    }

    @Override
    public void delData(String id) {
        try {
            ps = con.prepareStatement("DELETE  from t_bigdata where jobid = ?");
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            MyClose(rs, ps, con);
        }
    }
}
