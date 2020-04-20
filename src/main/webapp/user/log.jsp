<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function(){
        //初始化一个表格
        $("#userTable").jqGrid({
            url : "${path}/Log/queryByPage", //接收    page:当前页   rows：每页展示条数
            //editurl:"${path}/user/edit",
            datatype : "json",              //返回      page:当前页   rows：数据(List）  total：总页数   records:总条数
            rowNum : 5,
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#userPage',  //工具栏
            viewrecords : true,   //是否显示总条数
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            colNames : [ 'ID', '用户名', '操作时间', '操作详情', '状态'],
            colModel : [
                {name : 'id',width : 30},
                {name : 'adminname',width : 80},
                {name : 'data',width : 80,align:"center"},
                {name : 'operation',width : 150,align:"center",edittype:"file",},
                {name : 'status',editable:true,width : 80,align : "right"},
            ]
        });
    });


</script>

<%--初始化一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>


    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户信息</a></li>
    </div>



    <%--初始化表单--%>
    <table id="userTable" />

    <%--分页工具栏--%>
    <div id="userPage" />

</div>