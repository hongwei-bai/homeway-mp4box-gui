package homeway.mp4boxgui.controller;

import homeway.mp4boxgui.model.CommandObserver;
import homeway.mp4boxgui.model.Mp4boxCommand;
import homeway.mp4boxgui.model.Mp4boxParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SplitController {
    private volatile ConcurrentHashMap<String, String> mDurationStringBuffer = new ConcurrentHashMap<>();
    private volatile ConcurrentHashMap<String, Long> mDurationLongBuffer = new ConcurrentHashMap<>();

    public String split(File file, long startTime, long endTime, long duration, CommandObserver observer) {
        if (null == file) {
            return "[ERROR]No file selected!";
        }
        String msg = Mp4boxCommand.splitChunk(file.getAbsolutePath(), startTime, endTime, observer);
        return msg;

        // new test
//        String cmd = "mp4box -split-chunk " + startTime + ":" + endTime + " " + "双旗动画效果.mp4";
//        new RealtimeProcessCaller().test("D:\\Profiles\\Hongwei\\Desktop\\", cmd);
//        return "new test end.";
    }

    public List<File> getOutputFiles(File file) {
        if (null == file) {
            return null;
        }
        String name = file.getName();
        String nameWithoutEx = FileNameUtils.getFileNameNoEx(name);
        String extension = FileNameUtils.getExtensionName(name);

        File outputDir = new File(System.getProperty("user.dir"));
        File[] outputFiles = outputDir.listFiles();
        List<File> fileList = new ArrayList<>();
        for (File outputFile : outputFiles) {
            String outputFileName = outputFile.getName();
            if (outputFileName.startsWith(nameWithoutEx + "_")
                    && FileNameUtils.getExtensionName(outputFileName).equalsIgnoreCase(extension)) {
                fileList.add(outputFile);
            }
        }
        return fileList;
    }

    public String getDurationString(String filePath) {
        String path = filePath.trim();
        if (mDurationStringBuffer.containsKey(path)) {
            return mDurationStringBuffer.get(path);
        }

        String info = Mp4boxCommand.info(filePath);
        String durationString = Mp4boxParser.parseDurationString(info);
        mDurationStringBuffer.put(path, durationString);
        return durationString;
    }

    public long getDurationLong(String filePath) {
        String path = filePath.trim();
        if (mDurationLongBuffer.containsKey(path)) {
            return mDurationLongBuffer.get(path);
        }

        String durationString = getDurationString(path);
        long duration = Mp4boxParser.parseDurationLong(durationString);
        mDurationLongBuffer.put(path, duration);
        return duration;
    }
}
