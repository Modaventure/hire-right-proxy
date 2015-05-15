package com.mv.hr;

import com.mv.hr.config.HireRightConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HireRightTestConfiguration implements HireRightConfiguration {
    private Properties properties;

    public HireRightTestConfiguration() throws IOException {
        String filePath = System.getProperty("envConfigPropertiesFilePath");

        System.out.println("=============================================================");
        System.out.println("# Loading HireRight configuration from file:");
        System.out.println("# " + filePath);
        System.out.println("=============================================================");

        FileInputStream inputStream = new FileInputStream(new File(filePath));
        properties = new Properties();
        properties.load(inputStream);
        inputStream.close();
    }

    @Override
    public String getHireRightApiUrl() {
        return properties.getProperty("endpoint.url");
    }

    @Override
    public String getHireRightApiProfile() {
        return properties.getProperty("profile");
    }

    @Override
    public String getHireRightApiUsername() {
        return properties.getProperty("username");
    }

    @Override
    public String getHireRightApiPassword() {
        return properties.getProperty("password");
    }
}
