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
    <script
            src="/static/lib/bootstrap3-editable/js/bootstrap-editable.min.js"></script>
    <script
            src="/static/lib/bootstrap-table/extensions/editable/bootstrap-table-editable.min.js"></script>
    <link rel="stylesheet"
          href="/static/lib/bootstrap/css/bootstrap.min.css"/>
    <link rel="stylesheet"
          href="/static/lib/bootstrap-table/dist/bootstrap-table.min.css"/>
    <link rel="stylesheet"
          href="/static/lib/bootstrap3-editable/css/bootstrap-editable.css"/>
</head>
<body>
<button type="button" class="btn btn-primary btn-sm"
        onclick="openModal()" style="margin-left: 15px;">
    <i class="glyphicon glyphicon-plus"></i> 创建评分表
</button>
<button id="importExcel" type="button" class="btn btn-warning btn-sm" onclick="importExcel()"
        style="margin-left: 10px;">
    <i class="glyphicon glyphicon-open"></i> 导入评分表
</button>
<button type="button" class="btn btn-success btn-sm" onclick="push()"
        style="margin-left: 10px;">
    <i class="glyphicon glyphicon-send"></i> 发布
</button>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_score" class="table table-bordered"></table>
</div>
<%--创建评分的弹出框--%>
<div id="my_modal" class="modal fade bs-modal-sm" tabindex="-1"
     role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close" onclick="clear_form()">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel">创建评分表</h4>
            </div>
            <div class="modal-body">
                <form id="score_Form" action="/score/createScore" method="post" class="form-horizontal">
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">名称</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="name" name="name">
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" id="btn_save"
                        onclick="btn_save()">
                    <i class="glyphicon glyphicon-floppy-disk"></i>提交
                </button>
            </div>
        </div>
    </div>
</div>
</body>

<script type="text/javascript">

    $(function () {
        tableInit();
    });

    // 导入
    function importExcel() {
        $.ajax({
            url: "/score/isCreateScore",
            type: "get",
            success: function (data) {
                if (data) {
                    alert("未创建的评分表！");
                } else {
                    $('#importExcel').after('<input type="file" id="load_xls" name="file" style="display:none" onchange ="uploadFile()">');
                    $("#load_xls").click();
                }
            },
            error: function (data) {
                console.log(data);
                alert("查询失败！");
            }
        });
    }

    // 上传文件
    function uploadFile() {
        var myform = new FormData();
        myform.append('file', $('#load_xls')[0].files[0]);
        $.ajax({
            url: "/score/importExcel",
            type: "POST",
            data: myform,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data) {
                    alert("导入成功！");
                    $('#tb_score').bootstrapTable('refresh');
                } else {
                    alert("导入失败！");
                }
            },
            error: function (data) {
                console.log(data);
                alert("导入失败！");
            }
        });
    }

    // 弹出框
    function openModal() {
        $("#my_modal").modal();
    }

    //初始化Table
    function tableInit() {
        $('#tb_score').bootstrapTable({
            url: '/score/list', //请求后台的URL（*）
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
                    sort: params.sort + "," + params.order // 排序规则
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
            },{
                field: 'sanitary',
                title: '整齐度评分',
                sortable: true,
                editable: {
                    type: 'text',
                    emptytext: '-',
                    title: '整齐度评分',
                    validate: function (v) {
                        if (isNaN(v)) return '评分必须是数字';
                        if (!/^([1-9]\d?(\.\d{1,2})?|0\.\d{1,2}|100)$/.test(v)) {
                            return '输入格式不正确'
                        }
                    }
                }
            }, {
                field: 'tidy',
                title: '卫生评分',
                sortable: true,
                editable: {
                    type: 'text',
                    emptytext: '-',
                    title: '卫生评分',
                    validate: function (v) {
                        if (isNaN(v)) return '评分必须是数字';
                        if (!/^([1-9]\d?(\.\d{1,2})?|0\.\d{1,2}|100)$/.test(v)) {
                            return '输入格式不正确'
                        }
                    }
                }
            }, {
                field: 'sum',
                title: '总分',
                sortable: true
            }, {
                field: 'orderscore',
                title: '排名',
                sortable: true
            }],
            onEditableSave: function (field, row, oldValue, $el) {
                if ((parseFloat(row.sanitary) + parseFloat(row.tidy)) > 100) {
                    alert("总分不能超过100");
                    $('#tb_score').bootstrapTable('refresh');
                } else {
                    $.ajax({
                        type: "post",
                        url: "/score/updateScore",
                        data: row,
                        dataType: 'JSON',
                        success: function (data) {
                            $('#tb_score').bootstrapTable('refresh');
                        },
                        error: function (err) {
                            console.info(err);
                            alert("修改失败！");
                        }
                    });
                }
            }
        });
    };

    // 弹出框保存
    function btn_save() {
        var name = $("#name").val();
        if (name == null || name == "") {
            alert("输入不能为空！")
            return;
        }
        $.ajax({
            url: "/score/isCreateScore",
            type: "get",
            success: function (data) {
                if (data) {
                    $("#score_Form").submit();
                } else {
                    alert("存在未填写完的评分！");
                }
                clear_form();
                $('#my_modal').modal('hide');
            },
            error: function (data) {
                console.log(data);
                alert("查询失败！");
            }
        });
    }

    // 发布
    function push() {
        $.ajax({
            url: "/score/push",
            type: "get",
            success: function (data) {
                if (data) {
                    alert("发布成功！");
                    $('#tb_score').bootstrapTable('refresh');
                } else {
                    alert("发布失败！");
                }
            },
            error: function (data) {
                console.log(data);
                alert("发布失败！");
            }
        });
    }

    function clear_form() {
        $("#score_Form")[0].reset();
    }
</script>
</html>