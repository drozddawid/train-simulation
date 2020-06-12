package projekt;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 * Gets and caches values from resources/objectPaths.xml
 */
public class ObjectPathResourceGetter {
    private HashMap<String, String> resourceCache = new HashMap<>();
    private static ObjectPathResourceGetter instance;

    private ObjectPathResourceGetter() {}

    /**
     * @return a singleton instance
     */
    public static ObjectPathResourceGetter getInstance() {
        if(instance == null)
            instance = new ObjectPathResourceGetter();

        return instance;
    }

    /**
     * Get a value from resources/objectPaths.xml
     *
     * @param key the key associated with the needed value
     * @return the value
     */
    public String getValue(String key) {
        if(resourceCache.containsKey(key))
            return resourceCache.get(key);

        Properties properties = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/objectPaths.xml")) {
            properties.loadFromXML(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String value = (String) properties.get(key);
        resourceCache.put(key, value);
        return value;
    }
}
