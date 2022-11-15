package sg.nus.csf.diaryspring.controllers;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import sg.nus.csf.diaryspring.models.CreateChatroomRequest;
import sg.nus.csf.diaryspring.models.InviteChatroomRequest;
import sg.nus.csf.diaryspring.models.PostChatRequest;
import sg.nus.csf.diaryspring.services.ChatroomService;

@RestController
@RequestMapping(path="/spring/chatroom")
public class ChatroomController {
  
  private ChatroomService chatroomService;

  @Autowired
  public ChatroomController(ChatroomService chatroomService) {
    this.chatroomService = chatroomService;
  }

  @PostMapping("/create")
  public ResponseEntity<Object> createChatroom(@RequestBody CreateChatroomRequest createChatroomRequest) {
    return this.chatroomService.createChatroom(createChatroomRequest);
  }

  @PostMapping("/invite")
  public ResponseEntity<JSONObject> inviteToChatroom(@RequestBody InviteChatroomRequest inviteChatroomRequest) {
    return this.chatroomService.inviteToChatroom(inviteChatroomRequest);
  }

  @GetMapping("/name")
  public ResponseEntity<JSONObject> getChatroomName(@RequestParam String chatroomId) {
    return this.chatroomService.getChatroomName(chatroomId);
  }

  @GetMapping("/user")
  public ResponseEntity<JSONArray> getUserChatrooms(@RequestParam String userId) {
    return this.chatroomService.getUserChatrooms(userId);
  }

  @PostMapping("/chatroomUsers")
  public ResponseEntity<JSONArray> getChatroomUsers(@RequestBody String chatroomId) {
    return this.chatroomService.getChatroomUsers(chatroomId);
  }

  @PostMapping("/post")
  public ResponseEntity<JSONObject> postChat(@RequestBody PostChatRequest req) {
    return this.chatroomService.postChat(req);
  }

  @PostMapping("/getChats")
  public ResponseEntity<JSONArray> getChats(@RequestBody String chatroomId) {
    return this.chatroomService.getChats(chatroomId);
  }

  @PostMapping("/openaiTest")
  public String postChat(@RequestBody String post) {
    return this.chatroomService.openaiContentRating(post);
  }
}
