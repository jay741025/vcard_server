package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fet.carpool.serv.persistence.ChatLog;
import com.fet.carpool.serv.persistence.Friend;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class FriendChatDto {

	private String accountId;
	private String accountName;
	private String friendId;
	private String friendName;
	private String friendPic;
	
	private String message;
	private boolean messageRead;
	private long timestamp;
	private int messageNotReadCount;
	
	
	public FriendChatDto() {
		super();
	}
	public FriendChatDto( Friend friend ) {
		super();
		if( friend != null ) {
			setAccountId(friend.getAccountId());
			setFriendId(friend.getFriendId());
			setTimestamp(friend.getTimestamp());
		}
	}
	
	public String getFriendPic() {
		return friendPic;
	}
	public void setFriendPic(String friendPic) {
		this.friendPic = friendPic;
	}
	public FriendChatDto( ChatLog chatLog ) {
		super();
		if( chatLog != null ) {
			setAccountId(chatLog.getAccountId());
			setFriendId(chatLog.getFriendId());
			setMessage(chatLog.getMessage());
			setMessageRead(chatLog.isMessageRead());
			setTimestamp(chatLog.getTimestamp());
		}
	}
	public FriendChatDto( FriendChatDto friendChatLog ) {
		super();
		replace(friendChatLog);
	}
	
	public void replace( FriendChatDto friendChatLog ) {
		if( friendChatLog != null ) {
			setAccountId(friendChatLog.getAccountId());
			setAccountName(friendChatLog.getAccountName());
			setFriendId(friendChatLog.getFriendId());
			setFriendName(friendChatLog.getFriendName());
			setFriendPic(friendChatLog.getFriendPic());
			setMessage(friendChatLog.getMessage());
			setMessageRead(friendChatLog.isMessageRead());
			setTimestamp(friendChatLog.getTimestamp());
		}
	}
	
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getFriendId() {
		return friendId;
	}
	public void setFriendId(String friendId) {
		this.friendId = friendId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isMessageRead() {
		return messageRead;
	}
	public void setMessageRead(boolean messageRead) {
		this.messageRead = messageRead;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
//	public Date getDatetime() {
//		return new Date( timestamp );
//	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getFriendName() {
		return friendName;
	}
	public void setFriendName(String friendName) {
		this.friendName = friendName;
	}
	public int getMessageNotReadCount() {
		return messageNotReadCount;
	}
	public void setMessageNotReadCount(int messageNotReadCount) {
		this.messageNotReadCount = messageNotReadCount;
	}
}
