package xyz.jonmclean.EHealth.models;

public class InternalImageCallback {
	
	public long appointmentInfoId;
	public long imageId;
	
	public InternalImageCallback() {}
	
	public InternalImageCallback(long appointmentInfoId, long imageId) {
		this.appointmentInfoId = appointmentInfoId;
		this.imageId = imageId;
	}

	public long getAppointmentInfoId() {
		return appointmentInfoId;
	}

	public void setAppointmentInfoId(long appointmentInfoId) {
		this.appointmentInfoId = appointmentInfoId;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}
}
