package com.mycompany.gestion_de_presupuestos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BaseDeDatos {

    public void guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (ID_Usuario, Nombre_Usuario, Contraseña, Rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuario.getID_Usuario());
            pstmt.setString(2, usuario.getNombre_Usuario());
            pstmt.setString(3, usuario.getContraseña());
            pstmt.setString(4, usuario.getRol());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Usuario obtenerUsuario(String Nombre_Usuario) {
        String sql = "SELECT * FROM usuario WHERE Nombre_Usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Nombre_Usuario);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Usuario(rs.getInt("ID_Usuario"), rs.getString("Nombre_Usuario"),
                        rs.getString("Contraseña"), rs.getString("Rol"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
    public void registrarCliente(Cliente cliente) {
        String sql = "INSERT INTO cliente (ID_Cliente, Nombre_Cliente, Apellido_Cliente, Domicilio, Telefono) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, cliente.getID_Cliente());
            pstmt.setString(2, cliente.getNombre_Cliente());
            pstmt.setString(3, cliente.getApellido_Cliente());
            pstmt.setString(4, cliente.getDomicilio());
            pstmt.setString(5, cliente.getTelefono());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void modificarCliente(Cliente cliente) {
        String sql = "UPDATE cliente SET Nombre_Cliente = ?, Apellido_Cliente = ?, Domicilio = ?, Telefono = ? WHERE ID_Cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cliente.getNombre_Cliente());
            pstmt.setString(2, cliente.getApellido_Cliente());
            pstmt.setString(3, cliente.getDomicilio());
            pstmt.setString(4, cliente.getTelefono());
            pstmt.setInt(5, cliente.getID_Cliente());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarCliente(int ID_Cliente) {
        String sql = "DELETE FROM cliente WHERE ID_Cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Cliente);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void guardarPresupuesto(Presupuesto presupuesto) {
        String sql = "INSERT INTO presupuesto (ID_Cliente, ID_Vendedor, Numero_Pedido, Fecha_Emision, Fecha_Vencimiento, Estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, presupuesto.getID_Cliente());
            pstmt.setInt(2, presupuesto.getID_Vendedor());
            pstmt.setInt(3, presupuesto.getNumero_Pedido());
            pstmt.setDate(4, java.sql.Date.valueOf(presupuesto.getFecha_Emision()));
            pstmt.setDate(5, java.sql.Date.valueOf(presupuesto.getFecha_Vencimiento()));
            pstmt.setString(6, presupuesto.getEstado());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Presupuesto> listarPresupuestos() {
        List<Presupuesto> presupuestos = new ArrayList<>();
        String sql = "SELECT * FROM presupuesto";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int ID_Presupuesto = rs.getInt("ID_Presupuesto");
                int ID_Cliente = rs.getInt("ID_Cliente");
                int ID_Vendedor = rs.getInt("ID_Vendedor");
                int Numero_Pedido = rs.getInt("Numero_Pedido");
                LocalDate Fecha_Emision = rs.getDate("Fecha_Emision").toLocalDate();
                LocalDate Fecha_Vencimiento = rs.getDate("Fecha_Vencimiento").toLocalDate();
                String Estado = rs.getString("Estado");

                Presupuesto presupuesto = new Presupuesto(ID_Presupuesto, ID_Cliente, ID_Vendedor, Numero_Pedido, Fecha_Emision, Fecha_Vencimiento, Estado);
                presupuestos.add(presupuesto);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return presupuestos;
    }

    public void modificarPresupuesto(int ID_Presupuesto, String nuevoEstado) {
        String sql = "UPDATE presupuesto SET Estado = ? WHERE ID_Presupuesto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, ID_Presupuesto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void eliminarPresupuesto(int ID_Presupuesto) {
        String sql = "DELETE FROM presupuesto WHERE ID_Presupuesto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Presupuesto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

