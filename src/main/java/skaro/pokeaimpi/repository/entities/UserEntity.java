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
@Table(name = "user")
public class UserEntity implements PokeAimPIEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;
	
	@Column(name = "points")
	private Integer points;
	
	@Column(name = "discord_id", unique = true)
	private String discordId;
	
	@Column(name = "twitch_user_name", unique = true)
	private String twitchUserName;
	
	public UserEntity() {

	}
	
	public Integer getId() {
		return id;
	}
	public Integer getPoints() {
		return points;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public String getDiscordId() {
		return discordId;
	}
	public void setDiscordId(String discordId) {
		this.discordId = discordId;
	}
	public String getTwitchUserName() {
		return twitchUserName;
	}
	public void setTwitchUserName(String twitchUserName) {
		this.twitchUserName = twitchUserName;
	}

	public int incrementPointsBy(int pointsToAdd) {
		this.points += pointsToAdd;
		return this.points;
	}
	
}
