package com.pramati.imaginea.webCrawler;

import com.pramati.imaginea.webCrawler.utils.WebCrawlerConstants;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Created by babjik on 29/4/16.
 */
public class FileWriterTest {

    @Test
    public void testcase() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = null;
        try {
            is = classLoader.getResourceAsStream("./webcrawler_test.properties");
            properties.load(is);

            System.out.println(properties.getProperty("BASE_URL"));
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load properites " + e.getMessage());
        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException ignore) {
                    System.err.println(ignore.getMessage());
                }
        }

        assert (!properties.isEmpty());
    }

    @Test
    public void testPropertiesWithOutResourceStream() throws URISyntaxException {
        URI uri = Thread.currentThread().getContextClassLoader().getResource("webcrawler_test.properties").toURI();
        System.out.println(uri.getPath());
        File f = new File(uri);
        System.out.println("file exits :: " + f.exists());
        assert (f.exists());
    }
}
