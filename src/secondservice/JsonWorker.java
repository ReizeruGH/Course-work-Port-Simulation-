package secondservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import firstservice.TimeTable;


public class JsonWorker {
    Gson jsonObj;
    String jsonString;

    /**
     * Метод, который создает расписание использую метод из firstservice.TimeTable.java
     * Преобразует экземпляр TimeTable в json строку, в котором хранится расписание
     * И вызывает метод из secondservice.FileWorker для сохранения полученной строки
     * @param countShips - колличество кораблей, из которых будет состоять расписание
     * @param filename - имя файла для сохранения расписания в виду *.json файла
     */
    public void saveTimetableToJson(int countShips, String filename){
        TimeTable[] timeTable = new TimeTable[countShips];
        jsonObj = new GsonBuilder().setPrettyPrinting().create();

        timeTable =  TimeTable.generateTimeTable(countShips);
        jsonString =  jsonObj.toJson(timeTable);

        FileWorker.saveFile(jsonString, filename);
    }

    /**
     * Метод, который считывает сохраненную таблицу с расписанием из *.json  файла
     * @param filename имя файла из которого будем получать данные, которые хранятся в *.json файле
     * @return возвращает либо полученное расписание, либо null если не удалось открыть файл
     */
    public TimeTable[] readTimetableFromJson(String filename){
        TimeTable[] timetable = null;
        jsonObj = new Gson();

        jsonString =  FileWorker.readFile(filename);
        if(jsonString != null)
            timetable = jsonObj.fromJson(jsonString, TimeTable[].class);
        else return  null;

        return timetable;
    }
}
