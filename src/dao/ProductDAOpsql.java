package DP_P5.dao;

import DP_P5.domein.OVChipkaart;
import DP_P5.domein.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        for(OVChipkaart ov : product.getOvchipkaarten()){
            rdao.save(ov);
        }

        for(OVChipkaart ov : product.getOvchipkaarten()){
            //ckpdao.save(new OV_Chipkaart_Product(product, ov));
        }

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

            for(OVChipkaart ov : product.getOvchipkaarten()){
                //ckpdao.updateChipkaart(ov, product);
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
        for(OVChipkaart ov : product.getOvchipkaarten()){
            rdao.delete(ov);
            //ckpdao.deleteChipkaart(ov);
        }

        try{
            PreparedStatement prepStatement = conn.prepareStatement("""
                                                                        DELETE FROM public.product
                                                                        WHERE product_nummer=?;
                                                                        """);

            prepStatement.setInt(1, product.getProduct_nummer());

            boolean complete = prepStatement.execute();
            prepStatement.close();


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
            ResultSet rs = prepStatement.executeQuery();

            Product product = null;
            List<Product>  productList = new ArrayList<Product>();

            while (rs.next() ) {
                product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"),
                        rs.getDouble("prijs"));

                product.addOvchipkaart(ovChipkaart);
                productList.add(product);
            }

            prepStatement.close();
            rs.close();

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

            Product product = null;
            List<Product>  productList = new ArrayList<Product>();

            while (rs.next() ) {
                product = new Product(rs.getInt("product_nummer"), rs.getString("naam"), rs.getString("beschrijving"),
                        rs.getDouble("prijs"));

                productList.add(product);
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
