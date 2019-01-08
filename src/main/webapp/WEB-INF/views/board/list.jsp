<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page session="false"%>
<!DOCTYPE html>
<html lang="en">

<head>
</head>
<body>

	<%@include file="../includes/header.jsp"%>
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Tables</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<!-- /.row -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">
				<div class="panel-heading">
					Board List Page
					<button id="regBtn" type="button" class="btn btn-xs pull-right">
						Register New Board</button>
				</div>
				<!-- /.panel-heading -->
				<div class="panel-body">
					<table width="100%"
						class="table table-striped table-bordered table-hover"
						id="dataTables-example">
						<thead>
							<tr>
								<th>#번호</th>
								<th>제목</th>
								<th>작성자</th>
								<th>추천수</th>
								<th>작성일</th>
								<th>수정일</th>
								
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${list}" var="board" >
								<tr>
									<td><c:out value="${board.bno}" /></td>
									<td><a class="move" href="<c:out value="${board.bno}"/>">
											<c:out value="${board.title}" /> 
											<b>[<c:out value="${board.replyCnt}" />]</b>
									</a></td>
									<td>
									<span class="dropdown">
									<a href="#"class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true"><c:out value="${board.writer}" /></a>
									<!-- 친구추가 창 -->
									<ul class="dropdown-menu">
				                        <li><a href="${board.writer}" class="addfriend"><i class="fa fa-user fa-fw"></i> 친구추가</a>
				                        </li>
				                        <li><a href="${board.writer}" class="addfriend"><i class="fa fa-envelope fa-fw"></i>쪽지보내기</a>
				                        </li>
				                    </ul>
									</span>
									</td>
									
									<td><c:out value="${board.point}"/></td>
									<td><fmt:formatDate pattern="yyyy-MM-dd"
											value="${board.regdate}" /></td>
									<td><fmt:formatDate pattern="yyyy-MM-dd"
											value="${board.updateDate}" /></td>
								</tr>
							</c:forEach>
					</table>
					
					<!-- 검색 처리-->
					<form id="searchform" action="/board/list" method="get">
						<select name="type">
							<option value="" <c:out value="${pageMaker.cri.type==null? 'selected':'' }"/>>====</option>
							<option value="T" <c:out value="${pageMaker.cri.type eq T? 'selected':'' }"/>>제목</option>
							<option value="C" <c:out value="${pageMaker.cri.type eq C? 'selected':'' }"/>>내용</option>
							<option value="W" <c:out value="${pageMaker.cri.type eq W? 'selected':'' }"/>>작성자</option>
							<option value="TC" <c:out value="${pageMaker.cri.type eq TC? 'selected':'' }"/>>제목 or 내용</option>
							<option value="TW" <c:out value="${pageMaker.cri.type eq TW? 'selected':'' }"/>>제목 or 작성자</option>
							<option value="TWC" <c:out value="${pageMaker.cri.type eq TWC? 'selected':'' }"/>>제목 or 내용  or 작성자</option>
						</select>
						<input type="text" name="keyword" value='<c:out value="${pageMaker.cri.keyword}"/>'/>
						<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}"/>
						<input type="hidden" name="amount" value="${pageMaker.cri.amount}"/>
						<button class="btn btn-default">Search <i class="fa fa-search"></i></button>
					</form>
					<!-- 페이징 처리 -->
					<div class="pull-right">
						<nav aria-label="Page navigation example">
							<ul class="pagination">
								<c:if test="${pageMaker.prev}">
									<li class="paginate_button">
									<a href="${pageMaker.startPage-1 }">Previous</a></li>
								</c:if>
								<c:forEach var="num" begin="${pageMaker.startPage}"
									end="${pageMaker.endPage}">
									<li class="paginate_button ${pageMaker.cri.pageNum == num? "active":""}">
										<a href="${num}">${num}</a>
									</li>
								</c:forEach>
								<c:if test="${pageMaker.next}">
									<li class="paginate_button">
									<a href="${pageMaker.endPage+1 }">Next</a></li>
								</c:if>
							</ul>							
						</nav>
						<!-- 페이지 클릭 시 이동 경로 -->
						<form id="actionform" action="/board/list" method="get">
							<input type="hidden" name="pageNum" value="${pageMaker.cri.pageNum}"/>
							<input type="hidden" name="amount" value="${pageMaker.cri.amount}"/>
							<input type="hidden" name="keyword" value="${pageMaker.cri.keyword}"/>
							<input type="hidden" name="type" value="${pageMaker.cri.type}"/>
						</form>
					</div>
					<!-- 모달창 추가  -->
					<div class="modal fade in" id="myModal" tabindex="-1" role="dialog"
						aria-labelledby="myModalLabel" aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-header">
									<button type="button" class="close" data-dismiss="modal"
										aria-hidden="true">×</button>
									<h4 class="modal-title" id="myModalLabel">Modal title</h4>
								</div>
								<div class="modal-body">처리가 완료 되었습니다.</div>
								<div class="modal-footer">
									<button type="button" class="btn btn-default"
										data-dismiss="modal">Close</button>
									<button type="button" class="btn btn-primary">Save
										changes</button>
								</div>
							</div>
							<!-- /.modal-content -->
						</div>
						<!-- /.modal-dialog -->
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- /.row -->


	<%@include file="../includes/footer.jsp"%>
	
	<script type="text/javascript">
		$(document).ready(function() {
					var result = '<c:out value="${result}"/>';
					checkmodal(result);
					var currentState = history.state;
					console.log(currentState);
					history.replaceState({}, null, null);

					$("#regBtn").on("click", function() {
						self.location = "/board/register";
					})
					

					function checkmodal(result) {
						if (result === '' || history.state) {
							return;
						}
						if (parseInt(result) > 0) {
							$(".modal-body").html(
									"게시글" + parseInt(result) + "번이 등록 되었습니다.");
							$("#myModal").modal("show");
						}
						if (result === 'success') {
							$("#myModal").modal("show");
						}
					}
					var actionform = $("#actionform");
					
					$(".paginate_button a").on("click", function(e){
						e.preventDefault();
						actionform.find("input[name='pageNum']").val($(this).attr("href"));
						actionform.submit();
					})
					
					$(".move").on("click",function(e){
						e.preventDefault();
						actionform.append("<input type='hidden' name='bno' value='"+
											$(this).attr("href")+"'>");
						actionform.attr("action","/board/get");
						actionform.submit();
					})
					
					var searchform = $("#searchform")
					
					$("#searchform button").on("click",function(e){
						if(!searchform.find("option:selected").val()){
							alert("검색종류를 선택하세요");
							return false;
						}
						if(!searchform.find("input[name='keyword']").val()){
							alert("키워드를 입력하세요");
							return false;
						}
						searchform.find("input[name='pageNum']").val("1");
						e.preventDefault();
						searchform.submit();
						
					})
					//친구 추가 창 관련 toggle
					$(".addfriend").on("click",function(e){
						e.preventDefault();
						let userid=$(this).attr("href")
						console.log("userid")
						$.ajax({
							type:'get',
							url:'/user/requestfriend',
							data:{userid:userid},
							success:function(result){
								alert(result);
							}
						})
					})
				})
	</script>

</body>

</html>
