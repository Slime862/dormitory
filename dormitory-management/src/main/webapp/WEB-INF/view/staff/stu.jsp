<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
    <script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/lib/bootstrap-table/bootstrap-table.min.js"></script>
    <script src="/static/lib/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
    <script src="/static/lib/jquery-expand/jquery.expand.js"></script>
    <link rel="stylesheet" href="/static/lib/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="/static/lib/bootstrap-table/dist/bootstrap-table.min.css"/>
</head>
<body>
<form class="form-inline" style="padding-left: 15px;margin-top: 15px;">
    <label for="searchId">学号</label>
    <div class="input-group" style="margin-left: 10px;margin-right: 10px">
        <input type="text"
               class="form-control"
               id="searchId" name="searchId" placeholder="输入学号搜索">
        <span class="input-group-btn">
            <button class="btn btn-warning" type="button" onclick="search('searchId')">
                <i class="glyphicon glyphicon-search" title="搜索"></i>
            </button>
	    </span>
    </div>
    <label for="searchDormName">宿舍号</label>
    <div class="input-group" style="margin-left: 10px">
        <input type="text"
               class="form-control"
               id="searchDormName" name="searchDormName" placeholder="输入宿舍号搜索">
        <span class="input-group-btn">
            <button class="btn btn-warning" type="button" onclick="search('searchDormName')">
                <i class="glyphicon glyphicon-search" title="筛选"></i>
            </button>
        </span>
    </div>
</form>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_stus" class="table table-bordered"></table>
</div>
</body>
<script type="text/javascript">

    $(function () {
        tableInit();
    });

    // 搜索信息
    function search(val) {
        if (val == "searchId") {
            $("#searchDormName").val("")
        }
        if (val == "searchDormName") {
            $("#searchId").val("")
        }
        $('#tb_stus').bootstrapTable('refresh');
    }

    // 导出
    function exportInfo() {
        var form = $("<form action='/stu/exportInfo' method='post'></form>");
        $(document.body).append(form);
        form.submit();
        form.remove();
    }

    //初始化Table
    function tableInit() {
        $('#tb_stus').bootstrapTable(tableOption);
    };

    var tableOption = {
        url: '/stu/list', //请求后台的URL（*）
        method: 'get', //请求方式（*）
        cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        clickToSelect: true,
        sortable: true, //是否启用排序
        sortOrder: "asc", //排序方式
        sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
        pagination: true, //是否显示分页（*）
        pageNumber: 1, //初始化加载第一页，默认第一页
        pageSize: 10, //每页的记录行数（*）
        pageList: [10, 25, 50], //可供选择的每页的行数（*）
        uniqueId: 'id', //每一行的唯一标识，一般为主键列
        sortName: 'id', // 要排序的字段
        queryParams: function (params) {
            return {
                size: params.limit, // 每页要显示的数据条数
                page: params.offset / params.limit, //pageable 下标从0开始
                sort: params.sort + "," + params.order, // 排序规则
                searchId: $("#searchId").val(),
                searchDormName: $("#searchDormName").val()
            }
        },//传递参数（*）
        responseHandler: function (res) {
            return {
                total: res.totalElements, //总页数,前面的key必须为"total"
                rows: res.content//行数据，前面的key要与之前设置的dataField的值一致.
            };
        },
        columns: [{
            field: 'id',
            title: '学号',
            sortable: true
        }, {
            field: 'name',
            title: '姓名',
            sortable: true
        }, {
            field: 'sex',
            title: '性别',
            sortable: true,
            formatter: function (value, row, index) {
                var result = "";
                if (value == 0) {
                    result = "男";
                } else if (value == 1) {
                    result = "女"
                }
                return result;
            }
        }, {
            field: 'grade',
            title: '年级',
            sortable: true
        }, {
            field: 'academy',
            title: '学院',
            sortable: true
        }, {
            field: 'major',
            title: '专业',
            sortable: true
        }, {
            field: 'dormid',
            title: '宿舍号',
            sortable: true
        }, {
            field: 'phone',
            title: '电话'
        }, {
            field: 'status',
            title: '状态',
            sortable: true,
            formatter: function (value, row, index) {
                var result = "";
                if (value == 0) {
                    result = "在校";
                } else if (value == 1) {
                    result = "离校"
                }
                return result;
            }
        }]
    };

</script>
</html>