package sg.nus.csf.diaryspring.models;

public class CreateChatroomRequest {
  
  private String chatroom_name;
  private int account_id;

  public String getChatroom_name() {
    return chatroom_name;
  }
  public void setChatroom_name(String chatroom_name) {
    this.chatroom_name = chatroom_name;
  }
  public int getAccount_id() {
    return account_id;
  }
  public void setAccount_id(int account_id) {
    this.account_id = account_id;
  }
  
}
