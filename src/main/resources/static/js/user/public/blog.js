$(function(){
	//load article
	var url = "article/page"
	var $root = $("#article-list")
	console.info($root)
	loadArticle(url,$root);
})

function loadArticle(url,root){
	$.getJSON(url,function(data){
		$.each(data.rows,function(index,item){
			/*<div class="col-md-8 blog-main">
        	
            <img src="img/1.jpg" alt="" />
        	<div class="content">
            	<a  th:href="@{/page/article-detail.html}">spring booot.pdf</a>
            	<h3 class="meta">19 August 2015, John Doe</h3>
            </div></div>*/
			var $mainDiv = $('<div class="col-md-8 blog-main"></div>');
			var $img = $('<img src="img/1.jpg" alt="" />');
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
			root.append($mainDiv);
		})
	})
}

