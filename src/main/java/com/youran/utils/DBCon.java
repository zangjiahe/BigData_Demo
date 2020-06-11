package com.youran.utils;

import java.sql.*;

public class DBCon {
    Connection con;

    public Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1/bigdata?useUnicode=true&characterEncoding=utf-8&useSSL=false",
                    "root", "123456");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        return con;
    }
    public static void MyClose(ResultSet rs, PreparedStatement ps, Connection con) {
        try {
            if (rs != null) {
                rs.close();
            }

            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
