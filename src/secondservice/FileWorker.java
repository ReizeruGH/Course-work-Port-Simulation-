package secondservice;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Данный класс реализует метода для чтения и сохранения данных в файл
 */
public class FileWorker {

    /**
     * Метод сохраняет данные в файл
     * @param dataString - входящая строка данных, которую мы хотим сохранить в файл
     * @param filename - имя файла для сохранения строки данных
     */
    public static void saveFile(String dataString, String filename){
        FileWriter file = null;
        try {
            file = new FileWriter(filename,true);
            file.write(dataString);
            file.flush();
            file.close();
            System.out.println("Файл успешно сохранен");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод который считывает информацию из файла. Используется лишь для того, чтобы получить данные из *.json файла
     * @param filename имя файла, из которого будем считывать данные
     * @return возвращает строку, которую получили из файла, либо null если не удалось открыть файл
     */
    public static String readFile(String filename){
        String dataString = null;

        try {
           dataString = Files.readString(Paths.get(filename));
            System.out.println("Данные успешно считаны из файла");
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла");
            return null;
        }
        return dataString;
    }
}
