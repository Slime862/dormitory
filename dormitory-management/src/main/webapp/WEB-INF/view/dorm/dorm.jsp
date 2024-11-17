<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>宿舍楼信息管理</title>
<script src="/static/lib/jquery/jquery.1.9.1.min.js"></script>
<script src="/static/lib/bootstrap/js/bootstrap.min.js"></script>
<script src="/static/lib/bootstrap-table/bootstrap-table.min.js"></script>
<script src="/static/lib/bootstrap-table/locale/bootstrap-table-zh-CN.min.js"></script>
<link rel="stylesheet" href="/static/lib/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet"
	href="/static/lib/bootstrap-table/dist/bootstrap-table.min.css" />
</head>
<body>
	<button id="importExcel" type="button" class="btn btn-primary btn-sm"
		style="margin-left: 15px;">
		<i class="glyphicon glyphicon-open"></i> 导入
	</button>
	<div class="panel-body" style="padding-bottom: 0px;">
		<table id="tb_dorms" class="table table-bordered"></table>
	</div>
</body>

<script type="text/javascript">
	// 页面加载完成后调用
	$(function() {
		tableInit();
		importExcel();
	})
	
	// 导入
	function importExcel() {
		$('#importExcel').after('<input type="file" id="load_xls" name="file" style="display:none" onchange ="uploadFile()">');
		$('#importExcel').click(function(){
			$("#load_xls").click();
	    });
	}

	// 上传文件
	function uploadFile() {
        var myform = new FormData();
        myform.append('file',$('#load_xls')[0].files[0]);
        $.ajax({
            url: "/dorm/importExcel",
            type: "POST",
            data: myform,
            contentType: false,
            processData: false,
            success: function (data) {
                if (data) {
					alert("导入成功！");
                } else {
                	alert("导入失败！");
                }
                $('#tb_dorms').bootstrapTable('refresh');
            },
            error:function(data){
                console.log(data)
            }
        });
    }
	
	//初始化Table
	function tableInit() {
		$('#tb_dorms').bootstrapTable({
			url : '/dorm/list', //请求后台的URL（*）
			method : 'get', //请求方式（*）
			cache : false, //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
			sortable : true, //是否启用排序
			sortOrder : "asc", //排序方式
			sidePagination : "server", //分页方式：client客户端分页，server服务端分页（*）
			pagination : true, //是否显示分页（*）
			pageNumber : 1, //初始化加载第一页，默认第一页
			pageSize : 10, //每页的记录行数（*）
			pageList : [10, 25, 50], //可供选择的每页的行数（*）
			uniqueId : 'id', //每一行的唯一标识，一般为主键列
			sortName: 'dormname', // 要排序的字段
			queryParams : function(params) {
				return {
					size: params.limit, // 每页要显示的数据条数
					page: params.offset/params.limit, //pageable 下标从0开始
	                sort: params.sort + "," + params.order, // 排序规则
	            }
			},//传递参数（*）
			responseHandler: function(res) {
				return {
			        total : res.totalElements, //总页数,前面的key必须为"total"
			        rows :  res.content//行数据，前面的key要与之前设置的dataField的值一致.
			    };
			},
			columns : [ {
				field : 'dormname',
				title : '宿舍楼名称',
				sortable : true
			}, {
				field : 'dormno',
				title : '宿舍楼栋号',
				sortable : true
			}, {
				field : 'roomno',
				title : '房间号',
				sortable : true
			} ]
		});
	};
</script>
</html>