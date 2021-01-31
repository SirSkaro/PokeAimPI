package skaro.pokeaimpi.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

import skaro.pokeaimpi.sdk.config.PokeAimPiSdkMessagingConfig;

@Configuration
@Profile(MessagingConfig.MESSAGING_PROFILE)
@Import(PokeAimPiSdkMessagingConfig.class)
public class MessagingConfig {
	public static final String MESSAGING_PROFILE = "pub-sub";

	
}
