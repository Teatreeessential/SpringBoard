<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>
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
					<form role="form" action="/board/modify" method="post">
						<div class="form-group">
							<label>bno</label> <input class="form-control" name='bno'
								readonly="readonly" value="<c:out value="${board.bno}"/>">
						</div>
						<div class="form-group">
							<label>Title</label> <input class="form-control" name='title'
								value="<c:out value="${board.title}"/>">
						</div>
						<div class="form-group">
							<label>Text area</label>
							<textarea class="form-control" rows="3" name='content'><c:out
									value="${board.content}" /></textarea>
						</div>
						<div class="form-group">
							<label>Writer</label> <input class="form-control" name='writer' readonly="readonly"
								  value="<c:out value="${board.writer}"/>">
						</div>
						<input type ="hidden" name="pageNum" value="<c:out value='${cri.pageNum}'/>"/>
						<input type ="hidden" name="amount" value="<c:out value='${cri.amount}'/>"/>
						<input type ="hidden" name="keyword" value="<c:out value='${cri.keyword}'/>"/>
						<input type ="hidden" name="type" value="<c:out value='${cri.type}'/>"/>
						<sec:authentication property="principal" var="pinfo"/>
						<sec:authorize access="isAuthenticated()">
							<c:if test="${pinfo.username eq board.writer }">
							<button data-oper="modify" type="submit" class="btn btn-default">
								Modify</button>
							<button data-oper="remove" type="submit" class="btn btn-danger">
								Remove</button>
							</c:if>
						</sec:authorize>
						<button data-oper="list" type="submit" class="btn btn-info">
							List</button>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token }"/>
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
				<div class="form-group uploadDiv">
					<input type="file" name="uploadFile" multiple="multiple">
				</div>
				<div class="uploadResult">
					<ul>
					</ul>
				</div>
			</div>
		</div>
	</div>

	<%@include file="../includes/footer.jsp"%>
	<script type="text/javascript">
		$(document).ready(function(){
			var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
			var maxsize = 5242880;
			//처음 수정페이지에서 어떤 첨부 파일이있는지를 보여줌
			(function(){
				let bno = "<c:out value='${board.bno}'/>";
				
				$.getJSON("/board/getAttachList",{bno:bno},function(arr){
					showUploadResult(arr);
				});
			})();
			
			function showUploadResult(uploadResultArr){
				
				if(!uploadResultArr || uploadResultArr.length == 0) {return;}

				let uploadUL = $(".uploadResult ul");

				let str = "";

				$(uploadResultArr).each(function(i,obj){
					console.log(obj)
					if(obj.fileType){
						let fileCallPath = encodeURIComponent(obj.uploadPath+"\\s_"+obj.uuid+"_"+obj.fileName);
						console.log(fileCallPath);
						str += "<li data-path='"+obj.uploadPath+"'"
						str += " data-type='"+true+"'"
						str += " data-filename='"+obj.fileName+"'"
						str += " data-uuid='"+obj.uuid+"'"
						str +="><div>"
						str += "<span>"+obj.fileName+"</span>"
						str += "<button type='button' class='btn btn-warning btn-circle'"
						str += "data-type='image' data-file='"+fileCallPath+"'>"
						str += "<i class='fa fa-times'></i></button><br>"
						str += "<img src='/display?fileName="+fileCallPath+"'></div></li>"
					}else{
						let fileCallPath = encodeURIComponent(obj.uploadPath+"\\"+obj.uuid+"_"+obj.fileName);
						str += "<li data-path='"+obj.uploadPath+"'"
						str += " data-type='"+false+"'"
						str += " data-filename='"+obj.fileName+"'"
						str += " data-uuid='"+obj.uuid+"'"
						str +="><div>"
						str += "<a href='/download?fileName="+fileCallPath+"'>"
						str += "<span>"+obj.fileName+"</span></a>"
						str += "<button type='button' class='btn btn-warning btn-circle'"
						str += "data-type='file' data-file='"+fileCallPath+"'>"
						str += "<i class='fa fa-times'></i></button><br>"
						str += "<img src='/resources/attach.png'></div></li>"
					
					}
				})

				uploadUL.append(str);
			}
			//첨부파일 추가 시 파일이름과 사이즈 체크
			function checkExtension(fileName,fileSize){
				if(fileSize>maxsize){
					alert("파일 사이즈를 초과 했습니다.")
					return false;
				}
				if(regex.test(fileName)){
					alert("올바르지 않은 확장자 입니다.");
					return false;
				}
				return true;

			}
			//첨부파일 추가
			var csrfname ="${_csrf.headerName}";
			var csrfvalue = "${_csrf.token}"
			$("input[type='file']").on("change",function(e){
				let formData = new FormData();

				let inputFile = $("input[name='uploadFile']");
				let files = inputFile[0].files;
				
				for(let i = 0;i<files.length;i++){
					if(!checkExtension(files[i].name,files[i].size)){
						return false;
					}
					formData.append("uploadFile",files[i]);
				}
				$.ajax({
					url:'/uploadAjaxAction',
					processData:false,
					contentType:false,
					beforeSend: function(xhr){
						xhr.setRequestHeader(csrfname,csrfvalue);
					},
					data:formData,
					type:'post',
					datatype:'json',
					success:function(result){
						showUploadResult(result);
					}
				})
			})
			//첨부파일 삭제 버튼 실제 삭제 되진 않음.
			$(".uploadResult ul").on("click","button",function(e){
				console.log("delete 첨부파일");
				let str = "";
				
				if(confirm("remove this file?")){
					let targetLi = $(this).closest("li");
					targetLi.remove();
				}
			
			})
			
			//삭제,수정,리스트 버튼
			var formobj = $("form");
			
			$("button").on("click",function(e){
				e.preventDefault();
				var operation = $(this).data("oper");
				
				if(operation === 'remove'){
					formobj.attr("action", "/board/remove");
				}else if(operation === 'list'){
					formobj.attr("action", "/board/list").attr("method","get");
					
					var pageNum = $("input[name='pageNum']").clone();
					var amount = $("input[name='amount']").clone();
					var keyword = $("input[name='keyword']").clone();
					var type = $("input[name='type']").clone();
					
					
					formobj.empty(); //formobj를 감싸고 있는 모든 form group삭제
					formobj.append(keyword);
					formobj.append(type);
					formobj.append(pageNum);
					formobj.append(amount);
					
				}else if(operation === 'modify'){
					let str = ""
					
					$(".uploadResult ul li").each(function(i,obj){
						let jobj = $(obj);
						
							
						console.log(obj);
						console.log(jobj)
						str += "<input type='hidden' name='attachList["+i+"].fileName'"
						str += "value='"+jobj.data("filename")+"'>"
						
						str += "<input type='hidden' name='attachList["+i+"].uuid'"
						str += "value='"+jobj.data("uuid")+"'>"
						
						str += "<input type='hidden' name='attachList["+i+"].uploadPath'"
						str += "value='"+jobj.data("path")+"'>"
						
						str += "<input type='hidden' name='attachList["+i+"].fileType'"
						str += "value='"+jobj.data("type")+"'>"
						
						
					})
					formobj.append(str).submit();
				}
				formobj.submit();
			})
			
		})
	</script>


</body>
</html>