public class StationLink extends MapObject{
    public Station from;
    public Station to;

    StationLink(Station from, Station to){
        this.from = from;
        this.to = to;
    }
    public StationLink(){
        this(new Station(), new Station());
    }

    public String getLink(){
        return "Train from: " + this.from.getStation() + "\nTrain to: " + this.to.getStation();
    }
    @Override
    public void draw(){
        //draws StationLink
    }
}
