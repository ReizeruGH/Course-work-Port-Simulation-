import firstservice.TimeTable;
import secondservice.JsonWorker;

import java.util.Scanner;

public class App{
    public static void main(String[] args) {
        Scanner inputLine = new Scanner(System.in);
        System.out.println("1 - Показать расписание используя метод из первого сервиса(10 элементов)\n" +
                           "2 - Создать новую таблицу и сохранить ее в *.json");

        while (true) {
            if (checkInput(inputLine))
                switch (inputLine.nextInt()) {
                    case 1 -> printTimeTable();
                    case 2 -> saveToJson(inputLine);
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
     * Метод, который генерирует расписание используя метод из класса TimeTable, где количество кораблей вводит пользователь
     * Полученное расписание мы сохраняем в *.json файл, имя которого вводит пользователь
     * @param inputLine - для ввода countShips и filename
     */
    public static void saveToJson(Scanner inputLine){
        int countShips = 0;
        String filename;
        JsonWorker jsonWork = new JsonWorker();

        System.out.println("Введите число кораблей в расписании");
        if(checkInput(inputLine))
            countShips = inputLine.nextInt();
        if(countShips == 0){
            System.out.println("Колличество кораблей для расписания = " + countShips + ". Введите число >0");
            return;
        }
        TimeTable[] timeTable = new TimeTable[countShips];
        timeTable =  TimeTable.generateTimeTable(countShips);

        System.out.println("Введите имя файла для сохранения");
        filename = inputLine.nextLine() + ".json";

        jsonWork.saveTimetableToJson(timeTable, filename);
    }
}