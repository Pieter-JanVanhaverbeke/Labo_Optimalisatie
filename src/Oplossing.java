import java.sql.Time;

public class Oplossing {

    private Data data;
    private Oplossingsmatrix oplossingsmatrix;

    private Timematrix timematrix;
    private Distancematrix distancematrix;


    public Oplossing(Data data) {
        this.data = data;
        oplossingsmatrix = null; //Na oplossing toevoegen

        timematrix = data.getTimematrix();
        distancematrix = data.getDistancematrix();


    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public Oplossingsmatrix getOplossingsmatrix() {
        return oplossingsmatrix;
    }

    public void setOplossingsmatrix(Oplossingsmatrix oplossingsmatrix) {
        this.oplossingsmatrix = oplossingsmatrix;
    }

    //TODO oplossing die eerste resultaat brengt
    public void start(){

        int korstedistance = 999999999;
        Truck bestetruck = null;

        //VOOR ALLE COLLECTS
        for(int i=0; i<data.getCollectlijst().size(); i++){         //voor alle collects

            korstedistance=999999999; //resetten kortstedistance
            bestetruck = null; //resetten bestetruck

            Collect collect = data.getCollectlijst().get(i);        //huidig item dat gecollect moet worden
            int locatiemachineid = collect.getMachine().getLocation().getId();
            Location machinelocation = collect.getMachine().getLocation();          //locatie van collect


            for(int j=0; j<data.getTrucklijst().size();j++){                    //voor alle collects kijken naar iedere truck

                Truck truck = data.getTrucklijst().get(j);


                int trucklocationid = truck.getHuidigeLocatie();
                int calculateddistance = collect.getMachine().getLocation().getDistance(trucklocationid,distancematrix);        //distance die berekent is


                if(calculateddistance<korstedistance){
                    if(truck.heefttijd(locatiemachineid,timematrix)){              //kijken of truck tijd heeft //TODO meereken of tijd heeft voor servicetime
                    korstedistance = calculateddistance;
                    bestetruck = truck;                         //beste truck als huidige truck plaatsen
                }
            }


            bestetruck.verplaats(locatiemachineid,timematrix,distancematrix);                   //verplaatsen naar locatie
            if(korstedistance!=0){
                bestetruck.addStop(machinelocation);
            }

            bestetruck.pickUp(collect.getMachine());                                            //opnemen machine

            bestetruck.verplaats(bestetruck.getEndlocationid(),timematrix,distancematrix);      //terugkeren naar eindlocatie

            Location endLocation = data.getLocationlijst().get(bestetruck.getEndlocationid());
            bestetruck.addStop(endLocation);

            //TODO zetten in oplossingsmatrix
        }

        }

        for (int i=0; i<data.getDroplijst().size();i++) {
            Drop drop = data.getDroplijst().get(i);
            Location location = drop.getMachine().getLocation();
            int locatiemachineid = drop.getMachine().getLocation().getId();

            for (int j = 0; j < data.getTrucklijst().size(); j++) {                    //voor alle collects kijken naar iedere truck
                Truck truck = data.getTrucklijst().get(j);


                int huidigelocatieid =  truck.getHuidigeLocatie();
                int calculateddistance = location.getDistance(huidigelocatieid, distancematrix);

                if(calculateddistance < korstedistance){
                    if((truck.heefttijd(locatiemachineid,timematrix))){ //TODO meereken of tijd heeft voor servicetime
                        korstedistance=calculateddistance;
                        bestetruck = truck;
                    }
                }
            }


            bestetruck.pickUp(drop.getMachine());                       //opnemen machine
            bestetruck.verplaats(location.getId(),timematrix,distancematrix);           //verplaatsen
            bestetruck.dropOf(drop.getMachine());                       //afzetten
            bestetruck.verplaats(bestetruck.getEndlocationid(),timematrix,distancematrix);          //terugkeren

        }
    }

}
