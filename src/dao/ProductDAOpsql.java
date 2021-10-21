package DP_P5.dao;

import DP_P5.domein.OVChipkaart;
import DP_P5.domein.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOpsql implements ProductDAO{
    Connection conn;
    OVChipkaartDAO rdao;

    public ProductDAOpsql(Connection connection) {
        conn = connection;
        rdao = new OVChipkaartDAOpsql(conn, true);
    }

    public ProductDAOpsql(Connection connection, boolean skipConnection) {
        conn = connection;
        if(!skipConnection){
            rdao = new OVChipkaartDAOpsql(conn, true);
        }
    }

    @Override
    public boolean save(Product product) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        INSERT INTO public.product
                                                                        (product_nummer, naam, beschrijving, prijs)
                                                                        VALUES(?, ?, ?, ?);
                                                                        """);
            prepStatement.setInt(1, product.getProduct_nummer());
            prepStatement.setString(2, product.getNaam());
            prepStatement.setString(3, product.getBeschrijving());
            prepStatement.setDouble(4, product.getPrijs());

            boolean complete = prepStatement.execute();
            prepStatement.close();

            List<Integer> ovcardnummers = product.getChipkaartsnummers();

            String s2 = "INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, status, last_update) VALUES (?, ?, ?, ?)";
            for (Integer kaartnummer : ovcardnummers){
                PreparedStatement ps2 = conn.prepareStatement(s2);
                ps2.setInt(1, kaartnummer);
                ps2.setInt(2, product.getProduct_nummer());
                ps2.setString(3, "actief");
                ps2.setDate(4, new Date(System.currentTimeMillis()));
                ps2.executeQuery();
                ps2.close();
            }

            return complete;

        } catch(SQLException ex) {
            System.out.println("SQL Error - could not add product\n" +product.toString());
            ex.printStackTrace();
            return false;

        } catch(Exception ex) {
            System.out.println("Error - could not add product\n" +product.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Product product) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        UPDATE public.product
                                                                        SET naam=?, beschrijving=?, prijs=?
                                                                        WHERE product_nummer=?;
                                                                        """);


            prepStatement.setString(1, product.getNaam());
            prepStatement.setString(2, product.getBeschrijving());
            prepStatement.setDouble(3, product.getPrijs());
            prepStatement.setInt(4, product.getProduct_nummer());

            boolean complete = prepStatement.execute();
            prepStatement.close();

            List<Integer> ovcardnummers = product.getChipkaartsnummers();

            String s1 = """
                        UPDATE public.ov_chipkaart_product
                        SET status=?, last_update=?
                        WHERE kaart_nummer=? AND product_nummer=?;
                        """;
            for (Integer kaartnummer : ovcardnummers){
                PreparedStatement ps3 = conn.prepareStatement(s1);
                ps3.setString(1, "actief");
                ps3.setDate(2, new Date(System.currentTimeMillis()));
                ps3.setInt(3, kaartnummer);
                ps3.setInt(4, product.getProduct_nummer());
                ps3.executeQuery();
                ps3.close();
            }

            return complete;

        } catch(SQLException ex) {
            System.out.println("SQL Error - could not update product\n" +product.toString());
            ex.printStackTrace();
            return false;

        } catch(Exception ex) {
            System.out.println("Error - could not update product\n" +product.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Product product) throws SQLException {
        try{

            String s1 = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
            PreparedStatement ps1 = conn.prepareStatement(s1);
            ps1.setInt(1, product.getProduct_nummer());
            ps1.executeQuery();
            ps1.close();


            String s2 = "DELETE FROM product WHERE product_nummer = ?";
            PreparedStatement ps2 = conn.prepareStatement(s2);
            ps2.setInt(1, product.getProduct_nummer());
            ps2.executeQuery();
            ps2.close();

            boolean complete = ps2.execute();
            ps2.close();

            return complete;

        } catch(SQLException ex) {
            System.out.println("SQL Error - could not delete product\n" +product.toString());
            ex.printStackTrace();
            return false;

        } catch(Exception ex) {
            System.out.println("Error - could not delete product\n" +product.toString());
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        select p.*
                                                                        from product p
                                                                        left join ov_chipkaart_product ocp on p.product_nummer = ocp.product_nummer
                                                                        left join ov_chipkaart oc on oc.kaart_nummer = ocp.kaart_nummer
                                                                        where oc.kaart_nummer = ?
                                                                        """);


            prepStatement.setInt(1, ovChipkaart.getKaart_nummer());
            ResultSet rs1 = prepStatement.executeQuery();

            Product newproduct = null;
            List<Product>  productList = new ArrayList<Product>();

            while (rs1.next() ) {
                newproduct = new Product(rs1.getInt("product_nummer"), rs1.getString("naam"), rs1.getString("beschrijving"),
                        rs1.getDouble("prijs"));
                productList.add(newproduct);
            }

            prepStatement.close();
            rs1.close();

            String query2 = """
                            SELECT ov_chipkaart.kaart_nummer FROM ov_chipkaart
                            JOIN ov_chipkaart_product ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer
                            where ov_chipkaart_product.product_nummer = ?
                            """;

            for (Product product : productList){
                PreparedStatement ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, product.getProduct_nummer());
                ResultSet rs2 = ps2.executeQuery();
                if (!product.addChipkaartNummer(rs2.getInt("kaart_nummer"))){
                    throw new SQLException();
                };
                ps2.close();
                rs2.close();
            }

            return productList;
        } catch(SQLException ex) {
            System.out.println("SQL Error - could not findbyov with ovchipkaart\n" +ovChipkaart.toString());
            ex.printStackTrace();
            return null;

        } catch(Exception ex) {
            System.out.println("Error - could not findbyov with ovchipkaart\n" +ovChipkaart.toString());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Product> findAll() throws SQLException {
        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        SELECT *
                                                                        FROM public.product;
                                                                        """);

            ResultSet rs = prepStatement.executeQuery();

            Product newProduct = null;
            List<Product>  productList = new ArrayList<Product>();

            while (rs.next() ) {
                newProduct = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"),
                        rs.getDouble("prijs"));

                productList.add(newProduct);
            }

            String query2 = """
                            SELECT ov_chipkaart.kaart_nummer FROM ov_chipkaart
                            JOIN ov_chipkaart_product ON ov_chipkaart.kaart_nummer = ov_chipkaart_product.kaart_nummer
                            where ov_chipkaart_product.product_nummer = ?
                            """;

            for (Product product : productList){
                PreparedStatement ps2 = conn.prepareStatement(query2);
                ps2.setInt(1, product.getProduct_nummer());
                ResultSet rs2 = ps2.executeQuery();
                if (!product.addChipkaartNummer(rs2.getInt("kaart_nummer"))){
                    throw new SQLException();
                };
                ps2.close();
                rs2.close();
            }

            prepStatement.close();
            rs.close();

            return productList;

        } catch (SQLException ex){
            System.out.println("SQLError - could not find all producten");
            return null;
        } catch(Exception ex) {
            System.out.println("Error - could not find all producten");
            ex.printStackTrace();
            return null;
        }
    }
}
