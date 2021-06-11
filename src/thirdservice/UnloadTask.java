package thirdservice;


import Utils.Utils;
import firstservice.TimeTable;
import secondservice.FileWorker;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Реализует "разгрузку" кораблей
 */
public class UnloadTask implements  Runnable{
    TimeTable timeTable;
    PortTime currentTime;
    int startDate, startHour, startMinutes, unloadSleep;
    AtomicInteger countUnload;
    String[] threadNames = {"Loose - 0", "Liquid - 0", "Container - 0"};

    public UnloadTask(TimeTable timeTable, PortTime currentTime) {
        this.timeTable = timeTable;
        this.currentTime = currentTime;
        countUnload = new AtomicInteger(this.timeTable.getCountContainers() <= 0 ? this.timeTable.getWeightCargo() : this.timeTable.getCountContainers());
        unloadSleep = this.timeTable.getCountContainers() > 0 ? TimeTable.containerUnloadTime : timeTable.getTypeOfCargo().equals("Liquid") ? TimeTable.liquidUnloadTime : TimeTable.looseUnloadTime;
    }

    @Override
    public void run() {
        if(Arrays.asList(threadNames).contains(Thread.currentThread().getName())){
            startDate = currentTime.getDate();
            startHour = currentTime.getHour();
            startMinutes = currentTime.getMinutes();
            System.out.printf("Кран %s начал разгрузку в %d:%d:%d\n",timeTable.getTypeOfCargo(),startDate,startHour,startMinutes);
        }
        while (countUnload.get() > 0){
            Utils.sleep(unloadSleep);
            countUnload.getAndDecrement();
        }
        if(Arrays.asList(threadNames).contains(Thread.currentThread().getName())){
            System.out.printf("Кран %s закончил работу в %d:%d:%d\n", timeTable.getTypeOfCargo(), currentTime.getDate(), currentTime.getHour(), currentTime.getMinutes());
            String result = new String();
            result += "Корабль " + timeTable.getTypeOfCargo() + " начал работу в " + startDate + ":" + startHour + ":" + startMinutes;
            result += " закончил работу в " + currentTime.getDate() + ":" + currentTime.getHour() + ":" + currentTime.getMinutes() + "\n";
            synchronized (this){
                FileWorker.saveFile(result, "result.txt");
            }
            //Дописать сохранение
        }
    }
}
