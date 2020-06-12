<%--
  Created by IntelliJ IDEA.
  User: zangj
  Date: 2020/6/12
  Time: 8:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<html>
<head>
    <title>结果页</title>
</head>
<%--<script src="js/highcharts.js"></script>--%>
<script src="http://cdn.staticfile.org/jquery/2.1.4/jquery.min.js"></script>
<script src="http://cdn.hcharts.cn/highcharts/highcharts.js"></script>

<body>
<div id="container" style="width: 550px; height: 400px; margin: 0 auto"></div>
<%
    //获得当前url
    StringBuffer url = new StringBuffer(request.getScheme() + "://" + request.getServerName()
            + request.getRequestURI());

//判断当前url是否有参数
    if(request.getQueryString()!=null && !"".equals(request.getQueryString())){
        url.append("?" + request.getQueryString());
    }
    System.out.println(request.getQueryString());
    String path=request.getQueryString().split("=")[1];

%>
<script language="JavaScript">
    $(document).ready(function() {
           $.ajax({
            url: '/BigData_Demo_war_exploded/data?method=toResult',
            type: 'post',
            datatype: 'json',
            data:{
                out:"<%out.print(path);%>"
            },
            success: function (res) {
                $('#container').highcharts(res);
            }
    })



    });
</script>
</body>
</html>
