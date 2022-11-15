package sg.nus.csf.diaryspring.services;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

import net.minidev.json.JSONObject;
import sg.nus.csf.diaryspring.models.AppUser;
import sg.nus.csf.diaryspring.models.ChangeEmailRequest;
import sg.nus.csf.diaryspring.models.LoginRequest;
import sg.nus.csf.diaryspring.models.RegistrationRequest;
import sg.nus.csf.diaryspring.repositories.UserRepository;

@Service
public class UserService {

  private AWSCredentials credentials;

  private final UserRepository userRepository;

  @Value("${aws.bucket}")
  String bucketName;

  @Value("${aws.accesskey}")
  String accessKey;

  @Value("${aws.secretkey}")
  String secretKey;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public ResponseEntity<JSONObject> login(LoginRequest req) {

    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);

    Optional<AppUser> appUser = userRepository.login(req.getUsername(), DigestUtils.sha256Hex(req.getPassword()));
    if (appUser.isEmpty()) {
      responseStatus = HttpStatus.resolve(400);
      JSONObject responseBody = new JSONObject();
      responseBody.put("Error", "Username could not be found");
      return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
    }
    else {
      AppUser user = appUser.get();
      JSONObject responseBody = user.toJson();
      return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
    }
  }

  public ResponseEntity<JSONObject> registerUser(RegistrationRequest req) {

    JSONObject responseBody = new JSONObject();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);
    responseHeaders.set("Content-Type","application/json");
    responseHeaders.set("Accept", "application/json");

    AppUser newUser = new AppUser(req.getUsername(), DigestUtils.sha256Hex(req.getPassword()), req.getHandle(), req.getEmail());
    try {
      int queryResult = userRepository.registerUser(newUser);
      responseBody.put("Success", "User has been registered");
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "Registration Failed");
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONObject> changeEmail(ChangeEmailRequest req) {

    JSONObject responseBody = new JSONObject();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);

    try {
      userRepository.changeEmail(req.getUserId(), req.getNewEmail());
      responseBody.put("Success", "Email has been changed");
    } catch (Exception e) {
      responseStatus = HttpStatus.resolve(400);
      responseBody.put("Error", "User could not be found or email is already taken");
    }
    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

  public ResponseEntity<JSONObject> uploadProfilePicture(MultipartFile myfile, String title) {

    JSONObject responseBody = new JSONObject();
    HttpHeaders responseHeaders = new HttpHeaders();
    HttpStatus responseStatus = HttpStatus.resolve(200);

    Map<String, String> myData = new HashMap<>();
    myData.put("title", title);
    myData.put("createdOn", (new Date()).toString());
    
    credentials = new BasicAWSCredentials(
      accessKey, 
      secretKey
    );

    AmazonS3 s3client = AmazonS3ClientBuilder
      .standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withRegion(Regions.AP_SOUTHEAST_1)
      .build();

    // Metadata for the object
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType(myfile.getContentType());
    metadata.setContentLength(myfile.getSize());
    metadata.setUserMetadata(myData);

    try {
        PutObjectRequest putReq = new PutObjectRequest( bucketName, title, 
            myfile.getInputStream(), metadata);
        putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
        PutObjectResult result = s3client.putObject(putReq);
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    responseBody.put("content-type", myfile.getContentType());
    responseBody.put("name", myfile.getName());
    responseBody.put("original_Name", myfile.getOriginalFilename());
    responseBody.put("size", myfile.getSize());
    responseBody.put("userId", title);

    return new ResponseEntity<>(responseBody, responseHeaders, responseStatus);
  }

}
