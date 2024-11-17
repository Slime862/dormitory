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
<button type="button" class="btn btn-success btn-sm"
        style="margin-left: 15px;" onclick="openModal()">
    <i class="glyphicon glyphicon-send"></i> 登记
</button>
<hr>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_register" class="table table-bordered"></table>
</div>
<%--创建登记的弹出框--%>
<div id="my_modal" class="modal fade bs-modal-sm" tabindex="-1"
     role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close" onclick="clear_form()">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel">入校登记</h4>
            </div>
            <div class="modal-body">
                <form id="register_Form" class="form-horizontal">
                    <div class="form-group">
                        <label for="arrivetime" class="col-sm-2 control-label">到校时间</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="arrivetime" name="arrivetime">
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

    var inRegisterId = '${inRegisterId}';

    $(function () {
        tableInit();
    });

    // 弹出框
    function openModal() {
        $("#my_modal").modal();
    }

    //初始化Table
    function tableInit() {
        $('#tb_register').bootstrapTable({
            url: '/register/stuHistoryList', //请求后台的URL（*）
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
            sortName: 'arrivetime', // 要排序的字段
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
                field: 'registerBatchName',
                title: '登记名'
            }, {
                field: 'arrivetime',
                title: '到校时间',
                sortable: true,
                formatter: function (value, row, index) {
                    if (value != null) {
                        var date = new Date(value);
                        return date.getFullYear() + '-' + (date.getMonth() + 1) + '-' + date.getDate();
                    }
                }
            }]
        });
    };

    // 弹出框保存
    function btn_save() {
        var time = $("#arrivetime").val();
        if (time == null || time == "") {
            alert("输入不能为空！");
            return;
        }

        $.ajax({
            url: "/register/isStuSaveRegister",
            type: "get",
            success: function (data) {
                if (data) {
                    $.post("/register/saveStuRegister", {
                        arrivetime: new Date($("#arrivetime").val()),
                        inRegisterId: inRegisterId
                    }, function (res) {
                        if (res) {
                            $('#tb_register').bootstrapTable('refresh');
                            alert("保存成功，等待管理员审核！");
                        } else {
                            alert("保存失败！");
                        }
                        clear_form();
                        $('#my_modal').modal('hide');
                    });
                } else {
                    alert("管理员还未发布新的登记！");
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

    function clear_form() {
        $("#register_Form")[0].reset();
    }
</script>
</html>