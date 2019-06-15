package skaro.pokeaimpi.repository.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class BadgeEntity implements PokeAimPIEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private Integer pointThreshold;
	private Boolean canBeEarnedWithPoints;
	private String imageUri;
	private String title;
	private String description;
	private Long discordRoleId;
	
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
	public Long getDiscordRoleId() {
		return discordRoleId;
	}
	public void setDiscordRoleId(Long discordRoleId) {
		this.discordRoleId = discordRoleId;
	}
	
}
