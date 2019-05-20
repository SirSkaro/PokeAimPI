package skaro.pokeaimpi;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import skaro.pokeaimpi.repository.entities.UserEntity;
import skaro.pokeaimpi.web.dtos.UserDTO;

@Configuration
public class PokeAimAPIConfig {
	
	@Bean("modelMapper")
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ModelMapper getModelMapper() {
		return new ModelMapper();
	}
	
	@Bean("userModelMapper")
	@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
	public ModelMapper getUserModelMapper() {
		
		PropertyMap<UserEntity, UserDTO> userMapping = new PropertyMap<UserEntity, UserDTO>() {
			@Override
			protected void configure()
			{
				map().getSocialProfile().getDiscordConnection().setDiscordId(source.getDiscordId());
				map().getSocialProfile().getTwitchConnection().setUserName(source.getTwitchUserName());
			}
		};
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(userMapping);
		
		return modelMapper;
	}
	
}
