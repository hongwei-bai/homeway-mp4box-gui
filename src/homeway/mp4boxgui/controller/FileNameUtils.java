package homeway.mp4boxgui.controller;

import java.io.File;
import java.util.ArrayList;

public class FileNameUtils {
    public static String getExtensionName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < filename.length() - 1)) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < filename.length())) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getFolderName(String fullpath) {
        if ((fullpath != null) && (fullpath.length() > 0)) {
            int token = fullpath.lastIndexOf('\\');
            if ((token > -1) && (token < fullpath.length())) {
                return fullpath.substring(token + 1);
            }
        }
        return fullpath;
    }

    public static String getDiskVolumn(String fullpath) {
        if ((fullpath != null) && (fullpath.length() > 0)) {
            String netdisk = parseNetDisk(fullpath);
            if (null != netdisk) {
                return netdisk;
            }
            int token = fullpath.indexOf('\\');
            if ((token > -1) && (token < fullpath.length())) {
                return fullpath.substring(0, token);
            }
        }
        return fullpath;
    }

    public static boolean isNetDisk(String disk) {
        String NET_DISK_PREFIX = File.separator + File.separator;
        return disk.startsWith(NET_DISK_PREFIX);
    }

    public static String parseNetDisk(String fullpath) {
        if (isNetDisk(fullpath)) {
            int token1 = fullpath.indexOf(File.separator, 2);
            int token2 = fullpath.indexOf(File.separator, token1 + 1);
            if ((token2 > -1) && (token2 < fullpath.length())) {
                return fullpath.substring(0, token2).replace("\\", "\\\\");
            }
        }
        return null;
    }

    public static String getLastPath(String fullpath) {
        if ((fullpath != null) && (fullpath.length() > 0)) {
            int token = fullpath.lastIndexOf('\\');
            if ((token > -1) && (token < fullpath.length())) {
                String tmp = fullpath.substring(0, token);
                if (tmp.endsWith("\\")) {
                    int token2 = fullpath.lastIndexOf('\\');
                    if (token2 == tmp.length()) {
                        return tmp.substring(0, token2 - 1);
                    }
                }
                return tmp;
            }
        }
        return fullpath;
    }

    public static String stripCommonPrefix(String base, String token1, String token2) {
        int pos1 = base.indexOf(token1);
        int pos2 = base.indexOf(token2);
        if ((pos1 < 0) || (pos2 < 0)) {
            return base;
        }
        if (pos1 > pos2) {
            return base;
        }
        if (pos1 == 0) {
            return base.substring(pos2 + 1);
        }
        if (pos2 == base.length()) {
            return base.substring(0, pos1 - 1);
        }
        return base.substring(0, pos1 - 1) + " " + base.substring(pos2 + 1);
    }

    public static String normalizePath(String path) {
        String newpath = path.trim();
        int r = newpath.lastIndexOf("\\");
        if (r == newpath.length() - 1 && r > 0) {
            newpath = newpath.substring(0, newpath.length() - 1);
        }
        return newpath;
    }

    public static boolean isDiskExist(String fullpath) {
        if (fullpath != null && fullpath.length() > 1) {
            return new File(fullpath.substring(0, 2)).exists();
        }
        return false;
    }

    public static boolean isFileExist(String filepath) {
        if (filepath != null && filepath.length() > 1) {
            return new File(filepath).exists();
        }
        return false;
    }

    public static String getUpperLevelFolderName(String fullpath, String currentLevelFolderName) {
        return getFolderName(getLastPath(fullpath));
    }

    public static String getStringForDb(String org) {
        return org.replace("\\", "\\\\").replace("'", "\\'");
    }

    public static File getLongestNameFile(ArrayList<File> files) {
        File result = files.get(0);
        int maxLen = 0;
        for (File file : files) {
            int len = file.getName().length();
            if (len > maxLen) {
                maxLen = len;
                result = file;
            }
        }
        return result;
    }

    public static String convertPathForDb(String path) {
        if (path != null) {
            return path.replace("\\", "\\\\").replace("'", "\\\'");
        }
        return null;
    }

    public static char getLastCharacterOfFileName(File file) {
        String filename = getFileNameNoEx(file.getName());
        return filename.charAt(filename.length() - 1);
    }
}