package skaro.pokeaimpi.services;

import skaro.pokeaimpi.sdk.messaging.BadgeEventType;
import skaro.pokeaimpi.sdk.resource.Badge;

public interface BadgeMessageService {
	void sendBadgeMessage(Badge badge, BadgeEventType type);
}
