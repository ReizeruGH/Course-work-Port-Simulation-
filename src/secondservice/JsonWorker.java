package secondservice;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import firstservice.TimeTable;


public class JsonWorker {


    /**
     * Метод, который создает расписание использую метод из firstservice.TimeTable.java
     * Преобразует экземпляр TimeTable в json строку, в котором хранится расписание
     * И вызывает метод из secondservice.FileWorker для сохранения полученной строки
     * @param countShips - колличество кораблей, из которых будет состоять расписание
     * @param filename - имя файла для сохранения расписания в виду *.json файла
     */
    public void saveTimetableToJson(int countShips, String filename){
        TimeTable[] timeTable = new TimeTable[countShips];
        Gson jsonObj = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = new String();


        timeTable =  TimeTable.generateTimeTable(countShips);
        jsonString =  jsonObj.toJson(timeTable);

        FileWorker.saveFile(jsonString, filename);
    }
}
