package sg.nus.csf.diaryspring.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

  @Value("${openai.api.key}")
  String openaiApiKey;

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
    // openaiContentRating(req.getPost());
    try {
      this.chatroomRepository.postChat(req.getAccountId(), req.getChatroomId(),req.getPost());
      responseBody.put("Success", "Post has successfully been uploaded!");
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "Failed to post");
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
      responseBody = this.chatroomRepository.getChats(chatroomId);
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      JSONObject body = new JSONObject();
      body.put("Error", "Failed to retrieve chatroom posts");
      responseBody.add(body);
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public String openaiContentRating(String post){
    RestTemplate template = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add("Authorization", "Bearer " + openaiApiKey);
    JSONObject body = new JSONObject();
    body.put("model", "content-filter-alpha");
    body.put("prompt", "<|endoftext|>%s\n--\nLabel:".format(post));
    body.put("max_tokens", 1);
    body.put("user",0);
    body.put("temperature", 0.0);
    body.put("top_p", 0);
    body.put("logprobs", 10);

    String endpoint = UriComponentsBuilder
        .fromUriString("https://api.openai.com/v1/completions")
        .build().toString();

    HttpEntity<String> request = new HttpEntity<String>(body.toJSONString(), headers);
    return template.postForObject(endpoint, request, String.class);
  }
}
