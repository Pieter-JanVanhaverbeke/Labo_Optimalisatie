import java.util.ArrayList;

public class Stop {
    private int stoplocatieid;
    private ArrayList<Integer> machines;

    public Stop(int stoplocatieid){
        this.stoplocatieid=stoplocatieid;
        machines = new ArrayList<Integer>();
    }

    public int getStoplocatieid() {
        return stoplocatieid;
    }

    public void setStoplocatieid(int stoplocatieid) {
        this.stoplocatieid = stoplocatieid;
    }

    public ArrayList<Integer> getMachines() {
        return machines;
    }

    public void setMachines(ArrayList<Integer> machines) {
        this.machines = machines;
    }

    public void addMachine(Machine machine){
        machines.add(machine.getId());
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

