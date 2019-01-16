console.log("websocket module...")

var ws =(function(){
	
	function makeSocket(){
    if(ws!==undefined && ws.readyState!==WebSocket.CLOSED){
        writeResponse("WebSocket is already opened.");
        return;
    }
    //웹소켓 객체 만드는 코드
    ws=new WebSocket("ws://localhost:8080/rnb/echo.do");
    
    ws.onopen=function(event){
        if(event.data===undefined) return;
        
        writeResponse(event.data);
    };
    //서버로 부터 데이터를 받을 때
    ws.onmessage=function(event){
        writeResponse(event.data);
    };
    ws.onclose=function(event){
        writeResponse("Connection closed");
    }
}
//서버로 대화글 전송
function send(message){
    var text = message
	ws.send(text);
    text="";
}

function closeSocket(){
    ws.close();
}
function writeResponse(text,callback){
    messages.innerHTML+="<br/>"+text;
}
    return {writeResponse:writeResponse,
    	closeSocket:closeSocket,
    	send:send,
		add:add};
})();