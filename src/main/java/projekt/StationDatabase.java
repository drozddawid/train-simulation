package projekt;

import java.io.*;
import java.util.ArrayList;


public class StationDatabase {
    public ArrayList<Station> stations;
    public ArrayList<StationLink> stationLinks;
    public ArrayList<Train> trains;

    StationDatabase(File stationData, File linkData, File trainData){ //tworzy bazę stacji, pociągów i połączeń na podstawie danych w podanym pliku
            try { //wczytywanie bazy stacji
                System.out.println("Reading stations database ...");
                FileReader scan = new FileReader(stationData);
                BufferedReader buffer = new BufferedReader(scan);

                this.stations = new ArrayList<Station>();
                this.stations.add(new Station());

                int i = 0; // wskazuje jaką informacje przechowuje linia
                int j = 0; // indeks listy stations

                String line;
                while((line = buffer.readLine()) != null){
                    switch (i){
                        case 0:
                            this.stations.get(j).stationID = Integer.parseInt(line);
                            break;
                        case 1:
                            this.stations.get(j).name = line;
                            break;
                        case 2:
                            this.stations.get(j).profitability = Double.parseDouble(line);
                            break;
                        case 3:
                            this.stations.get(j).coordX = Double.parseDouble(line);
                            break;
                        case 4:
                            this.stations.get(j).coordY = Double.parseDouble(line);
                            break;
                        case 5:
                            char[] connections = line.toCharArray();
                            String buff = "";
                            int k = 0;
                            while (connections[k] != '/'){ // "/" kończy zapis łączących stacji np 002-094-/ oznacza że stacja jest połączona ze stacjami o ID 002 i 094
                                for(int l = 0; l<3; l++) { // koniecznie trzeba dać na końcu całego zapisu -/, bo jak dasz / to nie zadziała
                                    buff = buff + connections[k + l];
                                }
                                this.stations.get(j).connectedWith.add(Integer.parseInt(buff));
                                k = k + 4;
                                buff = "";
                            }
                            break;
                    }

                    i++;
                    if(i == 6){ //to znak że następna odczytana linia będzie pierwszą z następnej stacji
                        this.stations.add(new Station());
                        i = 0;
                        j++;
                    }

                }
                scan.close();
                buffer.close();
            } catch (IOException e){
                e.printStackTrace();
            }

            try{
                System.out.println("Reading links datafile ...");
                FileReader scan = new FileReader(linkData);
                BufferedReader buffer = new BufferedReader(scan);

                this.stationLinks = new ArrayList<StationLink>();
                this.stationLinks.add(new StationLink());
                int i = 0; //licznik linii
                int j = 0; //licznik obiektów projekt.StationLink w liście stationLinks
                String line;

                while ((line = buffer.readLine()) != null){
                    switch (i){
                        case 0:
                            this.stationLinks.get(j).linkID = Integer.parseInt(line);
                            break;
                        case 1:
                            this.stationLinks.get(j).lengthKM = Integer.parseInt(line);
                            break;
                        case 2:
                            this.stationLinks.get(j).from = stations.get(Integer.parseInt(line)-1);
                            break;
                        case 3:
                            this.stationLinks.get(j).to = stations.get(Integer.parseInt(line)-1);
                            break;
                    }
                    i++;
                    if(i==4){
                        i=0;
                        j++;
                        this.stationLinks.add(new StationLink());
                    }

                }
                buffer.close();
                scan.close();


            } catch (IOException e){
                e.printStackTrace();
            }

            try {
                System.out.println("Reading trains datafile ...");
                FileReader scan = new FileReader(trainData);
                BufferedReader buffer = new BufferedReader(scan);
                int i = 0; //licznik czytanych linii
                int j = 0; //licznik obiektów projekt.Train w liście trains
                String line;
                trains = new ArrayList<Train>();
                trains.add(new Train());

                while((line = buffer.readLine()) != null){
                    switch (i){
                        case 0:
                            this.trains.get(j).trainID = Integer.parseInt(line);
                            break;
                        case 1:
                            this.trains.get(j).name = line;
                            break;
                        case 2:
                            this.trains.get(j).costPerKM = Integer.parseInt(line);
                            break;
                        case 3:
                            this.trains.get(j).profitPerPassenger = Double.parseDouble(line);
                            break;
                        case 4:
                            this.trains.get(j).seats = Integer.parseInt(line);
                            break;
                        case 5:
                            this.trains.get(j).currentLink = stationLinks.get(Integer.parseInt(line)-1);
                            break;
                    }
                    i++;
                    if (i == 6) {
                        j++;
                        i = 0;
                        this.trains.add(new Train());
                    }

                }
                scan.close();
                buffer.close();

            } catch (IOException e){
                e.printStackTrace();
            }

    }}

