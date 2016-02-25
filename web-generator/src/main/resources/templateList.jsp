<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/jfz/static/common/jsp/common.jsp" %>  
  
<!DOCTYPE html>
<html lang="en">
    <head>
    
        <meta charset="utf-8">
        
        <!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame
        Remove this if you use the .htaccess -->
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">

        <title>列表</title>
        
        <jsp:include page="/jfz/static/common/jsp/header.jsp"/>

    </head>
    <body>
    	<div>
    		
			<!-- 搜索 -->
			<form id="{paramClassName}_search_form">
				<table>
					{searchTable}
				</table>
			</form>
			<a style="height:25px;" href="javascript:;" id='add_btn' onclick="add{ClassName}Dialog();">添加</a>
			<br/>
			
			<br/>
			
			<!-- grid内容展示区域 -->
			<table id="{paramClassName}_list"></table>
			<div id="{paramClassName}_pager"></div>
			
		</div>
		
		<!-- 添加用户 -->
		<form id="{paramClassName}_add_form" style="display: none;">
		<input type="text" name="id" id="id" hidden = "true" value ="0"
 							class="text ui-widget-content ui-corner-all">
			<table>
				{addTable}
				<tr>
					</td>
						<td colspan="2">
							<a style="height:22px;" href="javascript:;" id='search_btn' onclick="search()">搜索</a>
							<a style="height:25px;" href="javascript:;" id='reset_search_btn' onclick="resetSearch()">重置</a>
						</td>
					</tr>
			</table>
		</form>
		
		<jsp:include page="/jfz/static/common/jsp/bottom.jsp"/>
		
		<script type="text/javascript">
			//获取数据的url
			var baseUrl = "${path}{paramClassName}/data";
			
			$(document).ready(function() {
				
				initUI();
				
				initGrid();
				
				initVaildator();
				
				$("#{paramClassName}_add_form #userId").change(function(){  
				    $("#{paramClassName}_add_form #userId").removeData("previousValue");  
				}); 
				
			});
			
			/**初始化ui*/
			function initUI() {
				$("#search_btn").button({
					icons: {
			        	primary: "ui-icon-search"
			      	},
				});
				
				$("#reset_search_btn").button();
				//$("#createTimeFrom").datepicker();
				//$("#createTimeTo").datepicker();
				
				$("#{paramClassName}_add_submit").button();
				$("#add_btn").button();
				
			}
			
			/**初始化grid*/
			function initGrid() {
				$("#{paramClassName}_list").jqGrid({
					url : baseUrl,
					datatype : "json",
					colNames : ["操作",{colNameList}'id'],
					colModel : [
					    {name: 'action', index:'action',align : "center",
							formatter:function(cellvalue, options, rowObject){
								var {paramClassName}Id = rowObject.id;
								return  "<input type='button' class='{paramClassName}_del_btn' value='删除' onclick=\"invalid{ClassName}Dialog('"+{paramClassName}Id+"');\" /> "
							}, width : 55,
						sortable:false},
					   {colList}
					],
					autowidth:true,
					height:550,
					rowNum : 20,
					rowList : [10, 20, 30],
					pager : '#{paramClassName}_pager',
					viewrecords : true,
					gridComplete: function(){
						$(".{paramClassName}_del_btn").button();
					},
					caption : "列表"
				}).jqGrid('navGrid', '#{paramClassName}_pager', {
					edit : false,
					add : false,
					del : false,
					search:false
				}).jqGrid('navButtonAdd','#{paramClassName}_pager',{
					caption:"添加",
					buttonicon:"ui-icon-plus",
					onClickButton:function() {
						add{ClassName}Dialog();
					},
					position:"last"
				});
			}
			
			function formatteState(state) {
				switch(state) {
				case 0:
					return "正常";
				case 1:
					return "失效";
				default:
					return "异常";
				}
			}
			
			var {paramClassName}AddValidator;
			
			/**初始化校验*/
			function initVaildator() {
				
				var {paramClassName}AddValidateOptions = {
					focusCleanup:true,
					focusInvalid:false,
					errorPlacement:function(error,element) {
						element.parent().append("<br>");
						error.appendTo(element.parent());
					},
					rules:{
						id:{
							required:true,
						},
					},
					messages:{
						id:{
							required:"id不能为空",
						},
					}
						
				};
				
				{paramClassName}AddValidator = $("#{paramClassName}_add_form").validate({paramClassName}AddValidateOptions);
				
			}
			
			/**用户搜索*/
			function search(){
				var fields = $("#{paramClassName}_search_form").serializeArray();
				var url = baseUrl + "?";
				$.each( fields, function(i, field){
				  	url += (field.name + "=" + field.value + "&")
				});
				url = url.substring(0, url.length-1);
				$("#{paramClassName}_list").jqGrid('setGridParam',{url:url,page:1}).trigger("reloadGrid");
			}
			
			function resetSearch(){
				$("#{paramClassName}_search_form")[0].reset();
			}
			/**打开添加用户对话框*/
			function add{ClassName}Dialog() {
				$("#{paramClassName}_add_form").dialog({
				      modal: true,
				      height:300,
				      width:500,
				      show:{
				          effect: "fade",
				          duration: 500
				      },
				      buttons:{
				    	  "确认": function() {
				    		  //add{ClassName} && add{ClassName}.call($(this));
				    		  add{ClassName}();
				    	  }
				    	  
				      },
				      close:function() {
				    	  $("#{paramClassName}_add_form").dialog("destroy");
				    	  {paramClassName}AddValidator.resetForm();
				      }
				 });
			}
			
			/**添加用户*/
			function add{ClassName}() {
				$("#{paramClassName}_add_form").ajaxSubmit({
	    			type:"post",
	    			url:"${path}{paramClassName}/save",
	    			dataType:"html",
	    			beforeSubmit:showAdd{ClassName}Request,
	    			success:showAdd{ClassName}Response
	    		});
			}
			
			/**添加用户前，校验*/
			function showAdd{ClassName}Request() {
				return $("#{paramClassName}_add_form").valid();
			}
			
			/**添加用户之后*/
			function showAdd{ClassName}Response(responseText,statusText) {
				//调用close方法是已经调用过destroy
				$("#{paramClassName}_add_form").dialog("close");
				if("success" == statusText) {
					if($.parseJSON(responseText)["success"]) {
						$("#{paramClassName}_list").trigger("reloadGrid");
						$.simpleTip("操作成功");
					} else {
						$.simpleAlter("操作失败");
					}
					
				} else {
					$.simpleAlter("操作失败");
				}
				{paramClassName}AddValidator.resetForm();
			}
			
			//编辑对话框
			function edit{ClassName}Dialog(rowId) {
				$("#{paramClassName}_add_form").dialog({
				      modal: true,
				      show:{
				          effect: "fade",
				          duration: 500
				      },
				      buttons:{
				    	  "确认": function() {
				    		  //add{ClassName} && add{ClassName}.call($(this));
				    		  add{ClassName}();
				    	  }
				    	  
				      },
				      close:function() {
				    	  $("#{paramClassName}_add_form").dialog("destroy");
				    	  {paramClassName}AddValidator.resetForm();
				      }
				 });
				//往表单中填值
				setDialogValue(rowId);
			}
			
			function setDialogValue(rowId){
				//debugger;
				var data = $("#{paramClassName}_list").jqGrid('getRowData',rowId);
				var fields = $("#{paramClassName}_add_form").serializeArray();
				$.each( fields, function(i, field){
				  	var varId = field.name;
				  	$("#{paramClassName}_add_form #"+varId+"").val(data[varId]);
				});
			}
			
			/**打开删除用户对话框*/
			function invalid{ClassName}Dialog(id) {
				var args = {"id":id,"userId":key};
				$.simpleConfirm("确认删除吗?", 
						null, null, invalid{ClassName}, args);
			}
			
			/**删除用户*/
			function invalid{ClassName}(args) {
				$.ajax({
	        		type:"post",
	        		url:"${path}{paramClassName}/delete",
	        		data:args,
	        		dataType:"json",
	        		success:function(msg) {
	        			if(msg["success"]) {
	        				$.simpleTip("操作成功");
	        				$("#{paramClassName}_list").trigger("reloadGrid");
	        			} else {
	        				$.simpleAlter("无效id为"+args["id"]+"的失败");
	        			}
	        		},
	        		error:function() {
	        			$.simpleAlter("无效id为"+args["id"]+"的失败");
	        		}
	        	});
			}
			
		</script>
    
    </body>

</html>