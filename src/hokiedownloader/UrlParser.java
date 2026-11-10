package hokiedownloader;

import java.net.URI;
import java.net.URISyntaxException;

// -------------------------------------------------------------------------
/**
 * MODEL. Turns a pasted URL into a clean file name or rejects it, thus it never
 * prints and only throws an exception with a message for the controller to
 * show.
 *
 * @author Mikey
 * @version Nov 9, 2026
 */
public class UrlParser
{

    /**
     * Gets the file name at the end of a URL.
     *
     * @param url
     *            the URL the user pasted
     * @return the file name
     * @throws IllegalArgumentException
     *             if the URL is empty, too long, not http or https, or has no
     *             file name
     */
    public static String parseFileName(String url)
    {

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
            if (scheme == null || (!scheme.equalsIgnoreCase("http")
                && !scheme.equalsIgnoreCase("https")))
            {
                throw new IllegalArgumentException("Not a valid URL");
            }

            String path = uri.getPath();

            if (path == null || path.isEmpty())
            {
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
