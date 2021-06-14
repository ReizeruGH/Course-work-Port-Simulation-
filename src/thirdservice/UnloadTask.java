package thirdservice;


import Utils.Utils;
import firstservice.TimeTable;
import secondservice.FileWorker;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Реализует "разгрузку" кораблей
 */
@SuppressWarnings("ALL")
public class UnloadTask implements  Runnable {
    TimeTable timeTable;
    PortTime currentTime;
    int startDate, startHour, startMinutes, unloadSleep;
    AtomicInteger countUnload;
    AtomicBoolean printStart = new AtomicBoolean(false), saveResult = new AtomicBoolean(false);


    public UnloadTask(TimeTable timeTable, PortTime currentTime) {
        Random random = new Random();
        this.timeTable = timeTable;
        this.currentTime = currentTime;
        countUnload = new AtomicInteger(this.timeTable.getCountContainers() <= 0 ? this.timeTable.getWeightCargo() : this.timeTable.getCountContainers());
        if(random.nextBoolean())
            timeTable.setUnloadTime(timeTable.getUnloadTime() + random.nextInt(1440));
        unloadSleep = timeTable.getUnloadTime() / countUnload.get() + 1;
    }

    @Override
    public void run() {
        if(!printStart.getAndSet(true)) {
            System.out.printf("%s начал разгрузку в  %d:%d:%d", Thread.currentThread().getName(), currentTime.getDate(), currentTime.getHour(), currentTime.getMinutes());
            startDate = currentTime.getDate();
            startHour = currentTime.getHour();
            startMinutes = currentTime.getMinutes();
        }

        while (countUnload.get() > 0) {
            Utils.sleep(unloadSleep);
            countUnload.getAndDecrement();
        }
        if (Thread.currentThread().getName().equals("Loose"))
            Port.looseThreadCount.getAndIncrement();
        else if (Thread.currentThread().getName().equals("Liquid"))
            Port.liquidThreadCount.getAndIncrement();
        else
            Port.containerThreadCount.getAndIncrement();

        if(!saveResult.getAndSet(true)){
            FileWorker.saveFile(getThreadResult(),"result.txt");
            System.out.printf(Thread.currentThread().getName() + " end");
        }
    }

    private String getThreadResult(){
        String result = new String();
        Utils utils = new Utils();
        int sumDelay;
        int [] waitTime = new int[3];
        int[] unloadTime = new int[3];
        int[] delayTime = new int[3];

        utils.convectorTime(waitTime,startDate,startHour,startMinutes,timeTable.getDate(),timeTable.getHour(),timeTable.getMinutes());
        utils.convectorTime(unloadTime,currentTime.getDate(),currentTime.getHour(),currentTime.getMinutes(),startDate,startHour,startMinutes);
        utils.convectorTime(delayTime,currentTime.getDate(),currentTime.getHour(),currentTime.getMinutes(), timeTable.getDate(), timeTable.getHour(),timeTable.getMinutes());

        Port.waitTime.getAndAdd((waitTime[0] * 24) + (waitTime[1]) + (waitTime[2] / 60));

        if((sumDelay =(delayTime[0] * 24) + (delayTime[1]) + (delayTime[2] / 60)) > timeTable.getUnloadTime())
            Port.delayTime.add(sumDelay);

        result += "Имя судна " + timeTable.getNameOfShip();
        result += " Время прибытия в порт " + timeTable.getDate() + ":" + timeTable.getHour() + ":" + timeTable.getMinutes();
        result += " Время ожидания разгрузки " + waitTime[0] + ":" + waitTime[1] + ":" + waitTime[2];
        result += " Время начала разгрузки " + startDate + ":" + startHour + ":" + startMinutes;
        result += " Время продолжительности разгрузки " + unloadTime[0] + ":" + unloadTime[1] + ":" + unloadTime[2] + "\n";

        return result;
    }
}
