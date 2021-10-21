package DP_P5.dao;

import DP_P5.domein.Adres;
import DP_P5.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface AdresDAO {
    public boolean save(Adres adres) throws SQLException;
    public boolean update(Adres adres) throws SQLException;
    public boolean delete(Adres adres) throws SQLException;

    public Adres findbyReiziger(Reiziger reiziger);

    public List<Adres> findAll();
}
