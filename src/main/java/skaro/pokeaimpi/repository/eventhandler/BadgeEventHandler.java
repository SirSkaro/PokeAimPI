package skaro.pokeaimpi.repository.eventhandler;

import java.lang.invoke.MethodHandles;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import skaro.pokeaimpi.messaging.MessagingConfig;
import skaro.pokeaimpi.sdk.messaging.BadgeEventType;
import skaro.pokeaimpi.sdk.resource.Badge;
import skaro.pokeaimpi.services.BadgeMessageService;

@Component
@Profile(MessagingConfig.MESSAGING_PROFILE)
public class BadgeEventHandler {
	private static final Logger LOG = LogManager.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private BadgeMessageService messageService;
	
	public void sendBadgeDeleteEvent(Badge badge) {
		LOG.info("Sending delete event for Badge {}", badge.getTitle());
		messageService.sendBadgeMessage(badge, BadgeEventType.DELETE);
	}
	
}
