package skaro.pokeaimpi.web.dtos;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Embeddable
public class BadgeDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	@Min(value = 0, message = "point threshold must be positive")
	private Integer pointThreshold;
	private Boolean canBeEarnedWithPoints;
	@NotEmpty(message = "badge must have an image")
	@Pattern(regexp = "^(http|https)[^\\s]+$", message = "image url must point to a valid url")
	private String imageUri;
	@NotEmpty(message = "badge must have a title")
	private String title;
	@NotEmpty(message = "badge must have a description")
	private String description;
	@NotEmpty(message = "badge must have a valid Discord role id")
	private String discordRoleId;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getPointThreshold() {
		return pointThreshold;
	}
	public void setPointThreshold(Integer pointThreshold) {
		this.pointThreshold = pointThreshold;
	}
	public Boolean getCanBeEarnedWithPoints() {
		return canBeEarnedWithPoints;
	}
	public void setCanBeEarnedWithPoints(Boolean canBeEarnedWithPoints) {
		this.canBeEarnedWithPoints = canBeEarnedWithPoints;
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
	public String getDiscordRoleId() {
		return discordRoleId;
	}
	public void setDiscordRoleId(String discordRoleId) {
		this.discordRoleId = discordRoleId;
	}
	
}
