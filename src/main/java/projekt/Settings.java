package projekt;

public class Settings { //stores settings and some constant values
    private static int simulationSpeedMultiplier = 600; //can be set by user while simulation continues
    public final static Double vCoefficient = 10571429E-12; // coefficient used to scale trains speed
    private static int initialMoneyAmount; //should be set by user before simulation
    public static boolean useRouteManager = true; // false - trains randomly choose next station after arrival, true - trains get next station from its TrainRoute

    public static void setInitialMoneyAmount (int amount){
        if(amount < 0){
            StatisticsLogger.setMoney(amount * -1);
        }else StatisticsLogger.setMoney(amount);
    }
    public static void setSimulationSpeedMultiplier(int speedMultiplier){ // if you want to make 1s in real time equal to X min in simulation you have to set simulationSpeedMultiplier as X*60
        if(speedMultiplier < 0){                                          // for example if you set simulationSpeedMultiplier as 600 = 10*60, while 1s passes in real time, 10 min pass in simulation time (you sit for 1s, trains on map move as it would in 10mins)
            simulationSpeedMultiplier = speedMultiplier * -1;
        }else if(speedMultiplier == 0){
            simulationSpeedMultiplier = 1;
        }else simulationSpeedMultiplier = speedMultiplier;
    }
    public static int getSimulationSpeedMultiplier(){
        return simulationSpeedMultiplier;
    }

}
