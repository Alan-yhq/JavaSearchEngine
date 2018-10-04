package httpwebserver.util;

import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class Tools {
    public static String getMemory() {
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long max = osmxb.getTotalPhysicalMemorySize();
        long use = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize());
        return String.valueOf(((int) (use * 100.0 / max))) + "%";
    }
}
