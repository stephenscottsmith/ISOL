import java.io.File;

public class test {
	public static void main(String[] args) {
		File folder = new File("/Volumes/Drobo/Library");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
		  	if (listOfFiles[i].isFile()) {
		  		String filenameWithExtension = listOfFiles[i].getName();
		    	System.out.println(filenameWithExtension.substring(0, filenameWithExtension.lastIndexOf(".")));
		  	} else if (listOfFiles[i].isDirectory()) {
		    	System.out.println(listOfFiles[i].getName() + "/");
		  	}
		}
	}
}
		