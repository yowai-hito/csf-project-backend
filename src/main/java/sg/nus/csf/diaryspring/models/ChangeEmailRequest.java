package sg.nus.csf.diaryspring.models;

public class ChangeEmailRequest {
  private String newEmail;
  private String userId;
  public String getNewEmail() {
    return newEmail;
  }
  public void setNewEmail(String newEmail) {
    this.newEmail = newEmail;
  }
  public String getUserId() {
    return userId;
  }
  public void setUserId(String userId) {
    this.userId = userId;
  }

  
}
