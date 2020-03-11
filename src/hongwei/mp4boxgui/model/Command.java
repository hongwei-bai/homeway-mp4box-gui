package hongwei.mp4boxgui.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Command {
    public static String cd(String path) {
        String info = "";
        info += exec("cd \"" + path + "\"");
        info += exec(path.substring(0, 2));
        return info;
    }


    public static String exec(String cmd) {
        return exec(cmd, null);
    }

    public static String exec(String cmd, CommandObserver observer) {
        String info = "";
        try {
            Process p = Runtime.getRuntime().exec("cmd /C " + cmd);
//            Process p = Runtime.getRuntime().exec(cmd);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                info += line + "\n";
                if (observer != null) {
                    observer.print(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return info;
    }

    public static void open(String dir) {
        try {
            String[] cmd = new String[5];
            cmd[0] = "cmd";
            cmd[1] = "/c";
            cmd[2] = "start";
            cmd[3] = " ";
            cmd[4] = dir;
            Runtime.getRuntime().exec(cmd);
        } catch (Exception ex) {
            Log.i("IoUtils.open throw exception!");
        }
    }
}
