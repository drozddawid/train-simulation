package projekt;

public class Settings { //stores settings and some constant values
    private static int simulationSpeedMultiplier = 600; //can be set by user while simulation continues
    public final static Double vCoefficient = 10571429E-12; // coefficient used to scale trains speed
    private static int initialMoneyAmount = 1000; //should be set by user before simulation
    public static boolean useRouteManager = true; // false - trains randomly choose next station after arrival, true - trains get next station from its TrainRoute
    private static int stopCondition = 48; //stopcondition - time (in hours) after simulation stops


    public static void setInitialMoneyAmount (int amount){
        if(amount < 0){
            initialMoneyAmount = amount * -1;
        }else initialMoneyAmount = amount;
    }
    public static int getStopCondition(){ return stopCondition;}
    public static void setStopCondition(int hours) {stopCondition = hours;}
    public static int getInitialMoneyAmount(){ return initialMoneyAmount;}
    public static void setSimulationSpeedMultiplier(int speedMultiplier){ // if you want to make 1s in real time equal to X min in simulation you have to set simulationSpeedMultiplier as X*60
        simulationSpeedMultiplier = speedMultiplier;
    }
    public static int getSimulationSpeedMultiplier(){
        return simulationSpeedMultiplier;
    }

}
