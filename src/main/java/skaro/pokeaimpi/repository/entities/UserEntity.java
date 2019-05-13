package skaro.pokeaimpi.repository.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class UserEntity {
	
	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
	private Integer points;
	private SocialProfile socialProfile;
	
	public UserEntity() {
		
	}
	
	public Integer getId() {
		return id;
	}
	public Integer getPoints() {
		return points;
	}
	public SocialProfile getSocialProfile() {
		return socialProfile;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public void setPoints(Integer points) {
		this.points = points;
	}
	public void setSocialProfile(SocialProfile socialProfile) {
		this.socialProfile = socialProfile;
	}
	
	public int incrementPoints(int pointsToAdd) {
		this.points += pointsToAdd;
		return this.points;
	}
	
}
