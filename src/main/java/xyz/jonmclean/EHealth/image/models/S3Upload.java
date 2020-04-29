package xyz.jonmclean.EHealth.image.models;

public class S3Upload {
	
	public String uploadURL;
	public String callbackURL;
	
	public S3Upload(String uploadURL, String callbackURL) {
		this.uploadURL = uploadURL;
		this.callbackURL = callbackURL;
	}

	public String getUploadURL() {
		return uploadURL;
	}

	public void setUploadURL(String uploadURL) {
		this.uploadURL = uploadURL;
	}

	public String getCallbackURL() {
		return callbackURL;
	}

	public void setCallbackURL(String callbackURL) {
		this.callbackURL = callbackURL;
	}
}
