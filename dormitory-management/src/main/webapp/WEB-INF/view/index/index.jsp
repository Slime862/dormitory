<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
    <script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
    <script
            src="/static/lib/bootstrap-treeview/dist/bootstrap-treeview.min.js"></script>
    <link rel="stylesheet"
          href="/static/lib/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="/static/lib/bootstrap-treeview/dist/bootstrap-treeview.min.css"/>
    <link rel="stylesheet" href="/static/lib/css/index/index.css"/>
    <title>主页</title>
    <style type="text/css">
        .iframe {
            width: 100%;
            height: 610px;
            border: 1px solid #ddd;
            border-radius: 5px;
            padding: 15px
        }

        .nav {
            background-color: grey;
        }

        .navbar-item {
            color: white !important;
        }
    </style>
</head>
<body>
<nav class="navbar navbar-default nav">
    <div class="container-fluid">
        <div class="image-container">
        <img src="/static/img/logo.jpg" alt=""class="image-size">
            </div>
        <!-- Brand and toggle get grouped for better mobile display -->
        <div class="navbar-header">
            <button type="button" class="navbar-toggle collapsed"
                    data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"
                    aria-expanded="false">
                <span class="sr-only">Toggle navigation</span> <span
                    class="icon-bar"></span> <span class="icon-bar"></span> <span
                    class="icon-bar"></span>
            </button>
            <a class="navbar-brand navbar-item" href="#">宿舍管理系统</a>
        </div>




        <!-- Collect the nav links, forms, and other content for toggling -->
        <div class="collapse navbar-collapse"
             id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav navbar-right">
                <li><a class="navbar-item">当前用户： ${user.name }</a></li>
                <li><a class="navbar-item" href="#" onclick="logout()">注销</a></li>
            </ul>
        </div>
        <!-- /.navbar-collapse -->
    </div>
    <!-- /.container-fluid -->
</nav>
<div class="container-fluid">
    <div class="col-md-2">
        <div id="treeview"></div>
    </div>
    <div class="col-md-10">
        <iframe frameborder="0" scrolling="auto"
                class="iframe" src=""></iframe>
    </div>
</div>
</body>
<script type="text/javascript">
    var adminTree = [{
        text: "宿舍楼信息管理",
        href: '/dorm/view'
    }, {
        text: "学生信息管理",
        href: '/stu/view'
    }, {
        text: "职工信息管理",
        href: '/staff/view'
    }];

    var stuTree = [{
        text: "个人信息管理",
        nodes: [{
            text: "个人信息",
            href: '/stu/infoView'
        }, {
            text: "修改密码",
            href: '/stu/updatePwd'
        }]
    }, {
        text: "报修",
        nodes: [{
            text: "申请报修",
            href: "/stu/repairView"
        }, {
            text: "历史报修记录",
            href: "/stu/repairHistoryView"
        }]
    }, {
        text: "假期登记",
        nodes: [{
            text: "入校登记",
            href: '/register/stuInRegisterView'
        }, {
            text: "离校登记",
            href: '/register/stuOutRegisterView'
        }]
    }, {
        text: "宿舍评分查看",
        href: 'score/stuScoreView'
    }];

    var staffTree = [{
        text: "个人信息管理",
        nodes: [{
            text: "个人信息",
            href: '/staff/infoView'
        }, {
            text: "修改密码",
            href: '/staff/updatePwd'
        }]
    }, {
        text: "学生信息查询",
        href: '/staff/stu'
    }, {
        text: "报修单管理",
        nodes: [{
            text: "未处理报修单",
            href: '/staff/repairUntreatedView'
        }, {
            text: "处理中报修单",
            href: "/staff/repairProcessView"
        }, {
            text: "历史报修单",
            href: "/staff/repairHistoryView"
        }]
    }, {
        text: "假期登记",
        nodes: [{
            text: "入校登记管理",
            href: "/register/inRegisterView"
        }, {
            text: "入校登记历史管理",
            href: "/register/inRegisterHistoryView"
        }, {
            text: "离校登记管理",
            href: "/register/outRegisterView"
        }, {
            text: "离校登记历史管理",
            href: "/register/outRegisterHistoryView"
        }]
    }, {
        text: "宿舍评分管理",
        nodes: [{
            text: "评分录入",
            href: "score/view"
        }, {
            text: "历史评分表",
            href: "score/historyView"
        }]
    }, {
        text: "来访人员管理",
        nodes: [{
            text: "来访人员信息登记",
            href: "external/externalView"
        }, {
            text: "历史来访记录",
            href: "external/historyView"
        }]
    }];

    $(function () {
        var treeType = "${user.permission}";

        var tree = [];

        switch (parseInt(treeType)) {
            case 0:
                tree = stuTree;
                break;
            case 1:
                tree = staffTree;
                break;
            default:
                tree = adminTree;
                break;
        }

        $('#treeview').treeview({
            data: tree,
            onNodeSelected: function (event, data) {
                if (data.href != null) {
                    $("iframe").attr("src", data.href);
                }
            }
        });
    })

    function logout() {
        $.ajax({
            url: "/login/logout",
            type: "get",
            contentType: "application/x-www-form-urlencoded",
            dataType: "json",
            success: function (data) {
                window.location.reload();
            },
            error: function (data) {
                console.log(data);
                alert("请求失败！");
            }
        });
    }
</script>
</html>