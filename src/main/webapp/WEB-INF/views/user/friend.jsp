<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="sec" %>


<!DOCTYPE html>
<html>
<head>
</script>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
.notification {
	position: absolute;
	background-color: #FB404B;
	text-align: center;
	border-radius: 10px;
	min-width: 18px;
	padding: 0 5px;
	height: 18px;
	font-size: 12px;
	color: #FFFFFF;
	font-weight: bold;
	line-height: 18px;
	top: 0px;
	right: 10px;
}
  /* The Modal (background) */
        .modal {
            display: none; /* Hidden by default */
            position: fixed; /* Stay in place */
            z-index: 1; /* Sit on top */
            left: 0;
            top: 0;
            width: 100%; /* Full width */
            height: 100%; /* Full height */
            overflow: auto; /* Enable scroll if needed */
            background-color: rgb(0,0,0); /* Fallback color */
            background-color: rgba(0,0,0,0.4); /* Black w/ opacity */
        }
    
        /* Modal Content/Box */
        .modal-content {
            background-color: #fefefe;
            margin: 15% auto; /* 15% from the top and centered */
            padding: 30px;
            border: 1px solid #888;
            width: 65%; /* Could be more or less, depending on screen size */                          
        }
        /* The Close Button */
        .close {
            color: #aaa;
            float: right;
            font-size: 28px;
            font-weight: bold;
        }
        .close:hover,
        .close:focus {
            color: black;
            text-decoration: none;
            cursor: pointer;
        }
.chat-panel {
    height: 350px;
    overflow-y: auto;
}
</style>
</head>
<body>
	<%@include file="../includes/header.jsp"%>

	<br>
	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-bell fa-fw"></i> 친구요청
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group">

					<c:forEach var="item" items="${requests}">

						<a href="#" class="list-group-item">

							<div class="row">
								<span class="userid1 col-md-3">${item.userid1}</span> <span
									class="request_date col-md-5"><fmt:formatDate
										value="${item.time}" pattern="yyyy-MM-dd" /></span> <span
									class="pull-right">
									<button type="button" class="btn btn-info btn-circle accept">
										<i class="fa fa-check"></i>
									</button>
									<button type="button" class="btn btn-danger btn-circle cancel">
										<i class="fa fa-times"></i>
									</button>
								</span>
							</div>
						</a>
					</c:forEach>
				</div>
				<!-- /.list-group -->
				<a href="#" class="btn btn-default btn-block">View All Alerts</a>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>

	<div class="col-lg-4">
		<div class="panel panel-default">
			<div class="panel-heading">
				<i class="fa fa-user fa-fw"></i> 친구
			</div>
			<!-- /.panel-heading -->
			<div class="panel-body">
				<div class="list-group">

					<c:forEach var="item" items="${friends}">

						<a href="#" class="list-group-item">
							<div class="row">
								<span class="friend">${fn:substringBefore(item,'_')}</span>
								 <span class="notification hidden-xs" opponent_userid="${fn:substringBefore(item,'_')}">${fn:substringAfter(item,'-')}</span>
								<button class="btn btn-primary pull-right chat_button"
									 data-chat_room_num="${fn:substring(item,fn:indexOf(item,'_')+1,fn:indexOf(item,'-'))}">
									<i class="fa fa-comments"></i> 채팅하기
								</button>
								</button>
							</div>
						</a>
					</c:forEach>
				</div>
				<!-- /.list-group -->
				<a href="#" class="btn btn-default btn-block">View All Alerts</a>
			</div>
			<!-- /.panel-body -->
		</div>
	</div>
	<!-- 모달 채팅창 -->
	<div class="modal">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-hidden="true">×</button>
					<h4 class="modal-title" id="myModalLabel">채팅창</h4>
				</div>

				<div class="chat-panel">
					<ul class="chat">
						<!-- 채팅 내용 달려 있음 -->
					</ul>
				</div>

				<div class="panel-footer">
					<div class="input-group">
						<input id="btn-input" type="text" class="form-control input-sm"
							placeholder="Type your message here..."> 
						<span class="input-group-btn">
							<button class="btn btn-warning btn-sm" id="btn-chat">
								Send</button>
						</span>
					</div>
				</div>


			</div>
			<!-- /.modal-content -->
		</div>
		<!-- /.modal-dialog -->
	</div>
	<!-- 해당 페이지 로그인이 되었을 때  -->
	<sec:authorize access="isAuthenticated()">
		<sec:authentication property="principal.username" var="userid"/>
	</sec:authorize>


</body>
<script>
$(document).ready(function() {
	//date 포맷하기위해서 만듬
	Date.prototype.format = function(f) {
	    if (!this.valueOf()) return " ";
	 
	    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
	    var d = this;
	     
	    return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
	        switch ($1) {
	            case "yyyy": return d.getFullYear();
	            case "yy": return (d.getFullYear() % 1000).zf(2);
	            case "MM": return (d.getMonth() + 1).zf(2);
	            case "dd": return d.getDate().zf(2);
	            case "E": return weekName[d.getDay()];
	            case "HH": return d.getHours().zf(2);
	            case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
	            case "mm": return d.getMinutes().zf(2);
	            case "ss": return d.getSeconds().zf(2);
	            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
	            default: return $1;
	        }
	    });
	};
	String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	Number.prototype.zf = function(len){return this.toString().zf(len);};
	


	var chat = {};
	// Get the modal
    var modal = document.getElementById('myModal');
                               
    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }
    }

	
	
	$(".cancel").on("click", function() {
		let userid = $(this).closest(".list-group-item").find(".userid1").text();
		$.ajax({
			url:"/user/removefriend",
			type:"get",
			data:{userid:userid},
			success:function(result){
				alert(result);
				location.reload();
			}
		})

	})
	$(".accept").on("click", function() {
		let userid = $(this).closest(".list-group-item").find(".userid1").text();
		$.ajax({
			url:"/user/acceptfriend",
			type:"get",
			data:{userid:userid},
			success:function(result){
				alert(result);
				location.reload();
			}
		})
	})
	$(".chat_button").on("click",function(){
		let button = $(this)
		let chat_room_num = button.data("chat_room_num");
		let opponent_userid = button.closest(".list-group-item").find(".friend").text();
		console.log(typeof opponent_userid);
		$.ajax({
			url:"/reset",
			data:{chatRoomNum:chat_room_num},
			success:function(result){
				button.closest(".list-group-item").find(".notification").text(result);
				console.log(button.closest(".list-group-item").find(".notification").text(result));
				
			}
		})
		$.ajax({
			url:"/chat",
			type:"get", 
			data:{chatRoomNum:chat_room_num},
			async: false,
			success:function(result){
				console.log(result)
				let str = "";				
				if(null!==result){
					for(let i=0;i<result.length;i++){
						//userid가 상대방것일 경우 왼쪽으로
						if(result[i].userid!=`${userid}`){
								str	+=	`<li class="left clearfix"><span class="chat-img pull-left">`;
								str +=	`<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle">`;
								str	+= 	`</span>`
								str	+=	`<div class="chat-body clearfix">`;
								str	+=	`<div class="header">`;
								str	+=	`<strong class="primary-font">`+result[i].userid+`</strong>`;
								str	+=	`<small class="pull-right text-muted"><i class="fa fa-clock-o fa-fw"></i>`+result[i].message_date+`</small>`;
								str	+=	`</div>`;
								str	+=	`<p>`+result[i].message+`</p>`;
								str	+=	`</div></li>`;
						//userid가 내것일 경우 우측으로 정렬
						}else{
								str	+=	`<li class="right clearfix"><span class="chat-img pull-right">`;
								str +=	`<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar" class="img-circle">`;
								str	+= 	`</span>`
								str	+=	`<div class="chat-body clearfix">`;
								str	+=	`<div class="header">`;
								str	+=	`<small class=" text-muted"><i class="fa fa-clock-o fa-fw"></i>`+result[i].message_date+`</small>`
								str	+=	`<strong class="pull-right primary-font">`+result[i].userid+`(나)</strong>`;
								str	+=	`</div>`;
								str	+=	`<p class="pull-right">`+result[i].message+`</p>`;
								str	+=	`</div></li>`;
						}
						
					}	
				}
				
				chat.chat_room_num = chat_room_num;
				chat.opponent_userid = opponent_userid;
				chat.userid = `${userid}`;
				chat.message_date =new Date().format("yyyy.MM.dd.hh:mm:ss")
				$(".chat").append(str);
				$(".modal").css('display','block');
				$(".chat-panel").scrollTop($(".chat-panel")[0].scrollHeight);
			}
			
		})
	})
	//모달을 종료할 경우 기존에 있던 채팅이 다사라지게끔..
	$(".close").on("click",function(){
		$(".chat").empty();
		$(".modal").css('display','none');
	})
	
	$("#btn-input").on("keydown",function(key){
		if(key.keyCode === 13){
			//chat객체 내의 message는 꾸준히 바뀔 것
			chat.message = $(this).val();
			//보내기전 대화를 append
			writeResponse(chat);
			console.log(chat);
			//send로 보낸 뒤 db에 저장
			send(chat);
			//보낸뒤 text부분은 다시 초기화 시켜야하므로
			$(this).val("");
			
		}
	})
	$("#btn-chat").on("click",function(key){
			chat.message = $(this).closest(".input-group").find("#btn-input").val();
			writeResponse(chat);
			send(chat);
			$(this).closest(".input-group").find("#btn-input").val("");
	})
	
	//웹소켓 생성 및 정의 소켓은 해당페이지 방문시 바로 함수실행 후 생성
	var ws;
	var url = "ws://localhost:8090/Chatting"
	makeSocket();
	function makeSocket(){
      	ws=new WebSocket(url);
        
        ws.onopen=function(event){
            if(event.data===undefined) return;
        };
        ws.onmessage=function(event){
        	
        	let data = JSON.parse(event.data);
        	console.log(data);
        	$(".notification[opponent_userid='"+data.userid+"']").text(data.count);
        	writeResponse(data,1);
        	
        };
        ws.onclose=function(event){
            writeResponse("Connection closed");
        }
    };
   
    function send(chat){
        var message = JSON.stringify(chat);
        //서버로 데이터를 보냄
        ws.send(message);
    }
    
    function closeSocket(){
        ws.close();
    }
    function writeResponse(chat,who){
    	//userid가 나, opponent_userid가 상대 현재 채팅
    	//who 1은 상대방 0은 나
    	let str="";
    	if(who===1){
    		str	+=	`<li class="left clearfix"><span class="chat-img pull-left">`;
			str +=	`<img src="http://placehold.it/50/55C1E7/fff" alt="User Avatar" class="img-circle">`;
			str	+= 	`</span>`
			str	+=	`<div class="chat-body clearfix">`;
			str	+=	`<div class="header">`;
			str	+=	`<strong class="primary-font">`+chat.userid+`</strong>`;
			str	+=	`<small class="pull-right text-muted"><i class="fa fa-clock-o fa-fw"></i>`+chat.message_date+`</small>`;
			str	+=	`</div>`;
			str	+=	`<p>`+chat.message+`</p>`;
			str	+=	`</div></li>`;
		}else{
			str	+=	`<li class="right clearfix"><span class="chat-img pull-right">`;
			str +=	`<img src="http://placehold.it/50/FA6F57/fff" alt="User Avatar" class="img-circle">`;
			str	+= 	`</span>`
			str	+=	`<div class="chat-body clearfix">`;
			str	+=	`<div class="header">`;
			str	+=	`<small class=" text-muted"> <i class="fa fa-clock-o fa-fw"></i>`+chat.message_date+`</small>`;
			str	+=	`<strong class="pull-right primary-font">`+chat.userid+`(나)</strong>`;
			str	+=	`</div>`;
			str	+=	`<p class="pull-right">`+chat.message+`</p>`;
			str	+=	`</div></li>`;
		}
    	$(".chat").append(str);
    	//스크롤 당기기
    	$(".chat-panel").scrollTop($(".chat-panel")[0].scrollHeight);
		
    }
	
})



</script>
</html>