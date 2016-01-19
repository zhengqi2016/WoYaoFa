package com.lib_common.bean;

import java.io.Serializable;

/**
 * 手机设备的信息实体
 * 
 * @author m
 * 
 */
public class DeviceInfoBean implements Serializable {
	private static final long serialVersionUID = 5942043004697775092L;

	private int id;
	private String deviceId; // 设备ID
	private String currentVersion; // 当前软件版本号
	private String lastLoginPlatform; // 最后一次登录使用的手机平台
	private String deviceSoftwareVersion; // 操作系统版本号
	private String subscriberId; // 用户唯一ID
	private String phoneModel; // 手机型号
	private String mac; // 物理地址
	private String OSRelease; // 手机操作系统版本号
	private String accountId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getCurrentVersion() {
		return currentVersion;
	}

	public void setCurrentVersion(String currentVersion) {
		this.currentVersion = currentVersion;
	}

	public String getLastLoginPlatform() {
		return lastLoginPlatform;
	}

	public void setLastLoginPlatform(String lastLoginPlatform) {
		this.lastLoginPlatform = lastLoginPlatform;
	}

	public String getDeviceSoftwareVersion() {
		return deviceSoftwareVersion;
	}

	public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
		this.deviceSoftwareVersion = deviceSoftwareVersion;
	}

	public String getSubscriberId() {
		return subscriberId;
	}

	public void setSubscriberId(String subscriberId) {
		this.subscriberId = subscriberId;
	}

	public String getPhoneModel() {
		return phoneModel;
	}

	public void setPhoneModel(String phoneModel) {
		this.phoneModel = phoneModel;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOSRelease() {
		return OSRelease;
	}

	public void setOSRelease(String oSRelease) {
		OSRelease = oSRelease;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

}
