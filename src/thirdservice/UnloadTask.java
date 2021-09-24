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
    int startDate, startHour, startMinutes, unloadSleep, randomTimeDelay = 0;  //Время когда кран начал работать с судном
    AtomicInteger countUnload, unSleep; //Количество груза, которое необходимо разгрузить
    AtomicBoolean printStart = new AtomicBoolean(false), saveResult = new AtomicBoolean(false); //Необходимы, чтобы результат работы сохранял лишь один поток


    public UnloadTask(TimeTable timeTable, PortTime currentTime) {
        Random random = new Random();
        this.timeTable = timeTable;
        this.currentTime = currentTime;
        countUnload = new AtomicInteger(this.timeTable.getCountContainers() <= 0 ? this.timeTable.getWeightCargo() : this.timeTable.getCountContainers());
        if(random.nextBoolean()) {  //Генерация задержки разгрузки судна
            randomTimeDelay = random.nextInt(1440);
            this.timeTable.setUnloadTime(this.timeTable.getUnloadTime() + randomTimeDelay); //Сразу же прибавляем к основному времени для разгрузки
        }

        unloadSleep = this.timeTable.getUnloadTime() / countUnload.get();

        unSleep = new AtomicInteger(this.timeTable.getUnloadTime());
    }

    @Override
    public void run() {
        if(!printStart.getAndSet(true)) {
            System.out.printf("%s начал разгрузку в  %d:%d:%d", Thread.currentThread().getName(), currentTime.getDate(), currentTime.getHour(), currentTime.getMinutes());
            startDate = currentTime.getDate();
            startHour = currentTime.getHour();
            startMinutes = currentTime.getMinutes();
        }

        while (unSleep.get() > 0) {
            Utils.sleep(1);
            unSleep.getAndDecrement();
        }

//        while (countUnload.get() > 0){
//            Utils.sleep(unloadSleep);
//            countUnload.getAndDecrement();
//        }

        //Разгрузка закончилась, увеличиваем количество свободных потоков
        switch (Thread.currentThread().getName()) {
            case "Loose" -> {
                Port.looseThreadCount.getAndIncrement();
                Port.looseQueue.getAndDecrement();
            }
            case "Liquid" -> {
                Port.liquidThreadCount.getAndIncrement();
                Port.liquidQueue.getAndDecrement();
            }
            case "Container" -> {
                Port.containerThreadCount.getAndIncrement();
                Port.containerQueue.getAndDecrement();
            }
        }

        if(!saveResult.getAndSet(true)){
            //FileWorker.saveFile(getThreadResult(),"result.txt");
            Port.resultSimulation.getAndSet(Port.resultSimulation.get() + getThreadResult());
            System.out.printf(Thread.currentThread().getName() + " end\n");
        }
    }

    /**
     * Метод считает итоговые результаты после разгрузки, а так же промежуточные значения для итоговых результатов симуляции
     * @return возвращает строку с итоговыми результатами
     */
    private String getThreadResult(){
        String result = new String();
        Utils utils = new Utils();
        int sumDelay;
        int [] waitTime = new int[3];  //Время ожидания начала разгрузки, время начала разгрузки - время прибытия судна
        int[] unloadTime = new int[3]; //Время продолжения разгрузки, время конца разгрузки - время начала разгрузки
        int[] delayTime = new int[3];  //Время нахождения судна в порту, время конца разгрузки - время прибытия судна

        utils.convectorTime(waitTime,startDate,startHour,startMinutes,timeTable.getDate(),timeTable.getHour(),timeTable.getMinutes());
        utils.convectorTime(unloadTime,currentTime.getDate(),currentTime.getHour(),currentTime.getMinutes(),startDate,startHour,startMinutes);
        utils.convectorTime(delayTime,currentTime.getDate(),currentTime.getHour(),currentTime.getMinutes(), timeTable.getDate(), timeTable.getHour(),timeTable.getMinutes());

        //Подсчет промежуточных значений для итоговых результатов
        Port.waitTime.getAndAdd((waitTime[0] * 24) + (waitTime[1]) + (waitTime[2] / 60));
        if((sumDelay =(delayTime[0] * 24 * 60) + (delayTime[1] * 60) + (delayTime[2])) > timeTable.getUnloadTime())
            Port.delayTime.add((sumDelay - timeTable.getUnloadTime()));

        result += "Имя судна " + timeTable.getNameOfShip();
        result += " Время прибытия в порт " + timeTable.getDate() + ":" + timeTable.getHour() + ":" + timeTable.getMinutes();
        result += " Время ожидания разгрузки " + waitTime[0] + ":" + waitTime[1] + ":" + waitTime[2];
        result += " Время начала разгрузки " + startDate + ":" + startHour + ":" + startMinutes;
        result += " Время продолжительности разгрузки " + unloadTime[0] + ":" + unloadTime[1] + ":" + unloadTime[2] + "\n";

        return result;
    }
}
