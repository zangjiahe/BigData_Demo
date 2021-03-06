<%--
  Created by IntelliJ IDEA.
  User: zangj
  Date: 2020/6/11
  Time: 15:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>大数据课程设计</title>

</head>
<link href="http://cdn.static.runoob.com/libs/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
<script src="http://cdn.staticfile.org/jquery/2.1.4/jquery.min.js"></script>
<script src="js/highcharts.js"></script>
<!--字体图标-->
<link href="http://cdn.javaex.cn/javaex/pc/css/icomoon.css" rel="stylesheet"/>
<!--动画-->
<link href="http://cdn.javaex.cn/javaex/pc/css/animate.css" rel="stylesheet"/>
<!--骨架样式-->
<link href="http://cdn.javaex.cn/javaex/pc/css/common.css" rel="stylesheet"/>
<!--皮肤（缇娜）-->
<link href="http://cdn.javaex.cn/javaex/pc/css/skin/tina.css" rel="stylesheet"/>
<!--jquery，不可修改版本-->
<script src="http://cdn.javaex.cn/javaex/pc/lib/jquery-1.7.2.min.js"></script>
<!--全局动态修改-->
<script src="http://cdn.javaex.cn/javaex/pc/js/common.js"></script>
<!--核心组件-->
<script src="http://cdn.javaex.cn/javaex/pc/js/javaex.min.js"></script>
<!--表单验证-->
<script src="http://cdn.javaex.cn/javaex/pc/js/javaex-formVerify.js"></script>
<body>
<div class="container">
    <!--主体内容-->
    <div class="list-content">
        <!--块元素-->
        <div class="block">
            <!--页面有多个表格时，可以用于标识表格-->
            <h2>大数据课程设计</h2>
            <!--右上角的返回按钮-->
            <!--正文内容-->
            <div class="main">
                <!--表格上方的搜索操作-->
                <div class="admin-search">
                </div>
                <!--表格上方的操作元素，添加、删除等-->
                <div class="operation-wrap">
                    <div class="buttons-wrap">
                        <div class="right">
                            <input type="hidden" id="timestmap" name="timestmap" value="">
                            <input type="text" id="inputpath" class="text" placeholder="请输入需要分析的路径" name="input"/>
                            <input type="text" id="outputpath" class="text" placeholder="请输入存放结果的路径" name="out"/>
                            <input type="button" id="save" class="button green empty" onclick="submit()" value="提交计算"/>
                        </div>
                    </div>
                </div>
                <table id="table" class="table color2">
                    <thead>
                    <tr>
                        <th style="text-align: center;">ID</th>
                        <th style="text-align: center;">状态</th>
                        <th style="text-align: center;">输入地址</th>
                        <th style="text-align: center;">输出地址</th>
                        <th style="text-align: center;">操作</th>
                    </tr>
                    </thead>
                    <tbody id="tbody" style="text-align: center;line-height: 52px; ">
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script>

    $.ajax({
        url: '/BigData_Demo_war_exploded/data?method=query',
        type: 'post',
        datatype: 'json',
        success: function (res) {
            console.log(res.data);
            //将数据显示在页面上
            var str = "";
            //遍历数据
            $.each(res.data, function (index, element) {
                var path = "/BigData_Demo_war_exploded/data?method=toResul&out=" + element['outputpath']
                str += "<tr>" +
                    "<td style=\"text-align: center;line-height: 52px; \">" + element['jobid'] + "</td>" +
                    "<td style=\"text-align: center;line-height: 52px; \">" + element['jobstatus'] + "</td>" +
                    "<td style=\"text-align: center;line-height: 52px; \">" + element['inputpath'] + "</td>" +
                    "<td style=\"text-align: center;line-height: 52px; \">" + element['outputpath'] + "</td>" +
                    "<td style=\"text-align: center;line-height: 52px; \">" +
                    "<span class='group-button' style='margin-top: 8px'>" +
                    "<a  href=" + "/BigData_Demo_war_exploded/result.jsp?out=" + escape(element['outputpath']) + "><button class='button blue'><span class='icon-eye'></span>查看</button></a>" +
                    "<a href=" + "/BigData_Demo_war_exploded/data?method=del&jobid=" + element['jobid'] + "><button class='button red'><span class='icon-close2'></span> 删除</button>" +
                    "<a href=" + "/BigData_Demo_war_exploded/data?method=downloadFile&jobid=" + element['jobid'] + "&path=" + escape(element['outputpath']) + "><button class='button green'><span class='icon-cloud_download'></span> 下载</button>" +
                    " </span>" +
                    "</td>" +
                    "</tr>"
            })
//将表格添加到body中
            $('#tbody').append(str);
        }
    })

    function submit() {
        var time = javaex.getTime();
        $('#timestmap').val(time)
        javaex.optTip({
            content: "数据提交中，请稍候...",
            type: "submit"
        });
        $.ajax({
            url: '/BigData_Demo_war_exploded/data?method=start',
            type: 'post',
            datatype: 'json',
            data: {
                timestmap: $('#timestmap').val(),
                input: $('#inputpath').val(),
                out: $('#outputpath').val()
            },
            success: function (res) {
                if (res.status == 0) {
                    javaex.optTip({
                        content: res.time,
                        type: "error"
                    });
                    return;
                } else {
                    console.log(res)
                    var str = "<tr>" +
                        "<td style=\"text-align: center;line-height: 52px; \">" + res['jobid'] + "</td>" +
                        "<td style=\"text-align: center;line-height: 52px; \">" + res['jobstatus'] + "</td>" +
                        "<td style=\"text-align: center;line-height: 52px; \">" + res['inputpath'] + "</td>" +
                        "<td style=\"text-align: center;line-height: 52px; \">" + res['outputpath'] + "</td>" +
                        "<td style=\"text-align: center;line-height: 52px; \">" +
                        "<span class='group-button' style='margin-top: 8px'>" +
                        "<a href=" + "/BigData_Demo_war_exploded/result.jsp?out=" + escape(res['outputpath']) + "><button class='button blue'><span class='icon-eye'></span>查看</button></a>" +
                        "<a href=" + "/BigData_Demo_war_exploded/data?method=del&jobid=" + res['jobid'] + "><button class='button red'><span class='icon-close2'></span> 删除</button>" +
                        "<a href=" + "/BigData_Demo_war_exploded/data?method=downloadFile&jobid=" + res['jobid'] + "&path=" + escape(res['outputpath']) + "><button class='button green'><span class='icon-cloud_download'></span> 下载</button>" +
                        " </span>" +
                        "</td>" +
                        "</tr>";
                    $('#tbody').append(str);
                    javaex.optTip({
                        content: "识别成功",
                        type: "success"
                    });


                }
            }

        })
    }

</script>
