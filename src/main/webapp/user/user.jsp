<%@page pageEncoding="UTF-8" isELIgnored="false"  %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script>
    $(function(){
        //初始化一个表格
        $("#userTable").jqGrid({
            url : "${path}/user/queryByPage", //接收    page:当前页   rows：每页展示条数
            editurl:"${path}/user/edit",
            datatype : "json",              //返回      page:当前页   rows：数据(List）  total：总页数   records:总条数
            rowNum : 2,
            rowList : [ 2,5,10, 20, 30 ],
            pager : '#userPage',  //工具栏
            viewrecords : true,   //是否显示总条数
            styleUI:"Bootstrap",
            height:"auto",
            autowidth:true,
            colNames : [ 'ID', '用户名','性别','城市', '手机号', '头像', '签名','微信', '状态',"注册时间" ],
            colModel : [
                {name : 'id',width : 80},
                {name : 'username',editable:true,width : 80},
                {name : 'sex',editable:true,width : 80},
                {name : 'city',editable:true,width : 80},
                {name : 'phone',editable:true,width : 80,align:"center"},
                {name : 'headImg',editable:true,width : 150,align:"center",edittype:"file",
                    //参数;列的值,操作,行对象
                    formatter:function(cellvalue, options, rowObject){
                        //return "<img src='${path}/upload/photo/"+rowObject.headImg+"' width='200px' height='100px' />";
                        return "<img src='"+rowObject.headImg+"' width='200px' height='100px' />";
                    }
                },
                {name : 'sign',editable:true,width : 80,align : "right"},
                {name : 'wechat',editable:true,width : 80,align : "right"},
                {name : 'status',width : 80,align : "center",
                    //参数;列的值,操作,行对象
                    formatter:function(cellvalue, options, rowObject){
                        if(cellvalue==1){  // 1:正常  0:冻结
                            return "<button onclick='updateStatus(\""+rowObject.id+"\",\""+cellvalue+"\")' class='btn btn-success' >冻结</button>";
                        }else{
                            return "<button onclick='updateStatus(\""+rowObject.id+"\",\""+cellvalue+"\")' class='btn btn-danger' >解除冻结</button>";
                        }
                    }},
                {name : 'createDate',width : 80,align : "right"}
            ]
        });

        //表格工具栏
        $("#userTable").jqGrid('navGrid', '#userPage',
            {edit : true,add : true,del : true,addtext:"添加",edittext:"修改",deltext:"删除"},
            {

            },   //修改之后的额外操作
            {
                closeAfterAdd:true,  //关闭对话框
                //文件上传
                afterSubmit:function(response){

                    //数据的入库
                    //文件没有上传
                    // 图片路径不对
                    //在提交之后做文件上传   本地
                    //fileElementId　　　　　  需要上传的文件域的ID，即<input type="file">的ID。

                    //修改图片路径     id,图片路径
                    //response.responseText: "7c913729-c9ab-4ef3-89b8-e202ecbe07b0"

                    //异步文件上传
                    $.ajaxFileUpload({
                        url:"${path}/user/uploadUser",
                        type:"post",
                        dataType:"text",
                        fileElementId:"headImg",    //上传的文件域的ID
                        data:{id:response.responseText},
                        success:function(){
                            //刷新表单
                            $("#userTable").trigger("reloadGrid");
                        }
                    });

                    //必须有返回值
                    return "hello";
                }
            },   //添加
            {}    //删除
        );

        //发送验证码
        $("#basic-addon2").click(function(){

            var phone=$("#phoneInput").val();

            alert(phone);

        });

    });

    //修改状态
    function updateStatus(id,status){

        //根据id  修改状态
        //id   status  (0,1)
        if(status==1){
            //修改
            $.ajax({
                url:"${path}/user/edit",
                type:"post",
                data:{"id":id,"status":"0","oper":"edit"},
                success:function(){
                    //刷新页面
                    $("#userTable").trigger("reloadGrid")
                }
            });

        }else{
            $.ajax({
                url:"${path}/user/edit",
                type:"post",
                data:{"id":id,"status":"1","oper":"edit"},
                success:function(){
                    //刷新页面
                    $("#userTable").trigger("reloadGrid")
                }
            });
        }
    }

   $("#excel").click(function(){
        $.ajax({
            url:"${path}/user/sendExcel",
            type:"post",
            dataType:"json",
            success:function (message) {
                alert(message);
            }
        })
      
   })


</script>

<%--初始化一个面板--%>
<div class="panel panel-info">

    <%--面板头--%>
    <div class="panel panel-heading">
        <h2>用户信息</h2>
    </div>

    <%--发送验证码--%>
    <div class="input-group" style="width: 400px;height: 30px;float: right">
        <input id="phoneInput" type="text" class="form-control" placeholder="请输入验证码" aria-describedby="basic-addon2">
        <span class="input-group-addon" id="basic-addon2">点击发送验证码</span>
    </div>


    <%--标签页--%>
    <div class="nav nav-tabs">
        <li class="active"><a href="">用户信息</a></li>
    </div>


    <%--按钮--%>
        <div class="panel panle_body">
            <button id="excel" class="btn btn-info">导出用户信息</button>
        </div>

        <%--警告框--%>
        <div id="deleteMsg" class="alert alert-warning alert-dismissible"  role="alert" style="width: 300px;display: none">
            <span id="showMsg" />
        </div>

    <%--初始化表单--%>
    <table id="userTable" />

    <%--分页工具栏--%>
    <div id="userPage" />

</div>