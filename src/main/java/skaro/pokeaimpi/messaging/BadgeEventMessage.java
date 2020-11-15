package skaro.pokeaimpi.messaging;

import java.io.Serializable;

import skaro.pokeaimpi.sdk.resource.Badge;

public class BadgeEventMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private Badge badge;
	private BadgeEventType eventType;
	
	public Badge getBadge() {
		return badge;
	}
	
	public void setBadge(Badge badge) {
		this.badge = badge;
	}
	public BadgeEventType getEventType() {
		return eventType;
	}
	public void setEventType(BadgeEventType eventType) {
		this.eventType = eventType;
	}
	
}
