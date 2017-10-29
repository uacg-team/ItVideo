function testHtml(){
	var videoComments = document.getElementById("videoComments");
	videoComments.innerHTML =buildComment(1,"ala bala",6,23,0,null,false,12,3,"vilio","alabala",1,6);
}
function test(){
	//show more test
	var testDiv = document.getElementById("test");
	//remove old button
	var elem = document.getElementById('showmore');
    elem.parentNode.removeChild(elem);
	testDiv.insertAdjacentHTML('beforeend', '<p>1</p><div id="showmore"><button onclick="test()">testing</button></div>');
	//
}
//function test(){
//	showButton('test', 'showmore', 'hello')
//}
//function showButton(insertionDivName,deleteDivElement,html){
//	//remove button with name deleteDivElement
//	var elem = document.getElementById(deleteDivElement);
//    elem.parentNode.removeChild(elem);
//	//add html in div with name insertionDivName
//    var testDiv = document.getElementById(insertionDivName);
//    testDiv.insertAdjacentHTML('beforeend', html);
//}