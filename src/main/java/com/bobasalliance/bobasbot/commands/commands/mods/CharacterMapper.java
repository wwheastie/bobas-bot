package com.bobasalliance.bobasbot.commands.commands.mods;

import org.springframework.stereotype.Component;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Component
public class CharacterMapper extends ConfigurableMapper {
	@Override
	protected void configure(final MapperFactory mapperFactory) {
		mapperFactory.classMap(CharacterDao.class, CharacterDto.class)
				.byDefault()
				.register();
	}
}
