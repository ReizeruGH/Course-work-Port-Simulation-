package thirdservice;


import Utils.Utils;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import firstservice.TimeTable;
import secondservice.JsonWorker;
import secondservice.TimeTableWorker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Класс эмулирующий работу порта
 */
public class Port {
    static AtomicInteger  looseThreadCount, liquidThreadCount, containerThreadCount;
    static AtomicDouble waitTime = new AtomicDouble(0);
    static CopyOnWriteArrayList<Integer> delayTime = new CopyOnWriteArrayList<>();

    public void startSimulation(String filename){
        TimeTable[] timeTable =  new JsonWorker().readTimetableFromJson(filename);
        if(timeTable != null) {
            TimeTableWorker.generateDateDelay(timeTable);
            TimeTableWorker.sortTimeTable(timeTable);
            runSimulation(timeTable,1,1,3);
        }
    }

    private void runSimulation(TimeTable[] timeTable,int maxLooseThread, int maxLiquidThread, int maxContainerThread) {
        int index = 0, availableLooseThread, availableLiquidThread , availableContainerThread;
        PortTime currentTime = new PortTime();
        currentTime.setName("Current Time");

        ThreadFactory looseFactory = new ThreadFactoryBuilder().setNameFormat("Loose").build();
        ThreadFactory liquidFactory = new ThreadFactoryBuilder().setNameFormat("Liquid").build();
        ThreadFactory containerFactory = new ThreadFactoryBuilder().setNameFormat("Container").build();

        looseThreadCount = new AtomicInteger(maxLooseThread);
        liquidThreadCount = new AtomicInteger(maxLiquidThread);
        containerThreadCount = new AtomicInteger(maxContainerThread);


        ExecutorService looseExecutor = Executors.newFixedThreadPool(looseThreadCount.get(), looseFactory);
        ExecutorService liquidExecutor = Executors.newFixedThreadPool(liquidThreadCount.get(), liquidFactory);
        ExecutorService containerExecutor = Executors.newFixedThreadPool(containerThreadCount.get(), containerFactory);

        currentTime.start();
        currentTime.printTime();

        System.out.println(timeTable[index].toString());
        while (index != timeTable.length) {
            if (currentTime.getDate() == timeTable[index].getDate() && timeTable[index].getHour() == currentTime.getHour()
                    && currentTime.getMinutes() == timeTable[index].getMinutes()) {
                if(index + 1 != timeTable.length)
                    System.out.println(timeTable[index + 1].toString());
                switch (timeTable[index].getTypeOfCargo()) {
                    case "Loose" -> {
                        availableLooseThread = looseThreadCount.get() > 1 ? 2 : 1;
                        UnloadTask looseTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < availableLooseThread; i++) {
                            looseExecutor.execute(looseTask);
                            looseThreadCount.getAndDecrement();
                        }
                    }
                    case "Liquid" -> {
                        availableLiquidThread = liquidThreadCount.get() > 1 ? 2 : 1;
                        UnloadTask liquidTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < availableLiquidThread; i++) {
                            liquidExecutor.execute(liquidTask);
                            liquidThreadCount.getAndDecrement();
                        }
                    }
                    case "Container" -> {
                        availableContainerThread = containerThreadCount.get() > 1 ? 2 : 1;
                        UnloadTask containerTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < availableContainerThread; i++) {
                            containerExecutor.execute(containerTask);
                            containerThreadCount.getAndDecrement();
                        }
                    }
                }
                index++;
            }
        }
        while (looseThreadCount.get() != maxLooseThread || liquidThreadCount.get() != maxLiquidThread || containerThreadCount.get() != maxContainerThread)
            Utils.sleep(50);
        looseExecutor.shutdown();
        liquidExecutor.shutdown();
        containerExecutor.shutdown();
        currentTime.stop();
        printFinalResults(timeTable.length);
    }

    private void printFinalResults(int shipsCount){
        System.out.println("Итоговые результаты");
        System.out.println("Число разгруженных судов - " + shipsCount);
        System.out.println("Среднее время ожидания разгрузки(в часах) - " + waitTime.get()/shipsCount);
        if(delayTime.size()!=0) {
            System.out.println("Максимальная задержка разгрузки(в часах) - " + Collections.max(delayTime));
            System.out.println("Средняя задержка разгрузки(в часах) - " + (double) delayTime.stream().mapToInt(a -> a).sum() / shipsCount);
            System.out.println("Общая сумма штрафа - " + (double) delayTime.stream().mapToInt(a -> a).sum() * 100);
        }
    }
}

