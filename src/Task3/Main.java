package Task3;

import Task2.GameProgress;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Main {

    public static void main(String[] args) {
        String dirPath = "./Games/savegames/";
        File zipF = clearSaved(dirPath);
        if (zipF != null) {
            unZip(dirPath + zipF.getName(), dirPath);
            askToChoose(dirPath);
        } else {
            System.out.println("Нет сохраненных состояний. Пожалуйста, выполните второе задание. ");
        }
    }

    public static void askToChoose(String dirPath) {
        Scanner sc = new Scanner(System.in);
        File savedgames = new File(dirPath);
        List<String> saves = new ArrayList<>();
        for(File dat: savedgames.listFiles()) {
            if (dat.getName().matches(".*\\.(dat)")) {
                saves.add(dat.getName());
            }
        }
        while (true) {
            System.out.println("Выберите необходимое сохранение (0 для выхода)");
            int[] idx = { 1 };
            saves.stream().forEach(s -> System.out.println( (idx[0]++) + ". " + s));
            int in = sc.nextInt() - 1;
            if (in == -1) break;
            if ((in >= 0) && saves.get(in) != null) {
                GameProgress gp = openProgress(dirPath + saves.get(in));
                System.out.println(gp);
            }
        }
    }

    public static GameProgress openProgress(String datPath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(datPath))) {
            return (GameProgress) ois.readObject();
        } catch (Exception ex) {   System.out.println(ex.getMessage());}
        return null;
    }

    public static void unZip(String zipPath, String dirPath) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = dirPath + entry.getName();
                FileOutputStream fout = new FileOutputStream(name);
                int c;
                while ((c = zin.read()) != -1) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println("unZip: " + ex.getMessage());
        }
    }

    public static File clearSaved(String path) {
        File savedgames = new File(path);
        if (savedgames.exists()) {
            File zipF = null;
            for(File dat: savedgames.listFiles()) {
                if (dat.getName().matches(".*\\.(dat)")) {
                    dat.delete();
                } else if (dat.getName().matches(".*\\.(zip)")) {
                    zipF = dat;
                }
            }
            return zipF;
        } else {
            return null;
        }
    }
}
