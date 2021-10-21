package DP_P5.domein;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<Integer> chipkaartsnummers = new ArrayList<>();

    public Product(int product_nummer, String product, String beschrijving, double prijs) {
        this.product_nummer = product_nummer;
        this.naam = product;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public int getProduct_nummer() {
        return product_nummer;
    }

    public List<Integer> getOvchipkaarten() {
        return chipkaartsnummers;
    }

    public List<Integer> getChipkaartsnummers() {
        return chipkaartsnummers;
    }

    public boolean addChipkaartNummer(Integer chipkaartnummer) {
        this.chipkaartsnummers.add(chipkaartnummer);
        return this.chipkaartsnummers.contains(chipkaartnummer);
    }

    public boolean removeChipkaartNummer(Integer chipkaartnummer){
        this.chipkaartsnummers.remove(chipkaartnummer);
        return !this.chipkaartsnummers.contains(chipkaartnummer);
    }
    public void setProduct_nummer(int product_nummer) {
        this.product_nummer = product_nummer;
    }

    public String getNaam() {
        return naam;
    }

    public void setNaam(String naam) {
        this.naam = naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public void setBeschrijving(String beschrijving) {
        this.beschrijving = beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    @Override
    public String toString() {
        return "Product{" +
                "product_nummer=" + product_nummer +
                ", product='" + naam + '\'' +
                ", beschrijving='" + beschrijving + '\'' +
                ", prijs=" + prijs +
                '}';
    }
}
