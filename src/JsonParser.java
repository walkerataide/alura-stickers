import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonParser {

    private static final Pattern REGEX_ITEMS = Pattern.compile(".*\\[(.+)\\].*");
    private static final Pattern REGEX_ATTRIBUTES_JSON = Pattern.compile("\"(.+?)\":\"(.*?)\"");
    
    public List<Map<String, String>> parse(String json) {

        Matcher matcher = REGEX_ITEMS.matcher(json);
        if (!matcher.find()) {

            throw new IllegalArgumentException("Items not found!.");
        }

        String[] items = matcher.group(1).split("\\},\\{");

        List<Map<String, String>> data = new ArrayList<>();

        for (String item : items) {

            Map<String, String> itemAttribute = new HashMap<>();

            Matcher jsonAttributesMatcher = REGEX_ATTRIBUTES_JSON.matcher(item);
            while (jsonAttributesMatcher.find()) {
                String attribute = jsonAttributesMatcher.group(1);
                String value = jsonAttributesMatcher.group(2);
                itemAttribute.put(attribute, value);
            }

            data.add(itemAttribute);
        }

        return data;

    }
}