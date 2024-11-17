<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
<script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
<link rel="stylesheet"
	href="/static/lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/static/lib/css/login/login.css" />
<title>登录</title>
</head>
<body>
	<div class="layout">
		<div class="title-name">
			<h3>宿舍管理系统</h3>
		</div>
		<form id="loginForm" action="/login" name="loginForm" method="post">
			<table class="login-table">
				<tr>
					<td class="col-md-12"><input type="text" class="form-control"
						id="username" name="username" placeholder="请输入用户名"
						autofocus="autofocus" autocomplete="off"></td>
				</tr>
				<tr>
					<td class="col-md-12"><input type="password"
						class="form-control" id="password" name="password"
						placeholder="请输入密码"></td>
				</tr>
				<tr>
					<td class="col-md-12"><select class="form-control"
						id="permission" name="permission">
							<option value="0">学生</option>
							<option value="1">宿舍管理员</option>
							<option value="2">系统管理员</option>
					</select></td>
				</tr>
				<tr>
					<td class="col-md-12">
						<button type="button" onclick="login()" class="btn btn-primary"
							id="loginBtn">登录</button>
						<button type="reset" class="btn btn-warning text-right"
							style="float: right" id="cancelBtn">取消</button>
					</td>
				</tr>
			</table>
		</form>
	</div>
</body>
<script type="text/javascript">
	$(function() {
		var msg = '${msg}';
		if ("error" == msg) {
			alert("登录失败！");
		}
	})
	function login() {
		var username = $("#username").val();
		var password = $("#password").val();
		if (username != null && username != "" && password != null
				&& password != "") {
			$("#loginForm").submit()
		} else {
			alert("用户名或密码不能为空！");
		}
	}
</script>
</html>