<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>修改密码</title>
<script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
<script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/lib/jquery-expand/jquery.expand.js"></script>
<link rel="stylesheet" href="/static/lib/bootstrap/css/bootstrap.min.css" />
</head>
<body>
	<div class="container">
		<form id="pwd_Form" class="form-horizontal">
			<div class="form-group">
				<label for="id" class="col-sm-2 control-label">
					<h3>修改密码</h3>
				</label>
			</div>
			<div class="form-group">
				<label for="name" class="col-sm-2 control-label">旧密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="oldPwd" name="oldPwd">
				</div>
			</div>
			<div class="form-group">
				<label for="sex" class="col-sm-2 control-label">新密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="newPwd" name="newPwd">
				</div>
			</div>
			<div class="form-group">
				<label for="grade" class="col-sm-2 control-label">确认新密码</label>
				<div class="col-sm-4">
					<input type="password" class="form-control" id="confirmNewPwd" name="confirmNewPwd">
				</div>
			</div>
			<div class="form-group" style="margin-left: 194px;">
				<button type="button" class="btn btn-success" id="btnSave"
					onclick="updatePwd()">
					<i class="glyphicon glyphicon-floppy-disk"></i>保存
				</button>
				<button type="button" class="btn btn-default" data-dismiss="modal"
					onclick="clearPwd()">
					<i class="glyphicon glyphicon-remove"></i>取消
				</button>
			</div>
		</form>
	</div>
</body>
<script type="text/javascript">
	function clearPwd() {
		$("#pwd_Form")[0].reset();
	}
	
	function updatePwd() {
		var oldPwd = $("#oldPwd").val();
		var newPwd = $("#newPwd").val();
		var confirmNewPwd = $("#confirmNewPwd").val();
		
		if (newPwd == null || confirmNewPwd == null || oldPwd == null) {
			alert("输入不能为空！")
			return
		}
		if (newPwd != confirmNewPwd) {
			alert("密码不一致！")
		}else {
			
			var id = '${id}';
			$.ajax({
				url : "/stu/updatePwd",
				type : "POST",
				data : {
					id: id,
					oldPwd: oldPwd,
					newPwd: newPwd
				},
				contentType : "application/x-www-form-urlencoded",
				dataType : "json",
				success : function(data) {
					if (data) {
						alert("修改成功！");
						clearPwd();
					} else {
						alert("修改失败！");
					}
				},
				error : function(data) {
					console.log(data)
					alert("修改失败！");
				}
			});
		}
	};
</script>
</html>