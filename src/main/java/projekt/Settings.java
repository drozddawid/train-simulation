package projekt;

public class Settings { //stores settings and some constant values
    private static int simulationSpeedMultiplier = 600;
    public final static Double vCoefficient = 10571429E-12; // coefficient used to scale trains speed


    public static void setSimulationSpeedMultiplier(int speedMultiplier){ // if you want to make 1s in real time equal to X min in simulation you have to set simulationSpeedMultiplier as X*60
        simulationSpeedMultiplier = speedMultiplier;                      // for example if you set simulationSpeedMultiplier as 600 = 10*60, while 1s passes in real time, 10 min pass in simulation time (you sit for 1s, trains on map move as it would in 10mins)
    }
    public static int getSimulationSpeedMultiplier(){
        return simulationSpeedMultiplier;
    }

}
