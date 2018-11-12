public class Oplossing {

    private Data data;
    private Oplossingsmatrix oplossingsmatrix;

    private Timematrix timematrix;
    private Distancematrix distancematrix;


    public Oplossing(Data data) {
        this.data = data;
        oplossingsmatrix = new Oplossingsmatrix(); //Na oplossing toevoegen

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

            for(int j=0; j<data.getTrucklijst().size();j++) {                   //voor alle collects kijken naar iedere truck

                Truck truck = data.getTrucklijst().get(j);


                int trucklocationid = truck.getHuidigeLocatie();
                int calculateddistance = collect.getMachine().getLocation().getDistance(trucklocationid, distancematrix);        //distance die berekent is
                //    System.out.println("cal: " + calculateddistance);

                if (calculateddistance < korstedistance) {
                    if (truck.heefttijd(locatiemachineid, timematrix)) {              //kijken of truck tijd heeft //TODO meereken of tijd heeft voor servicetime
                        korstedistance = calculateddistance;
                        bestetruck = truck;                         //beste truck als huidige truck plaatsen
                    }
                }
            }


            //steken in opl
        //        System.out.println(bestetruck.toString());
            bestetruck.verplaats(locatiemachineid,timematrix,distancematrix);                   //verplaatsen naar locatie
            if(korstedistance!=0){
                bestetruck.addStop(machinelocation);
            }

            bestetruck.pickUp(collect.getMachine());                                            //opnemen machine

            bestetruck.verplaats(bestetruck.getEndlocationid(),timematrix,distancematrix);      //terugkeren naar eindlocatie

            //steken in opl
            Location endLocation = data.getLocationlijst().get(bestetruck.getEndlocationid());
            bestetruck.addStop(endLocation);                                                        //adden stop

            //TODO zetten in oplossingsmatrix


        }

        for (int i=0; i<data.getDroplijst().size();i++) {
            Drop drop = data.getDroplijst().get(i);


            int machinetypeid = drop.getMachineTypeId();
            Location location = drop.getLocation();

            int locatiemachineid = drop.getLocation().getId();

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


            Machine machine = new Machine();        //dummy machine meenemen en plaatsen op droppoint
            MachineType machineType = data.getMachinetypelijst().get(machinetypeid);
            machine.setMachineType(machineType);


            bestetruck.pickUp(machine);                       //opnemen dummy machine bij depots
            bestetruck.verplaats(location.getId(),timematrix,distancematrix);           //verplaatsen
            if(korstedistance!=0){
                bestetruck.addStop(location);
            }
            bestetruck.dropOf(machine);                       //afzetten
            bestetruck.verplaats(bestetruck.getEndlocationid(),timematrix,distancematrix);          //terugkeren

            //Steken in opl
            Location endLocation = data.getLocationlijst().get(bestetruck.getEndlocationid());
            bestetruck.addStop(endLocation);

        }

        //omzetten naar opl vorm
        zetommatrixopl();
    }




    public void zetommatrixopl(){
        int [] [] matrix;
        //Zoeken naar langste route
        int langste=0;
        for(int i=0; i<data.getTrucklijst().size();i++){
            Truck truck = data.getTrucklijst().get(i);

            if(truck.stoplijst.size()>langste){
                langste=truck.stoplijst.size();
            }
        }

        //matrix initilialiseren
        matrix = new int[data.getTrucklijst().size()][langste];
        for(int i=0;i<langste;i++){
            for(int j=0; j<langste;j++){
                matrix [i][j] = 0;
            }
        }


        for(int i=0; i<data.getTrucklijst().size();i++){
            Truck truck = data.getTrucklijst().get(i);
            for(int j=0; j<truck.stoplijst.size();j++){
                matrix[i][j] = truck.stoplijst.get(j).getId();              //locationid meegeven
            }
        }

        oplossingsmatrix.setOplossing(matrix);

        for(int i=0; i<langste;i++){
            for(int j=0; j<langste;j++){
                System.out.print(oplossingsmatrix.getOplossing()[i][j] + " ");
            }
            System.out.println();
        }


    }




}
