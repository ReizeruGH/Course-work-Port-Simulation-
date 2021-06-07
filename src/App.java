import firstservice.TimeTable;
import secondservice.JsonWorker;
import secondservice.TimeTableWorker;

import java.util.Arrays;
import java.util.Scanner;

public class App{

    public static void main(String[] args) {
        Scanner inputLine = new Scanner(System.in);
        System.out.println("""
                1 - Показать расписание используя метод из первого сервиса(10 элементов)
                2 - Создать новую таблицу и сохранить ее в *.json
                3 - Вывести таблицу из *.json файла
                4 - Добавить новый корабль в расписание""");

        while (true) {
            if (checkInput(inputLine))
                switch (inputLine.nextInt()) {
                    case 1 -> printTimeTable();
                    case 2 -> saveToJson(inputLine);
                    case 3 -> readFromJson(inputLine);
                    case 4 -> addRecord(inputLine);
                    case 0 -> System.exit(0);
                }
        }
    }


    /**
     * Метод проверяет, ввел ли пользователь с консоли число или нет
     * @param inputLine - для ввода данных с консоли
     * @return возвращает false - если пользовательский ввод не является числом
     */
    public static boolean checkInput(Scanner inputLine){
        if(inputLine.hasNextInt())
            return true;
        else {
            inputLine.nextLine();
            return  false;
        }
    }

    /**
     * Выводит расписание, которое генерируется в firstservice.TimeTable.java
     */
    public static void printTimeTable(){
        for (int i = 0; i < 10; i++){
            System.out.println(new TimeTable().toString());
        }
    }

    /**
     * @param inputLine  - для ввода имени файла
     * @return - вовзращает имя полученного файла и добавляет *.json, так как мне лень было это писать каждый в консоли
     */
    public static String getFileName(Scanner inputLine){
        inputLine.nextLine();
        System.out.println("Введите имя файла для сохранения/чтения/добавления");
        return  inputLine.nextLine() + ".json";
    }

    /**
     * Метод, который получает от пользователя колличество кораблей и имя файла, куда будет сохранен json - строка
     * @param inputLine - для ввода countShips и filename
     */
    public static void saveToJson(Scanner inputLine){
        int countShips = 0;
        TimeTable[] timeTable;

        System.out.println("Введите число кораблей в расписании");
        if(checkInput(inputLine))
            countShips = inputLine.nextInt();
        if(countShips == 0){
            System.out.println("Колличество кораблей для расписания = " + countShips + ". Введите число >0");
            return;
        }

        timeTable =  TimeTable.generateTimeTable(countShips);

        new JsonWorker().saveTimetableToJson(timeTable, getFileName(inputLine));
    }

    /**
     * Вызывает метод из secondservice.JsonWorker, который считывает расписание из введенного файла
     * Возвращает полученную таблицу с расписанием и выводит в консоль
     * @param inputLine для ввода имени файла
     */
    public  static void  readFromJson(Scanner inputLine){
        TimeTable[] timeTable = new JsonWorker().readTimetableFromJson(getFileName(inputLine));
        if(timeTable != null)
            for (TimeTable table : timeTable) System.out.println(table.toString());
    }

    /**
     * Метод, который позволяет пользователю добавлять свои собсвтенные записи к расписанию
     * Вызывает основной метод из secondservice.TimeTableWorker
     * @param inputLine необходим для ввода данных с консоли
     */
    public  static void addRecord(Scanner inputLine){
        TimeTable[] timeTable = new JsonWorker().readTimetableFromJson(getFileName(inputLine));
        if(timeTable == null)
            return;
        TimeTable[] newTimeTable = Arrays.copyOf(timeTable,timeTable.length + 1);
        TimeTableWorker.addNewFields(newTimeTable, newTimeTable.length - 1, inputLine);
        new JsonWorker().saveTimetableToJson(newTimeTable,getFileName(inputLine));
    }
}