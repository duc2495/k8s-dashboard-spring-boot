package kubernetes.client.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<User> {
	
	public static final String BASE_SQL //
			= "Select u.user_id, u.user_name, u.encryted_password, u.email, u. From User u ";

	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {

		Long userId = rs.getLong("User_Id");
		String userName = rs.getString("User_Name");
		String encrytedPassword = rs.getString("Encryted_Password");

		return new AppUser(userId, userName, encrytedPassword);
	}
}
