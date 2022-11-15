package sg.nus.csf.diaryspring.configs;

public class AppConstants {

  // User SQL Statements
  public static final String SQL_USERS_SELECT_USER_BY_USERNAME_AND_PASSWORD = 
  "SELECT * from csf_project.users where account_name = ? AND account_password = ?";

  public static final String SQL_USERS_CREATE_USER_ACCOUNT = "INSERT INTO csf_project.users(account_name, account_password, account_role, account_handle, account_email) " + 
  "VALUES (?,?,?,?,?)";

  public static final String SQL_USERS_SELECT_USERID_FROM_HANDLE = 
  "SELECT account_id FROM csf_project.users WHERE account_handle = ?";

  public static final String SQL_USERS_CHANGE_EMAIL = "UPDATE csf_project.users SET account_email = ? WHERE account_id = ? ";

  public static final String SQL_USERS_SELECT_ID_WITH_HANDLE = "SELECT account_id from csf_project.users WHERE account_handle = ?";

  // Chatroom SQL Statements
  public static final String SQL_CHATROOM_CREATE_CHATROOM = "INSERT INTO csf_project.chatrooms(chatroom_id, chatroom_name)" +
  "VALUES (?, ?)";

  public static final String SQL_CHATROOM_ADD_TO_CHATROOM = "INSERT INTO csf_project.chatroom_users(chatroom_id, account_id)" +
  "VALUES (?,?)";

  public static final String SQL_CHATROOM_POST_TO_CHATROOM = "INSERT INTO csf_project.chatroom_chats(chatroom_id, account_id, post) " +
  "VALUES (?,?,?)";

  public static final String SQL_CHATROOM_GET_CHATROOM_POSTS = "SELECT * FROM csf_project.chatroom_chats WHERE chatroom_id = ?";

  public static final String SQL_CHATROOM_SELECT_USER_CHATROOMS = "SELECT csf_project.chatrooms.chatroom_name, " +
  "csf_project.chatrooms.chatroom_id, csf_project.chatroom_users.account_id FROM csf_project.chatrooms RIGHT JOIN" +
  " csf_project.chatroom_users ON csf_project.chatrooms.chatroom_id = csf_project.chatroom_users.chatroom_id " +
  "WHERE account_id = ?";

  public static final String SQL_CHATROOM_SELECT_CHATROOM_USERS = "SELECT csf_project.chatroom_users.account_id, " +
  "csf_project.users.account_name FROM csf_project.chatroom_users LEFT JOIN" +
  " csf_project.users ON csf_project.chatroom_users.account_id = csf_project.users.account_id " +
  "WHERE chatroom_id = ?";

  public static final String SQL_CHATROOM_GET_CHATROOM_NAME = "SELECT chatroom_name FROM csf_project.chatrooms " +
  "WHERE chatroom_id = ?";

}
