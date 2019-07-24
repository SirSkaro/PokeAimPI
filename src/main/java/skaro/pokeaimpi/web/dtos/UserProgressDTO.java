package skaro.pokeaimpi.web.dtos;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class UserProgressDTO {

	private UserDTO user;
	private BadgeDTO nextBadge;
	private BadgeDTO currentHighestBadge;
	private Integer pointToNextReward;
	private Integer currentPoints;
	
	public UserDTO getUser() {
		return user;
	}
	public void setUser(UserDTO user) {
		this.user = user;
	}
	public BadgeDTO getNextBadge() {
		return nextBadge;
	}
	public void setNextBadge(BadgeDTO nextBadge) {
		this.nextBadge = nextBadge;
	}
	public BadgeDTO getCurrentHighestBadge() {
		return currentHighestBadge;
	}
	public void setCurrentHighestBadge(BadgeDTO currentHighestBadge) {
		this.currentHighestBadge = currentHighestBadge;
	}
	public Integer getPointToNextReward() {
		return pointToNextReward;
	}
	public void setPointToNextReward(Integer pointToNextReward) {
		this.pointToNextReward = pointToNextReward;
	}
	public Integer getCurrentPoints() {
		return currentPoints;
	}
	public void setCurrentPoints(Integer currentPoints) {
		this.currentPoints = currentPoints;
	}
	
}
