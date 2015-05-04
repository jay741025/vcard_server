package com.fet.carpool.serv.dao;

import java.util.List;

import com.fet.carpool.serv.dto.GroupDto;

public interface GroupDao {

	public void createGroup( GroupDto group );
	public void updateGroup( GroupDto group );
	public List<GroupDto> getGroup( int groupId, String accountId );
	public void deleteGroup( int groupId );
}
