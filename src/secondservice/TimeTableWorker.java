package secondservice;

import firstservice.TimeTable;
import java.util.*;


/**
 * Данный класс реализует вспомогательные метода для работы с таблицей
 */
public class TimeTableWorker {

    /**
     * Метод позволяет пользователю добавлять собственные записи в расписание кораблей
     * Так же он проверяет вводимые значение, чтобы они не превышали или же не были меньше максимальных и минимальных значений
     * @param timeTable исходное расписание, куда пользователь будет добовлять записи
     * @param index элемент таблицы расписания, куда пользователь будет добовлять запись
     * @param inputLine для ввода данных
     */
    public static void addNewFields(TimeTable[] timeTable, int index, Scanner inputLine){
        timeTable[index] = new TimeTable();

        checkInput(timeTable,inputLine,"Date", 0,31,index);
        checkInput(timeTable,inputLine,"Hour", 0,23,index);
        checkInput(timeTable,inputLine,"Minutes", -1,59,index);

        inputLine.nextLine();
        System.out.println("Имя корабля -  ");
        if(inputLine.hasNextLine())
            timeTable[index].setNameOfShip(inputLine.nextLine());

        System.out.println("Type of cargo\n1 - Loose\n2 - Liquid\n3 - Container");
        while (true) {
            if(inputLine.hasNextInt()){
                switch (inputLine.nextInt()){
                    case 1 -> {
                        timeTable[index].setTypeOfCargo("Loose");
                        checkInput(timeTable,inputLine,"Weight",0,101,index);
                        timeTable[index].setUnloadTime(timeTable[index].getWeightCargo() * TimeTable.looseUnloadTime);
                        timeTable[index].setCountContainers(0); //Для добавления новоого корабля с консоли создается новый эелмент расписание с рандомным числом контейнеров. Обнуляем, потому что груз не контейнеры
                    }
                    case 2 -> {
                        timeTable[index].setTypeOfCargo("Liquid");
                        checkInput(timeTable,inputLine,"Weight",0,101,index);
                        timeTable[index].setUnloadTime(timeTable[index].getWeightCargo() * TimeTable.liquidUnloadTime);
                        timeTable[index].setCountContainers(0); //Для добавления новоого корабля с консоли создается новый эелмент расписание с рандомным числом контейнеров. Обнуляем, потому что груз не контейнеры
                    }
                    case 3 -> {
                        timeTable[index].setTypeOfCargo("Container");
                        checkInput(timeTable,inputLine,"Count",0,101,index);
                        timeTable[index].setUnloadTime(timeTable[index].getCountContainers() * TimeTable.containerUnloadTime);
                    }
                }
                break;
            }
            else{
                System.out.println("Введите число");
                inputLine.nextLine();
            }
        }
    }

    /**
     * Метод, который проверяет вводимые значения
     * @param timeTable расписание, куда добовляем новую запись
     * @param inputLine для ввода данных
     * @param checkField хранит имя поля класса TimeTable
     * @param minValue минимальное значения для поля
     * @param maxValue максимальное значение для поля
     * @param index индекс таблицы, куда будем сохранять вводимые данные
     */
    private static void checkInput(TimeTable[] timeTable, Scanner inputLine, String checkField, int minValue, int maxValue, int index){
        int inputNum;
        System.out.println("Введите значение для " + checkField);
        while (true){
            if(inputLine.hasNextInt()){
                inputNum = inputLine.nextInt();
                if(inputNum > minValue && inputNum < maxValue){
                    switch (checkField){
                        case "Date" -> timeTable[index].setDate(inputNum);
                        case "Hour" -> timeTable[index].setHour(inputNum);
                        case "Minutes" -> timeTable[index].setMinutes(inputNum);
                        case "Weight" -> timeTable[index].setWeightCargo(inputNum);
                        case "Count" -> timeTable[index].setCountContainers(inputNum);
                    }
                    break;
                }
                else {
                    System.out.println("Ошибка," + checkField + " =< " + minValue + " or => " + maxValue);
                    inputLine.nextLine();
                }
            }
            else{
                System.out.println("Введите число");
                inputLine.nextLine();
            }
        }
    }

    /**
     * Метод, который сортирует расписание
     * @param timeTable расписание, которое нужно отсортировать
     * @return возвращает отсортированное расписание
     */
    public static TimeTable[] sortTimeTable(TimeTable[] timeTable){
       Arrays.sort(timeTable);
       return timeTable;
    }

    /**
     * Генерирует ззапаздания/преждевременное прибытие кораблей
     * @param timeTable входное расписание
     * @return возвращает расписание с сгенерированной задержкой по дате
     */
    public static TimeTable[] generateDateDelay(TimeTable[] timeTable) {
        Random random = new Random();
        for (int i = 0; i < timeTable.length; i++) {
            if (random.nextBoolean())
                timeTable[i].setDateDelay(random.nextInt((14 + 1) - 1));
            timeTable[i].setDate(timeTable[i].getDateDelay() + timeTable[i].getDate());
            if(timeTable[i].getDate() + timeTable[i].getDateDelay() <= 0){
                timeTable[i].setDate(1);
            } else if(timeTable[i].getDate() + timeTable[i].getDateDelay() > 31){
                timeTable[i].setDate(31);
            }
        }
        return timeTable;
    }

}
