<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
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
<form class="form-inline" style="padding-left: 15px;margin-top: 15px;">
    <input type="text"
           class="form-control"
           id="stuname" name="stuname" placeholder="输入被访人搜索">
    <input type="date"
           class="form-control"
           id="startTime" name="startTime" placeholder="输入来访时间起始时间">
    <input type="date"
           class="form-control"
           id="endTime" name="endTime" placeholder="输入来访时间结束时间">
    <button class="btn btn-warning" type="button" onclick="search()">
        <i class="glyphicon glyphicon-search" title="搜索"></i> 搜索
    </button>
    <button id="importExcel" type="button" class="btn btn-primary" onclick="exportInfo()"
            style="margin-left: 15px;">
        <i class="glyphicon glyphicon-export"></i> 导出
    </button>
</form>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_external" class="table table-bordered"></table>
</div>
</body>

<script type="text/javascript">
    $(function () {
        tableInit();
    });

    // 导出
    function exportInfo() {
        var form = $("<form action='/external/exportHistoryExcel' method='get'></form>");
        $(document.body).append(form);
        form.submit();
        form.remove();
    }

    // 搜索信息
    function search() {
        $('#tb_external').bootstrapTable('refresh');
        $("form")[0].reset();
    }

    //初始化Table
    function tableInit() {
        $('#tb_external').bootstrapTable({
            url: '/external/externalHistoryList', //请求后台的URL（*）
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
            sortName: 'visittime', // 要排序的字段
            queryParams: function (params) {
                return {
                    size: params.limit, // 每页要显示的数据条数
                    page: params.offset / params.limit, //pageable 下标从0开始
                    sort: params.sort + "," + params.order, // 排序规则
                    stuname: $("#stuname").val(),
                    startTime: $("#startTime").val(),
                    endTime: $("#endTime").val()
                }
            },//传递参数（*）
            responseHandler: function (res) {
                return {
                    total: res.totalElements, //总页数,前面的key必须为"total"
                    rows: res.content//行数据，前面的key要与之前设置的dataField的值一致.
                };
            },
            columns: [{
                field: 'name',
                title: '来访姓名'
            }, {
                field: 'sex',
                title: '性别',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value == 0) {
                        return "男";
                    }
                    if (value == 1) {
                        return "女"
                    }
                }
            }, {
                field: 'phone',
                title: '联系方式',
                sortable: true
            }, {
                field: 'stuname',
                title: '被访人',
                sortable: true
            }, {
                field: 'visittime',
                title: '来访时间',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value != null) {
                        var date = new Date(value);
                        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
                    }
                }
            }, {
                field: 'endtime',
                title: '结束时间',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value != null) {
                        var date = new Date(value);
                        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate() + " " + date.getHours() + ":" + date.getMinutes();
                    }
                }
            }]
        });
    };
</script>
</html>