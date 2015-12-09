import java.io.File;
import java.net.URL;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

public class DownloadPoster {
	public static void main(String[] args) {
		// File file = new File("test.jpg");

		// FileUtils.copyUrlToFile(new URL("http://ia.media-imdb.com/images/M/MV5BMTM2MTM4MzY2OV5BMl5BanBnXkFtZTcwNjQ3NzI4NA@@._V1_SY317_CR0,0,214,317_AL_.jpg"), file);

		String fileName = "test.jpg"; //The file that will be saved on your computer
		try {
			//Code to download

			URL link = new URL("http://ia.media-imdb.com/images/M/MV5BMTM2MTM4MzY2OV5BMl5BanBnXkFtZTcwNjQ3NzI4NA@@._V1_SY317_CR0,0,214,317_AL_.jpg"); //The file that you want to download
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
}