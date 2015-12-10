import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;

public class IsoLibrary {
	private File library;
	private File posters;
	private String[] movieTitles;

	public static void main(String[] args) {

		String defaultDirectoryPath = "/Volumes/Drobo/Library";
		IsoLibrary isol = null;

		try {
			isol = new IsoLibrary(args[0]);
		} catch (Exception e) {
			System.out.println("You did not input a custom iso library directory, so I will use the default: " + defaultDirectoryPath);	
		}

		isol = new IsoLibrary(defaultDirectoryPath);
		String [] titles = isol.getMovieTitles();
		long startTime = System.currentTimeMillis();
		isol.downloadPosters();
		System.out.println("Finished downloading posters in: " + (((System.currentTimeMillis() - startTime) / 1000) / 60) + 
						   " minutes and " + (((System.currentTimeMillis() - startTime) / 1000) % 60) + " seconds."); 

		System.out.println("NUMBER OF MOVIES: " + titles.length);
	}

	public IsoLibrary(String isoLibraryDirectoryPath) {
		library = new File(isoLibraryDirectoryPath);
		movieTitles = library.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".iso");
			}
		});
	}

	public String [] getMovieTitles() {
		return this.movieTitles;
	}

	public void downloadPosters() {
		this.createPostersDirectory();
		// iterate over each file string with global string without .iso
		String previousTitle = "";
		for (String isoTitle : this.movieTitles) {
			String title = this.constructSearchTitle(isoTitle);
			if (!title.equals(previousTitle)) {
				try {
					this.downloadPoster(title, this.getPosterDownloadLink(this.getIMDBMovieId(title)));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			previousTitle = title;	
		}
	}

	private String constructSearchTitle(String isoTitle) {
		int isoIndex = isoTitle.lastIndexOf(".iso"),
			seasonIndex = isoTitle.indexOf("Season"),
			volumeIndex = isoTitle.indexOf("Volume");

		if (seasonIndex > -1) {
			return isoTitle.substring(0, seasonIndex);
		} 

		if (volumeIndex > -1) {
			return isoTitle.substring(0, volumeIndex);
		} 

		int discIndex = isoTitle.indexOf("Disc"); 

		return isoTitle.substring(0, (discIndex > -1) ? discIndex : isoIndex);
	}

	public void downloadPoster(String title, String downloadPosterLink) {
		String fileName = posters.getPath() + "/" + title + ".jpg"; //The file that will be saved on your computer

		try {
			// Code to download
			System.out.println(downloadPosterLink);
			URL link = new URL(downloadPosterLink); //The file that you want to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;

			while (-1!=(n=in.read(buf)))
			{
			   out.write(buf, 0, n);
			}

			out.close();
			in.close();
			byte[] response = out.toByteArray();
	 
			FileOutputStream fos = new FileOutputStream(fileName);
			fos.write(response);
			fos.close();

		} catch (Exception e) {	 
			e.printStackTrace();
		}
	}

	// Might consider getting posters from a google - wikipedia search
	private String getIMDBMovieId(String title) {
		Element element = null;

		try {
			Document doc = Jsoup.connect("http://www.imdb.com/find?ref_=nv_sr_fn&q=" + title.replace(" ", "+").trim() + "&s=all").get();
			element = doc.select("table.findList tbody tr td a[href]").first(); // needs higher specificity for being in titles group
			System.out.println(title);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// regex for the IMDB id 
		return (element == null) ? "" : getTitleLinkString(element.toString());
	}

	private static String getTitleLinkString(String elementString) {
		Pattern p = Pattern.compile("\\/title\\/[A-Za-z0-9]*\\/");
		Matcher m = p.matcher(elementString);
		m.find();

		return m.group();
	}

	private String getPosterDownloadLink(String imdbId) {
		Element element = null;

		try {
			Document doc = Jsoup.connect("http://www.imdb.com" + imdbId).get();
			element = doc.select("table#title-overview-widget-layout tbody td div a img").first(); // needs higher specificity for being in titles group 
			System.out.println(imdbId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return (element == null) ? "" : 
				element.toString().substring(element.toString().indexOf("http"), (element.toString().indexOf(".jpg") + 4));
	}

	public void createPostersDirectory() {
		this.posters = new File(this.library.getPath() + "/posters");

		if (!this.posters.exists()) {
		    System.out.println("Creating posters directory...");
		    boolean result = false;

		    try {
		        this.posters.mkdir();
		        result = true;
		    } catch (SecurityException se){
		        se.printStackTrace();
		    }  

		    if (result) {    
		        System.out.println("SUCCESSFUL Creation of posters directory!");  
		    }
		}
	}
}









