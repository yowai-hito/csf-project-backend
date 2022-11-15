package sg.nus.csf.diaryspring.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import net.minidev.json.JSONObject;
import sg.nus.csf.diaryspring.models.ChangeEmailRequest;
import sg.nus.csf.diaryspring.models.LoginRequest;
import sg.nus.csf.diaryspring.models.RegistrationRequest;
import sg.nus.csf.diaryspring.services.UserService;

@RestController
@RequestMapping(path="/spring/user")
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/login")
  public ResponseEntity<JSONObject> login(@RequestBody LoginRequest req) {
    return this.userService.login(req);
  }

  @PostMapping("/register")
  public ResponseEntity<JSONObject> doRegistration(@RequestBody RegistrationRequest req) {
    return this.userService.registerUser(req); 
  }

  @PutMapping("/changeEmail")
  @CrossOrigin
  public ResponseEntity<JSONObject> doRegistration(@RequestBody ChangeEmailRequest req) {
    return this.userService.changeEmail(req); 
  }

  @PostMapping(path = "/uploadProfilePic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<JSONObject> doRegistration(@RequestPart MultipartFile myfile, @RequestPart String title) {
    return this.userService.uploadProfilePicture(myfile, title); 
  }
}
