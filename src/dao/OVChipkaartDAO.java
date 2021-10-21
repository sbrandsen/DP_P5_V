package DP_P5.dao;

import DP_P5.domein.OVChipkaart;
import DP_P5.domein.Reiziger;

import java.sql.SQLException;
import java.util.List;

public interface  OVChipkaartDAO {
    public boolean save(OVChipkaart ovchipkaart) throws SQLException;
    public boolean update(OVChipkaart ovchipkaart) throws SQLException;
    public boolean delete(OVChipkaart ovchipkaart) throws SQLException;

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) throws SQLException;
    public OVChipkaart findById(int id) throws SQLException;

    public List<OVChipkaart> findAll();
}
