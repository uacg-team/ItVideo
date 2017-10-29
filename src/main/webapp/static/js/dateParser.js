/**
 * @param date-JSON object LocalDateTime received from rest comment
 * @returns string representation of date
 */
function dateParse(date){
	var month;
	switch(date.month) {
	 case 'JANUARY':
	        month = 1;
	        break;
	 case 'FEBRUARY':
	        month = 2;
	        break;
	 case 'MARCH':
	        month = 3;
	        break;
	 case 'APRIL':
	        month = 4;
	        break;
	 case 'MAY':
	        month = 4;
	        break;
	 case 'JUNE':
	        month = 4;
	        break;
	 case 'JULY':
	        month = 4;
	        break;
	 case 'AUGUST':
	        month = 4;
	        break;
	 case 'SEPTEMBER':
	        month = 4;
	        break;
	 case 'OCTOBER':
	        month = 4;
	        break;
	 case 'NOVEMBER':
	        month = 4;
	        break;
	 case 'DECEMBER':
	        month = 4;
	        break;
    default:
        month = 0;
	}
	var time = date.dayOfMonth + '/' + month + '/' + date.year +" "+ date.hour + ':' + date.minute + ':' + date.second;
	return time;
}