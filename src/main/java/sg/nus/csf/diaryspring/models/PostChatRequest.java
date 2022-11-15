package sg.nus.csf.diaryspring.models;

public class PostChatRequest {
  private String accountId;
  private String chatroomId;
  private String post;
  public String getAccountId() {
    return accountId;
  }
  public void setAccountId(String accountId) {
    this.accountId = accountId;
  }
  public String getChatroomId() {
    return chatroomId;
  }
  public void setChatroomId(String chatroomId) {
    this.chatroomId = chatroomId;
  }
  public String getPost() {
    return post;
  }
  public void setPost(String post) {
    this.post = post;
  }

  
}
