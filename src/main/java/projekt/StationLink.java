package projekt;

public class StationLink extends MapObject{
    public int linkID;
    public int lengthKM;
    public Station from;
    public Station to;

    StationLink(int linkID,int lengthKM, Station from, Station to){
        this.linkID = linkID;
        this.lengthKM = lengthKM;
        this.from = from;
        this.to = to;
    }
    public StationLink(){
        this(0,0,new Station(),new Station());
    }

    public String getLink(){
        return "\nLink ID:" + this.linkID +  "\nfrom: " + this.from.name
                + "\nTo: " + this.to.name + "\nlength: " + this.lengthKM +"\ncoordX: " + this.from.coordX
                + "\ncoordY: " + this.from.coordY +"\ncoordX2: " + this.to.coordX
                + "\ncoordY2: " + this.to.coordY + "\n\n";
    }
    @Override
    public void draw(){
        //draws projekt.StationLink
    }
}
