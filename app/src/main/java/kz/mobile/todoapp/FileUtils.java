package kz.mobile.todoapp;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileUtils {

    public static void saveToFile(File file) {
        FileOutputStream fos;
        ObjectOutputStream oos;
        try {
            fos = new FileOutputStream(file);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(MainActivity.toDos);
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readFromFile(File file) {
        FileInputStream fis;
        ObjectInputStream ois;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                ois =  new ObjectInputStream(fis);
                MainActivity.toDos = (ArrayList<ToDo>) ois.readObject();
                ois.close();
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
