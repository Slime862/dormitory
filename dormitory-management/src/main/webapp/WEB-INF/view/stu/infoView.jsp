<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>个人信息</title>
    <script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
    <script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/lib/jquery-expand/jquery.expand.js"></script>
    <link rel="stylesheet" href="/static/lib/bootstrap/css/bootstrap.min.css"/>
</head>
<body>
<div class="container">
    <form id="stuinfo_Form" class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label">
                <h3>个人信息</h3>
            </label>
        </div>
        <div class="form-group">
            <label for="id" class="col-sm-2 control-label">学号</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="id" name="id"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">姓名</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="name" name="name"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="sex" class="col-sm-2 control-label">性别</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="sex" name="sex"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="grade" class="col-sm-2 control-label">年级</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="grade" name="grade"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="academy" class="col-sm-2 control-label">学院</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="academy" name="academy"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="major" class="col-sm-2 control-label">专业</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="major" name="major"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="dormid" class="col-sm-2 control-label">宿舍号</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="dormid" name="dormid"
                       readonly="readonly">
            </div>
        </div>
        <div class="form-group">
            <label for="phone" class="col-sm-2 control-label">电话</label>
            <div class="col-sm-4">
                <input type="text" class="form-control" id="phone" name="phone"
                       placeholder="电话">
            </div>
            <div class="col-sm-2">
                <button type="button" class="btn btn-success" id="update_phone"
                        onclick="updatePhone()">
                    <i class="glyphicon glyphicon-edit"></i> 修改
                </button>
            </div>
        </div>
    </form>
</div>
</body>
<script type="text/javascript">
    $(function () {
        var stuinfo = JSON.parse('${stuinfo}');
        $("#stuinfo_Form").initForm({
            jsonValue: stuinfo,
            isDebug: false
        });
        if (stuinfo.sex != undefined && stuinfo.sex != null) {
            if (stuinfo.sex == 0) {
                $("#sex").val("男");
            } else {
                $("#sex").val("女");
            }
        }
    });

    // 保存
    function updatePhone() {

        var phone = $("#phone").val();

        if (!(/^1[34578]\d{9}$/.test(phone))) {
            alert("手机号码有误，请重填");
            $("#phone").val("");
            return false;
        }

        $.ajax({
            url: "/stu/updatePhone",
            type: "POST",
            data: {
                id: $("#id").val(),
                phone: phone
            },
            contentType: "application/x-www-form-urlencoded",
            dataType: "json",
            success: function (data) {
                if (data) {
                    alert("修改成功！");
                    window.location.reload();
                } else {
                    alert("修改失败！");
                }
            },
            error: function (data) {
                console.log(data);
                alert("修改失败！");
            }
        });
    }
</script>
</html>