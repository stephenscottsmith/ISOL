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

public class ReplaceDemo {
	public static void main(String[] args) {
		String input = 
                  "<a href=\"/title/tt0103359/?ref_=fn_al_tt_1\"><img src=\"http://ia.media-imdb.com/images/M/MV5BMTU3MjcwNzY3NF5BMl5BanBnXkFtZTYwNzA2MTI5._V1_UX32_CR0,0,32,44_AL_.jpg\"></a>";

		Pattern p = Pattern.compile("\\/title\\/[A-Za-z0-9]*\\/");
		Matcher m = p.matcher(input);

		StringBuffer result = new StringBuffer();
		// while (m.find()) {
			// System.out.println(m.group());
		// }
		m.find();
		System.out.println(m.group());
	}
}