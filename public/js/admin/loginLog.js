$(function(){
	$(".login-manage").addClass("active");
	
	log();
});

function log(){
	$('#grid').DataTable({
      "language": {"url": "//cdn.datatables.net/plug-ins/1.10.12/i18n/Chinese.json"},
      "paging": true,
      "lengthChange": true,
      "aLengthMenu": [[10, 25, 50], [10, 25, 50]],
      "searching": true,
      "ordering": false,
      "info": false,
      "autoWidth": true
	});
	/*$("#grid").kendoGrid({
		dataSource : {
			transport : {
				read : {
						type : "post",
						url : "../loginLog/query",
						dataType : "json",
						contentType : "application/json"
				},
				parameterMap : function(options, operation) {
						if (operation == "read") {
							return JSON.stringify(options);
						}
				}
			},
			pageSize : 10,
			schema : {
				total : "totalRecord",
				model : {
					fields : {
						loginname : {
							type : "string"
						},loginstart: {
							type : "date"
						},loginend: {
							type : "date"
						},timesum:{
							type : "string"
						},logintype:{
							type : "number"
						},ip:{
							type : "string"
						},descinfo:{
							type : "string"
						}
					}
				},
				data : "results"
			},
			serverPaging : true,
			serverFiltering: true
		},
		pageable : true,
		pageable : {
			refresh : true,
			pageSizes : true,
			buttonCount : 5,
			pageSizes : [ 5, 10, 20 ],
			messages : {
				display : "显示{0}-{1}条，共{2}条",
				empty : "没有数据",
				page : "页",
				of : "/ {0}",
				itemsPerPage : "条/页",
				first : "第一页",
				previous : "前一页",
				next : "下一页",
				last : "最后一页",
				refresh : "刷新"
			}
		},
		height : 460,
		selectable : "multiple cell",
		sortable : false,
		reorderable : false,
		columnMenu : false,
		filterable : false,
		noRecords : {
			template : "暂无数据！"
		},
		columns: [{
	         field: "rowNumber",
	         title: "序号",
	         template : "<span class='row-number'></span>",
	         width: 80,
	         editable : false
	     },{
	         field: "loginname",
	         title: "用户名",
	         width: 120
	     },{
	         field: "loginstart",
	         title: "登入时间",
	         format: "{0: yyyy-MM-dd HH:mm:ss}", //格式化时间  
	         width: 180
	     },{
	         field: "loginend",
	         title: "登出时间",
	         format: "{0: yyyy-MM-dd HH:mm:ss}", //格式化时间
	         width: 180
	     },{
	         field: "timesum",
	         title: "停留时长",
	         width: 180
	     },{
	         field: "logintype",
	         title: "登录类型",
	         values : [{"value": 1, "text": "登入" },{"value": 0,"text": "登出"}],
	     	 width: 75
	     }],
		dataBound : function() {
			var rows = this.items();
			var page = this.pager.page() - 1;
			var pagesize = this.pager.pageSize();
			$(rows).each(function() {
				var index = $(this).index() + 1 + page * pagesize;
				var rowLabel = $(this).find(".row-number");
				$(rowLabel).html(index);
			});
		}
	});*/
}
