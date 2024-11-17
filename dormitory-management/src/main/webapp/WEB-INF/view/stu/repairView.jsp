<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>申请报修</title>
<script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
<script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/lib/bootstrap-table/bootstrap-table.min.js"></script>
<script
	src="/static/lib/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<script src="/static/lib/jquery-expand/jquery.expand.js"></script>
<link rel="stylesheet"
	href="/static/lib/bootstrap/css/bootstrap.min.css" />
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
	<button type="button" class="btn btn-primary btn-sm"
		style="margin-left: 15px;" onclick="applyRepair()">
		<i class="glyphicon glyphicon-wrench"></i> 申请报修
	</button>
	<div class="panel-body" style="padding-bottom: 0px;">
		<table id="tb_repairView" class="table table-bordered"></table>
	</div>
	<%--弹出框--%>
	<div id="my_modal" class="modal fade bs-modal-sm" tabindex="-1"
		role="dialog" aria-labelledby="myLargeModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" onclick="clear_form()">
						<span aria-hidden="true">&times;</span>
					</button>
					<h4 class="modal-title" id="gridSystemModalLabel">学生信息</h4>
				</div>
				<div class="modal-body">
					<form id="repair_Form" class="form-horizontal">
						<div class="form-group">
							<label for="reason" class="col-sm-2 control-label">报修类型</label>
							<div class="col-sm-9">
								<select id="reason" name="reason" class="form-control">
									<option value="0">灯</option>
									<option value="1">床，桌椅</option>
									<option value="2">门窗</option>
									<option value="3">其它</option>
								</select>
							</div>
						</div>
						<div class="form-group">
							<label for="note" class="col-sm-2 control-label">说明</label>
							<div class="col-sm-9">
								<textarea id="note" name="note" class="form-control" rows="2"
									style="resize: none;"></textarea>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-success" id="btn_save"
						onclick="btn_save()">
						<i class="glyphicon glyphicon-floppy-disk"></i>提交
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						onclick="clear_form()">
						<i class="glyphicon glyphicon-remove"></i>取消
					</button>
				</div>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript">

$(function() {
	tableInit();
})
//初始化Table
function tableInit() {
	$('#tb_repairView').bootstrapTable(tableOption);
};

var tableOption = {
		url : '/stu/repairInfo', //请求后台的URL（*）
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
		uniqueId : 'dormid', //每一行的唯一标识，一般为主键列
		sortName: 'dormid', // 要排序的字段
		queryParams : function(params) {
			return {
				size: params.limit, // 每页要显示的数据条数
				page: params.offset/params.limit, //pageable 下标从0开始
                sort: params.sort + "," + params.order // 排序规则
            }
		},//传递参数（*）
		responseHandler: function(res) {
			return {
		        total : res.totalElements, //总页数,前面的key必须为"total"
		        rows :  res.content//行数据，前面的key要与之前设置的dataField的值一致.
		    };
		},
		columns : [ {
			field : 'id',
			visible:false
		}, {
			field : 'dormid',
			title : '宿舍号',
			sortable : true
		}, {
			field : 'reason',
			title : '报修类型',
			formatter: function (value,row,index) {
				var result="";
				if (value == 0) {
					result = "灯";
				}else if(value == 1) {
					result = "床，桌椅";
				}else if(value == 2) {
					result = "门窗";
				}else if (value == 3){
					result = "其它";
				}
				return result;
			}
		}, {
			field : 'date',
			title : '报修时间',
			sortable : true,
			formatter: function (value,row,index) {
				if (value != null) {
		            var date = new Date(value);
		            return dateFtt("yyyy-MM-dd hh:mm",date);
		        }
			}
		},{
			field : 'note',
			title : '说明'
		},{
			field : 'status',
			title : '状态',
			formatter: function (value,row,index) {
				var result="";
				if (value == 0) {
					result = "未处理";
				}else if(value == 1) {
					result = "处理中"
				}
				return result;
			}
		},{
			 field:'operation',
			 title: '操作',
			 width: 60,
		  	 align: 'center',
			 valign: 'middle',
			 events: {
					"click #del":function(e,value,row,index) {
						del(row.id);
					}
			},	
		    formatter: actionFormatter
		}]
	};

//操作栏的格式化
function actionFormatter(value, row, index) {
	if (row.status == 0) {
		return "<button id='del' type='button' class='btn btn-xs red' title='删除'><i class='glyphicon glyphicon-remove'></i></button>";
	}else {
		return "<button id='del' type='button' class='btn btn-xs red' title='删除' disabled='disabled'><i class='glyphicon glyphicon-remove'></i></button>"
	}
}

// 申请报修
function applyRepair() {
	// 弹出编辑表单
	$('#my_modal').modal();
}

// 弹出框保存
function btn_save() {
	$.ajax({
        url: "/stu/createRepair",
        type: "POST",
        data: $("#repair_Form").serialize(),
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        success: function (data) {
            if (data) {
				alert("添加成功！");
				clear_form();
	            $('#tb_repairView').bootstrapTable('refresh');
            } else {
            	alert("添加失败！");
            }
        },
        error:function(data){
            console.log(data)
            alert("添加失败！");
        }
    });
	$('#my_modal').modal('hide');
}

function clear_form() {
	$("#repair_Form")[0].reset(); 
}

// 删除信息
function del(val) {
	$.ajax({
        url: "/stu/delRepairInfo",
        type: "get",
        data:{"repairId":val},
        contentType: "application/x-www-form-urlencoded",
        dataType: "json",
        success: function (data) {
            if (data) {
				alert("删除成功！");
	            $('#tb_repairView').bootstrapTable('refresh');
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


/**************************************时间格式化处理************************************/
function dateFtt(fmt,date) { //author: meizz   
  var o = {   
    "M+" : date.getMonth()+1,                 //月份   
    "d+" : date.getDate(),                    //日   
    "h+" : date.getHours(),                   //小时   
    "m+" : date.getMinutes(),                 //分   
    "s+" : date.getSeconds(),                 //秒   
    "q+" : Math.floor((date.getMonth()+3)/3), //季度   
    "S"  : date.getMilliseconds()             //毫秒   
  };   
  if(/(y+)/.test(fmt))   
    fmt=fmt.replace(RegExp.$1, (date.getFullYear()+"").substr(4 - RegExp.$1.length));   
  for(var k in o)   
    if(new RegExp("("+ k +")").test(fmt))   
  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
  return fmt;   
}
</script>
</html>