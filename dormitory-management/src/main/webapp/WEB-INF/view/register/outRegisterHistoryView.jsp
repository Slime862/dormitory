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
<button id="importExcel" type="button" class="btn btn-primary" onclick="exportInfo()"
        style="margin-left: 15px;">
    <i class="glyphicon glyphicon-export"></i> 导出
</button>
<hr>
<div style="line-height: 34px;">
    <label class="col-sm-2 control-label" style="width: 10.666667%;">离校登记分类</label>
    <div class="col-sm-3">
        <select id="registerNames" class="form-control" onchange="registerNameChange()">
            <option value="" selected>请选择</option>
        </select>
    </div>
</div>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_register" class="table table-bordered"></table>
</div>
</body>

<script type="text/javascript">

    $(function () {
        initRegisterBatch();
        tableInit();
    });

    // 导出
    function exportInfo() {
        var form = $("<form action='/register/outExportExcel' method='get'></form>");
        $(document.body).append(form);
        form.submit();
        form.remove();
    }

    // 弹出框
    function openModal() {
        $("#my_modal").modal();
    }

    // 初始化登记列表
    function initRegisterBatch() {
        $.ajax({
            url: "/register/getOutRegisterNames",
            type: "get",
            success: function (data) {
                if (data) {
                    $.each(data, function (i, e) {
                        $("#registerNames").append("<option value='" + e.id + "'>" + e.name + "</option>");
                    });
                }
            },
            error: function (data) {
                console.log(data);
                alert("查询失败！");
            }
        });
    }

    // 下拉选择事件
    function registerNameChange() {
        var val = $("#registerNames").val();
        if (val == null || val == "") return;
        $('#tb_register').bootstrapTable('refresh');
    }

    //初始化Table
    function tableInit() {
        $('#tb_register').bootstrapTable({
            url: '/register/outHistoryList', //请求后台的URL（*）
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
            sortName: 'leavetime', // 要排序的字段
            queryParams: function (params) {
                return {
                    size: params.limit, // 每页要显示的数据条数
                    page: params.offset / params.limit, //pageable 下标从0开始
                    sort: params.sort + "," + params.order, // 排序规则
                    registerBatchId: $("#registerNames").val()
                }
            },//传递参数（*）
            responseHandler: function (res) {
                return {
                    total: res.totalElements, //总页数,前面的key必须为"total"
                    rows: res.content//行数据，前面的key要与之前设置的dataField的值一致.
                };
            },
            columns: [{
                field: 'stuId',
                title: '学号'
            }, {
                field: 'stuName',
                title: '姓名'
            }, {
                field: 'grade',
                title: '年级'
            }, {
                field: 'dormid',
                title: '宿舍号'
            }, {
                field: 'phone',
                title: '联系电话'
            }, {
                field: 'registerBatchName',
                title: '登记名'
            }, {
                field: 'leavetime',
                title: '离校时间',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value != null) {
                        var date = new Date(value);
                        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                    }
                }
            }, {
                field: 'city',
                title: '离校去向',
                sortable: true
            }]
        });
    };
</script>
</html>