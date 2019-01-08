<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>

<%@ page session="false"%>

<!DOCTYPE html>
<html>
<head>
</script>
<meta charset="UTF-8">
<title>Insert title here</title>
<style type="text/css">
.uploadResult {
			width:100%;
			background-color:gray;
		}
		.uploadResult ul{
			display:flex;
			flex-flow: row;
			justify-content:center;
			align-items:center;
		}
		.uploadResult ul li{
			list-style: none;
			padding: 10px;
		}
		.uploadResult ul li img{
			width: 20px;
		}
</style>
</head>
<body>
	<%@include file="../includes/header.jsp"%>
	<div class="row">
		<div class="col-lg-12">
			<h1 class="page-header">Board Read Page</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>

	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">

				<div class="panel-heading">Board Read Page</div>
				<div class="panel-body">
					<div class="form-group">
						<label>bno</label> <input class="form-control" name='bno'
							readonly="readonly" value="<c:out value="${board.bno}"/>">
					</div>
					<div class="form-group">
						<label>Title</label> <input class="form-control" name='title'
							readonly="readonly" value="<c:out value="${board.title}"/>">
					</div>
					<div class="form-group">
						<label>Text area</label>
						<textarea class="form-control" rows="3" name='content'
							readonly="readonly"><c:out value="${board.content}" /></textarea>
					</div>
					<div class="form-group">
						<label>Writer</label> <input class="form-control" name='writer'
							readonly="readonly" value="<c:out value="${board.writer}"/>">
					</div>
					<div class="board_button">
						<sec:authentication property="principal" var="pinfo"/>
							<sec:authorize access="isAuthenticated()">
								<c:if test="${pinfo.username eq board.writer}">
									<button data-oper="modify" class="btn btn-default">Modify</button>
								</c:if>
							</sec:authorize>
						
						<button data-oper="list" class="btn btn-info">List</button>
					</div></br>
					<!-- 현재 로그인 상태이고 추천한 상태일경우 빨간버튼 , 그렇지않을경우 노란 버튼, 비로그인 상태일경우 로그인 창으로 이동 -->
						<div class="recommend">
						<c:choose>
					         <c:when test = "${isrecommend}">
					         <button type="button" class="btn btn-danger" data-oper="cancel">추천취소</button>
					         </c:when>
					         <c:otherwise>
					         <button type="button" class="btn btn-warning" data-oper="recommend">추천</button>
					         </c:otherwise>
					    </c:choose>
					    </div>
					<form id="operForm" action="/board/modify" method="get">
						<input type="hidden" id="bno" name="bno"
							value="<c:out value="${board.bno}"/>"> <input
							type="hidden" name="pageNum"
							value="<c:out value="${cri.pageNum}"/>"> <input
							type="hidden" name="amount"
							value="<c:out value="${cri.amount}"/>"> <input
							type="hidden" name="keyword" value="${cri.keyword}" /> <input
							type="hidden" name="type" value="${cri.type}" />
					</form>
					</form>
				</div>

			</div>
		</div>
	</div>
	<!-- 첨부파일 -->
	<div class="col-lg-12">
		<div class="panel panel-default">

			<div class="panel-heading">Attach File</div>
			<div class="panel-body">
				<div class="uploadResult">
					<ul>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- 댓글창  -->

		<div class="panel-heading">
			<i class="fa fa-comments fa-fw"></i> Reply
			<sec:authorize access="isAuthenticated()">
				<button id="addreplyBtn" class="btn btn-xs pull-right">Register New Reply</button>
			</sec:authorize>
		</div>
		<!-- /.panel-heading -->
		<div class="panel-body">
			<ul class="chat">
				<!-- 실제 댓글 달리는 곳  -->
			</ul>
		</div>
		<div class="panel-footer">
		</div>

	<!-- 댓글 등록 모달창 -->
	<div class="modal fade in" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">reply modal</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<label>replyer</label> <input class="form-control" name='replyer'
							value="<c:out value="${board.bno}"/>" readonly="readonly">
					</div>
					<div class="form-group">
						<label>reply</label> <input class="form-control" name='reply'>
					</div>
					<div class="form-group">
						<label>reply date</label> <input class="form-control"
							name='replyDate' value="<c:out value="${board.bno}"/>">
					</div>

				</div>
				<div class="modal-footer">
					<button id="modalModBtn" type="button" class="btn btn-warning">Modify</button>
					<button id="modalRemoveBtn" type="button" class="btn btn-primary">Remove</button>
					<button id="modalRegisterBtn" type="button" class="btn btn-warning">Register</button>
					<button id="modalCloseBtn" type="button" class="btn btn-default"
						data-dismiss="modal">Close</button>
				</div>
			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>







<div></div>

</body>
<script type="text/javascript" src="/resources/js/Reply.js"></script>
<script type="text/javascript">

$(document).ready(
		
					
						function() {
							
							var bno = "<c:out value='${board.bno}'/>";
							
							(function(){
								$.getJSON("/board/getAttachList",{bno:bno},function(arr){
									showUploadResult(arr);
								});
							})();
							
							//추천수 관련 로직 이벤트 위임
							$(".recommend").on("click","button",function(){
								let button = $(this)
								let status = button.data("oper")
								console.log(button)
								console.log(status)
								if(status=="recommend"){
									$.ajax({
										type:'get',
										url:'/board/addpoint',
										data:{bno:bno},
										contentType:'application/json; charset=utf-8',
										success: function(result){
											if(result==="false"){
												alert("로그인 후 가능 합니다.");
											}else{
												alert(result);
												button.text('추천취소');
												button.attr({
													'class':'btn btn-danger'
												})
												button.data('oper','cancel')
											}
											
											
											
										}
											
									})
									
								}else{
									$.ajax({
										type:'get',
										url:'/board/minuspoint',
										data:{bno:bno},
										contentType:'application/json; charset=utf-8',
										success: function(result){
											if(result==="false"){
												alert("로그인 후 가능 합니다.");
											}else{
												alert(result);
												button.text('추천');
												button.attr({
													'class':'btn btn-warning'
												})
												button.data('oper','recommend')
											}
											
											
										}
											
									})
								}
							})
							
							
							var bnoValue = '<c:out value="${board.bno}"/>';
							var replyUL = $(".chat");
							var pageNum = 1;
							showList(pageNum);
							
							//reply관련 페이징과 리스트						
							
							let replyPageFooter = $(".panel-footer");
							
							
							function showReplyPage(replyCnt,pageNum){
								

									
								let endnum = Math.ceil(pageNum/10.0) * 10;
								let startnum = endnum - 9;
								let prev = startnum != 1;
								let next = false;
								
								
								
								if(replyCnt < endnum*10 ){
									endnum = Math.ceil(replyCnt/10.0)
								}
								if(endnum*10 < replyCnt){
									next = true;
								}

								let str = "<ul class='pagination pull-right'>";

								if(prev){
									str += "<li class='page-item'><a class='page-link' href='"+(startnum-1)+"'>Previous</a></li>"
								}
								for(let i = startnum;i<=endnum;i++){
									 let active = pageNum==i? "active":"";

									 str += "<li class='page-item "+active+"'><a class='page-link' href='"+i+"'>"+i+"</a></li>"
								}
								if(next){
									str += "<li class='page-item'><a class='page-link' href='"+(endnum+1)+"'>next</a></li>"
								}
								str += "</ul></div>";

								replyPageFooter.html(str);
							}
							//첨부파일 조회
							function showUploadResult(uploadResultArr){
								
								if(!uploadResultArr || uploadResultArr.length == 0) {return;}

								let uploadUL = $(".uploadResult ul");

								let str = "";

								$(uploadResultArr).each(function(i,obj){
									if(obj.fileType){
										let fileCallPath = encodeURIComponent(obj.uploadPath+"\\s_"+obj.uuid+"_"+obj.fileName);
										str += "<li data-path='"+obj.uploadPath+"'"
										str += " data-type='"+true+"'"
										str += " data-filename='"+obj.fileName+"'"
										str += " data-uuid='"+obj.uuid+"'"
										str +="><div>"
										str += "<a href='/download?fileName="+fileCallPath+"'>"
										str += "<span>"+obj.fileName+"</span></a><br>"
										str += "<img src='/display?fileName="+fileCallPath+"'></div></li>"
									}else{
										let fileCallPath = encodeURIComponent(obj.uploadPath+"\\"+obj.uuid+"_"+obj.fileName);
										str += "<li data-path='"+obj.uploadPath+"'"
										str += " data-type='"+false+"'"
										str += " data-filename='"+obj.fileName+"'"
										str += " data-uuid='"+obj.uuid+"'"
										str +="><div>"
										str += "<a href='/download?fileName="+fileCallPath+"'>"
										str += "<span>"+obj.fileName+"</span></a><br>"
										str += "<img src='/resources/attach.png'></div></li>"
									
									}
								})

								uploadUL.append(str);
							}

							function showList(page) {
								replyService
										.getList(
												{
													bno : bnoValue,
													page : page || 1
												},
												function(replyCnt,list) {
													
													if(page === -1){
														pageNum = Math.ceil(replyCnt/10.0);
														showList(pageNum);
														return;
													} 
													
													if (list === null
															|| list.length === 0) {
														replyUL.html("");
														return;
													} else {
														let str = "";
														for (let i = 0, len = list.length || 0; i < len; i++) {
															str += "<li class='left clearfix' data-rno='"+list[i].rno+"'>";
															str += "<div class='header'>"
															str += "<strong class='primary-font'>["
																	+ list[i].rno + "]" 
																	+ list[i].replyer
																	+ "</strong> "
															str += "<small class='pull-right text-muted'> "
															str += "<i class='fa fa-clock-o fa-fw'></i>"
																	+ replyService
																			.displayTime(list[i].replyDate)
																	+ "</small></div>"
															str += "<p>"
																	+ list[i].reply
																	+ "</p></div></li>"

														}
														
														replyUL.html(str);
														showReplyPage(replyCnt,page);
													}

												})
							}
							replyPageFooter.on("click","li a",function(e){
								e.preventDefault();
				
								
								let targetPageNum = $(this).attr("href");
								
								pageNum = targetPageNum;
								showList(pageNum);
							})
							
							//reply 등록 조회 수정 삭제
							var modal = $(".modal");
							var modalInputReply = modal
									.find("input[name='reply']");
							var modalInputReplyer = modal
									.find("input[name='replyer']");
							var modalInputReplyDate = modal
									.find("input[name='replyDate']");
							
							var modalModBtn = $("#modalModBtn");
							var modalRemoveBtn = $("#modalRemoveBtn");
							var modalRegisterBtn = $("#modalRegisterBtn");
							
							var replyer = null;
							
							<sec:authorize access="isAuthenticated()">
								replyer ='<sec:authentication property="principal.username"/>';
								var csrfname = "${_csrf.headerName}";
								var csrfvalue = "${_csrf.token}";

								
							
							</sec:authorize>
							
						

							
							$("#addreplyBtn").on(
									"click",
									function(e) {
										e.preventDefault();
										modal.find("input[name='replyer']").val(replyer);
										modalInputReplyDate.closest("div")
												.hide();
										modal.find(
												"button[id!='modalCloseBtn']")
												.hide();

										modalRegisterBtn.show();
										$(".modal").modal("show");
									});

							
							
							modalRegisterBtn.on("click",function(e){
								let reply = {
										replyer:modalInputReplyer.val(),
										reply:modalInputReply.val(),
										bno:bnoValue
								};
								replyService.add(reply,function(result){
									alert(result);
									
									modal.find("input").val("");
									modal.modal("hide");
									showList(1);
								})
								
							})
							//동적으로 생성된 html부분은 이벤트를 따로등록해주어야하는데 이때 이벤트위임을 통해서 처리
							$(".chat").on("click","li",function(e){
								var rno =$(this).data("rno");
		
								replyService.get(rno,function(ReplyVO){
									modalInputReply.val(ReplyVO.reply);
									modalInputReplyer.val(ReplyVO.replyer);
									modalInputReplyDate.val(replyService.displayTime(ReplyVO.replyDate)).attr("readonly","readonly");
									modal.data("rno",ReplyVO.rno);
									
									modal.find("button[id = 'modalRegisterBtn']").hide();
									modal.find("button[id!= 'modalRegisterBtn']").show();
									modal.modal("show");
									
								})
							})
							modalModBtn.on("click",function(e){
								let originalReplyer = modalInputReplyer.val();
								
								if(!replyer){
									alert('로그인 후 수정 할 수 있습니다.');
									modal.modal("hide");
									return;
								}
								if(originalReplyer != replyer){
									alert('자신이 작성한 댓글만 수정이 가능합니다.');
									modal.modal('hide');
									return;
								}
								
								let reply = {
										rno:modal.data("rno"),
										reply: modalInputReply.val(),
										replyer: replyer
								};
								
								replyService.update(reply,function(result){
									alert(result);
									modal.modal("hide");
									showList(pageNum);
								})
							})
							modalRemoveBtn.on("click",function(e){
								let rno = modal.data("rno");
								
								if(!replyer){
									alert('로그인 후 삭제가 가능합니다.');
									modal.modal('hide');
									return;
								}
								
								var orginalReplyer = modalInputReplyer.val();
								
								if(replyer != orginalReplyer){
									alert("자신이 작성한 댓글만 삭제가 가능합니다.");
									modal.modal("hide");
									return;
								}
								
								
								
								replyService.remove(rno,replyer,function(result){
									alert(result);
									modal.modal("hide");
									showList(pageNum);
								})
							})
						
						
							
							
							
							//게시판 버튼 관련 이벤트
							
							var operForm = $("#operForm");

							$(".board_button button").on("click", function(e) {

								var operation = $(this).data("oper");

								if (operation === "modify") {
									operForm.submit();
								} else {
									operForm.attr("action", "/board/list");
									operForm.find("#bno").remove();
									operForm.submit();
								}

							})
							
							
							

						})
	</script>
</html>