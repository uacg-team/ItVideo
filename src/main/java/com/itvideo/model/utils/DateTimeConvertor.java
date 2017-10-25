package   com.itvideo.model.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeConvertor {
	private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
	/**
	 * @param localDateTime put java LocalDateTime
	 * @return String with pattern 2017-10-12 15:16:56 ready to parse to mysql
	 * to parse use STR_TO_DATE('2017-10-12 15:16:56','%Y-%m-%d %H:%i:%s.%f')
	 */
	public static String ldtToSql(LocalDateTime localDateTime) {
		return localDateTime.format(FORMAT);
	}
	/**
	 * @param sqlDateTime- 
	 * @return LocalDateTime
	 */
	public static LocalDateTime sqlToLdt(String sqlDateTime) {
		return LocalDateTime.parse(sqlDateTime, FORMAT);
	}

	public static void main(String[] args) throws SQLException {
		Connection con = DBConnection.CON1.getConnection();
		//Demo insert date
		PreparedStatement ps = con.prepareStatement("update videos set date=STR_TO_DATE(?,'%Y-%m-%d %H:%i:%s.%f') where id=1;");
		ps.setString(1, ldtToSql(LocalDateTime.now()));
		ps.executeUpdate();
		//Demo get date
		ps=con.prepareStatement("select date from videos where id=1;");
		ResultSet rs=ps.executeQuery();
		rs.next();
		String s = rs.getString(1);
		DBConnection.CON1.closeConnection();
		System.out.println(sqlToLdt(s));
		System.out.println(s);
	}
}
