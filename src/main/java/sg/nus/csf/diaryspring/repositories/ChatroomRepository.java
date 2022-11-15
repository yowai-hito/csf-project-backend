package sg.nus.csf.diaryspring.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import static sg.nus.csf.diaryspring.configs.AppConstants.*;

import java.sql.ResultSet;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChatroomRepository {
  
  @Autowired
  JdbcTemplate jdbc;

  public int createChatroom(byte[] byteUuid, String chatroomName){
    return jdbc.update(SQL_CHATROOM_CREATE_CHATROOM, byteUuid, chatroomName);
  }

  public int inviteToChatroom (byte[] chatroomId, int accountId){
    return jdbc.update(SQL_CHATROOM_ADD_TO_CHATROOM, chatroomId, accountId);
  }

  public Optional<String> getChatroomName (String chatroomId) {
    return jdbc.query(SQL_CHATROOM_GET_CHATROOM_NAME, 
    (ResultSet rs) -> {
        if (!rs.next()){return Optional.empty();}
        String chatroomName = rs.getString("chatroom_name");
        return Optional.of(chatroomName);
    },
    chatroomId);
  }

  public Optional<JSONArray> getUserChatrooms (String userId) {
    try {
      return jdbc.query(SQL_CHATROOM_SELECT_USER_CHATROOMS, 
      (ResultSet rs) -> {
        JSONArray userChatrooms = new JSONArray();
        while (rs.next()){
          JSONObject userChatroomDetails = new JSONObject();
          userChatroomDetails.put("chatroomId", rs.getString("chatroom_id"));
          userChatroomDetails.put("userId", rs.getInt("account_id"));
          userChatroomDetails.put("chatroomName", rs.getString("chatroom_name"));
          userChatrooms.add(userChatroomDetails);
        }   
        return Optional.of(userChatrooms);
      },
      userId);
    } catch (Exception e) {
      return Optional.empty();
    }
    
  }
}
