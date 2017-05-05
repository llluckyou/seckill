<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2017/5/4
  Time: 13:03
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>秒杀详情页</title>
    <%@ include file="common/head.jsp"%>
</head>
<body>
    <div class="container">
        <div class="panel panel-default text-center">
            <div class="panel-heading">
                <h1>${seckill.name}</h1>
            </div>
            <div class="panel-body">
                <h2 class="text-danger">
                    <span class="glyphicon glyphicon-time" id="seckillBox"></span>
                    <span class="glyphicon"></span>
                </h2>
            </div>
        </div>
    </div>

    <!-- 弹出层 -->
    <div class="modal fade" id="killPhoneModal">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3 class="modal-title text-center">
                        <span class="glyphicon glyphicon-phone">

                        </span>
                    </h3>
                </div>

                <!-- 弹出层窗体，填写手机号 -->
                <div class="modal-body">
                    <div class="row">
                        <div class="col-xs-8 col-xs-offset-2">
                            <input type="text" name="killPhone" id="killPhone" placeholder="填写手机号" class="form-control">
                        </div>
                    </div>
                </div>

                <!-- 反馈信息 -->
                <div class="modal-footer">
                    <span id="killPhoneMessage" class="glyphicon"></span>
                    <button type="button" id="killPhoneBtn" class="btn btn-success text-center">
                        <span class="glyphicon glyphicon-phone"></span>
                        Submit
                    </button>
                </div>


            </div>
        </div>
    </div>
</body>

<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<script src="https://cdn.bootcss.com/jquery-cookie/1.4.1/jquery.cookie.js"></script>

<script src="https://cdn.bootcss.com/jquery.countdown/2.2.0/jquery.countdown.js"></script>

<script src="/resources/js/seckill.js" type="application/javascript" ></script>

<script type="application/javascript">
    $(function () {
        seckill.detail.init({
            seckillId : ${seckill.seckillId},
            startTime : ${seckill.startTime.time},
            endTime : ${seckill.endTime.time},

        });
    });
</script>

</html>
