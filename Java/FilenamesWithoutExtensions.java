import java.io.File;

public class FilenamesWithoutExtensions {
	private File folder = new File("/Volumes/Drobo/Library");
	private File[] listOfFiles = folder.listFiles();

	public static void main(String[] args) {
		
		
	}

	private static int getMovieTitleStrings() {
		int count = 0;

		for (int i = 0; i < listOfFiles.length; i++) {
		  	if (listOfFiles[i].isFile() && !listOfFiles[i].getName().equals(".DS_Store")) {
		  		String filenameWithExtension = listOfFiles[i].getName();
		    	System.out.println(filenameWithExtension.substring(0, filenameWithExtension.lastIndexOf(".")));
		    	count++;
		  	} else if (listOfFiles[i].isDirectory()) {
		    	System.out.println(listOfFiles[i].getName() + "/");
		  	}
		}

		return count;
	}
}
	
	