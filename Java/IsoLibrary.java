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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Document;

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
			e.printStackTrace();
		}

		isol = new IsoLibrary(defaultDirectoryPath);
		String [] titles = isol.getMovieTitles();
		isol.downloadPosters();

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

	private String constructTitle(String isoTitle) {
		isoTitle = isoTitle.substring(0, isoTitle.lastIndexOf(".iso"));

		return isoTitle;
	}

	public void downloadPosters() {
		this.createPostersDirectory();
		// iterate over each file string with global string without .iso

		for (String isoTitle : this.movieTitles) {
			String title = this.constructTitle(isoTitle);

			try {
				this.downloadPoster(title, this.getPosterDownloadLink(this.getIMDBMovieId(title)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void downloadPoster(String title, String downloadPosterLink) {
		String fileName = posters.getPath() + "/" + title + ".jpg"; //The file that will be saved on your computer

		try {
			// Code to download
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

		}
	}

	// Might consider getting posters from a google - wikipedia search
	public String getIMDBMovieId(String title) {
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

	public String getPosterDownloadLink(String imdbId) {
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
		        //handle it
		    }  

		    if (result) {    
		        System.out.println("SUCCESSFUL Creation of posters directory!");  
		    }
		}
	}
}









