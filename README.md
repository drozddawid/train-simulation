# Symulacja pociągów PKP.

Projekt zajmuje się symulacją pociągów na mapie, na wzór [podobnej mapy PKP Intercity](https://portalpasazera.pl/MapaOL)

Projekt znajduje się na Githubie: [karolba/projekt-programowanie-obiektowe](https://github.com/karolba/projekt-programowanie-obiektowe).

## Dokumentacja

- [Skład grupy projektowej](https://github.com/karolba/projekt-programowanie-obiektowe/blob/master/docs/kroki-milowe/1/krok1.md)

- [Analiza czasownikowo-rzeczownikowa](https://github.com/karolba/projekt-programowanie-obiektowe/blob/master/docs/analiza-czasownikowo-rzeczownikowa.md)

- [Diagram przypadków użycia](https://github.com/karolba/projekt-programowanie-obiektowe/blob/master/docs/diagram-przypadkow-użycia.svg)

- [Karty CRC](https://github.com/karolba/projekt-programowanie-obiektowe/blob/master/docs/crc.pdf)

- [Javadoc](https://s3.baraniecki.eu/javadoc/)

- [Diagram klas](https://github.com/karolba/projekt-programowanie-obiektowe/blob/master/docs/diagram-klas.svg)

- [Diagram maszyny stanów interfejsu](https://github.com/karolba/projekt-programowanie-obiektowe/blob/master/docs/diagram-maszyny-stanow.pdf)


## Instrukcja budowania


- potrzebna jest [Java w wersji przynajmniej 11](https://adoptopenjdk.net/).
- dla Windowsa:
    - aby zbudować oraz uruchomić projekt należy w katalogu projektu wykonać `gradlew.bat run`
    - aby zbudować oraz spakować projekt razem z wszystkimi zależnościami należy w katalogu projektu wykonać `gradlew.bat distZip`
- dla systemów unixowych:
    - aby zbudować oraz uruchomić projekt należy w katalogu projektu wykonać `./gradlew run`
    - aby zbudować oraz spakować projekt razem z wszystkimi zależnościami należy w katalogu projektu wykonać `./gradlew distZip`
