package ru.ok.botapi.comment;

public class ModerationComment {
    
    private long postId;
    private long commentId;
    private String text;
    
    public ModerationComment(long postId, long commentId, String text) {
        this.postId = postId;
        this.commentId = commentId;
        this.text = text;
    }
    
    public long getPostId() {
        return postId;
    }
    
    public void setPostId(long postId) {
        this.postId = postId;
    }
    
    public long getCommentId() {
        return commentId;
    }
    
    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    @Override
    public String toString() {
        return "ModerationComment{" +
                "postId=" + postId +
                ", commentId=" + commentId +
                ", text='" + text + '\'' +
                '}';
    }
}
