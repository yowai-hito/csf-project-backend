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
import sg.nus.csf.diaryspring.models.PostChatRequest;
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
    int responseBody = chatroomRepository.createChatroom(uuid, req.getChatroom_name());
    chatroomRepository.inviteToChatroom(uuid, String.valueOf(req.getAccount_id()));
    if (responseBody != 1) {
      responseStatus = HttpStatus.resolve(400);
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONObject> inviteToChatroom(InviteChatroomRequest req) {
    
    String userId;
    JSONObject responseBody = new JSONObject();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    try {
      userId = this.userRepository.getUserIdWithHandle(req.getUserHandle()).get();
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "User Handle does not exist.");
      return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
    }
    try {  
      this.chatroomRepository.inviteToChatroom(req.getChatroomId(), userId);
      responseBody.put("Success", "User Successfully added to room.");
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "Could not add user to chatroom.");
      return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
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

  public ResponseEntity<JSONArray> getChatroomUsers(String chatroomId){
    JSONArray responseBody = new JSONArray();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    responseHeaders.set("Content-Type","application/json");
    responseHeaders.set("Accept", "application/json");
    try {
      responseBody = this.chatroomRepository.getChatroomUsers(chatroomId).get();
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      JSONObject body = new JSONObject();
      body.put("Error", "Failed to retrieve user chatrooms");
      responseBody.add(body);
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONObject> postChat(PostChatRequest req) {

    JSONObject responseBody = new JSONObject();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    responseHeaders.set("Content-Type","application/json");
    responseHeaders.set("Accept", "application/json");

    try {
      this.chatroomRepository.postChat(req.getAccountId(), req.getChatroomId(),req.getPost());
      responseBody.put("Success", "Post has successfully been uploaded!");
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "Failed to retrieve chatroom name");
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONArray> getChats(String chatroomId){
    JSONArray responseBody = new JSONArray();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    responseHeaders.set("Content-Type","application/json");
    responseHeaders.set("Accept", "application/json");
    try {
      responseBody = this.chatroomRepository.getChats(chatroomId).get();
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      JSONObject body = new JSONObject();
      body.put("Error", "Failed to retrieve chatroom posts");
      responseBody.add(body);
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }
}
