import firstservice.TimeTable;

import java.util.Scanner;

public class App{
    public static void main(String[] args) {
        Scanner inputLine = new Scanner(System.in);
        System.out.println("1 - Показать расписание используя метод из первого сервиса(10 элементов)\n" +
                           "2 - Что то еще");
        do {
            if (inputLine.hasNextInt()) {
                switch (inputLine.nextInt()) {
                    case 1 -> printTimeTable();
                    default -> System.out.println("Неверное число");
                }
            } else {
                System.out.println("Введите число");
                inputLine.nextLine();
            }
        }while (true);
    }

    public static void printTimeTable(){
        for (int i = 0; i < 10; i++){
            System.out.println(new TimeTable().toString());
        }
    }
}