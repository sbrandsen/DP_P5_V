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

        } catch(Exception ex) {
            System.out.println("Error - could not save adres\n" +adres.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        UPDATE public.adres
                                                                        SET adres_id=?, postcode=?, huisnummer=?, straat=?, woonplaats=?
                                                                        WHERE reiziger_id=?;
                                                                        """);

            prepStatement.setInt(1, adres.getId());
            prepStatement.setString(2, adres.getPostcode());
            prepStatement.setString(3, adres.getHuisnummer());
            prepStatement.setString(4, adres.getStraat());
            prepStatement.setString(5, adres.getWoonplaats());
            prepStatement.setInt(6, adres.getReiziger().getId());

            return prepStatement.execute();

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
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        SELECT adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id
                                                                        FROM public.adres
                                                                        WHERE reiziger_id = ?;
                                                                        """);
            prepStatement.setInt(1, reiziger.getId());
            ResultSet rs = prepStatement.executeQuery();

            Adres adres = null;

            while (rs.next() ) {
                //adres = new Adres(rs.getInt("adres_id"), rs.getString("postcode"), rs.getString("huisnummer"),
                        //rs.getString("straat"), rs.getString("woonplaats"))
            }

            return adres;
        } catch(Exception ex) {
            System.out.println("Error - could not find adres by id: " + reiziger.getId());
            return null;
        }
    }

    @Override
    public List<Adres> findAll() {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        SELECT *
                                                                        FROM public.adres;
                                                                        """);

            ResultSet rs = prepStatement.executeQuery();

            Adres adres = null;
            List<Adres> adresList = new ArrayList<Adres>();

            while (rs.next() ) {
                //adres = new Adres(rs.getInt("adres_id"), rs.getString("adres_id"), rs.getString("huisnummer"),
                        //.getString("straat"), rs.getString("woonplaats"), rs.getInt("reiziger_id"));
                adresList.add(adres);
            }

            return adresList;
        } catch(Exception ex) {
            System.out.println("Error - could not find all adressen");
            return null;
        }
    }
}
