/*引入js*/
document.write("<script language=javascript src='/js/layer-ext.js'></script>");

/*全局方法*/
var loginUser;
var hideMenu = 0;


function alert(content){
	  layer.alert(content?content:"默认信息",{ title:'温馨提示',shadeClose: true, closeBtn: 0,btn:['确定']});
}
function error(content,data){
	if(!data){
		data = {};
	}
	var resJson = data.responseJSON;
	var content = resJson?(resJson.msg?resJson.msg:resJson.message):content;
	 layer.alert(content?content:"默认信息",{ title:'警告', icon:"layui-icon-face-cry",shadeClose: true, closeBtn: 0,btn:['确定']});
}
function checkZtree(data){
	 for(var i = 0; i < data.length; i++){
		   var node = this.getNodeByParam("id",data[i].id, null);
		   this.checkNode(node,true,false);
		   //special handle for chkDisabled
		   var chkDisabled = node.chkDisabled;
		   if(chkDisabled){
			   this.setChkDisabled(node,false,false,false);
			   this.checkNode(node,true,false);
			   this.setChkDisabled(node,true,false,false);
		   }
		   if(data[i].children){
			   checkZtree.call(this,data[i].children);
		   }
	 }
}
/** 将text中的html字符转义， 仅转义  ', ", <, > 四个字符
      * @param  { String } str 需要转义的字符串
      * @returns { String }     转义后的字符串 
      */
function unhtml(str) {
         return str ? str.replace(/[<">']/g, (a) => {
             return {
                 '<': '&lt;',
                 '"': '&quot;',
                '>': '&gt;',
                 "'": '&#39;'
             }[a]
         }) : '';
}
function isJson(data) {
    var isJson = false
    try {
        // this works with JSON string and JSON object, not sure about others
       var json = $.parseJSON(data);
       isJson = typeof json === 'object' ;
    } catch (ex) {
        console.warn('data is not JSON');
    }
    return isJson;
}

layui.use(['layer'], function(){
  var layer = layui.layer
//左侧导航栏收缩功能动画效果
	$('#LAY_app_flexible').click(function(){
		//这里定义一个全局变量来方便判断动画收缩的效果,也就是放在最外面
		if(hideMenu==0){
			$(this).children().removeClass("layui-icon-shrink-right");
			$(this).children().addClass("layui-icon-spread-left");
			$("#LAY_app").addClass("layadmin-side-shrink");
			hideMenu++;
		}else{
			$(this).children().addClass("layui-icon-shrink-right");
			$(this).children().removeClass("layui-icon-spread-left");
			$("#LAY_app").removeClass("layadmin-side-shrink");
			hideMenu--;
		}		
		
	});
  });
if (window.top!=null && window.top.document.URL!=document.URL && document.URL.indexOf("login.html") != -1){    
    window.top.location= document.URL;     
	 }  


jQuery.fn.extend({
	
	  hasAttr:function(name){
		  var attr = $(this).attr(name);
			// For some browsers, `attr` is undefined; for others,
			// `attr` is false.  Check for both.
			if (typeof attr !== typeof undefined && attr !== false) {  
				return true;
			}
			return false;
	  },
	  resetForm:function(isAdd){
			  $(this)[0].reset();
			  if(isAdd){
				  $("input[type='hidden']", $(this)).val('');
			  }
			 
	  }, 
	  fillForm:function(form,data){
		  $(this).find("input[type=checkbox]").prop("checked",true);
		  for (var name in data) {
			  if(name == 'enable'){
					$(this).find('input[name="'+name+'"]').prop("checked",data[name]=='YES');
				}else{
					$(this).find('input[name="' + name + '"],select[name="' + name + '"],radio[name="' + name + '"],textarea[name="' + name + '"]').val(data[name]);
				}
	        }
		  form.render("checkbox",$(this).attr("lay-filter"));
		
	  },
	  select:function(form,config){
		  var e = $(this);
		  if(config.url){
			  $.ajax({url:config.url,type:'get',async:false})
				.done(function(data){
					config.data = data;
				});
		  }
		  for(var i = 0; i < e.length; i++){
				for(var j = 0; j < config.data.length; j++){
					$(e.get(i)).append(
					`<option  value='${config.data[j].value}'>${config.data[j].label}</option>`
					);
				}
			}
		form.render('select');
		
	  },
	  showTips:function(content){
		  if($(this).is('select')){
		    	layer.tips(content,$(this).next(),{tipsMore: false, tips:[2,"red"]});
		    }else{
		    	layer.tips(content,
						this,{tipsMore: false, tips:[2,"red"]});
		    }  
	  },
	  validateFail:function(tip){
		  $(this).focus();
		  $(this).css("cssText","border-color:red!important;");
		  $(this).hover(function(){
			  $(this).css("cssText","border-color:red!important;");
		  })
		  $(this).showTips(tip);
	  },
	  clearValidate:function(){
		  $($(this).find("input[required],select[required],textarea[required]").not(".noborder").get()).each(function(){
			  $(this).css("cssText","border-color:#e6e6e6!important;");
			  $(this).unbind('mouseenter').unbind('mouseleave')
		  });
	  },
	  validateLength:function(){
		//get max length
		var maxLength = $(this).attr("max");
		if(maxLength){
			if($(this).val().length > maxLength){
				$(this).validateFail("最多输入"+maxLength+"个字符");
				return false;
			}
		}
		layer.closeAll('tips');
		return true;
	  },
	  validateJson:function(){
		  if($(this).hasAttr("json") && $(this).val()){
				if( !isJson($(this).val()) ){
					$(this).validateFail($(this).attr("json") == 'array'
						?"不是合法的json数组":'不是合法的json');
					return false;
				}else if($(this).attr("json") == 'array' && !Array.isArray(JSON.parse($(this).val()))){
					$(this).validateFail("不是合法的json数组");
					return false;
				}
		  }
		layer.closeAll('tips');
		return true;
	  },
	  validateRequired:function(){
		  if($(this).hasAttr("required") && !$(this).val()){
			$(this).validateFail("必填项不能为空");
			return false;
		  }
		layer.closeAll('tips');
		return true;
	  },
	  
	  validate: function() {
		  var $this = $(this);
		  var isPass = true;
		  layui.use('layer',function(){
			  var layer = layui.layer;
			  $($this.find("input[required],select[required],textarea[required],textarea[json]").not(".noborder").get()).each(function(){
				  var tip = ($(this).attr("placeholder")?$(this).attr("placeholder"):"必填项")+"不能为空";
				  $(this).unbind('input').bind('input',function(){
					  isPass = $(this).validateRequired();
					  if(isPass){
						  $(this).unbind('mouseenter').unbind('mouseleave')
						  isPass = $(this).validateLength();
						 
					  }
					  if(isPass){
						  $(this).css("cssText","border-color:#e6e6e6!important;");
					  }
					  return isPass;
				  })
			      isPass = $(this).validateRequired();
				  if(isPass){
					  isPass = $(this).validateLength();
					  if(isPass){
						  isPass =  $(this).validateJson();
					  }
				  }
				  if(isPass){
					  $(this).css("cssText","border-color:#e6e6e6!important;");
				  }
				  return isPass;
			    })
			 
		  })
		  return isPass;
	  },
	  /* serialize form to json */
	  serializeFormJSON : function() {
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	      if (o[this.name]) {
	        if (!o[this.name].push) {
	          o[this.name] = [ o[this.name] ];
	        }{}
	        o[this.name].push(this.value || '');
	      } else {
	        o[this.name] = this.value || '';
	      }
	    });
	    return o;
	  },
	});

//Extend the default Number object with a formatMoney() method:
//usage: someVar.formatMoney(decimalPlaces, symbol, thousandsSeparator, decimalSeparator)
//defaults: (2, "$", ",", ".")
Number.prototype.formatMoney = function(places, symbol, thousand, decimal) {
	places = !isNaN(places = Math.abs(places)) ? places : 2;
	symbol = symbol !== undefined ? symbol : "$";
	thousand = thousand || ",";
	decimal = decimal || ".";
	var number = this, 
	    negative = number < 0 ? "-" : "",
	    i = parseInt(number = Math.abs(+number || 0).toFixed(places), 10) + "",
	    j = (j = i.length) > 3 ? j % 3 : 0;
	return symbol + negative + (j ? i.substr(0, j) + thousand : "") + i.substr(j).replace(/(\d{3})(?=\d)/g, "$1" + thousand) + (places ? decimal + Math.abs(number - i).toFixed(places).slice(2) : "");
};

/* get url param */
function getUrlParam(name) {
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null)
    return (r[2]);
  return null;
}

function hasPermission(id){
	//TODO check permission
	return true;
}

$(document).ajaxComplete(function( event, xhr, settings) {
	layui.use(['layer'], function(){
	var layer = layui.layer
	if(xhr.responseJSON){
		/*if(xhr.responseJSON.code=='400101'){
			layer.confirm('session已过期，请重新登录!', {icon: 5, title:'提示',
				btn:['确定'],closeBtn:0}, 
					function(index){
					window.parent.location="/logout";
				});
			
		}*/
		/*if(xhr.responseJSON.code=='400102'){
			error("你没有权限进行此操作！");
		}*/
	}
	});
});

	$( document ).ajaxError(function(event, jqxhr, settings, thrownError) {
		console.info(jqxhr)
		if(jqxhr.status != 200){
			if(jqxhr.responseJSON){
				if(jqxhr.responseJSON.code=='400101'){
					layui.use(['layer'], function(){
						var layer = layui.layer
					layer.confirm('session已过期，请重新登录!', {icon: 5, title:'提示',
						btn:['确定'],closeBtn:0}, 
							function(index){
							window.parent.location="/logout";
						});
					});
					return;
				}
			}
			error("服务器异常",jqxhr);
		}
	
	});



jQuery.ajaxSetup({
    dataFilter: function (data, type) {
    	if(type==undefined && !isJson(data)){
    		var $newData = $(data).clone();
    		$newData.find(".layui-btn").each(function(){
				if($(this).hasAttr("permission-id")){
					if(!hasPermission($(this).attr("permission-id"))){
						$(this).hide();
					}
				}
			})
			var $content = $('<div/>');
    		$newData.each(function(){
    			$content.append($(this));
    		})
			return $content.html();
    	}
    	return data;
    }
});
