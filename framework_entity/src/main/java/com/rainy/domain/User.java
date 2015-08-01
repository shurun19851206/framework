package com.rainy.domain;

import java.util.Date;

public class User  {

	/**
	 * id编号
	 */
	private String id;
	
	/**
	 * 用户代码
	 */
	private String userCode;
	
	/**
	 * 用户名称
	 */
	private String userName;
	
	/**
	 * 用户昵称
	 */
	private String nickName;
	
	/**
	 * 用户地址
	 */
	private String address;
	
	/**
	 * 用户权限id
	 */
	private String authId;
	
	/**
	 * 创建人名称
	 */
	private String createdByUserName;
	
	/**
	 * 创建人编码
	 */
	private String createdByUserCode;
	
	/**
	 * 创建时间
	 */
	private Date createdDtmLoc;
	
	/**
	 * 更新人名称
	 */
	private String updatedByUserName;
	
	/**
	 * 更新人编码
	 */
	private String updatedByUserCode;
	
	/**
	 * 更新时间
	 */
	private Date updatedDtmLoc;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getCreatedByUserName() {
		return createdByUserName;
	}

	public void setCreatedByUserName(String createdByUserName) {
		this.createdByUserName = createdByUserName;
	}

	public String getCreatedByUserCode() {
		return createdByUserCode;
	}

	public void setCreatedByUserCode(String createdByUserCode) {
		this.createdByUserCode = createdByUserCode;
	}

	public Date getCreatedDtmLoc() {
		return createdDtmLoc;
	}

	public void setCreatedDtmLoc(Date createdDtmLoc) {
		this.createdDtmLoc = createdDtmLoc;
	}

	public String getUpdatedByUserName() {
		return updatedByUserName;
	}

	public void setUpdatedByUserName(String updatedByUserName) {
		this.updatedByUserName = updatedByUserName;
	}

	public String getUpdatedByUserCode() {
		return updatedByUserCode;
	}

	public void setUpdatedByUserCode(String updatedByUserCode) {
		this.updatedByUserCode = updatedByUserCode;
	}

	public Date getUpdatedDtmLoc() {
		return updatedDtmLoc;
	}

	public void setUpdatedDtmLoc(Date updatedDtmLoc) {
		this.updatedDtmLoc = updatedDtmLoc;
	}

	public User(String id, String userCode, String userName, String nickName,
			String address, String authId, String createdByUserName,
			String createdByUserCode, Date createdDtmLoc,
			String updatedByUserName, String updatedByUserCode,
			Date updatedDtmLoc) {
		super();
		this.id = id;
		this.userCode = userCode;
		this.userName = userName;
		this.nickName = nickName;
		this.address = address;
		this.authId = authId;
		this.createdByUserName = createdByUserName;
		this.createdByUserCode = createdByUserCode;
		this.createdDtmLoc = createdDtmLoc;
		this.updatedByUserName = updatedByUserName;
		this.updatedByUserCode = updatedByUserCode;
		this.updatedDtmLoc = updatedDtmLoc;
	}

	public User() {
		super();
	}
	
}
