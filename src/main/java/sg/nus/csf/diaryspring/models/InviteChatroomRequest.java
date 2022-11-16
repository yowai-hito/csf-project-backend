package sg.nus.csf.diaryspring.models;

public class InviteChatroomRequest {
  private String userHandle;
  private String chatroomId;
  public String getUserHandle() {
    return userHandle;
  }
  public void setUserHandle(String userHandle) {
    this.userHandle = userHandle;
  }
  public String getChatroomId() {
    return chatroomId;
  }
  public void setChatroomId(String chatroomId) {
    this.chatroomId = chatroomId;
  }
}
