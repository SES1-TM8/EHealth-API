package xyz.jonmclean.EHealth.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MapCoordinate {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long coordinateId;
	@ManyToOne
	OfficeBoundary boundary;
	double latitude;
	double longitude;
	int coordGroup;
	int coordOrder;
	
	public MapCoordinate() {}

	public MapCoordinate(OfficeBoundary boundary, double latitude, double longitude, int coordGroup,
			int coordOrder) {
		this.boundary = boundary;
		this.latitude = latitude;
		this.longitude = longitude;
		this.coordGroup = coordGroup;
		this.coordOrder = coordOrder;
	}



	public long getCoordinateId() {
		return coordinateId;
	}

	public void setCoordinateId(long coordinateId) {
		this.coordinateId = coordinateId;
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

	public OfficeBoundary getBoundary() {
		return boundary;
	}

	public void setBoundary(OfficeBoundary boundary) {
		this.boundary = boundary;
	}

	public int getCoordGroup() {
		return coordGroup;
	}

	public void setCoordGroup(int coordGroup) {
		this.coordGroup = coordGroup;
	}

	public int getCoordOrder() {
		return coordOrder;
	}

	public void setCoordOrder(int coordOrder) {
		this.coordOrder = coordOrder;
	}
}
