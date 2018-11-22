layui.use(['layer'], function(){
(function(win,$,layer){
	var _open=layer.open;
	var _$doc=$(document);
	layer.open = function(opt) {
		var fn = {
				success: function(layero, index) {},
				yes: function(index, layero) {layer.close(index);},
				cancel: function( index,layero) {},
				end: function() {},
			}
		
		if (opt.success) {
			fn.success = opt.success;
		}
		if (opt.yes) {
			fn.yes = opt.yes;
		}
		if (opt.cancel) {
			fn.cancel = opt.cancel;
		}
		if (opt.end) {
			fn.end = opt.end;
		}
		var _opt = $.extend(opt, {
			success:function(layero, index){
				fn.success(layero, index)
				$("#layui-layer-shade"+index).insertAfter($("#layui-layer"+index));
			},
			end: function() {
				fn.end();
			},
			yes:function(index, layero){
				fn.yes(index, layero)
			},
			cancel:function( index,layero){
				fn.cancel(index)
			}
		});
		
		var def = _open.call($, _opt);
		return def;
	}
	
})(window,jQuery,layer);
});
