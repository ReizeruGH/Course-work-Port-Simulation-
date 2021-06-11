package Utils;

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

    public static void sleep(int milllis){
        try {
            Thread.sleep(0, milllis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
