package hongwei.mp4boxgui.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mp4boxParser {
    // Computed Duration 02:00:01.160
    private static final String REGEX_DURATION_SENTENCE = "Computed Duration \\d{2}\\:\\d{2}\\:\\d{2}.\\d{3}";
    private static final String REGEX_DURATION = "\\d{2}\\:\\d{2}\\:\\d{2}.\\d{3}";

    public static String parseDurationString(String info) {
        if (null == info || info.isEmpty() || !info.contains("\n")) {
            return null;
        }
        String[] lines = info.split("\n");
        if (null == lines || lines.length < 1) {
            return null;
        }

        Pattern pattern = Pattern.compile(REGEX_DURATION_SENTENCE);
        for (String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                String sentence = matcher.group(0);
                Pattern pattern2 = Pattern.compile(REGEX_DURATION);
                Matcher matcher2 = pattern2.matcher(sentence);
                if (matcher2.find()) {
                    return matcher2.group(0);
                }
            }
        }
        return null;
    }

    public static long parseDurationLong(String durationString) {
        try {
            String[] s1 = durationString.split("\\.");
            String[] s2 = s1[0].split(":");
            long duration = Integer.valueOf(s2[0]) * 3600 + Integer.valueOf(s2[1]) * 60 + Integer.valueOf(s2[2]);
            return duration;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long parseDurationLongForInput(String time) {
        try {
            if (!time.contains(":")) {
                return Long.valueOf(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String[] s2 = time.split(":");
            if (2 == s2.length) {
                long duration = Integer.valueOf(s2[0]) * 60 + Integer.valueOf(s2[1]);
                return duration;
            }
            long duration = Integer.valueOf(s2[0]) * 3600 + Integer.valueOf(s2[1]) * 60 + Integer.valueOf(s2[2]);
            return duration;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
