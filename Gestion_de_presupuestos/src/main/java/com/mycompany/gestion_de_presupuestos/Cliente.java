package com.mycompany.gestion_de_presupuestos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Cliente {
    private int ID_Cliente;
    private String Nombre_Cliente;
    private String Apellido_Cliente;
    private String Domicilio;
    private String Telefono;

    public Cliente(int ID_Cliente, String Nombre_Cliente, String Apellido_Cliente, String Domicilio, String Telefono) {
        this.ID_Cliente = ID_Cliente;
        this.Nombre_Cliente = Nombre_Cliente;
        this.Apellido_Cliente = Apellido_Cliente;
        this.Domicilio = Domicilio;
        this.Telefono = Telefono;
    }

    public int getID_Cliente() {
        return ID_Cliente;
    }

    public void setID_Cliente(int ID_Cliente) {
        this.ID_Cliente = ID_Cliente;
    }

    public String getNombre_Cliente() {
        return Nombre_Cliente;
    }

    public void setNombre_Cliente(String Nombre_Cliente) {
        this.Nombre_Cliente = Nombre_Cliente;
    }

    public String getApellido_Cliente() {
        return Apellido_Cliente;
    }

    public void setApellido_Cliente(String Apellido_Cliente) {
        this.Apellido_Cliente = Apellido_Cliente;
    }

    public String getDomicilio() {
        return Domicilio;
    }

    public void setDomicilio(String Domicilio) {
        this.Domicilio = Domicilio;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String Telefono) {
        this.Telefono = Telefono;
    }

    public void registrarCliente() {
        String sql = "INSERT INTO cliente (ID_Cliente, Nombre_Cliente, Apellido_Cliente, Domicilio, Telefono) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Cliente);
            pstmt.setString(2, Nombre_Cliente);
            pstmt.setString(3, Apellido_Cliente);
            pstmt.setString(4, Domicilio);
            pstmt.setString(5, Telefono);
            pstmt.executeUpdate();
            System.out.println("Cliente registrado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }

    public void modificarCliente() {
        String sql = "UPDATE cliente SET Nombre_Cliente = ?, Apellido_Cliente = ?, Domicilio = ?, Telefono = ? WHERE ID_Cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, Nombre_Cliente);
            pstmt.setString(2, Apellido_Cliente);
            pstmt.setString(3, Domicilio);
            pstmt.setString(4, Telefono);
            pstmt.setInt(5, ID_Cliente);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Cliente modificado exitosamente.");
            } else {
                System.out.println("No se encontró ningún cliente con el ID especificado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al modificar cliente: " + e.getMessage());
        }
    }

    public void eliminarCliente() {
        String sql = "DELETE FROM cliente WHERE ID_Cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Cliente);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Cliente eliminado exitosamente.");
            } else {
                System.out.println("No se encontró ningún cliente con el ID especificado.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar cliente: " + e.getMessage());
        }
    }
}
