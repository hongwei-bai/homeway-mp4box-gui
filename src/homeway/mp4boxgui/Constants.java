package homeway.mp4boxgui;

import java.io.File;

public class Constants {
    public static final String TITLE_APP = "Homeway mp4boxgui";

    public static final String EXE_VERSION_R = "1.0";
    public static final String PLATFORM = "Java";
    public static final String TITLE = TITLE_APP + " v" + EXE_VERSION_R + "(" + PLATFORM + ")";

    public static final String ICON = "res" + File.separator + "gpac.jpg";

    public static final String COMPLETION_NOTIFICATION_STRING = "[~]";

    public interface TEXT {
        public static String DRAG_HINT = "Drag file here...";
    }
}
