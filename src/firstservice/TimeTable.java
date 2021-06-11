package firstservice;

import org.jetbrains.annotations.NotNull;
import java.util.Random;


/**
 * Класс реализует методы и поля для создания расписание прибытия судов
 */
public class TimeTable implements  Comparable<TimeTable>{
    private Integer date, hour, minutes, unloadTime, unloadTimeDelay, dateDelay, weightCargo = 0, countContainers = 0;
    private  String nameOfShip, typeOfCargo;

    final static public int looseUnloadTime = 500, liquidUnloadTime = 400, containerUnloadTime = 350;

    final  static transient private  int MAX_COUNT_CONTAINER = 200, MAX_WEIGHT_CARGO = 100;
    final static transient private String[] NAMES_FOR_SHIPS = {"Дио", "Джотаро", "Канеки", "Баам", "Ичиго", "Мадара", "Лайт",
                                                    "Кира", "Синдзи", "Аска", "Аянакоджи", "Лелуш", "Холо", "Айка,",
                                                    "Мария", "Идзая", "Линали", "Макисима", "Йохан", "Субару", "Эрен",
                                                    "Анни", "Ято", "Окабэ", "Курису", "Сики", "Мадока", "Юки", "Кайт"};
    final static transient private String[] TYPE_OF_CARGO = {"Liquid", "Loose", "Container"};


    /**
     *  Конструктор для класса TimeTable
     *  При создании нового экземпляра все изначальные поля генерируются случайным образом
     *  Поля unloadTimeDelay и dateDelay будут генерироваться в thirdservice
     *  Для генерации имени корабля используется массив с именами
     */
    public TimeTable() {
        Random random = new Random();
        date = random.nextInt(31) + 1;
        hour = random.nextInt(24);
        minutes = random.nextInt(60);
        typeOfCargo = TYPE_OF_CARGO[random.nextInt(TYPE_OF_CARGO.length)];
        nameOfShip = NAMES_FOR_SHIPS[random.nextInt(NAMES_FOR_SHIPS.length)];
        switch (typeOfCargo) {
            case "Liquid" -> {
                weightCargo = random.nextInt(MAX_WEIGHT_CARGO + 1);
                unloadTime = liquidUnloadTime * weightCargo;
            }
            case "Loose" ->{
                weightCargo = random.nextInt(MAX_WEIGHT_CARGO + 1);
                unloadTime = looseUnloadTime * weightCargo;
            }
            case "Container"-> {
                countContainers = random.nextInt(MAX_COUNT_CONTAINER + 1);
                unloadTime = containerUnloadTime * countContainers;
            }
        }
    }

    public int getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getUnloadTime() {
        return unloadTime;
    }

    public int getDateDelay() {
        return dateDelay;
    }

    public int getUnloadTimeDelay() {
        return unloadTimeDelay;
    }

    public String getTypeOfCargo() {
        return typeOfCargo;
    }

    public int getCountContainers() {
        return countContainers;
    }

    public int getWeightCargo() {
        return weightCargo;
    }

    public String getNameOfShip() {
        return nameOfShip;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setWeightCargo(int weightCargo) {
        this.weightCargo = weightCargo;
    }

    public void setCountContainers(int countContainers) {
        this.countContainers = countContainers;
    }

    public void setNameOfShip(String nameOfShip) {
        this.nameOfShip = nameOfShip;
    }

    public void setTypeOfCargo(String typeOfCargo) {
        this.typeOfCargo = typeOfCargo;
    }

    public void setUnloadTime(int unloadTime) {
        this.unloadTime = unloadTime;
    }

    public void setDateDelay(Integer dateDelay) {
        this.dateDelay = dateDelay;
    }

    /**
     * Генерирует расписание с указанным количеством кораблей
     * @param countShips - количество кораблей, которые будут в расписании
     * @return - возвращает сгенерированное расписание
     */
    public static TimeTable[] generateTimeTable(int countShips){
        TimeTable[] timeTable = new TimeTable[countShips];
        for (int i = 0; i < countShips; i++)
            timeTable[i] = new TimeTable();
        return timeTable;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "date=" + date +
                ", hour=" + hour +
                ", minutes=" + minutes +
                ", unloadTime=" + unloadTime +
                ", unloadTimeDelay=" + unloadTimeDelay +
                ", dateDelay=" + dateDelay +
                ", weightCargo=" + weightCargo +
                ", countContainers=" + countContainers +
                ", nameOfShip='" + nameOfShip + '\'' +
                ", typeOfCargo='" + typeOfCargo + '\'' +
                '}';
    }

    /**
     * Сравнивает поля date,hour,minutes
     * @param o расписание, с которым будем сравнивать
     * @return возвращает результат сравнения по 3 полям TimeTable
     */
    @Override
    public int compareTo(@NotNull TimeTable o) {
        int compareResult = this.date.compareTo(o.date);
        if(compareResult == 0) {
            compareResult = this.hour.compareTo(o.hour);
            if(compareResult == 0)
                compareResult = this.minutes.compareTo(o.minutes);
        }
        return compareResult;
    }
}

