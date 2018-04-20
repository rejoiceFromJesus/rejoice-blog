var ctx;
var loadCategoryUrl = "category/parent-id/";
$(function(){
	
	ctx = $("meta[name='ctx']").attr("content");
	initUrls();
	//1、loadCategory
	loadCategory($("#category-list"));
	
})

function initUrls(){
	ctx = $("meta[name='ctx']").attr("content");
	loadCategoryUrl = ctx+loadCategoryUrl;
}

function loadCategory(root){
	var $dropdownMenu = root.children(".dropdown-menu");
	if($dropdownMenu.length > 0){
		return;
	}
	var parentId;
	var href = root.children("a").attr("href");
	parentId=href=="#"?"-1":href.substring(href.lastIndexOf("/"));
	$.getJSON(loadCategoryUrl+parentId,function(data){
		/**
		 * <li class="dropdown" id="category-list"><a href="#">分类</a>  
                        <ul class="dropdown-menu">  
                            <li><a href="#">sub SVN1</a></li>  
                            <li><a href="#">sub SVN2</a></li>  
                            <li class="dropdown" ><a href="#">目录</a>  
		                      <ul class="dropdown-menu second-dropdown">  
		                            <li><a href="#">sub SVN1</a></li>  
		                            <li><a href="#">sub SVN2</a></li>  
		                            <li><a href="#">sub SVN2</a></li>  
		                        </ul>
                   			</li>  
                        </ul>  
                   </li>  
		 */
		if(data.length == 0){return;}
		$dropdownMenu = parentId=='-1'?$('<ul class="dropdown-menu"></ul>'):$('<ul class="dropdown-menu second-dropdown"></ul>');
		root.append($dropdownMenu);
		$.each(data,function(index,element){
			var $li = $('<li></li>');
			var $a = $('<a target="_blank" href="'+ctx+'article/category/'+element.id+'">'+element.name+'</a>');
			$a.mouseover(function(){
				loadCategory($(this).parent());
			});
			$li.append($a)
			$dropdownMenu.append($li);
		})
		
	});
}