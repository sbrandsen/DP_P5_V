package DP_P5.dao;

import DP_P5.domein.Reiziger;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface ReizigerDAO {
    public boolean save(Reiziger reiziger) throws SQLException;
    public boolean update(Reiziger reiziger) throws SQLException;
    public boolean delete(Reiziger reiziger) throws SQLException;

    public Reiziger findById(int id) throws SQLException;

    public List<Reiziger> findByGbDatum(Date Datum);
    public List<Reiziger> findAll();

}
