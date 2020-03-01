package ru.ok.botapi.entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

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
	
	@Column(name = "last_comment_id", columnDefinition = "integer default 0")
	private long lastCommentId;
	
	@Column(name = "timestamp")
	private Timestamp createdAt;
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public long getPostId() {
		return postId;
	}
	
	public void setPostId(long postId) {
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
	
	public void setLastCommentId(long lastCommentId) {
		this.lastCommentId = lastCommentId;
	}
	
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	
	@PrePersist
	public void prePersist() {
		createdAt = Timestamp.from(Instant.now());
	}
	
	@Override
	public String toString() {
		return "Subscription{" +
				"id=" + id +
				", postId=" + postId +
				", url='" + url + '\'' +
				", lastCommentId=" + lastCommentId +
				", createdAt=" + createdAt +
				'}';
	}
}
