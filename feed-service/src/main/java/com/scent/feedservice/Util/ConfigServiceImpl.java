package com.scent.feedservice.Util;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.*;

import static com.scent.feedservice.Util.Constants.*;


@Service
public class ConfigServiceImpl {

    Map<String, Properties> appConfigsMap;

    /**
     * Service Initialization
     */
    @PostConstruct
    public void init() {
        appConfigsMap = new HashMap<>();
        final File[] propertyFileList = FileUtil.getPropertyFileList();
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        for (File file : propertyFileList) {
            final String fileName = file.getName();
            final String brandName = CommonUtil.substringBefore(CommonUtil.substringBefore(fileName, DOT),
                    SEPARATOR);
            StringBuilder filePath = new StringBuilder();
            filePath.append(System.getProperty(SHARED_LOCATION)).append(FORWARD_SLASH)
                    .append(fileName);
            yaml.setResources(new FileSystemResource(new File(filePath.toString())));
            final Properties props = yaml.getObject();
            final Optional<Properties> staticProps = getStaticProperties(yaml, filePath);
            if (staticProps.isPresent()) {
                props.putAll(staticProps.get());
            }
            appConfigsMap.put(brandName.toLowerCase(), props);
        }
    }

    /**
     * This method returns the "-keys.yml" Properties from file system
     *
     * @param yaml
     *            The YamlPropertiesFactoryBean instance used to load the
     *            resources
     * @param filePath
     *            the path of the config file
     * @return the Properties Object
     */
    private Optional<Properties> getStaticProperties(YamlPropertiesFactoryBean yaml, StringBuilder filePath) {

        String staticFilePath = filePath.substring(0, filePath.lastIndexOf("-config.yml"));
        File staticFile = new File(staticFilePath.concat(YAML_KEYS_FILE));
        if (staticFile.exists()) {
            yaml.setResources(new FileSystemResource(staticFile));
            return Optional.of(yaml.getObject());
        } else {
            return Optional.empty();
        }
    }

    /**
     * This method will return the object value for the given brand and key.
     *
     * @param brandName
     *            Input brand to get property value.
     * @param key
     *            Input Key to get property value.
     * @return value Returns property value as Object.
     */
    public Object getPropertyValue(String fileName, String key) {
        Object value = null;
        if (CommonUtil.isEmpty(fileName) || CommonUtil.isEmpty(key)) {
            throw new IllegalArgumentException("Key is required!");
        } else {
            final Properties props = appConfigsMap.get(fileName.toLowerCase());

            if (props != null && props.containsKey(key)) {
                value = XssSanitizerUtil.stripXSS(props.get(key).toString());

            }
            if (value instanceof List && props.containsKey(key)) {
                value = XssSanitizerUtil.stripXSS(props.getProperty(key));
            }
        }
        return value;
    }

    /**
     * This method will return the object value for the given brand and key.
     *
     * @param brand
     *            Input brand to get property value.
     * @param key
     *            Input Key to get property value.
     * @return value Returns property value as String.
     */

    public String getPropertyValueAsString(String fileName, String key) {
        Object value = this.getPropertyValue(fileName, key);
        if (value == null && !GLOBAL_CONFIG.equals(fileName)) {
            value = this.getPropertyValue(GLOBAL_CONFIG, key);
        }
        if (value == null) {
            //LOG.info(key + Constants.PROPERTY_NOT_FOUND_IN + brand + " property file!");
            return null;
        } else {
            return value.toString();
        }
    }

    /**
     * This method will return the integer value for the given brand and key.
     *
     * @param brand
     *            Input brand to get property value.
     * @param key
     *            Input Key to get property value.
     * @return value the property value as integer.
     */
    public int getPropertyValueAsInteger(String fileName, String key) {
        Object value = this.getPropertyValue(fileName, key);
        if (value == null && !GLOBAL_CONFIG.equals(fileName)) {
            value = this.getPropertyValue(GLOBAL_CONFIG, key);
        }
        if (value == null) {
           // LOG.info(key + Constants.PROPERTY_NOT_FOUND_IN + brand + Constants.PROPERTY_FILE);
            return INVALID_VAL;
        } else {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException e) {
                throw new NumberFormatException(key + " is not an integer value in ");
            }

        }
    }
}
