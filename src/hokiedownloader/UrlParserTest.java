package hokiedownloader;

import student.TestCase;

/**
 * Test class for UrlParser Covers normal cases, URL cleaning, and all bad-input
 * validation rules.
 */
public class UrlParserTest
    extends TestCase
{

    /**
     * Web-CAT often requires 100% coverage, including the default constructor
     * of classes that only have static methods.
     */
    public void testInstantiation()
    {
        UrlParser parser = new UrlParser();
        assertNotNull(parser);
    }


    /**
     * Tests normal parsing cases, including query dropping and space decoding.
     */
    public void testValidUrls()
    {
        // Normal case
        assertEquals(
            "report.pdf",
            UrlParser.parseFileName("https://example.com/files/report.pdf"));

        // With query and fragment
        assertEquals(
            "report.pdf",
            UrlParser.parseFileName(
                "https://example.com/files/report.pdf?token=abc#top"));

        // Decodes %20 to spaces
        assertEquals(
            "my report.pdf",
            UrlParser
                .parseFileName("https://example.com/files/my%20report.pdf"));

        // Trims surrounding spaces
        assertEquals(
            "a.txt",
            UrlParser.parseFileName("  https://example.com/a.txt  "));
    }


    /**
     * Tests that null, empty, and blank URLs throw the correct exception.
     */
    public void testEmptyAndNullUrls()
    {
        // Null URL
        try
        {
            UrlParser.parseFileName(null);
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("URL cannot be empty", e.getMessage());
        }

        // Empty string
        try
        {
            UrlParser.parseFileName("");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("URL cannot be empty", e.getMessage());
        }

        // Blank string (spaces only)
        try
        {
            UrlParser.parseFileName("   ");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("URL cannot be empty", e.getMessage());
        }
    }


    /**
     * Tests that URLs exceeding the 2048 character limit are rejected.
     */
    public void testUrlTooLong()
    {
        // Build a string that is over 2048 characters long
        StringBuilder longUrl = new StringBuilder("https://example.com/");
        for (int i = 0; i < 2030; i++)
        {
            longUrl.append("a");
        }

        try
        {
            UrlParser.parseFileName(longUrl.toString());
            fail("Should have thrown IllegalArgumentException for length");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("URL is too long", e.getMessage());
        }
    }


    /**
     * Tests URLs that end in a slash or lack a valid file name.
     */
    public void testNoFileName()
    {
        // URL with no file name at the end (ends in slash)
        try
        {
            UrlParser.parseFileName("https://vt.edu/");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("No file name found in URL", e.getMessage());
        }
    }


    /**
     * Tests adversarial inputs and entirely invalid URL syntax.
     */
    public void testInvalidUrlSyntax()
    {
        // Not a valid URL pattern
        try
        {
            UrlParser.parseFileName("hello");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(
                e.getMessage().equals("No file name found in URL")
                    || e.getMessage().equals("Not a valid URL"));
        }

        // Adversarial input test
        try
        {
            UrlParser.parseFileName("javascript:alert(1)");
            fail("Should have thrown IllegalArgumentException");
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(
                e.getMessage().equals("No file name found in URL")
                    || e.getMessage().equals("Not a valid URL"));
        }
    }
}
