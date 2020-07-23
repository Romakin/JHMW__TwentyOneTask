package Task1;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        String mainPath = "./Games",
        logFilePath = mainPath + "/temp/temp.txt";
        File gamesDir = new File(mainPath);
        if (gamesDir.exists()) {
            clearAll(gamesDir);
        }
        gamesDir.mkdir();
        File logF = createLogFile(logFilePath);
        log(logF, "Hi!");
        log(logF, "here is some logs:");

        List<String> install = Arrays.asList(
                mainPath + "/src/main/Main.java",
                mainPath + "/src/main/Utils.java",
                mainPath + "/src/test",
                mainPath + "/res/drawables",
                mainPath + "/res/vectors",
                mainPath + "/res/icons",
                mainPath + "/savegames"
        );
        for (String iPath : install) {
            try {
                if (iPath.matches(".+\\.[^\\\\\\/]+")) {
                    File instF = new File(iPath);
                    new File(instF.getParent()).mkdirs();
                    if (instF.createNewFile()) {
                        log(logF, iPath + " file successfully created");
                    } else {
                        log(logF, iPath + " creation failed");
                    }
                } else {
                    new File(iPath).mkdirs();
                    log(logF, iPath + " successfully created");
                }
            } catch (IOException e) {
                log(logF, iPath + " creation failed");
            }
        }
    }

    public static File createLogFile(String path) {
        File lf = new File(path);
        if (lf.exists() && lf.canRead() && lf.canWrite()) {
            try(FileOutputStream fos = new FileOutputStream(lf)) {
                fos.write(("").getBytes());
            } catch (IOException e) {
                System.out.println("createLogFile: " + e.getMessage());
            }
        } else {
            try {
                new File(lf.getParent()).mkdirs();
                if (lf.createNewFile()) {
                    System.out.println("Log file created");
                }
            } catch (IOException e) {
                System.out.println("createLogFile: " + e.getMessage());
            }
        }
        return lf;
    }

    public static Boolean log(File logF, String logText) {
        logText += "\n";
        try(FileOutputStream fos = new FileOutputStream(logF, true)) {
            fos.write(logText.getBytes());
            return true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Boolean clearAll(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                clearAll(c);
        }
        if (!f.delete()) {
            throw new FileNotFoundException("Failed to delete file: " + f);
        }
        return true;

    }
}
