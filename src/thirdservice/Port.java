package thirdservice;

import Utils.Utils;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import firstservice.TimeTable;
import secondservice.JsonWorker;
import secondservice.TimeTableWorker;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.*;


/**
 * Класс эмулирующий работу порта
 */
public class Port {

    public void startSimulation(String filename){
        TimeTable[] timeTable =  new JsonWorker().readTimetableFromJson(filename);
        if(timeTable != null) {
            TimeTableWorker.sortTimeTable(timeTable);
            runSimulation(timeTable);
        }
    }

    private void runSimulation(TimeTable[] timeTable) {
        int index = 0;
        PortTime currentTime = new PortTime();
        currentTime.setName("Current Time");

        ThreadFactory looseFactory = new ThreadFactoryBuilder().setNameFormat("Loose - %d").build();
        ThreadFactory liquidFactory = new ThreadFactoryBuilder().setNameFormat("Liquid - %d").build();
        ThreadFactory containerFactory = new ThreadFactoryBuilder().setNameFormat("Container - %d").build();

        ExecutorService looseExecutor = Executors.newFixedThreadPool(2, looseFactory);
        ExecutorService liquidExecutor = Executors.newFixedThreadPool(2, liquidFactory);
        ExecutorService containerExecutor = Executors.newFixedThreadPool(2, containerFactory);

        currentTime.start();
        currentTime.printTime();

        System.out.println(timeTable[index].toString());
        while (index + 1 != timeTable.length) {
            if (currentTime.getDate() == timeTable[index].getDate() && timeTable[index].getHour() == currentTime.getHour()
                    && currentTime.getMinutes() == timeTable[index].getMinutes()) {
                System.out.println(timeTable[index + 1].toString());
                switch (timeTable[index].getTypeOfCargo()) {
                    case "Loose" -> {
                        UnloadTask looseTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < 2; i++)
                            looseExecutor.execute(looseTask);
                    }
                    case "Liquid" -> {
                        UnloadTask liquidTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < 2; i++)
                            liquidExecutor.execute(liquidTask);
                    }
                    case "Container" -> {
                        UnloadTask containerTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < 2; i++)
                            containerExecutor.execute(containerTask);
                    }
                }
                index++;

            }
        }
    }

    private void saveFinalResults(){

    }
}

