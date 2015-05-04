package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fet.carpool.serv.persistence.GroupChat;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GroupChatDto {

	private int seqNo;
	private int groupId;
	private String groupName;
	private String accountId;
	private String accountName;
	private String member;
	
	private String message;
	
	private long timestamp;
	
	private long timestampStart;
	private long timestampEnd;
	private int notRead;
	
	
	public GroupChatDto() {
		super();
	}
	public GroupChatDto( GroupChat chat ) {
		super();
		replaceValue( chat );
	}
	
	public void replaceValue(  GroupChat chat ) {
		if( chat != null ) {
			setSeqNo(chat.getSeqNo());
			setGroupId(chat.getGroupId());
			setAccountId(chat.getAccountId());
			setMessage(chat.getMessage());
			setTimestamp(chat.getTimestamp());
		}
	}
	
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public long getTimestampStart() {
		return timestampStart;
	}
	public void setTimestampStart(long timestampStart) {
		this.timestampStart = timestampStart;
	}
	public long getTimestampEnd() {
		return timestampEnd;
	}
	public void setTimestampEnd(long timestampEnd) {
		this.timestampEnd = timestampEnd;
	}
	public String getAccountName() {
		return accountName;
	}
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public int getNotRead() {
		return notRead;
	}
	public void setNotRead(int notRead) {
		this.notRead = notRead;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
}
