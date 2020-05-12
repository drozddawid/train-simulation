import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class StationDatabase {
    public ArrayList<Station> stations;
    public ArrayList<StationLink> stationLinks;
    public ArrayList<Train> trains;

    StationDatabase(File stationData, File linkData, File trainData){
            try {
                FileReader scan = new FileReader(stationData);
                BufferedReader buffer = new BufferedReader(scan);
                int i = 0;
                int j = 0;
                stations = new ArrayList<>();
                String line;
                while((line = buffer.readLine()) != null){
                    switch (i++){
                        case 0:
                            stations.get(j).stationID = Integer.parseInt(line);
                        case 1:
                            stations.get(j).name = line;
                        case 2:
                            stations.get(j).profitability = Double.parseDouble(line);
                        case 3:
                            //tu zrobie coś żeby zapisywało ID stacji do listy StationLink


                                    0 
                    }

                }


            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }


    }
    public StationDatabase(){
       // creates default StationDatabase when no file given
    }
}
