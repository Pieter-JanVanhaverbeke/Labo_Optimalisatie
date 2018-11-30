package dataclasses;

import java.util.ArrayList;

public class Machinematrix {
    private int aantalMachinesid;
    private int aantalLocatiesid;
    private int[][] matrix;
    public Machinematrix(){
    }
    public void setAantalMachinesid(int aantalMachinesid){
        this.aantalMachinesid=aantalMachinesid;
    }
    public void setAantalLocatiesid(int aantalLocatiesid){
        this.aantalLocatiesid=aantalLocatiesid;
    }
    public void genereerMatrix(){
        matrix=new int[aantalLocatiesid][aantalMachinesid];
        for(int i=0;i<aantalLocatiesid;i++){
            for(int j=0;j<aantalMachinesid;j++){
                matrix[i][j]=0;
            }
        }
    }
    public void setDrop(ArrayList<Drop> drops){
        for (Drop d: drops) {
            matrix[d.getLocation().getId()][d.getMachineTypeId()]--;
        }

    }
    public void setCollect(ArrayList<Collect> collects){
        for (Collect c: collects) {
            matrix[c.getMachine().getLocation().getId()][c.getMachine().getMachineTypeId()]++;
        }

    }
    public void dropGebeurt(int locatieId, int machineTypeId){
        matrix[locatieId][machineTypeId]++;
    }
    public void collectGebeurt(int locatieId, int machineTypeId){
        matrix[locatieId][machineTypeId]--;
    }
}
