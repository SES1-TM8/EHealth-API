package xyz.jonmclean.EHealth.image.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long imageId;
	@OneToMany
	List<ImageVariant> imageVariants = new ArrayList<>();
	@Column(nullable = true)
	Long ownerId;
	@Column(nullable = true)
	String accessibilityDescription;
	@Column
	boolean explicit = false;
	
	public Image(){}
	
	public Image(long imageId, List<ImageVariant> imageVariants) {
		super();
		this.imageId = imageId;
		this.imageVariants = imageVariants;
	}

	public Image(long imageId, List<ImageVariant> imageVariants, Long ownerId, String accessibilityDescription) {
		super();
		this.imageId = imageId;
		this.imageVariants = imageVariants;
		this.ownerId = ownerId;
		this.accessibilityDescription = accessibilityDescription;
	}

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public List<ImageVariant> getImageVariants() {
		return imageVariants;
	}

	public void setImageVariants(List<ImageVariant> imageVariants) {
		this.imageVariants = imageVariants;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public String getAccessibilityDescription() {
		return accessibilityDescription;
	}

	public void setAccessibilityDescription(String accessibilityDescription) {
		this.accessibilityDescription = accessibilityDescription;
	}

	public boolean isExplicit() {
		return explicit;
	}

	public void setExplicit(boolean explicit) {
		this.explicit = explicit;
	}
	
}