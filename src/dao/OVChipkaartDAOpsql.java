package DP_P5.dao;

import DP_P5.domein.OVChipkaart;
import DP_P5.domein.Product;
import DP_P5.domein.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOpsql implements OVChipkaartDAO {
    Connection conn;
    ReizigerDAO rdao;
    ProductDAO pdao;

    public OVChipkaartDAOpsql(Connection connection){
        this.conn = connection;
        this.rdao = new ReizigerDAOPsql(connection, true);
        this.pdao = new ProductDAOpsql(connection, true);
    }

    OVChipkaartDAOpsql(Connection connection, boolean skipConnection){
        this.conn = connection;
        if(!skipConnection){
            this.rdao = new ReizigerDAOPsql(connection, true);
        }
    }

    @Override
    public boolean save(OVChipkaart ovchipkaart) throws SQLException {

        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        INSERT INTO public.ov_chipkaart
                                                                        (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id)
                                                                        VALUES(?, ?, ?, ?, ?);
                                                                        """);

            prepStatement.setInt(1, ovchipkaart.getKaart_nummer());
            prepStatement.setDate(2, ovchipkaart.getGeldig_tot());
            prepStatement.setInt(3, ovchipkaart.getKlasse());
            prepStatement.setDouble(4, ovchipkaart.getSaldo());
            prepStatement.setInt(5, ovchipkaart.getReiziger().getId());

            boolean complete = prepStatement.execute();
            prepStatement.close();

            return complete;

        } catch(SQLException ex) {
            System.out.println("SQL Error - could not save chipkaart\n" +ovchipkaart.toString());
            ex.printStackTrace();
            return false;

        } catch(Exception ex) {
            System.out.println("Error - could not save chipkaart\n" +ovchipkaart.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovchipkaart) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        UPDATE public.ov_chipkaart
                                                                        SET geldig_tot=?, klasse=?, saldo=?, reiziger_id=?
                                                                        WHERE kaart_nummer=?;
                                                                        """);


            prepStatement.setDate(1, ovchipkaart.getGeldig_tot());
            prepStatement.setInt(2, ovchipkaart.getKlasse());
            prepStatement.setDouble(3, ovchipkaart.getSaldo());
            prepStatement.setInt(4, ovchipkaart.getReiziger().getId());
            prepStatement.setInt(5, ovchipkaart.getKaart_nummer());

            boolean complete = prepStatement.execute();
            prepStatement.close();
            return complete;

        } catch(SQLException ex) {
            System.out.println("SQL Error - could not update chipkaart\n" +ovchipkaart.toString());
            ex.printStackTrace();
            return false;

        } catch(Exception ex) {
            System.out.println("Error - could not update chipkaart\n" +ovchipkaart.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovchipkaart) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        DELETE FROM public.ov_chipkaart
                                                                        WHERE kaart_nummer=?;
                                                                        """);

            prepStatement.setInt(1, ovchipkaart.getKaart_nummer());

            boolean complete = prepStatement.execute();
            prepStatement.close();

            return complete;

        } catch(SQLException ex) {
            System.out.println("SQL Error - could not delete chipkaart\n" +ovchipkaart.toString());
            ex.printStackTrace();
            return false;

        } catch(Exception ex) {
            System.out.println("Error - could not delete chipkaart\n" +ovchipkaart.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException {
        try{
            List<Product> products = pdao.findAll();

            String query = "SELECT * FROM ov_chipkaart WHERE reiziger_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            List<OVChipkaart> chipkaarten = new ArrayList<>();

            while (rs.next()){
                int kaartNummer = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                int reizigerId = rs.getInt("reiziger_id");
                OVChipkaart ovChipkaart = new OVChipkaart(kaartNummer, geldigTot, klasse, saldo, rdao.findById(reizigerId));

                for (Product product : products){
                    if (product.getChipkaartsnummers().size() != 0){
                        for (int nummer : product.getChipkaartsnummers()){
                            if (nummer == kaartNummer){
                                ovChipkaart.addProduct(product);
                            }
                        }
                    }
                }

                chipkaarten.add(ovChipkaart);
            }

            ps.close();
            rs.close();

            return chipkaarten;
        } catch (SQLException ex){
            System.out.println("SQLException - could not ovchipkaart by id " + reiziger.getId());
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not ovchipkaart by id " + reiziger.getId());
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {
        try {

            List<Product> products = pdao.findAll();

            String query = "SELECT * FROM ov_chipkaart";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            List<OVChipkaart> chipkaarts = new ArrayList<>();

            while (rs.next()){
                int kaartNummer = rs.getInt("kaart_nummer");
                Date geldigTot = rs.getDate("geldig_tot");
                int klasse = rs.getInt("klasse");
                double saldo = rs.getDouble("saldo");
                int reizigerId = rs.getInt("reiziger_id");
                OVChipkaart ovChipkaart = new OVChipkaart(kaartNummer, geldigTot, klasse, saldo, rdao.findById(reizigerId));

                for (Product product : products){
                    if (product.getChipkaartsnummers().size() != 0){
                        for (int nummer : product.getChipkaartsnummers()){
                            if (nummer == kaartNummer){
                                ovChipkaart.addProduct(product);
                            }
                        }
                    }
                }

                chipkaarts.add(ovChipkaart);
            }
            ps.close();
            rs.close();
            return chipkaarts;
        } catch (SQLException ex){
            System.out.println("SQLException - could not get ovchipkaarten");
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not get ovchipkaarten");
            return null;
        }
    }
}
