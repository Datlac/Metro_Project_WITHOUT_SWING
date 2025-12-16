package metro.models.transport;
public class Route {
    private String routeId;
    private double distance;
    
    public Route(String id, double dist) { 
        this.routeId = id; 
        this.distance = dist; 
    }
    
    @Override 
    public String toString() { return "Route " + routeId; }
}