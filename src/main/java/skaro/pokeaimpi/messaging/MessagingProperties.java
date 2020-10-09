package skaro.pokeaimpi.messaging;

import javax.validation.constraints.NotNull;

public class MessagingProperties {
	@NotNull
	private String badges;
	@NotNull
	private String fanout;

	public String getBadges() {
		return badges;
	}
	public void setBadges(String badges) {
		this.badges = badges;
	}
	public String getFanout() {
		return fanout;
	}
	public void setFanout(String fanout) {
		this.fanout = fanout;
	}
	
}
