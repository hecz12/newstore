<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!doctype html>
<html>

	<head>
		<meta charset="utf-8" />
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<title>会员登录</title>
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css" />
		<script src="${pageContext.request.contextPath}/js/jquery-1.11.3.min.js" type="text/javascript"></script>
		<script src="${pageContext.request.contextPath}/js/bootstrap.min.js" type="text/javascript"></script>
		<!-- 引入自定义css文件 style.css -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css" />

		<style>
			body {
				margin-top: 20px;
				margin: 0 auto;
				width: 100%;
			}
			.carousel-inner .item img {
				width: 100%;
				height: 300px;
			}
		</style>
	</head>

	<body>
		<!-- 导入顶部页面 -->
		<jsp:include page="/jsp/head.jsp"></jsp:include>

			<c:forEach items="${pb.list }" var="p">
				<div class="col-md-2">
					<a href="${pageContext.request.contextPath }/product?method=getProductById&pid=${p.pid}">
						<img src="${p.pimage}" width="170" height="170" style="display: inline-block;">
					</a>
					<c:if test="${fn:length(p.pname)>10}">
							<p><a href="${pageContext.request.contextPath }/product?method=getProductById&pid=${p.pid}" style='color:green'>${fn:substring(p.pname,0,10) }..</a></p>
							</c:if>
							<c:if test="${fn:length(p.pname)<=10}">
							<p><a href="${pageContext.request.contextPath }/product?method=getProductById&pid=${p.pid}" style='color:green'>${p.pname}</a></p>
							</c:if>
					<p><font color="#FF0000">商城价：&yen;${p.shop_price }</font></p>
				</div>
			</c:forEach>
		</div>

		<!--分页 -->
		<div style="width:380px;margin:0 auto;margin-top:50px;">
			<ul class="pagination" style="text-align:center; margin-top:10px;">
				<!-- 判断上一页按钮能不能用 -->
				<c:if test="${pb.currPage==1 }">
				<li class="disabled"><a href="javascript:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
				</c:if>
				<c:if test="${pb.currPage!=1 }">
				<li><a href="${pageContext.request.contextPath }/product?method=findByPage&cid=${param.cid }&currPage=${pb.currPage-1}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
				</c:if>
				
				<c:forEach begin="${pb.currPage-5>0?pb.currPage:1 }" end="${pb.currPage+4<totalPage?pb.currPage+4:pb.totalPage}" var="p">
					<c:if test="${p!=pb.currPage }">
						<li><a  href="${pageContext.request.contextPath }/product?method=findByPage&cid=${param.cid }&currPage=${p}">${p}</a></li>
					</c:if>
					<c:if test="${p==pb.currPage }">
						<li class="active"><a  href="jacvascript:void(0)">${p}</a></li>
					</c:if>
				</c:forEach>
				
				<!-- 判断下一页按钮是否可用 -->
				<c:if test="${pb.currPage==pb.totalPage }">
				<li class="disabled">
					<a href="javascript:void(0)" aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
				<c:if test="${pb.currPage!=pb.totalPage }">
				<li>
					<a href="${pageContext.request.contextPath }/product?method=findByPage&cid=${param.cid }&currPage=${pb.currPage+1}" aria-label="Next">
						<span aria-hidden="true">&raquo;</span>
					</a>
				</li>
				</c:if>
			</ul>
		</div>
		<!-- 分页结束=======================        -->

		<!--
       		商品浏览记录:
        -->
		<div style="width:1210px;margin:0 auto; padding: 0 9px;border: 1px solid #ddd;border-top: 2px solid #999;height: 246px;">

			<h4 style="width: 50%;float: left;font: 14px/30px " 微软雅黑 ";">浏览记录</h4>
			<div style="width: 50%;float: right;text-align: right;"><a href="">more</a></div>
			<div style="clear: both;"></div>

			<div style="overflow: hidden;">
				<ul style="list-style: none;">
					<c:forEach items="${plist }" var="l">
					
					<a href="${pageContext.request.contextPath }/product?method=getProductById&pid=${l.pid}">
					
					<li style="width: 150px;height: 216;float: left;margin: 10px 8px 0 0;padding: 0 18px 15px;text-align: center;"><img src="${l.pimage}" width="130px" height="130px" /></li>
				
					</a>
					</c:forEach>
				</ul>
			</div>
		</div>
		<div style="margin-top:50px;">
			<img src="${pageContext.request.contextPath}/image/footer.jpg" width="100%" height="78" alt="我们的优势" title="我们的优势" />
		</div>

		<div style="text-align: center;margin-top: 5px;">
			<ul class="list-inline">
				<li><a>关于我们</a></li>
				<li><a>联系我们</a></li>
				<li><a>招贤纳士</a></li>
				<li><a>法律声明</a></li>
				<li><a>友情链接</a></li>
				<li><a target="_blank">支付方式</a></li>
				<li><a target="_blank">配送方式</a></li>
				<li><a>服务声明</a></li>
				<li><a>广告声明</a></li>
			</ul>
		</div>
		<div style="text-align: center;margin-top: 5px;margin-bottom:20px;">
			Copyright &copy;版权所有
		</div>

	</body>

</html>