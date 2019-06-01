package skaro.pokeaimpi.web.dtos;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class BadgeDTO {
	
	private Integer pointThreshold;
	private String imageUri;
	private String title;
	private String description;
	public Integer getPointThreshold() {
		return pointThreshold;
	}
	public void setPointThreshold(Integer pointThreshold) {
		this.pointThreshold = pointThreshold;
	}
	public String getImageUri() {
		return imageUri;
	}
	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
