package xyz.jonmclean.EHealth.models.response;

import java.util.List;

import xyz.jonmclean.EHealth.models.Doctor;

public class BoundaryResponse {
	
	Long id;
	String name;
	List<List<Coordinate>> coordinates;
	
	Doctor owner;
	
	public BoundaryResponse() {}

	public BoundaryResponse(Long id, String name, List<List<Coordinate>> coordinates, Doctor owner) {
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<List<Coordinate>> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<List<Coordinate>> coordinates) {
		this.coordinates = coordinates;
	}

	public Doctor getOwner() {
		return owner;
	}

	public void setOwner(Doctor owner) {
		this.owner = owner;
	}
}
