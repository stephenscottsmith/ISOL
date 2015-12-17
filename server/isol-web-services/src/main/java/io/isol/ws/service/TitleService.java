package io.isol.ws.service;

import java.util.Collection;

import io.isol.ws.model.Title;

public interface TitleService {
	Collection<Title> findAll();
	
	Title downloadTitle(String title);
}
