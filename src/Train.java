public class Train extends MapObject{
    public int trainID;
    public String name;
    public StationLink currentLink;
    double linkProgress;

    Train(String name, StationLink currentLink){
        this.name = name;
        this.currentLink = currentLink;
        this.linkProgress = 0;
    }
    public Train(){
        this("defaultTrainName",new StationLink());
    }
    @Override
    public void draw(){
        //draws Train
    }
}
