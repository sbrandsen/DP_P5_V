package DP_P5.dao;

import DP_P5.domein.Adres;
import DP_P5.domein.Reiziger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    Connection conn;
    ReizigerDAO rdao;

    public AdresDAOPsql(Connection connection){
        conn = connection;
        rdao = new ReizigerDAOPsql(connection, true);
    }

    AdresDAOPsql(Connection connection, boolean skipConnection){
        conn = connection;
        if(!skipConnection){
            rdao = new ReizigerDAOPsql(connection);
        }
    }

    @Override
    public boolean save(Adres adres) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        INSERT INTO public.adres
                                                                        (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id)
                                                                        VALUES(?, ?, ?, ?, ?, ?);
                                                                        """);
            prepStatement.setInt(1, adres.getId());
            prepStatement.setString(2, adres.getPostcode());
            prepStatement.setString(3, adres.getHuisnummer());
            prepStatement.setString(4, adres.getStraat());
            prepStatement.setString(5, adres.getWoonplaats());
            prepStatement.setInt(6, adres.getReiziger().getId());

            return prepStatement.execute();

        } catch(SQLException ex) {
            System.out.println("SQLException - could not save adres " + adres.toString());
            return false;
        } catch(Exception ex) {
            System.out.println("Error - could not save adres\n" +adres.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        try{
            PreparedStatement ps = conn.prepareStatement("""
                                                             UPDATE public.adres
                                                             SET postcode=?, huisnummer=?, straat=?, woonplaats=?, reiziger_id=?
                                                             WHERE adres_id=?;
                                                             """);

            ps.setString(1, adres.getPostcode());
            ps.setString(2, adres.getHuisnummer());
            ps.setString(3, adres.getStraat());
            ps.setString(4, adres.getWoonplaats());
            ps.setInt(5, adres.getReiziger().getId());
            ps.setInt(6, adres.getId());

            return ps.execute();

        } catch(SQLException ex) {
            System.out.println("SQLException - could not update adres " + adres.toString());
            return false;
        } catch(Exception ex) {
            System.out.println("Error - could not update adres\n" +adres.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        DELETE FROM public.adres
                                                                        WHERE adres_id=?;
                                                                        """);

            prepStatement.setInt(1, adres.getId());

            return prepStatement.execute();

        } catch(Exception ex) {
            System.out.println("Error - could not delete adres\n" +adres.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Adres findbyReiziger(Reiziger reiziger) {
        try{
            String query =  """
                            SELECT * FROM adres
                            WHERE reiziger_id = ?"
                            """;

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, reiziger.getId());
            ResultSet rs = ps.executeQuery();

            rs.next();
            int adres_id = rs.getInt("adres_id");
            String postcode = rs.getString("postcode");
            String huisnummer = rs.getString("huisnummer");
            String straat = rs.getString("straat");
            String woonplaats = rs.getString("woonplaats");
            Adres adres = new Adres(adres_id, postcode, huisnummer, straat, woonplaats, reiziger);
            ps.close();
            return adres;

        } catch(SQLException ex) {
            System.out.println("SQLException - could not find by reiziger " + reiziger.toString());
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not find by reiziger " + reiziger.toString());
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        try{
            PreparedStatement ps = conn.prepareStatement("""
                                                             SELECT *
                                                             FROM public.adres;
                                                             """);

            ResultSet rs = ps.executeQuery();

            List<Adres> adresList = new ArrayList<Adres>();

            while (rs.next() ) {
                int id = rs.getInt("adres_id");
                String postcode = rs.getString("postcode");
                String huisnummer = rs.getString("huisnummer");
                String straat = rs.getString("straat");
                String woonplaats = rs.getString("woonplaats");
                int reiziger_id = rs.getInt("reiziger_id");
                adresList.add(new Adres(id, postcode, huisnummer, straat, woonplaats, rdao.findById(reiziger_id)));
            }

            ps.close();
            rs.close();

            return adresList;
        } catch(SQLException ex) {
            System.out.println("SQLException - could not find all adressen");
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not find all adressen");
            return null;
        }
    }
}
