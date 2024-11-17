<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>宿舍楼信息管理</title>
    <script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
    <script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/lib/bootstrap-table/bootstrap-table.min.js"></script>
    <script
            src="/static/lib/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
    <link rel="stylesheet"
          href="/static/lib/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="/static/lib/bootstrap-table/dist/bootstrap-table.min.css"/>
</head>
<body>
<div style="line-height: 34px;">
    <label class="col-sm-1 control-label">评分分类</label>
    <div class="col-sm-3">
        <select id="scoreNames" class="form-control" onchange="scoreNameChange()">
            <option value="" selected>请选择</option>
        </select>
    </div>
    <button id="importExcel" type="button" class="btn btn-primary" onclick="exportInfo()"
            style="margin-left: 15px;">
        <i class="glyphicon glyphicon-export"></i> 导出
    </button>
</div>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_score" class="table table-bordered"></table>
</div>
</body>

<script type="text/javascript">

    $(function () {
        initScoringBatch();
        tableInit();
    });

    // 导出
    function exportInfo() {
        var form = $("<form action='/score/exportHistoryExcel' method='get'></form>");
        $(document.body).append(form);
        form.submit();
        form.remove();
    }

    // 初始化评分列表
    function initScoringBatch() {
        $.ajax({
            url: "/score/getScoreNames",
            type: "get",
            success: function (data) {
                if (data) {
                    $.each(data, function (i, e) {
                        $("#scoreNames").append("<option value='" + e.id + "'>" + e.name + "</option>");
                    });
                } else {
                    alert("查询失败！");
                }
            },
            error: function (data) {
                console.log(data);
                alert("查询失败！");
            }
        });
    }

    // 下拉选择事件
    function scoreNameChange() {
        var val = $("#scoreNames").val();
        if (val == null || val == "") return;
        $('#tb_score').bootstrapTable('refresh');
    }

    //初始化Table
    function tableInit() {
        $('#tb_score').bootstrapTable({
            url: '/score/historyList', //请求后台的URL（*）
            method: 'get', //请求方式（*）
            cache: false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
            sortable: true, //是否启用排序
            sortOrder: "asc", //排序方式
            sidePagination: "server", //分页方式：client客户端分页，server服务端分页（*）
            pagination: true, //是否显示分页（*）
            pageNumber: 1, //初始化加载第一页，默认第一页
            pageSize: 10, //每页的记录行数（*）
            pageList: [10, 25, 50], //可供选择的每页的行数（*）
            uniqueId: 'id', //每一行的唯一标识，一般为主键列
            sortName: 'orderscore', // 要排序的字段
            queryParams: function (params) {
                return {
                    size: params.limit, // 每页要显示的数据条数
                    page: params.offset / params.limit, //pageable 下标从0开始
                    sort: params.sort + "," + params.order, // 排序规则
                    scoringBatchId: $("#scoreNames").val()
                }
            },//传递参数（*）
            responseHandler: function (res) {
                return {
                    total: res.totalElements, //总页数,前面的key必须为"total"
                    rows: res.content//行数据，前面的key要与之前设置的dataField的值一致.
                };
            },
            columns: [{
                field: 'dormid',
                title: '宿舍楼号',
                sortable: true
            }, {
                field: 'academy',
                title: '学院',
                sortable: true
            }, {
                field: 'sanitary',
                title: '整齐度评分',
                sortable: true
            }, {
                field: 'tidy',
                title: '卫生评分',
                sortable: true
            }, {
                field: 'sum',
                title: '总分',
                sortable: true
            }, {
                field: 'orderscore',
                title: '排名',
                sortable: true
            }]
        });
    };
</script>
</html>