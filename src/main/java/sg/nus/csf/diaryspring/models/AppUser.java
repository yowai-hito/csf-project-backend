package sg.nus.csf.diaryspring.models;

import net.minidev.json.JSONObject;

public class AppUser {

  private int id = 0;
  private String handle;
  private String username;
  private String password;
  private String email;
  private String role = "USER";

  

  public AppUser(String username, String password, String handle, String email) {
    this.handle = handle;
    this.username = username;
    this.password = password;
    this.email = email;
  }
  public AppUser(String handle, String username, String email) {
    this.handle = handle;
    this.username = username;
    this.email = email;
  }

  public AppUser(String handle, String username, String password, String email, String role) {
    this.handle = handle;
    this.username = username;
    this.password = password;
    this.email = email;
    this.role = role;
  }

  
  public String getHandle() {
    return handle;
  }
  public void setHandle(String handle) {
    this.handle = handle;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public String getPassword() {
    return password;
  }
  public void setPassword(String password) {
    this.password = password;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getRole() {
    return role;
  }
  public void setRole(String role) {
    this.role = role;
  }
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }

  public JSONObject toJson() {
    JSONObject json = new JSONObject();
    json.put("handle", this.handle);
    json.put("username", this.username);
    json.put("email", this.email);
    json.put("id", this.id);
    return json;
  }
}
