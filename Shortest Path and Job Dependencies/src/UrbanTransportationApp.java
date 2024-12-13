import java.io.Serializable;
import java.util.*;

class UrbanTransportationApp implements Serializable {
    static final long serialVersionUID = 99L;
    HashMap<String , Double> distTo = new HashMap<>();
    HashMap<Station , HashSet<Weight>> hs = new HashMap<>();
    HashMap<Station , Station> parents = new HashMap<>();
    double dur1 =0;

    
    public HyperloopTrainNetwork readHyperloopTrainNetwork(String filename) {
        HyperloopTrainNetwork hyperloopTrainNetwork = new HyperloopTrainNetwork();
        hyperloopTrainNetwork.readInput(filename);
        return hyperloopTrainNetwork;
    }

    /**
     * Function calculate the fastest route from the user's desired starting point to 
     * the desired destination point, taking into consideration the hyperloop train
     * network. 
     * @return List of RouteDirection instances
     */
    public List<RouteDirection> getFastestRouteDirections(HyperloopTrainNetwork network) {
        List<TrainLine> lines = network.lines;
        Station startSt = network.startPoint;
        Station endSt = network.destinationPoint;
        distTo.put(startSt.description , 0.0);
        distTo.put(endSt.description , Double.MAX_VALUE);
        hs.put(startSt , new HashSet<>());
        hs.put(endSt , new HashSet<>());
        hs.get(startSt).add(new Weight(endSt , weightCal(startSt , endSt , false, network), false));
        for (int i = 0; i < lines.size(); i++) {
            for (int j = 0; j < lines.get(i).trainLineStations.size(); j++) {
                Station dest = lines.get(i).trainLineStations.get(j);
                distTo.put(dest.description , Double.MAX_VALUE);
                hs.get(startSt).add(new Weight(dest, weightCal(startSt , dest, false ,network) , false));
                if(!hs.containsKey(dest)){
                    hs.put(dest , new HashSet<>());
                }
                hs.get(dest).add(new Weight(endSt, weightCal(dest,endSt,false,network), false));
                if(j != lines.get(i).trainLineStations.size()-1){
                    hs.get(dest).add(new Weight(lines.get(i).trainLineStations.get(j+1) , weightCal(dest , lines.get(i).trainLineStations.get(j+1) , true, network) , true ));
                }
                if(j != 0){
                    hs.get(dest).add(new Weight(lines.get(i).trainLineStations.get(j-1) , weightCal(dest , lines.get(i).trainLineStations.get(j-1) , true, network) , true ));

                }
                for (int k = i+1; k < lines.size() ; k++) {
                    for (int l = 0; l < lines.get(k).trainLineStations.size(); l++) {
                        Station m = lines.get(k).trainLineStations.get(l);
                        if(!hs.containsKey(dest)){
                            hs.put(dest, new HashSet<>());
                        }
                        if(!hs.containsKey(m)){
                            hs.put(m , new HashSet<>());
                        }
                        hs.get(dest).add(new Weight(m , weightCal(dest , m , false , network) , false));
                        hs.get(m).add(new Weight(dest , weightCal(m , dest , false , network) , false));
                    }
                }
            }
        }
        shortestPath(network);
        Station curr = network.destinationPoint;
        List<RouteDirection> rt = new ArrayList<>();

        while (!curr.equals(network.startPoint)){
            int k = 0;
            for (int i = parents.get(curr).description.length() - 1; parents.get(curr).description.charAt(i) != ' ' ; i--) {
                k = i;
            }
            int a =0;
            for (int i = curr.description.length() - 1; curr.description.charAt(i) != ' ' ; i--) {
                a = i;
            }
            if(parents.get(curr).description.substring(0, k-1).equals(curr.description.substring(0,a-1))){
                rt.add(new RouteDirection(parents.get(curr).description , curr.description , weightCal(parents.get(curr) , curr ,true ,network),true));
            }else{
                rt.add(new RouteDirection(parents.get(curr).description , curr.description , weightCal(parents.get(curr) , curr ,false ,network),false));
            }
            dur1+= rt.get(rt.size()-1).duration;
            curr = parents.get(curr);
        }
        Collections.reverse(rt);
        // TODO: Your code goes here

        return rt;
    }
    public double weightCal(Station a , Station b , boolean c , HyperloopTrainNetwork network){
        double x = a.coordinates.x - b.coordinates.x;
        double y = a.coordinates.y - b.coordinates.y;
        double dist = Math.sqrt((x*x) + (y*y));
        double dur;
        if(c){
            dur = dist / network.averageTrainSpeed;
        }else{
            dur = dist / network.averageWalkingSpeed;
        }
        return dur;
    }

    public void shortestPath(HyperloopTrainNetwork network ){
        PriorityQueue<Weight> pq = new PriorityQueue<>(Comparator.comparingDouble(o -> o.dist));
        pq.add(new Weight(network.startPoint , 0.0 , false));
        while (!pq.isEmpty()){
            Station st = pq.poll().st;
            for (Weight v : hs.get(st)) {
                if(distTo.get(v.st.description) > distTo.get(st.description) + v.dist){
                    distTo.put(v.st.description , distTo.get(st.description) + v.dist );
                    pq.add(new Weight(v.st , v.dist , v.t));
                    parents.put(v.st , st);
                }
            }
        }

    }

    /**
     * Function to print the route directions to STDOUT
     */
    public void printRouteDirections(List<RouteDirection> directions) {
        System.out.println("The fastest route takes "+ Math.round(dur1) +" minute(s).");
        System.out.println("Directions");
        System.out.println("----------");
        for (int i = 0; i < directions.size(); i++) {
            String d = String.format("%.2f", directions.get(i).duration);
            if(directions.get(i).trainRide){
                System.out.println((i+1) + ". Get on the train from " + "\"" + directions.get(i).startStationName + "\" to " + "\"" + directions.get(i).endStationName+ "\" for " + d + " minutes.");
            }
            else{
                System.out.println((i+1) + ". Walk from " + "\"" + directions.get(i).startStationName + "\" to " + "\"" + directions.get(i).endStationName+ "\" for " + d + " minutes.");
            }

        }


        // TODO: Your code goes here

    }
}