package Utils;

import firstservice.TimeTable;
import thirdservice.PortTime;

import java.util.Scanner;

public class Utils {

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
     * @param inputLine  - для ввода имени файла
     * @return - вовзращает имя полученного файла и добавляет *.json, так как мне лень было это писать каждый в консоли
     */
    public static String getFileName(Scanner inputLine){
        inputLine.nextLine();
        System.out.println("Введите имя файла для сохранения/чтения/добавления");
        return  inputLine.nextLine() + ".json";
    }

    /**
     * Метод для сна тредов
     * @param millis количество милисекунд для сна
     */
    public static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    /**
     * Метод отнимает время и проверяет, соотсвествуют ли они общепринятому стандарту времени (1-31:0-23:0-59)
     * @param convertTime массив времени, для сохранения результатов после конвертации времени
     *                    [0] - Хранит дату, [1] - хранит часы, [2] - хранит минуты
     * @param firstDate - первая дата
     * @param firstHour - первый час
     * @param firstMinutes - первые минуты
     * @param secondDate - вторая дата
     * @param secondHour - второй час
     * @param secondMinutes - вторая минута
     */
    public void convectorTime(int[] convertTime, int firstDate, int firstHour, int firstMinutes, int secondDate, int secondHour, int secondMinutes) {
        convertTime[0] = firstDate - secondDate;
        convertTime[1] = firstHour - secondHour;
        convertTime[2] = firstMinutes - secondMinutes;

        if (convertTime[2] < 0) {
            convertTime[2] += 60;
            convertTime[1]--;
        }
        if (convertTime[1] < 0) {
            convertTime[1] += 24;
            convertTime[0]--;
        }
    }
}
