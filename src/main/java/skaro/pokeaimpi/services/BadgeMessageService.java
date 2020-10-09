package skaro.pokeaimpi.services;

import skaro.pokeaimpi.messaging.BadgeEventType;
import skaro.pokeaimpi.web.dtos.BadgeDTO;

public interface BadgeMessageService {
	void sendBadgeMessage(BadgeDTO badge, BadgeEventType type);
}
