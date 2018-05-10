var articlePageUrl = "article/page"
var $articleList = $("#article-list")
$(function(){
	//load article
	loadPage();
})

function loadArticle(url){
	$.getJSON(url,function(data){
		$articleList.empty();
		$.each(data.data,function(index,item){
			/*<div class="col-md-8 blog-main">
        	
            <img src="img/1.jpg" alt="" />
        	<div class="content">
            	<a  th:href="@{/page/article-detail.html}">spring booot.pdf</a>
            	<h3 class="meta">19 August 2015, John Doe</h3>
            </div></div>*/
			var $mainDiv = $('<div class="col-md-8 blog-main"></div>');
			var imgUrl = item.imgUrl?item.imgUrl:"img/1.jpg";
			var $img = $('<img src="'+imgUrl+'" alt="" />');
			$mainDiv.append($img);
			var $articleDiv = $('<div class="article"></div>');
			var $title = '<a target="_blank" class="title" href="'+ctx+'article/'+item.id+'.html"><h3>'+item.title+'</h3></a>';
			var content = item.summary?item.summary:item.content.substring(0,150)+"...";
			var $content = $('<p class="blog-content"></p>');
			$content.text(content);
			var $meta = $('<h4 class=" meta">阅读:&nbsp;&nbsp;'+item.readCount+'·评论:&nbsp;&nbsp;'+item.commentCount+'</h4><h4 class=" meta right">'+item.author+' 发布于 '+item.postTime+'</h4>');
			$articleDiv.append($title);
			$articleDiv.append($content);
			$articleDiv.append($meta);
			$mainDiv.append($articleDiv);
			$articleList.append($mainDiv);
		})
		   $("html,body").animate({scrollTop:0}, 0);
	})
}

function loadPage(){
	layui.use(['laypage', 'layer'], function(){
		  var laypage = layui.laypage
		  ,layer = layui.layer;
	$.get("/article/count").done(function(data){
		
		//完整功能
		  laypage.render({
		    elem: 'article-page'
		    ,count: data
		    ,layout: ['count', 'prev', 'page', 'next', 'skip']
		    ,jump: function(obj){
		    	loadArticle(articlePageUrl+"?page="+obj.curr+"&limit="+obj.limit+"&order=desc&sort=post_time")
		    }
		  });
	});
	
	});
}
