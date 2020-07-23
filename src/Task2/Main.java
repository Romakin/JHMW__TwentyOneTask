package Task2;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        String dirPath = "./Games/savegames/";
        if(clearSaved(dirPath, false)) {
            List<GameProgress> gpList = Arrays.asList(
                    new GameProgress(100, 0, 1, 5),
                    new GameProgress(100, 10, 5, 500),
                    new GameProgress(10, 15, 20, 3150)
            );
            List<String> savedPathes = new ArrayList<>();
            for (GameProgress gp : gpList) {
                savedPathes.add(saveGame(dirPath, gp));
            }
            zipFiles(dirPath, savedPathes);
        } else {
            System.out.println("Пожалуйста, запустите сначала 1ое задание.");
        }
    }

    public static String saveGame(String path, GameProgress gp) {
        int saveFileNumber = (new File(path)).listFiles().length + 1;
        path = path + "save" + saveFileNumber + ".dat";
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
            return path;
        } catch (Exception ex) {
            System.out.println("saveGame: " + ex.getMessage());
            return null;
        }
    }

    public static void zipFiles(String path, List<String> filesPathes) {
        int saveFileZipNumber = (new File(path)).listFiles().length + 1;
        String saveFileZip = path + "saveZipped" + saveFileZipNumber + ".zip";
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(saveFileZip))) {
            for (String fp : filesPathes) {
                try (FileInputStream fis = new FileInputStream(fp)) {
                    ZipEntry entry = new ZipEntry(fp.replaceAll("[^\\\\\\/]*(\\\\|\\/)",""));
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer);
                    zout.write(buffer);
                    zout.closeEntry();
                } catch (IOException ex) {
                    System.out.println("zipFiles:" + ex.getMessage());
                }
            }
            clearSaved(path, true);
        } catch (Exception ex) {
            System.out.println("zipFiles:" + ex.getMessage());
        }
    }

    public static boolean clearSaved(String path, boolean removeOnlyDat) {
        File savedgames = new File(path);
        if (savedgames.exists()) {
            for(File dat: savedgames.listFiles()) {
                if (dat.getName().matches(
                        removeOnlyDat ?
                                ".*\\.(dat)" :
                                ".*\\.(dat)|.*\\.(zip)"
                )) {
                    dat.delete();
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
