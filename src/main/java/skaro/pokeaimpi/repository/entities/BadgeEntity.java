package skaro.pokeaimpi.repository.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "badge")
public class BadgeEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;
	
	@Column(name = "point_threshold")
	private Integer pointThreshold;
	
	@Column(name = "earnable", nullable = false)
	private Boolean canBeEarnedWithPoints;
	
	@Column(name = "image_uri", nullable = false)
	private String imageUri;
	
	@Column(name = "title", nullable = false)
	private String title;
	
	@Column(name = "description", nullable = false)
	private String description;
	
	@Column(name = "discord_role_id", unique = true, nullable = false)
	private String discordRoleId;
	
	public BadgeEntity() {
		
	}
	
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
