<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>

<script>
    /*延迟加载*/
    $(function(){
        //初始化表格的方法
        pageInit();


        //删除按钮
        $("#delBtn").click(function(){
            //1.判断选择一行
            //selrow:  只读属性，最后选择行的id
            //getGridParam: 返回请求的参数信息
            var rowId=$("#cateTable").jqGrid("getGridParam","selrow");
            if(rowId!=null){
                //根据行id获取整行数据
                //getRowData:返回指定行的数据，返回数据类型为name:value，name为colModel中的名称，value为所在行的列的值，
                // 如果根据rowid找不到则返回空。
                // 在编辑模式下不能用此方法来获取数据，它得到的并不是编辑后的值
                var row =$("#cateTable").jqGrid("getRowData",rowId);

                //发送请求
                $.post("${path}/category/edit",{"id":rowId,"oper":"del"},
                    function(data){
                    //页面刷新
                    $("#cateTable").trigger("reloadGrid");

                    //向警告框中写入内容
                    $("#showMsg").html(data.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function(){
                        //关闭提示框
                        $("#deleteMsg").hide();
                    },3000);
                },"JSON");

            }else{
                alert("请选择一行")
            }

            //2.确认是否删除
            //3.掉用删除方法  传递相关参数
            //4.回显提示信息
        });

    });

    //初始化表格方法
    function pageInit(){

        //父表格
        $("#cateTable").jqGrid({
            url : "${path}/category/queryByOnePage",
            editurl:"${path}/category/edit",
            datatype : "json",
            rowNum : 8,
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            rowList : [ 8, 10, 20, 30 ],
            pager : '#catePage',
            viewrecords : true,
            colNames : [ 'Id', '类别名', '级别'],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',editable:true,index : 'invdate',width : 90},
                {name : 'levels',index : 'name',width : 100}
            ],
            subGrid : true,  //是否开启子表格
            //1.subgrid_id   点击一行时会在父表格中创建一个div，用来容纳子表格，subgrid_id就是div的id
            //2.row_id   点击行的id
            subGridRowExpanded : function(subgridId, rowId) {  //设置子表格相关的属性
                //复制子表格内容
                addSubGrid(subgridId, rowId);
            }
        });

        //父表格分页工具栏
        $("#cateTable").jqGrid('navGrid', '#catePage', {add : true,edit : true,del : true},
            {closeAfterEdit:true},
            {
                closeAfterAdd:true
            },
            {
                closeAfterdel:true,  //关闭对话框
                //提交之后的操作
                afterSubmit:function(response){
                    //向警告框中写入内容
                    $("#showMsg").html(response.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function(){
                        //关闭提示框
                        $("#deleteMsg").hide();
                    },3000);

                    return "hello";
                }
            }
        );
    }

    //子表格
    function addSubGrid(subgridId, rowId){

        var subgridTableId= subgridId+"Table";  //定义子表格 Table的id
        var pagerId= subgridId+"Page";   //定义子表格工具栏id

        //在子表格容器中创建子表格和子表格分页工具栏
        $("#" + subgridId).html("<table id='"+subgridTableId+"' /> <div id='"+pagerId+"'>");


        //子表格
        $("#" + subgridTableId).jqGrid({
            url:"${path}/category/queryByTwoPage?parentId="+rowId,
            editurl:"${path}/category/edit?parentId="+rowId,
            datatype : "json",
            rowNum : 8,
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            rowList : [ 8, 10, 20, 30 ],
            pager : "#"+pagerId,
            viewrecords : true,
            colNames : [ 'Id', '类别名', '级别','上级类别id'],
            colModel : [
                {name : 'id',index : 'id',  width : 55},
                {name : 'cateName',editable:true,index : 'invdate',width : 90},
                {name : 'levels',index : 'name',width : 100},
                {name : 'parentId',index : 'name',width : 100}
            ]
        });

        //子表格分页工具栏
        $("#" + subgridTableId).jqGrid('navGrid',"#" + pagerId, {edit : true,add : true,del : true},
            {closeAfterEdit:true},
            {closeAfterAdd:true},
            {
                closeAfterdel:true,  //关闭对话框
                //提交之后的操作
                afterSubmit:function(response){
                    //向警告框中写入内容
                    $("#showMsg").html(response.responseJSON.message);
                    //展示警告框
                    $("#deleteMsg").show();

                    //自动关闭
                    setTimeout(function(){
                        //关闭提示框
                        $("#deleteMsg").hide();
                    },3000);
                    return "hello";
                }
            }

        );
    }

</script>

<%--初始化一个面板--%>
<div class="panel panel-success">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>类别信息</h2>
    </div>

    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">类别信息</a></li>
    </div>

    <%--警告框--%>
    <div id="deleteMsg" class="alert alert-warning alert-dismissible"  role="alert" style="width: 300px;display: none">
        <span id="showMsg" />
    </div>

    <%--按钮组--%>
    <div class="panel panel-body">
        <button class="btn btn-info" id="delBtn">删除类别</button>
    </div>


    <%--初始化表单--%>
    <table id="cateTable" />

    <%--分页工具栏--%>
    <div id="catePage" />

</div>