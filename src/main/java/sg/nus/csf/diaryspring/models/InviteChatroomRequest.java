package sg.nus.csf.diaryspring.models;

import java.util.List;

public class InviteChatroomRequest {
  private List<String> userHandles;
  private byte[] chatroomId;
  public List<String> getUserHandles() {
    return userHandles;
  }
  public void setUserHandles(List<String> userHandles) {
    this.userHandles = userHandles;
  }
  public byte[] getChatroomId() {
    return chatroomId;
  }
  public void setChatroomId(byte[] chatroomId) {
    this.chatroomId = chatroomId;
  }
}
