package com.bobasalliance.bobasbot.commands.commands.mods;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends CrudRepository<CharacterDao, String> {
	List<CharacterDao> findAll();
}
