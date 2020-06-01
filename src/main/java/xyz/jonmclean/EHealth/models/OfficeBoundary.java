package xyz.jonmclean.EHealth.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class OfficeBoundary {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long boundaryId;
	
	@Column(nullable = true)
	String name;
	
	@OneToMany
	@OrderBy("coord_group,coord_order")
	@JoinColumn(name = "boundaryId")
	List<MapCoordinate> bounds = new ArrayList<MapCoordinate>();
	
	@Column
	long ownerId;
	
	public OfficeBoundary() {}
	
	public OfficeBoundary(String name, List<MapCoordinate> bounds, long ownerId) {
		this.name = name;
		this.bounds = bounds;
		this.ownerId = ownerId;
	}

	public long getBoundaryId() {
		return boundaryId;
	}

	public void setBoundaryId(long boundaryId) {
		this.boundaryId = boundaryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MapCoordinate> getBounds() {
		return bounds;
	}

	public void setBounds(List<MapCoordinate> bounds) {
		this.bounds = bounds;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}
}
