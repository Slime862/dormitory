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
    <style type="text/css">
        .red {
            color: #fff;
            background-color: #d9534f;
            border-color: #d43f3a;
        }
    </style>
</head>
<body>
<button type="button" class="btn btn-success btn-sm"
        style="margin-left: 15px;" onclick="openModal()">
    <i class="glyphicon glyphicon-send"></i> 添加来访
</button>
<hr>
<div class="panel-body" style="padding-bottom: 0px;">
    <table id="tb_external" class="table table-bordered"></table>
</div>
<%--来访信息登记的弹出框--%>
<div id="my_modal" class="modal fade bs-modal-sm" tabindex="-1"
     role="dialog" aria-labelledby="myLargeModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close" onclick="clear_form()">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title" id="gridSystemModalLabel">来访信息登记</h4>
            </div>
            <div class="modal-body">
                <form id="external_Form" class="form-horizontal">
                    <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">来访姓名</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="name" name="name">
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">性别</label>
                        <div class="col-sm-9">
                            <label class="radio-inline">
                                <input type="radio" name="sex" id="sexRadio1" value="0" checked>男
                            </label>
                            <label class="radio-inline">
                                <input type="radio" name="sex" id="sexRadio2" value="1">女
                            </label>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="phone" class="col-sm-2 control-label">联系方式</label>
                        <div class="col-sm-9">
                            <input type="text" class="form-control" id="phone" name="phone">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="dormid" class="col-sm-2 control-label">宿舍号</label>
                        <div class="col-sm-9">
                            <select id="dormid" class="form-control" onchange="dormidChange()">
                                <option value="" selected>请选择</option>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="stuname" class="col-sm-2 control-label">被访姓名</label>
                        <div class="col-sm-9">
                            <select id="stuname" name="stuname" class="form-control" disabled>
                                <option value="" selected>请选择</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-success" id="btn_save"
                        onclick="btn_save()">
                    <i class="glyphicon glyphicon-floppy-disk"></i>登记
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

    // 弹出框
    function openModal() {
        $("#my_modal").modal();
        initDormIds();
    }

    // 初始化宿舍列表
    function initDormIds() {
        $.ajax({
            url: "/dorm/getDorms",
            type: "get",
            success: function (data) {
                if (data) {
                    $.each(data, function (i, e) {
                        $("#dormid").append("<option value='" + e.id + "'>" + e.id + "</option>");
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

    // 下拉选择
    function dormidChange() {
        var dormid = $("#dormid").val();
        if (dormid != "") {
            $.ajax({
                url: "/stu/getStus",
                type: "get",
                data: {dormid: dormid},
                success: function (data) {
                    if (data) {
                        $("#stuname").empty();
                        $.each(data, function (i, e) {
                            $("#stuname").append("<option value='" + e.name + "'>" + e.name + "</option>");
                        });
                        $("#stuname").removeAttr("disabled");
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
    }

    //初始化Table
    function tableInit() {
        $('#tb_external').bootstrapTable({
            url: '/external/externalList', //请求后台的URL（*）
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
                field: 'id',
                visible: false
            }, {
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
                field: 'operation',
                title: '操作',
                width: 150,
                align: 'center',
                valign: 'middle',
                events: {
                    "click #over": function (e, value, row, index) {
                        over(row.id);
                    }
                },
                formatter: function () {
                    return "<button id='over' type='button' class='btn btn-xs red'>来访结束</button>";
                }
            }
            ]
        });
    };

    // 结束来访
    function over(id) {
        $.get('/external/overVisit', {id: id}, function (res) {
            if (res) {
                alert("结束成功！")
                $('#tb_external').bootstrapTable('refresh');
            }
        });
    }

    // 弹出框保存
    function btn_save() {
        var name = $("#name").val();
        var sex = $('input[name="sex"]:checked').val();
        var phone = $("#phone").val();
        var stuname = $("#stuname").val();

        if (name == null || name == "" ||
            sex == null || sex == "" ||
            phone == null || phone == "" ||
            stuname == null || stuname == "") {
            alert("输入不能为空！");
            return;
        }

        $.ajax({
            url: "/external/saveExternal",
            type: "post",
            data: $("#external_Form").serialize(),
            success: function (res) {
                if (res) {
                    $('#tb_external').bootstrapTable('refresh');
                } else {
                    alert("保存失败！");
                }
                clear_form();
                $("#stuname").attr("disabled","disabled");
                $('#my_modal').modal('hide');
            },
            error: function (data) {
                console.log(data);
                alert("保存失败！");
            }
        });
    }

    function clear_form() {
        $("#external_Form")[0].reset();
    }
</script>
</html>