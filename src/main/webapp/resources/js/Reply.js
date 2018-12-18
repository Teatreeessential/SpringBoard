console.log("reply module..");

var replyService = (function(){
	function add(reply,callback,error){
		$.ajax({
			type:'post',
			url:'/replies/new',
			data:JSON.stringify(reply),
			contentType:'application/json; charset=utf-8',
			success: function(result,status,xhr){
				console.log("여기까지 오나ㅏ?");
				if(callback){
					callback(result);
				}
			},
			error:function(xhr,status,er){
				if(error){
					error(er);
				}
			}
		})
	}
	function getList(param,callback,error){
		var bno = param.bno;
		var page = param.page || 1;
		
		$.getJSON("/replies/pages/"+bno+"/"+page+".json",
				function(data){
					if(callback){
						callback(data.replyCnt,data.list);	
					}
				}).fail(function(xhr,status,err){
					if(error){
						error();
					}
				});
	}
	function remove(rno,replyer,callback,error){
		$.ajax({
			type:'delete',
			url: '/replies/'+rno,
			data: JSON.stringify({rno:rno,replyer:replyer}),
			contentType:"application/json;charset=utf-8",
			success: function(deleteResult,status,xhr){
				if(callback){
					callback(deleteResult);
				}
			},
			error: function(xhr,status,er){
				if(error){
					error(er);
				}
			}
		})
	}
	function update(ReplyVO,callback,error){
		$.ajax({
			type:'put',
			url:'/replies/'+ReplyVO.rno,
			data:JSON.stringify(ReplyVO),
			contentType:'application/json; charset=utf-8',
			success:function(result,status,xhr){
				if(callback){
					callback(result);
				}
			},
			error:function(xhr,status,er){
				if(error){
					error(er);
				}
			}
		});
	}
	function get(rno,callback,error){
		$.ajax({
			type:'get',
			url:'/replies/'+rno,
			success:function(result,status,xhr){
				if(callback){
					callback(result);
				}
			},
			error:function(xhr,status,er){
				if(error){
					error(er);
				}
			}
		});
	}
	function displayTime(timeValue){
		let today = new Date();
		let gap = today.getTime() - timeValue;
		
		let dateObj = new Date(timeValue);
		
		if(gap<(1000*60*60*24)){
			
			let hh = dateObj.getHours();
            let mm = dateObj.getMinutes();
			let ss = dateObj.getSeconds();
			
			return [(hh>9?'':'0')+hh,':',(mm>9?'':'0')+mm,':',(ss>9?'':'0')+ss].join('');

		}else{
			let year = dateObj.getFullYear();
			let month = dateObj.getMonth();
			let dd = dateObj.getDay();
			
			return [year,'/',(month>9?'':'0')+month,'/',(dd>9?'':'0')+dd].join('');
		}
	}
	return {displayTime:displayTime,
			get:get,
			update:update,
			add:add,
			getList:getList,
			remove,remove};
})();