package DP_P5.dao;

import DP_P5.domein.OVChipkaart;
import DP_P5.domein.Product;
import DP_P5.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        for(Product p : ovchipkaart.getProducten()){
            //
        }
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

            for(Product p : ovchipkaart.getProducten()){
                //
            }

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
        for(Product p : ovchipkaart.getProducten()){
            //
        }

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
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id
                                                                        FROM public.ov_chipkaart
                                                                        WHERE reiziger_id = ?;
                                                                        """);
            prepStatement.setInt(1, reiziger.getId());
            ResultSet rs = prepStatement.executeQuery();

            List<OVChipkaart> ovchipkaart = new ArrayList<OVChipkaart>();

            while (rs.next() ) {
                ovchipkaart.add(new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"),
                        rs.getInt("saldo"), reiziger));
            }

            prepStatement.close();
            rs.close();

            return ovchipkaart;
        } catch (SQLException ex){
            System.out.println("SQLException - could not ovchipkaart by id " + reiziger.getId());
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not ovchipkaart by id " + reiziger.getId());
            return null;
        }
    }

    @Override
    public OVChipkaart findById(int id) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id
                                                                        FROM public.ov_chipkaart
                                                                        WHERE kaart_nummer = ?;
                                                                        """);
            prepStatement.setInt(1, id);
            ResultSet rs = prepStatement.executeQuery();

            OVChipkaart ovchipkaart = null;

            while (rs.next() ) {
                ovchipkaart = new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"),
                        rs.getInt("saldo"), rdao.findById(rs.getInt("reiziger_id")));
            }

            prepStatement.close();
            rs.close();

            return ovchipkaart;
        } catch (SQLException ex){
            System.out.println("SQLException - could not ovchipkaart by id " + id);
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not ovchipkaart by id " + id);
            return null;
        }
    }

    @Override
    public List<OVChipkaart> findAll() {

        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        SELECT kaart_nummer, geldig_tot, klasse, saldo, reiziger_id
                                                                        FROM public.ov_chipkaart;
                                                                        """);
            ResultSet rs = prepStatement.executeQuery();

            List<OVChipkaart> ovchipkaart = new ArrayList<OVChipkaart>();

            while (rs.next() ) {
                ovchipkaart.add(new OVChipkaart(rs.getInt("kaart_nummer"), rs.getDate("geldig_tot"), rs.getInt("klasse"),
                        rs.getInt("saldo"), rdao.findById(rs.getInt("reiziger_id"))));
            }

            prepStatement.close();
            rs.close();

            return ovchipkaart;
        } catch (SQLException ex){
            System.out.println("SQLException - could not get ovchipkaarten");
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not get ovchipkaarten");
            return null;
        }
    }
}
