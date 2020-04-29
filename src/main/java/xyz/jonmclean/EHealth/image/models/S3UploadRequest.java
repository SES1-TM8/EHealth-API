package xyz.jonmclean.EHealth.image.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;

public class S3UploadRequest {
	
	@Id
	@Column(length = 512)
	public String id;
	
	@Column(length = 512)
	public String service;
	
	@Column(nullable = true)
	public Long ownerId;
	
	@Column
	public Date expire;
	
	@Column
	public String mimeType;
	
	public S3UploadRequest() {}
	
	public S3UploadRequest(String id, String service, Long ownerId, Date expire, String mimeType) {
		this.id = id;
		this.service = service;
		this.ownerId = ownerId;
		this.expire = expire;
		this.mimeType = mimeType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire(Date expire) {
		this.expire = expire;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}
}
