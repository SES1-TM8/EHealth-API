package xyz.jonmclean.EHealth.models.response;

import java.sql.Timestamp;

public class SessionResponse {
	
	public long sessionId;
	public String token;
	public long expiry;
	public long userId;
	public String firebaseToken;
	
	public SessionResponse() {}
	
	public SessionResponse(long sessionId, String token, long expiry, long userId, String firebaseToken) {
		this.sessionId = sessionId;
		this.token = token;
		this.expiry = expiry;
		this.userId = userId;
		this.firebaseToken = firebaseToken;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public long getExpiry() {
		return expiry;
	}

	public void setExpiry(long expiry) {
		this.expiry = expiry;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirebaseToken() {
		return firebaseToken;
	}

	public void setFirebaseToken(String firebaseToken) {
		this.firebaseToken = firebaseToken;
	}
}
