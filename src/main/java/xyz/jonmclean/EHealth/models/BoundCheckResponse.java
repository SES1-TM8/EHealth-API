package xyz.jonmclean.EHealth.models;

public class BoundCheckResponse {
	
	Long boundaryId;
	double latitude;
	double longitude;
	boolean withinBounds = false;
	
	public BoundCheckResponse() {}
	
	public BoundCheckResponse(double latitude, double longitude, boolean withinBounds) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.withinBounds = withinBounds;
	}

	public Long getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(Long boundaryId) {
		this.boundaryId = boundaryId;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public boolean isWithinBounds() {
		return withinBounds;
	}

	public void setWithinBounds(boolean withinBounds) {
		this.withinBounds = withinBounds;
	}
}
