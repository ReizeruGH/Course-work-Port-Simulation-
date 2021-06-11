package secondservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import firstservice.TimeTable;


/**
 * Данный класс реализует методы для работы с *.json файлом
 */
public class JsonWorker {
    String jsonString;

    /**
     * Метод, который создает расписание использую метод из firstservice.TimeTable.java
     * Преобразует экземпляр TimeTable в json строку, в котором хранится расписание
     * И вызывает метод из secondservice.FileWorker для сохранения полученной строки
     * @param timeTable - расписание, которое мы должны сохранить
     * @param filename - имя файла для сохранения расписания в виду *.json файла
     */
    public void saveTimetableToJson(TimeTable[] timeTable, String filename){
        jsonString =  new GsonBuilder().setPrettyPrinting().create().toJson(timeTable);
        FileWorker.saveFile(jsonString, filename);
    }

    /**
     * Метод, который считывает сохраненную таблицу с расписанием из *.json  файла
     * @param filename имя файла из которого будем получать данные, которые хранятся в *.json файле
     * @return возвращает либо полученное расписание, либо null если не удалось открыть файл
     */
    public TimeTable[] readTimetableFromJson(String filename){
        jsonString =  FileWorker.readFile(filename);
        if(jsonString != null)
            return new Gson().fromJson(jsonString, TimeTable[].class);
        else return  null;
    }
}
