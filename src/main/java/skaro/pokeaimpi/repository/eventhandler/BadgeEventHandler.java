package skaro.pokeaimpi.repository.eventhandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;

import skaro.pokeaimpi.messaging.MessagingConfig;
import skaro.pokeaimpi.sdk.messaging.BadgeEventType;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.BadgeMessageService;

@RepositoryEventHandler(Badge.class)
@Profile(MessagingConfig.MESSAGING_PROFILE)
public class BadgeEventHandler {
	@Autowired
	private BadgeMessageService messageService;
	
	@HandleAfterDelete
	public void sendBadgeDeleteEvent(Badge badge) {
		messageService.sendBadgeMessage(badge, BadgeEventType.DELETE);
	}
	
}
