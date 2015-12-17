package io.isol.ws.service;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.stereotype.Service;

import io.isol.ws.model.Title;

@Service
public class TitleServiceBean implements TitleService {

	@Override
	public Collection<Title> findAll() {
		File library = new File("/Volumes/Drobo/Library");
		File [] isoTitles = library.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".iso");
			}
		});
		Collection<Title> titles = new ArrayList<Title>();
		
		for (int i = 0; i < isoTitles.length; i++) {
			String title = isoTitles[i].getName();
			
			titles.add(new Title(title.substring(0, title.indexOf(".iso"))));
		}
		
		return titles;
	}

	@Override
	public Title downloadTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}

}
