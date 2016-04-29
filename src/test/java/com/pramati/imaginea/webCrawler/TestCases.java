package com.pramati.imaginea.webCrawler;

import com.pramati.imaginea.webCrawler.utils.WebCrawlerConstants;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by babjik on 29/4/16.
 */
public class TestCases {

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
}
