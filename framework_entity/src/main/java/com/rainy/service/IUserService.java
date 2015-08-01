package com.rainy.service;

import java.io.Serializable;
import java.util.List;

import com.rainy.dto.UserDtoDetail;

public interface IUserService extends Serializable{
	
	/**
	 * 根据实体Id查找实体对象
	 * @param IUserId
	 * @return
	 */
	UserDtoDetail getIUserById (String id);
	
	/**
	 * 根据用户名查找所有用户
	 * @param IUserId
	 * @return
	 */
	List<UserDtoDetail> getIUsersByName (String entityName);
	
	/**
	 * 根据用户代码查找用户
	 * @param code
	 * @return
	 */
	UserDtoDetail getIUserByCode (String code);

}
