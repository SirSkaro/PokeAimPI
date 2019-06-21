package skaro.pokeaimpi.repository.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserEntity implements PokeAimPIEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private Integer points;
	private Long discordId;
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
	public Long getDiscordId() {
		return discordId;
	}
	public void setDiscordId(Long discordId) {
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
