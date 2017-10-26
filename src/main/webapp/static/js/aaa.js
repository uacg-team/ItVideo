/**
 * 
 */
function likeVideo() {
    var request = new XMLHttpRequest();
    
    request.onreadystatechange = function() {
        //when response is received
        if (this.readyState == 4 && this.status == 200) {
            var button = document.getElementById("likebutton");
            button.innerHTML = "Unlike";
            button.style.background='red';
        }
        else
        if (this.readyState == 4 && this.status == 401) {
            alert("Sorry, you must log in to like this video!");
        }
            
    }
    request.open("post", "commentLove", true);
    request.send();
}
function likeButton(){
    var like = document.getElementById("like");
    var dislike = document.detElementById("dislike");
    if(like.alt==="like" && dislike.alt==="dislike"){
        like.alt="liked";
        like.src="<c:url value="/pics/liked.png"/>";
    }else if(like.alt==="like"){
        like.alt="liked";
        dislike.src="<c:url value="/pics/dislike.png"/>"
        like.src="<c:url value="/pics/liked.png"/>";
    }else{
        //like.alt=="liked"
        like.alt="like";
        like.src="<c:url value="/pics/like.png"/>";
    }
}