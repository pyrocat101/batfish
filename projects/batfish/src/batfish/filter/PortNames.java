package batfish.filter;

import java.util.HashMap;
import java.util.Map;

public class PortNames {
    private static final Map<String, Integer> nameToPort = new HashMap<String, Integer>();

    static {
        // list of conventional port name->number mapping below
        nameToPort.put("ftp-data", 20);
        nameToPort.put("ftp", 21);
        nameToPort.put("ssh", 22);
        nameToPort.put("http", 80);
        nameToPort.put("ntp", 123);
        nameToPort.put("https", 443);
    }

    public static int get(String name) {
        if (nameToPort.containsKey(name)) {
            return nameToPort.get(name);
        } else {
            throw new IllegalArgumentException("unknown port name " + name);
        }
    }
}
