<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<meta name="renderer" content="webkit">
<title>JDeploy自动化部署平台</title>
<link rel="stylesheet" href="http://cdn.bootcss.com/materialize/0.97.0/css/materialize.min.css">
<link href="${pageContext.request.contextPath}/resources/css/icon.css" rel="stylesheet">
	<link rel="stylesheet"
		  href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css"
		  integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u"
		  crossorigin="anonymous">

	<!-- 可选的 Bootstrap 主题文件（一般不用引入） -->
	<link rel="stylesheet"
		  href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css"
		  integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp"
		  crossorigin="anonymous">
<script src="http://cdn.bootcss.com/jquery/2.1.4/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/materialize/0.97.0/js/materialize.min.js"></script>
<script src="${pageContext.request.contextPath}/resources/js/index.js"></script>
	<script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"
			integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa"
			crossorigin="anonymous"></script>
</head>
<body style="font-family: 'Roboto', 'Droid Sans Fallback', '微软雅黑'; min-height: 100vh;display: flex;flex-direction: column;">

<div class="modal fade" style="width:300px;height: 80px" id="addAdminUser" tabindex="-1" role="dialog" aria-labelledby="addAdminUserLabel" aria-hidden="true">
	<div>
                        <span style="text-align: right;width: 30%;display: inline-table">
                            新增属性名:
                        </span>
		<span style="text-align: left;width: 60%;">
                        <input type="text" placeholder="请填写list接口需要返回的属性名" id="prop" name="prop" value="" class="form-control" style="width:60%;;display: inline">
                    </span>
		<div style="text-align: center"><a href="javascript:addEle()">保存</a>&nbsp;&nbsp;&nbsp;&nbsp;<a  data-dismiss="modal">关闭</a></div>
	</div>
</div>
<a href="javascript:addEle()" data-toggle="modal" data-target="#addAdminUser" >触发</a>
<div id="login" data-targert ></div>
<span id="st">${datas}</span>
<div id="content"></div>

	<script type="text/javascript">

        var jsons;
        $(document).ready(function(){
             var datas = '${datas}';
             jsons = eval('(' + datas + ')');
            for(var i in jsons){
                $("#content").append("<span  style=\"background-color:#bbdeff;width:100%;height:100%;\"><b>"+jsons[i]+"</b></span><span>&nbsp;&nbsp;&nbsp;</span>");
            }
        });


        function addEle(){
            var el = $("#prop").val();
            jsons.push(el);
            $("#content").append("<span  style=\"background-color:#bbdeff;width:100%;height:100%;\"><b>"+el+"</b></span><span>&nbsp;&nbsp;&nbsp;</span>");
            $('#addAdminUser').modal('hide')
        }

	</script>
</body>
</html>