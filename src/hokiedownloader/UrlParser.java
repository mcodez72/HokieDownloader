package hokiedownloader;

import java.net.URI;
import java.net.URISyntaxException;

// -------------------------------------------------------------------------
/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author Mikey
 *  @version Nov 9, 2026
 */
public class UrlParser {
    
    public static String parseFileName(String url) {
        
        if (url == null || url.trim().isEmpty()) 
        {
            throw new IllegalArgumentException("URL cannot be empty");
        }
        
        url = url.trim();
        
        if (url.length() > 2048)
        {
            throw new IllegalArgumentException("URL is too long");
        }
        
        try 
        {
            URI uri = new URI(url);

            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equalsIgnoreCase("http") && !scheme.equalsIgnoreCase("https"))) {
                throw new IllegalArgumentException("Not a valid URL");
            }

            String path = uri.getPath();

            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("No file name found in URL");
            }

            String filename = path.substring(path.lastIndexOf("/") + 1);
            if (filename.equals(""))
            {
                throw new IllegalArgumentException("No file name found in URL");
            }
            
            return filename;

        } 
        
        catch (URISyntaxException e) 
        {
            throw new IllegalArgumentException("Not a valid URL");
        }
    }
}