package DP_P5;

import DP_P5.dao.*;
import DP_P5.domein.Adres;
import DP_P5.domein.OVChipkaart;
import DP_P5.domein.Reiziger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Connection connection;


    public static void main(String[] args) throws SQLException {
    getConnection();

    ReizigerDAO rdao = new ReizigerDAOPsql(connection);
    AdresDAO adao = new AdresDAOPsql(connection);
    OVChipkaartDAO odao = new OVChipkaartDAOpsql(connection);

    testOVChipkaartDAO(rdao, odao);

    closeConnection();
    }

    private static void getConnection(){
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip",
                    "stephanbrandsen", "long80");


        } catch (Exception ex) {
            System.out.println("Could not connect to Database");
            ex.printStackTrace();
        }

    }

    private static void closeConnection(){
        try {
            connection.close();
        } catch (Exception ex){
            System.out.println("Could not close connection");
        }
    }

    private static void testOVChipkaartDAO(ReizigerDAO rdao, OVChipkaartDAO odao) throws SQLException {
        System.out.println("\n---------- Test OVChipkaartDAO -------------");
        System.out.println("Alle ov chipkaarten: ");
        List<OVChipkaart> kaarten = odao.findAll();
        for(OVChipkaart ov : kaarten){
            System.out.println(ov.toString());
        }

        System.out.println("\n\n");

        Reiziger reiziger = rdao.findById(1);
        System.out.println(reiziger);

        OVChipkaart ov = new OVChipkaart(10500, java.sql.Date.valueOf("2024-01-01"),  1, 100, reiziger);
        reiziger.addChipkaart(ov);

        System.out.println("\nSaving chipkaart....");
        rdao.update(reiziger);
        System.out.println("Saved chipkaart");

        reiziger = rdao.findById(1);
        System.out.println(reiziger.toString());

        System.out.println("Nog een kaart toevoegen aan reiziger\n");
        reiziger.addChipkaart(new OVChipkaart(10501, java.sql.Date.valueOf("2024-01-01"),  1, 150, reiziger));
        rdao.update(reiziger);
        reiziger = rdao.findById(1);
        System.out.println(reiziger);

        System.out.println("\n\nVerwijder alle kaarten bij deze reiziger\n");
        reiziger.clearOvchipkaarten();
        rdao.update(reiziger);
        System.out.println(reiziger);

    }

    private static void testAdresDAO(ReizigerDAO rdao, AdresDAO adao) throws SQLException {
        System.out.println("\n---------- Test adresDAO -------------");


        List<Reiziger> reizigersList = rdao.findAll();
        Reiziger reiziger = reizigersList.get(0);
        System.out.println(reiziger);

        System.out.println("\n[Test] Update woonplaats van reiziger naar Heiloo");
        Adres adres = reiziger.getAdres();
        adres.setWoonplaats("Heiloo");
        adao.update(adres);
        System.out.println(reiziger);

        //reset
        adres.setWoonplaats("Utrecht");
        adao.update(adres);

        //findbyreiziger
        System.out.println("\n[Test] Findbyreiziger");
        Adres ad = adao.findbyReiziger(reiziger);
        System.out.println(ad.toString()+ "\n\n");

        List<Reiziger> reizigers = rdao.findAll();
        List<Adres> adressen = adao.findAll();

        // Maak een nieuwe reiziger + adres aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        //adres = new Adres(6, "1852", "5", "Kruislaan", "Castricum", 77);
        //Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum), adres);
        System.out.print("[Test] Eerst " + adressen.size() + " adressen, na ReizigerDAO.save() ");
        //rdao.save(sietske);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " reizigers\n");

        //Delete nieuwe reiziger
        System.out.println("[Test] Delete nieuwe adres");
        //.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " adressen\n");





    }

    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        //Adres adres = new Adres(6, "1852", "5", "Kruislaan", "Castricum", 77);
        //Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum), adres);
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        //rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        //Delete nieuwe reiziger
        System.out.println("[Test] Delete nieuwe reiziger");
        //rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Vind reiziger van bepaalde ID
        Reiziger GvanRijn = rdao.findById(1);
        System.out.println("[Test] " + GvanRijn + "\nMoet G. van Rijn zijn\n");

        // Vind reiziger(s) van bepaalde datum
        String testdatum = "2002-12-03";
        reizigers = rdao.findByGbDatum(testdatum);
        System.out.println("[Test] ReizigerDAO.findByGbDatum() geeft de volgende reizigers bij datum " + testdatum + ":");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        //update bepaalde reiziger
        Reiziger BvanRijn = rdao.findById(2);
        System.out.println("[Test] B. van Rijn achternaam veranderen naar van Duuren");
        System.out.println(BvanRijn);
        BvanRijn.setAchternaam("Duuren");
        rdao.update(BvanRijn);

        System.out.println(rdao.findById(2));
        BvanRijn.setAchternaam("Rijn");
        rdao.update(BvanRijn); //reset

    }


}
