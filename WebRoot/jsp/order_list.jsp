<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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
	
		<div class="container">
			<div class="row">

				<div style="margin:0 auto; margin-top:10px;width:950px;">
					<strong>我的订单</strong>
					<table class="table table-bordered">
					<!-- 一个订单 -->
					<c:forEach items="${pb.list }" var="order">
						<tbody>
							<tr class="success">
								<th colspan="5">订单编号:${order.oid } 总金额${order.total}
									<!-- 判断订单状态 -->
									<c:if test="${order.state==0 }">
										<a href="${pageContext.request.contextPath }/order?method=getById&oid=${order.oid }">付款</a>
									</c:if>
									<c:if test="${order.state==1 }">
										已付款
									</c:if>
									<c:if test="${order.state==2 }">
										<a href="${pageContext.request.contextPath }/order?method=updateState&oid=${order.oid }&state=3">确认收货</a>
									</c:if>
									<c:if test="${order.state==3 }">
										已收货
									</c:if>
								 </th>
							</tr>
							<c:forEach items="${order.items }" var="oi">
							<tr class="warning">
								<th>图片</th>
								<th>商品</th>
								<th>价格</th>
								<th>数量</th>
								<th>小计</th>
							</tr>
							<tr class="active">
								<td width="60" width="40%">
									<input type="hidden" name="id" value="22">
									<img src="${pageContext.request.contextPath}/${oi.product.pimage}" width="70" height="60">
								</td>
								<td width="30%">
									<a target="_blank" href="${pageContext.request.contextPath }/product?method=getProductById&pid=${oi.product.pid}">${oi.product.pname }</a>
								</td>
								<td width="20%">
									￥${oi.product.shop_price }
								</td>
								<td width="10%">
									${oi.count }
								</td>
								<td width="15%">
									<span class="subtotal">￥${oi.subtotal }</span>
								</td>
							</tr>
							</c:forEach>
						</tbody>
						</c:forEach>
						
					</table>
				</div>
			</div>
			<div style="text-align: center;">
				
				<ul class="pagination">
					<!-- 判断是否为第一页 -->
					<c:if test="${pb.currPage==1 }">
						<li class="disabled"><a href="javasctipt:void(0)" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
					</c:if>
					<c:if test="${pb.currPage!=1 }">
						<li><a href="${pageContext.request.contextPath }/order?method=findAllOrder&currPage=${pb.currPage-1}" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
					</c:if>

					<!-- 遍历页码 -->
					<c:forEach begin="${pb.currPage-5>0?pb.currPage-5:1 }" end="${pb.currPage+4<pb.totalPage?pb.currPage+4:pb.totalPage }" var="n">
						<c:if test="${pb.currPage==n }">
							<li class="active"><a href="javascript:void(0)">${n }</a></li>
						</c:if>
						<c:if test="${pb.currPage!=n }">
							<li><a href="${pageContext.request.contextPath }/order?method=findAllOrder&currPage=${n}">${n }</a></li>
						</c:if>
					</c:forEach>
					
					<!-- 判断是否为最后一页 -->
					<c:if test="${pb.currPage==pb.totalPage }">
						<li class="disabled"><a href="javasctipt:void(0)" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
					</c:if>
					<c:if test="${pb.currPage!=pb.totalPage }">
						<li><a href="${pageContext.request.contextPath }/order?method=findAllOrder&currPage=${pb.currPage+1}" aria-label="Next"><span aria-hidden="true">&raquo;</span></a></li>
					</c:if>
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
			Copyright &copy; 版权所有
		</div>
	</body>

</html>