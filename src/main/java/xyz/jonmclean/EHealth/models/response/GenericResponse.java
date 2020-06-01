package xyz.jonmclean.EHealth.models.response;

public class GenericResponse {
	
	public boolean success = false;
	public String body = "";
	
	public GenericResponse(boolean success, String body) {
		this.success = success;
		this.body = body;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
