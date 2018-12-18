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
			<h1 class="page-header">Board Register</h1>
		</div>
		<!-- /.col-lg-12 -->
	</div>
	<!--  게시판 글 등록 -->
	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-default">

				<div class="panel-heading">Board Register</div>
				<div class="panel-body">
					<form role="form" action="/board/register" method="post">
						<div class="form-group">
							<label>Title</label> <input class="form-control" name='title'>
						</div>
						<div class="form-group">
							<label>Text area</label>
							<textarea class="form-control" rows="3" name='content'></textarea>
						</div>
						<div class="form-group">
							<label>Writer</label> <input class="form-control" name='writer'
							value='<sec:authentication property="principal.username"/>' readonly="readonly">
						</div>
						<input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
						<button type="submit" class="btn btn-default">Submit</button>
						<button type="reset" class="btn btn-default">Reset Button</button>
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
	<script>
	$(document).ready(function () {
		var formobj = $("form[role='form']");
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxsize = 5242880;
		$("button[type='submit']").on("click",function(e){
			e.preventDefault();
			
			var str = "";
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
		})

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
		
		var csrfname = "${_csrf.headerName}";
		var csrftokenvalue = "${_csrf.token}";

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
				beforeSend:function(xhr){
					xhr.setRequestHeader(csrfname,csrftokenvalue);
				},
				data:formData,
				type:'post',
				datatype:'json',
				success:function(result){
					showUploadResult(result);
				}
			})
		})
		$(".uploadResult").on("click","button",function(e){
			let filePath = $(this).data("file");
			let fileType = $(this).data("type");
			let targetLi = $(this).closest("li");
			$.ajax({
				url:"/deleteFile",
				data:{fileName:filePath,type:fileType},
				beforeSend:function(xhr){
					xhr.setRequestHeader(csrfname,csrftokenvalue);
				},
				dataType:"text",
				type:"post",
				success:function(result){
					alert(result);
					targetLi.remove();
				}
			})
		})
		


	});
	</script>
</body>
</html>