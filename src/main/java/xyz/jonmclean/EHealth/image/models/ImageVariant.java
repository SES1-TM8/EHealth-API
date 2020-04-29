package xyz.jonmclean.EHealth.image.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class ImageVariant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	long imageVariantId;
	@Column
	int width;
	@Column
	int height;
	@Column(length = 512)
	String url;
	
	public ImageVariant(){}

	public ImageVariant(long imageVariantId, int width, int height, String url) {
		super();
		this.imageVariantId = imageVariantId;
		this.width = width;
		this.height = height;
		this.url = url;
	}
	
	public ImageVariant(int width, int height, String url) {
		super();
		this.width = width;
		this.height = height;
		this.url = url;
	}
	
	@JsonIgnore
	public long getImageVariantId() {
		return imageVariantId;
	}

	public void setImageVariantId(long imageVariantId) {
		this.imageVariantId = imageVariantId;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}