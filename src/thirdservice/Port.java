package thirdservice;


import Utils.Utils;
import com.google.common.util.concurrent.AtomicDouble;
import com.google.common.util.concurrent.ThreadFactoryBuilder;


import firstservice.TimeTable;
import secondservice.JsonWorker;
import secondservice.TimeTableWorker;
import java.util.Collections;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Класс эмулирующий работу порта
 */
public class Port {
    static AtomicInteger looseThreadCount, liquidThreadCount, containerThreadCount;
    static AtomicDouble waitTime;
    static CopyOnWriteArrayList<Integer> delayTime;
    static AtomicReference <String> resultSimulation;
    static AtomicInteger looseQueue = new AtomicInteger(), liquidQueue = new AtomicInteger(), containerQueue = new AtomicInteger();
    int looseQueueCount = 0, liquidQueueCount = 0, containerQueueCount = 0, looseQueueSum = 0, liquidQueueSum = 0, containerQueueSum = 0;

    /**
     * Метод считывает таблицу из файла, генерирует опоздание/раннее прибытие кораблей и сортирует их от самого раннего к позднему
     * @param filename имя файлаБ где хранится расписания в формате *.json
     */
    public void startSimulation(String filename, Scanner inputLine){
        TimeTable[] timeTable =  new JsonWorker().readTimetableFromJson(filename);
        if(timeTable != null) {
            TimeTableWorker.generateDateDelay(timeTable);  //Генерация задержки по дням от -7 до +7 дней.
            TimeTableWorker.sortTimeTable(timeTable);
            System.out.println("Задайте количество кранов");
            runSimulation(timeTable, getThreadsCount(inputLine, "Loose"), getThreadsCount(inputLine, "Liquid"), getThreadsCount(inputLine, "Container"));
        }
    }

    /**
     * Основной метод для симуляции работы порта
     * @param timeTable считанная таблица из файла
     * @param maxLooseThread максимальное колличество потоков для крана Loose
     * @param maxLiquidThread максимальное колличество потоков для крана Liquid
     * @param maxContainerThread максимальное колличество потоков для крана Container
     */
    private void runSimulation(TimeTable[] timeTable,int maxLooseThread, int maxLiquidThread, int maxContainerThread) {
        int index = 0, availableLooseThread, availableLiquidThread , availableContainerThread;
        delayTime = new CopyOnWriteArrayList<>();
        resultSimulation = new AtomicReference<>();
        waitTime = new AtomicDouble(0);

        PortTime currentTime = new PortTime(); //Для счета времени в порту
        currentTime.setName("Current Time");

        //Для именования потоков в пуле
        ThreadFactory looseFactory = new ThreadFactoryBuilder().setNameFormat("Loose").build();
        ThreadFactory liquidFactory = new ThreadFactoryBuilder().setNameFormat("Liquid").build();
        ThreadFactory containerFactory = new ThreadFactoryBuilder().setNameFormat("Container").build();

        //Доступное колличество потокв каждого крана в конкретный момент времени
        looseThreadCount = new AtomicInteger(maxLooseThread);
        liquidThreadCount = new AtomicInteger(maxLiquidThread);
        containerThreadCount = new AtomicInteger(maxContainerThread);

        //Пулы потокв
        ExecutorService looseExecutor = Executors.newFixedThreadPool(looseThreadCount.get(), looseFactory);
        ExecutorService liquidExecutor = Executors.newFixedThreadPool(liquidThreadCount.get(), liquidFactory);
        ExecutorService containerExecutor = Executors.newFixedThreadPool(containerThreadCount.get(), containerFactory);

        //Старт отсчета времени
        currentTime.start();
        currentTime.printTime();

        resultSimulation.getAndSet("Отчет\n");

        System.out.println(timeTable[index].toString());
        while (index != timeTable.length) {
            if (currentTime.getDate() == timeTable[index].getDate() && timeTable[index].getHour() == currentTime.getHour()
                    && currentTime.getMinutes() == timeTable[index].getMinutes()) {
                if(index + 1 != timeTable.length)
                    System.out.println(timeTable[index + 1].toString());
                switch (timeTable[index].getTypeOfCargo()) {
                    case "Loose" -> {
                        looseQueue.getAndIncrement(); //Увеличиваем очередь
                        if(looseQueue.get() > 0){  //Если очередь больше 0
                            looseQueueSum += looseQueue.get();
                            looseQueueCount++;
                        }
                        availableLooseThread = looseThreadCount.get() > 1 ? 2 : 1;   //Узнаем, сколько потоков(кранов) нам доступно
                        UnloadTask looseTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < availableLooseThread; i++) {
                            looseExecutor.execute(looseTask);
                            looseThreadCount.getAndDecrement();
                        }
                    }
                    case "Liquid" -> {
                        liquidQueue.getAndIncrement(); //Увеличиваем очередь
                        if(liquidQueue.get() > 0){  //Если очередь больше 0
                            liquidQueueSum += liquidQueue.get();
                            liquidQueueCount++;
                        }
                        availableLiquidThread = liquidThreadCount.get() > 1 ? 2 : 1;
                        UnloadTask liquidTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < availableLiquidThread; i++) {
                            liquidExecutor.execute(liquidTask);
                            liquidThreadCount.getAndDecrement();
                        }
                    }
                    case "Container" -> {
                        containerQueue.getAndIncrement(); //Увеличиваем очередь
                        if(containerQueue.get() > 0){  //Если очередь больше 0
                            containerQueueSum += containerQueue.get();
                            containerQueueCount++;
                        }
                        availableContainerThread = containerThreadCount.get() > 1 ? 2 : 1;
                        UnloadTask containerTask = new UnloadTask(timeTable[index], currentTime);
                        for (int i = 0; i < availableContainerThread; i++) {
                            containerExecutor.execute(containerTask);
                            containerThreadCount.getAndDecrement();
                        }
                    }
                }
                index++;
                System.out.println(looseThreadCount.get() + " " + liquidThreadCount.get() + " " + containerThreadCount.get());
            }
        }
        while (looseThreadCount.get() != maxLooseThread || liquidThreadCount.get() != maxLiquidThread || containerThreadCount.get() != maxContainerThread)
            Utils.sleep(50);
        looseExecutor.shutdown();
        liquidExecutor.shutdown();
        containerExecutor.shutdown();
        currentTime.stop();
        printFinalResults(timeTable.length, maxLooseThread, maxLiquidThread, maxContainerThread);
    }

    /**
     * Выводит итоговые результаты работы симуляции порта
     * @param shipsCount количесвто разгруженных кораблей
     */
    private void printFinalResults(int shipsCount, int maxLooseThread, int maxLiquidThread, int maxContainerThread) {
        double sumDelayTime = (double) delayTime.stream().mapToInt(a -> a).sum();
        double fine, liquidQueueMid = liquidQueueSum/liquidQueueCount, looseQueueMid = looseQueueSum/looseQueueCount, containerQueueMid = containerQueueSum/containerQueueCount;
        System.out.println("Итоговые результаты");
        System.out.println("Число разгруженных судов - " + shipsCount);
        System.out.println("Средняя длина очереди для крана Liquid - " +  liquidQueueMid);
        System.out.println("Средняя длина очереди для крана Loose - " +  looseQueueMid);
        System.out.println("Средняя длина очереди для крана Container - " +  containerQueueMid);
        System.out.println("Среднее время ожидания разгрузки(в часах) - " + waitTime.get() / shipsCount);

        //Если никакой задержки не было и все суда ушли в срок, добавляем элемент со значением 0, чтобы не было ошибок в таком случае
        if (delayTime.size() == 0)
            delayTime.add(0);

        System.out.println("Максимальная задержка разгрузки(в часах) - " + Collections.max(delayTime)/60);
        System.out.println("Средняя задержка разгрузки(в часах) - " + (sumDelayTime / 60) / shipsCount);
        System.out.println("Общая сумма штрафа - " + (sumDelayTime / 60) * 100);
        if((fine = ((sumDelayTime / 60) * 100))> 30000){
            int countAddThread = (int) (fine / 30000);
            while (countAddThread !=0) {
                if (liquidQueueMid > 1) {
                    liquidQueueMid -= 0.5;
                    countAddThread--;
                    maxLooseThread++;
                }
                if(looseQueueMid > 1 && countAddThread != 0){
                    looseQueueMid-= 0.5;
                    countAddThread--;
                    maxLiquidThread++;
                }
                if(containerQueueMid > 1 && countAddThread != 0){
                    containerQueueMid-= 0.5;
                    countAddThread--;
                    maxContainerThread++;
                }
            }
        }
        System.out.println("Необходимо кранов для Loose - " + maxLooseThread + " Liquid - " + maxLiquidThread + " Container - " + maxContainerThread);
        System.out.println("\n" + resultSimulation.get());
    }

    private int getThreadsCount(Scanner inputLine, String threadName){
        int count = 0;
        System.out.println("Введите колличество кранов для " + threadName);
        if(Utils.checkInput(inputLine))
            count = inputLine.nextInt();
        return count;
    }
}

