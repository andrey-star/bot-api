package ru.ok.botapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "subscriptions")
public class Subscription {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;
	
	@Column(name = "post_id")
	private long postId;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "last_comment_id")
	private long lastCommentId;
	
	public Subscription() {
	}
	
	public Subscription(long postId, String url, long lastCommentId) {
		this.postId = postId;
		this.url = url;
		this.lastCommentId = lastCommentId;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getPostId() {
		return postId;
	}
	
	public void setPostId(int postId) {
		this.postId = postId;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public long getLastCommentId() {
		return lastCommentId;
	}
	
	public void setLastCommentId(int lastCommentId) {
		this.lastCommentId = lastCommentId;
	}
	
	@Override
	public String toString() {
		return "Subscription{" +
				"id=" + id +
				", postId=" + postId +
				", url='" + url + '\'' +
				", lastCommentId=" + lastCommentId +
				'}';
	}
}
