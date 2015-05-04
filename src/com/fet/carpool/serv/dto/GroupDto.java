package com.fet.carpool.serv.dto;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fet.carpool.serv.persistence.Group;

@XmlRootElement
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GroupDto {

	private int groupId;
	private String groupName;
	private String member;
	private String creatorAccountId;
	private long createTime;
	
	
	
	
	public GroupDto() {
		super();
	}
	public GroupDto( Group g ) {
		super();
		replaceValue( g );
	}
	
	public void replaceValue( Group g ) {
		if( g != null ) {
			setGroupId(g.getGroupId());
			setGroupName(g.getGroupName());
			setMember(g.getMember());
			setCreatorAccountId(g.getCreatorAccountId());
			setCreateTime(g.getCreateTime());
		}
	}
	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getMember() {
		return member;
	}
	public void setMember(String member) {
		this.member = member;
	}
	public String getCreatorAccountId() {
		return creatorAccountId;
	}
	public void setCreatorAccountId(String creatorAccountId) {
		this.creatorAccountId = creatorAccountId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
