package xyz.jonmclean.EHealth.models.response;

public class Coordinate {
	
	double latitude;
	double longitude;
	int order;
	
	public Coordinate() {}

	public Coordinate(double latitude, double longitude, int order) {
		this.latitude = latitude;
		this.longitude = longitude;
		this.order = order;
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
}
