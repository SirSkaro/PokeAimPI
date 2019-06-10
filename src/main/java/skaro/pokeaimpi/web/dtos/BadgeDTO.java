package skaro.pokeaimpi.web.dtos;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.URL;

import lombok.Data;

@Data
@Embeddable
public class BadgeDTO {
	
	private Integer pointThreshold;
	@URL(protocol = "http", message = "image url must point to a valid url")
	private String imageUri;
	@NotEmpty(message = "badge must have a title")
	private String title;
	@NotEmpty(message = "badge must have a description")
	private String description;
	@Positive(message = "badge must have a valid Discord role id")
	private Long discordRoleId;
	
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
	public Long getDiscordRoleId() {
		return discordRoleId;
	}
	public void setDiscordRoleId(Long discordRoleId) {
		this.discordRoleId = discordRoleId;
	}
	
}
