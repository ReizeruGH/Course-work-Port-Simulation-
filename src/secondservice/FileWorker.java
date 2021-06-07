package secondservice;

import java.io.FileWriter;
import java.io.IOException;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean readFile(){
        return false;
    }
}
