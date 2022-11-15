package sg.nus.csf.diaryspring.repositories;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import sg.nus.csf.diaryspring.models.AppUser;

import static sg.nus.csf.diaryspring.configs.AppConstants.*;

@Repository
public class UserRepository {
  @Autowired
  JdbcTemplate jdbc;

  public Optional<AppUser> login(String username, String password) {
    
    return jdbc.query(SQL_USERS_SELECT_USER_BY_USERNAME_AND_PASSWORD, 
    (ResultSet rs) -> {
        if (!rs.next()){return Optional.empty();}
        AppUser user = new AppUser(
          rs.getString("account_handle"),  
          rs.getString("account_name"),
          rs.getString("account_email")
        );
        user.setId(rs.getInt("account_id"));
        return Optional.of(user);
    },
    username, password);
  }
  
  public int registerUser(AppUser user) {

    return jdbc.update(SQL_USERS_CREATE_USER_ACCOUNT,
      user.getUsername(), user.getPassword(), user.getRole(), user.getHandle(), user.getEmail());
  }

  public int changeEmail(String userId, String newEmail){
    return jdbc.update(SQL_USERS_CHANGE_EMAIL, newEmail, userId);
  }

  public Optional<String> getUserIdWithHandle(String userHandle) {
    return jdbc.query(SQL_USERS_SELECT_USERID_FROM_HANDLE, 
    (ResultSet rs) -> {
        if (!rs.next()){return Optional.empty();}
        return Optional.of(rs.getString("account_id"));
    },
    userHandle);
  }
}
