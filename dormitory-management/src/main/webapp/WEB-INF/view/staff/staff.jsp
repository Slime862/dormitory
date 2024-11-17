<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>职工信息管理</title>
<script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
<script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/lib/bootstrap-table/bootstrap-table.min.js"></script>
<script src="/static/lib/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="/static/lib/jquery-expand/jquery.expand.js"></script>
<link rel="stylesheet" href="/static/lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="/static/lib/bootstrap-table/dist/bootstrap-table.min.css" />
<style type="text/css">
.green {
	color: #fff;
	background-color: #5bc0de;
	border-color: #46b8da;
}

.blue {
	color: #fff;
	background-color: #337ab7;
	border-color: #2e6da4;
}

.red {
	color: #fff;
	background-color: #d9534f;
	border-color: #d43f3a;
}
</style>
</head>
<body>

	<button id="addStaff" type="button" class="btn btn-primary btn-sm"
			style="margin-left: 15px;" onclick="addStaff()">
		<i class="glyphicon glyphicon-plus"></i> 添加
	</button>
	<form class="form-inline" style="padding-left: 15px;margin-top: 15px;">
		<label for="searchName">姓名</label>
		<input type="text"
			   class="form-control"
			   id="searchName" name="searchName" placeholder="输入姓名搜索">
		<label for="searchDorm">宿舍楼栋</label>
		<input type="text"
			   class="form-control"
			   id="searchDorm" name="searchDorm" placeholder="输入宿舍楼栋搜索">
		<button class="btn btn-warning" type="button" onclick="search()">
			<i class="glyphicon glyphicon-search" title="搜索"></i> 搜索
		</button>
	</form>
	<div class="panel-body" style="padding-bottom: 0px;">
		<table id="tb_staffs" class="table table-bordered"></table>
	</div>
	<%--学生信息弹出框--%>
	<div id="my_modal" class="modal fade bs-modal-sm" tabindex="-1"
		role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" onclick="clear_form()">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">职工信息</h4>
				</div>
				<div class="modal-body">
					<form id="staffinfo_Form" class="form-horizontal">
						<div class="form-group">
							<label for="id" class="col-sm-2 control-label">职工号</label>
							<div class="col-sm-9">
								<input type="text"
								class="form-control" id="id" name="id" readonly="readonly">
							</div>
						</div>
						<div class="form-group">
							<label for="staffname" class="col-sm-2 control-label">姓名</label>
							<div class="col-sm-9">
								<input type="text"
								class="form-control" id="staffname" name="staffname" placeholder="姓名">
							</div>
						</div>
						<div class="form-group">
							<label class="col-sm-2 control-label">性别</label>
							<div class="col-sm-4">
								<label class="radio-inline">
									<input type="radio" name="sex" id="sexRadio1" value="0">男
								</label>
								<label class="radio-inline">
									<input type="radio" name="sex" id="sexRadio2" value="1">女
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="age" class="col-sm-2 control-label">年龄</label>
							<div class="col-sm-9">
								<input type="text"
								class="form-control" id="age" name="age" placeholder="年龄">
							</div>
						</div>
						<div class="form-group">
							<label for="dormname" class="col-sm-2 control-label">宿舍名称</label>
							<div class="col-sm-9">
								<input type="text"
									class="form-control" id="dormname" name="dormname"
									placeholder="宿舍楼名称">
							</div>
						</div>
						<div class="form-group">
							<label for="dormno" class="col-sm-2 control-label">宿舍栋号</label>
							<div class="col-sm-9">
								<input type="text"
									class="form-control" id="dormno" name="dormno" placeholder="宿舍楼栋号">
							</div>
						</div>
						<div class="form-group">
							<label for="phone" class="col-sm-2 control-label">电话</label>
							<div class="col-sm-9">
								<input type="text"
									class="form-control" id="phone" name="phone" placeholder="电话">
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="btn_save"
						onclick="btn_save()">
						<i class="glyphicon glyphicon-floppy-disk"></i>确认
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="clear_form()">
						<i class="glyphicon glyphicon-remove"></i>关闭
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">

$(function() {
	tableInit();
	importExcel();
});

// 导入
function importExcel() {
	$('#importExcel').after('<input type="file" id="load_xls" name="file" style="display:none" onchange ="uploadFile()">');
	$('#importExcel').click(function(){
		$("#load_xls").click();
    });
}

// 添加信息
function addStaff() {
	$('#my_modal').modal();
	$("#id").removeAttr("readonly");
}

// 搜索信息
function search() {
	$('#tb_staffs').bootstrapTable('destroy');
	$("#tb_staffs").bootstrapTable(tableOption);
}

// 上传文件
function uploadFile() {
    var myform = new FormData();
    myform.append('file',$('#load_xls')[0].files[0]);
    $.ajax({
        url: "/staff/importExcel",
        type: "POST",
        data: myform,
        contentType: false,
        processData: false,
        success: function (data) {
            if (data) {
				alert("导入成功！");
				$('#tb_staffs').bootstrapTable('refresh');
            } else {
            	alert("导入失败！");
            }
        },
        error:function(data){
            console.log(data);
            alert("导入失败！");
        }
    });
}

//初始化Table
function tableInit() {
	$('#tb_staffs').bootstrapTable(tableOption);
};

var tableOption = {
		url : '/staff/list', //请求后台的URL（*）
		method : 'get', //请求方式（*）
		cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
		clickToSelect:true,
		sortable : true, //是否启用排序
		sortOrder : "asc", //排序方式
		sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
		pagination : true, //是否显示分页（*）
		pageNumber : 1, //初始化加载第一页，默认第一页
		pageSize : 10, //每页的记录行数（*）
		pageList : [10, 25, 50], //可供选择的每页的行数（*）
		uniqueId : 'id', //每一行的唯一标识，一般为主键列
		sortName: 'id', // 要排序的字段
		queryParams : function(params) {
			return {
				size: params.limit, // 每页要显示的数据条数
				page: params.offset/params.limit, //pageable 下标从0开始
                sort: params.sort + "," + params.order, // 排序规则
                searchName: $("#searchName").val(),
                searchDorm: $("#searchDorm").val()
            }
		},//传递参数（*）
		responseHandler: function(res) {
			//if(res==null)
			return {
		        total : res.totalElements, //总页数,前面的key必须为"total"
		        rows :  res.content[0]==null?[]:res.content//行数据，前面的key要与之前设置的dataField的值一致.
		    };
		},
		columns : [ {
			field : 'id',
			title : '职工号',
			sortable : true
		}, {
			field : 'staffname',
			title : '姓名',
			sortable : true
		}, {
			field : 'sex',
			title : '性别',
			sortable : true,
			formatter: function (value,row,index) {
				var result="";
				if (value == 0) {
					result = "男";
				}else if(value == 1) {
					result = "女"
				}
				return result;
			}
		},{
			field : 'age',
			title : '年龄',
			sortable : true
		},{
			field : 'other',
			title : '管理楼栋',
			formatter: function (value,row,index) {
				return row.dormname + "-" + row.dormno;
			}
		},{
			 field:'operation',
			 title: '操作',
			 width: 150,
		  	 align: 'center',
			 valign: 'middle',
			 events: {
					"click #reset":function(e,value,row,index) {
						reset(row.id);
					},
					"click #edit":function(e,value,row,index) {
						edit(row);
					},
					"click #del":function(e,value,row,index) {
						del(row.id);
					}
			},	
		    formatter: actionFormatter
		}]
	};

//操作栏的格式化
function actionFormatter(value, row, index) {
	return [
		"<button id='reset' type='button' class='btn btn-xs green' title='重置'><i class='glyphicon glyphicon-retweet'></i></button>",
		"<button id='edit' type='button' class='btn btn-xs blue' title='编辑'><i class='glyphicon glyphicon-pencil'></i></button>",
		"<button id='del' type='button' class='btn btn-xs red' title='删除'><i class='glyphicon glyphicon-remove'></i></button>"
	].join(" ")
}

// 重置密码
function reset(val) {
	$.ajax({
        url: "/staff/resetPwd",
        type: "get",
        data:{"staffId":val},
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        success: function (data) {
            if (data) {
				alert("重置成功！");
            } else {
            	alert("重置失败！");
            }
        },
        error:function(data){
            console.log(data)
            alert("重置失败！");
        }
    });
}

// 编辑
function edit(val) {
	$("#id").attr("readonly","readonly");
	$('#my_modal').modal();
	var options = { jsonValue: val, isDebug: false };
	$("#staffinfo_Form").initForm(options);
}

// 弹出框保存
function btn_save() {
	$.ajax({
        url: "/staff/update",
        type: "POST",
        data: $("#staffinfo_Form").serialize(),
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        success: function (data) {
            if (data) {
				alert("保存成功！");
	            $('#tb_staffs').bootstrapTable('refresh');
            } else {
            	alert("保存成功！");
            }
        },
        error:function(data){
            console.log(data)
            alert("保存成功！");
        }
    });
	$('#my_modal').modal('hide');
}

function clear_form() {
	$("#staffinfo_Form")[0].reset(); 
}

// 删除信息
function del(val) {
	$.ajax({
        url: "/staff/del",
        type: "get",
        data:{"staffId":val},
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        success: function (data) {
            if (data) {
				alert("删除成功！");
	            $('#tb_staffs').bootstrapTable('refresh');
            } else {
            	alert("删除失败！");
            }
        },
        error:function(data){
            console.log(data);
            alert("删除失败！");
        }
    });
}

</script>
</html>