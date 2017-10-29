/**
 * 
 */
function comments(){
	var request = new XMLHttpRequest();
	request.onreadystatechange = function() {
		if (this.readyState == 4 && this.status == 200) {
			var listComments = JSON.parse(this.responseText);
			var comment_box = document.createElement('div');
            comment_box.className = "comment-box";
		}
	}
	request.open("GET", "player/getCommentsWithVotes", true);
	request.send();
}