import java.io.File;
import java.io.FilenameFilter;	
import java.util.Set;
import java.util.LinkedHashSet;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ISOL {
	private File [] isoFiles;
	private Set<String> uniqueSearchTerms;
	private File library;
	private File posters;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		String defaultDirectoryPath = "/Volumes/Drobo/Library";
		ISOL isol = null;

		try {
			isol = new ISOL(args[0]);
		} catch (Exception e) {
			System.out.println("You did not input a custom iso library directory, so I will use the default: " + defaultDirectoryPath);	
		}

		isol = new ISOL(defaultDirectoryPath);
		isol.finished(startTime, isol.downloadPostersViaApi());
	}

	public ISOL(String isoLibraryDirectoryPath) {
		library = new File(isoLibraryDirectoryPath);
		isoFiles = library.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".iso");
			}
		});
		uniqueSearchTerms = new LinkedHashSet<String>();
	}

	public void finished(long startTime, int posterCount) {
		System.out.println("Finished downloading " + posterCount + " posters in: " + (((System.currentTimeMillis() - startTime) / 1000) / 60) + 
						   " minutes and " + (((System.currentTimeMillis() - startTime) / 1000) % 60) + " seconds."); 

		System.out.println("NUMBER OF MOVIES: " + isoFiles.length);
	}

	public int downloadPostersViaApi() {
		this.createPostersDirectory();
		// Create unique strings set
		int count = 0;

		System.out.println("Creating unique search terms from the iso file names...");
		for (File isoFile : isoFiles) {
			uniqueSearchTerms.add(constructSearchTitle(isoFile.getName()));
		}
		System.out.println("There are " + uniqueSearchTerms.size() + " titles to search for.");

		// Create search id link, get id, download 
		System.out.println("Downloading by search term...");
		for (String uniqueSearchTerm : uniqueSearchTerms) {
			try {	
				System.out.println("Downloading at www.omdbapi.com/?t=" + uniqueSearchTerm);
				String searchUrl = "http://www.omdbapi.com/?t=" + uniqueSearchTerm;
				JSONObject jsonSearchResult = (JSONObject) new JSONTokener(IOUtils.toString(new URL(searchUrl))).nextValue();
				String downloadLink = null;

				try {
					downloadLink = jsonSearchResult.getString("Poster");
					
				} catch (Exception e) {
					System.out.println("NO DOWNLOAD LINK PROVIDED: Adding to the redo list!");
					e.printStackTrace();
				}

				downloadPoster(uniqueSearchTerm, downloadLink);
				count++;
				
			} catch (Exception e) {
				System.out.println("MALFORMED URL: Adding to the redo list!");
				///// DO THIS !!!! /////
				e.printStackTrace();
			}
		}

		return count;
	}

	public void downloadPoster(String title, String downloadPosterLink) {
		String fileName = posters.getPath() + "/" + title.replace("%20", " ") + ".jpg"; //The file that will be saved on your computer

		try {
			// Code to download
			System.out.println(downloadPosterLink);
			URL link = new URL(downloadPosterLink); //The file that you want to download
			InputStream in = new BufferedInputStream(link.openStream());
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			int n = 0;

			while (-1 != (n = in.read(buf))) {
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

	private String constructSearchTitle(String isoTitle) {
		int cutoffIndex = smallestNonNegative(
							isoTitle.lastIndexOf(".iso"),
							isoTitle.indexOf("Season"),
							isoTitle.indexOf("Volume"),
							isoTitle.indexOf("Disc"),
							isoTitle.length()
						);

		String searchTitle = isoTitle.substring(0, cutoffIndex >= 0 ? cutoffIndex : 
															isoTitle.length()).trim();
		int dashIndex = searchTitle.lastIndexOf("-");

		if (dashIndex == searchTitle.length() - 1) {
			return searchTitle.substring(0, dashIndex).trim();
		}

		return searchTitle.replaceAll(" ", "%20").trim();
	}

	private int smallestNonNegative(int ... indices) {
		int smallest = indices[0];

		for (int i = 1; i < indices.length; i++) {
			int current = indices[i];

			if (current < smallest && current >= 0) {
				smallest = current;
			}
		}

		return smallest;
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