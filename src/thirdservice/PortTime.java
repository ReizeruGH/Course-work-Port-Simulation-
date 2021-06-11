package thirdservice;

import Utils.Utils;

/**
 * Класс наследуется от класса Thread для отсчета времени в порту
 * Одни минута в порту = 30 миллисекундам
 */
public class PortTime extends  Thread{
    volatile private int date = 1, hour = 0, minutes = 0;

    @Override
    public void run(){
        System.out.println("Порт начал работу");
        while (!Thread.currentThread().isInterrupted()){
            Utils.sleep(5);
            minutes++;
            if(minutes == 60){
                minutes = 0;
                hour++;
                printTime();
            }
            if(hour == 24){
                hour = 0;
                date++;
            }
        }
    }

    public void printTime(){
        System.out.printf("Текущее время %d:%d:%d\n",date,hour,minutes);
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHour() {
        return hour;
    }

    public int getDate() {
        return date;
    }
}
