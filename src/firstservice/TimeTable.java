package firstservice;

import java.util.Random;

public class TimeTable{
    private int date, hour, minutes, unloadTime, unloadTimeDelay, dateDelay, weightCargo = 0, countContainers = 0;
    private final String nameOfShip, typeOfCargo;

    final  static transient private  int MAX_COUNT_CONTAINER = 200, MAX_WEIGHT_CARGO = 100;
    final static transient private String[] NAMES_FOR_SHIPS = {"Дио", "Джотаро", "Канеки", "Баам", "Ичиго", "Мадара", "Лайт",
                                                    "Кира", "Синдзи", "Аска", "Аянакоджи", "Лелуш", "Холо", "Айка,",
                                                    "Мария", "Идзая", "Линали", "Макисима", "Йохан", "Субару", "Эрен",
                                                    "Анни", "Ято", "Окабэ", "Курису", "Сики", "Мадока", "Юки", "Кайт"};
    final static transient private String[] TYPE_OF_CARGO = {"Liquid", "Loose", "Container"};

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
                unloadTime = 400 * weightCargo;
            }
            case "Loose" ->{
                weightCargo = random.nextInt(MAX_WEIGHT_CARGO + 1);
                unloadTime = 500 * weightCargo;
            }
            case "Container"-> {
                countContainers = random.nextInt(MAX_COUNT_CONTAINER + 1);
                unloadTime = 350 * countContainers;
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
}

