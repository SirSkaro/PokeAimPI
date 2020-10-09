package skaro.pokeaimpi.messaging;

import java.io.Serializable;

import skaro.pokeaimpi.web.dtos.BadgeDTO;

public class BadgeEventMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private BadgeDTO badge;
	private BadgeEventType eventType;
	
	public BadgeDTO getBadge() {
		return badge;
	}
	
	public void setBadge(BadgeDTO badge) {
		this.badge = badge;
	}
	public BadgeEventType getEventType() {
		return eventType;
	}
	public void setEventType(BadgeEventType eventType) {
		this.eventType = eventType;
	}
	
}
