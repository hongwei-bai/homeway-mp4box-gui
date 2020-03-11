package hongwei.mp4boxgui.model;

public class Mp4boxCommand {
    public static String info(String path) {
        return Command.exec("mp4box -info " + "\"" + path + "\"");
    }

    public static String splitChunk(String file, long start, long end, CommandObserver observer) {
        return Command.exec("mp4box -split-chunk " + start + ":" + end + " \"" + file + "\"", observer);
    }
}
