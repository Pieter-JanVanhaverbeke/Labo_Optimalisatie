package dataclasses;
import java.util.HashMap;

public class Stop {
    private int stoplocatieid;
    private HashMap<Machine, Integer> machines;

    public Stop(int stoplocatieid){
        this.stoplocatieid=stoplocatieid;
        machines = new HashMap<>();
    }

    public int getStoplocatieid() {
        return stoplocatieid;
    }

    public void setStoplocatieid(int stoplocatieid) {
        this.stoplocatieid = stoplocatieid;
    }

    public HashMap<Machine, Integer> getMachines() {
        return machines;
    }

    public void setMachines(HashMap<Machine, Integer> machines) {
        this.machines = machines;
    }

    public void addMachine(Machine machine, int collectDropId){
        machines.put(machine, collectDropId);
    }

    public void addMachinelijst(HashMap<Machine, Integer> machinelijst){
        for (Machine machine: machinelijst.keySet()){
            machines.put(machine, machinelijst.get(machine));
        }
    }

    @Override
    public String toString() {
        String string = "" + stoplocatieid;
        for(int i=0; i<machines.size();i++){
            string = string + ":" + machines.get(i);
        }
        return string;

    }
}

