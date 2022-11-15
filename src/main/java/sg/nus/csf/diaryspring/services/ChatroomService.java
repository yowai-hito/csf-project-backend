package sg.nus.csf.diaryspring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sg.nus.csf.diaryspring.models.CreateChatroomRequest;
import sg.nus.csf.diaryspring.models.InviteChatroomRequest;
import sg.nus.csf.diaryspring.repositories.ChatroomRepository;
import sg.nus.csf.diaryspring.repositories.UserRepository;

@Service
public class ChatroomService {
  
  @Autowired
  ChatroomRepository chatroomRepository;
  @Autowired
  UserRepository userRepository;

  public ResponseEntity<Object> createChatroom(CreateChatroomRequest req) {
    
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    String rawUuid = UUID.randomUUID().toString();
    String uuid = rawUuid.replace("-","");
    byte[] byteUuid = hexStringToByteArray(uuid);
    int responseBody = chatroomRepository.createChatroom(byteUuid, req.getChatroom_name());
    chatroomRepository.inviteToChatroom(byteUuid, req.getAccount_id());
    if (responseBody != 1) {
      responseStatus = HttpStatus.resolve(400);
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<Object> inviteToChatroom(InviteChatroomRequest req) {
    
    String responseBody = "";
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    List<Integer> userIds = new ArrayList<Integer>();
    userIds = userRepository.getUserIdFromHandles(req.getUserHandles());
    int count = 0;
    for (int userId : userIds){
      chatroomRepository.inviteToChatroom(req.getChatroomId(), userId);
      count++;
    }
    if (count == 0){
      responseStatus = HttpStatus.resolve(400);
      responseBody = "Error, users have not been added to the group";
    } else {
      responseBody = count + " users have been added to the group.";
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONObject> getChatroomName(String chatroomId) {

    JSONObject responseBody = new JSONObject();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    responseHeaders.set("Content-Type","application/json");
    responseHeaders.set("Accept", "application/json");
    try {
      responseBody.put("chatroomName", this.chatroomRepository.getChatroomName(chatroomId).get());
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "Failed to retrieve chatroom name");
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONArray> getUserChatrooms(String userId){
    JSONArray responseBody = new JSONArray();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    responseHeaders.set("Content-Type","application/json");
    responseHeaders.set("Accept", "application/json");
    try {
      responseBody = this.chatroomRepository.getUserChatrooms(userId).get();
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      JSONObject body = new JSONObject();
      body.put("Error", "Failed to retrieve user chatrooms");
      responseBody.add(body);
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public static byte[] hexStringToByteArray(String s) {
    int len = s.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                            + Character.digit(s.charAt(i+1), 16));
    }
    return data;
  }

}
