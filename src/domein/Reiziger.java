package DP_P5.domein;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboortedatum;
    private Adres adres;
    private List<OVChipkaart> ovchipkaarten = new ArrayList<OVChipkaart>();

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboortedatum, Adres adres, List<OVChipkaart> ovchipkaarten) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
        this.adres = adres;
        this.ovchipkaarten = ovchipkaarten;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setGeboortedatum(Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    private static String RemoveNull(String str){
        if(str == null){
            return "";
        } else {
            return str + " ";
        }
    }

    @Override
    public String toString() {
        String outString = "Reiziger: " +
                "["+ id + "] " +
                voorletters + ". " +
                RemoveNull(tussenvoegsel) +
                RemoveNull(achternaam) +
                "(" + geboortedatum + "), ";

        if(!(adres == null)){
            outString += adres.toString();
        }
        if(ovchipkaarten.size() > 0){
            outString += "\nChipkaarten:";
            StringBuilder sb = new StringBuilder();
            for(OVChipkaart ov : ovchipkaarten){
                sb.append("\n").append(ov.toString());
            }
            outString += sb;
        } else{
            outString += "\nBezit geen chipkaarten";
        }


        return outString;
    }

    public Adres getAdres() {
        return adres;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public List<OVChipkaart> getOvchipkaarten() {
        return ovchipkaarten;
    }

    public void addChipkaart(OVChipkaart ov) {
        if(ovchipkaarten.contains(ov)){
            System.out.println("Can not add since it reiziger already has this ov.");
        } else {
            ovchipkaarten.add(ov);
        }
    }

    public void removeChipkaart(OVChipkaart ov) {
        if(ovchipkaarten.contains(ov)){
            System.out.println("Can not delete since it reiziger doesn't have this ov.");
        } else {
            ovchipkaarten.remove(ov);
        }
    }

    public void clearOvchipkaarten(){
        this.ovchipkaarten = new ArrayList<>();
    }

    public void setOvchipkaarten(List<OVChipkaart> ovchipkaarten) {
        this.ovchipkaarten = ovchipkaarten;
    }
}
