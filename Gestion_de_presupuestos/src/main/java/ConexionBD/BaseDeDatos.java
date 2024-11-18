package ConexionBD;

import Modelos.Cliente;
import Modelos.Presupuesto;
import Modelos.Usuario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BaseDeDatos {

    // Método para obtener el siguiente ID de usuario disponible
    public int obtenerSiguienteIDUsuario() {
        int siguienteID = 1;
        String sql = "SELECT MAX(ID_Usuario) FROM usuario";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                siguienteID = rs.getInt(1) + 1;  // Sumar 1 al ID máximo encontrado
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el siguiente ID de usuario: " + e.getMessage());
        }
        return siguienteID;
    }

    // Método para guardar un usuario en la base de datos
    public void guardarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuario (ID_Usuario, Nombre_Usuario, Contraseña, Rol) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Usamos el siguiente ID disponible en lugar del ID proporcionado por el usuario
            int ID_Usuario = obtenerSiguienteIDUsuario();
            pstmt.setInt(1, ID_Usuario);  // Usamos el ID generado automáticamente
            pstmt.setString(2, usuario.getNombre_Usuario());
            pstmt.setString(3, usuario.getContraseña());
            pstmt.setString(4, usuario.getRol());
            pstmt.executeUpdate();
            System.out.println("Usuario registrado exitosamente con ID: " + ID_Usuario);
        } catch (SQLException e) {
            System.out.println("Error al guardar el usuario: " + e.getMessage());
        }
    }

    // Método para obtener un usuario por su nombre de usuario
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

    // Método para registrar un cliente en la base de datos
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

    // Método para modificar los datos de un cliente
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

    // Método para eliminar un cliente de la base de datos
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

    // Método para listar todos los clientes
    public List<Cliente> listarClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM cliente";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                int ID_Cliente = rs.getInt("ID_Cliente");
                String Nombre_Cliente = rs.getString("Nombre_Cliente");
                String Apellido_Cliente = rs.getString("Apellido_Cliente");
                String Domicilio = rs.getString("Domicilio");
                String Telefono = rs.getString("Telefono");

                Cliente cliente = new Cliente(ID_Cliente, Nombre_Cliente, Apellido_Cliente, Domicilio, Telefono);
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return clientes;
    }

    // Método para obtener un cliente por su ID
    public Cliente obtenerClientePorID(int id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE ID_Cliente = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int ID_Cliente = rs.getInt("ID_Cliente");
                String Nombre_Cliente = rs.getString("Nombre_Cliente");
                String Apellido_Cliente = rs.getString("Apellido_Cliente");
                String Domicilio = rs.getString("Domicilio");
                String Telefono = rs.getString("Telefono");

                cliente = new Cliente(ID_Cliente, Nombre_Cliente, Apellido_Cliente, Domicilio, Telefono);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el cliente: " + e.getMessage());
        }
        return cliente;
    }

    // Método para guardar un presupuesto
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

    // Método para obtener un presupuesto por su ID
    public Presupuesto obtenerPresupuestoPorID(int id) {
        Presupuesto presupuesto = null;
        String sql = "SELECT * FROM presupuesto WHERE ID_Presupuesto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int ID_Presupuesto = rs.getInt("ID_Presupuesto");
                int ID_Cliente = rs.getInt("ID_Cliente");
                int ID_Vendedor = rs.getInt("ID_Vendedor");
                int Numero_Pedido = rs.getInt("Numero_Pedido");
                LocalDate Fecha_Emision = rs.getDate("Fecha_Emision").toLocalDate();
                LocalDate Fecha_Vencimiento = rs.getDate("Fecha_Vencimiento").toLocalDate();
                String Estado = rs.getString("Estado");

                presupuesto = new Presupuesto(ID_Presupuesto, ID_Cliente, ID_Vendedor, Numero_Pedido, Fecha_Emision, Fecha_Vencimiento, Estado);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el presupuesto: " + e.getMessage());
        }
        return presupuesto;
    }
    // Método para listar todos los presupuestos
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

    // Métodos para modificar y eliminar presupuestos
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

    // Método para generar un informe de presupuestos
     public void generarInformePresupuestos(String estado, String filePath) {
        List<Presupuesto> presupuestos = listarPresupuestosPorEstado(estado);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Presupuestos");

            // Crear estilo para las fechas
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper creationHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-mm-dd"));

            // Encabezado
            Row headerRow = sheet.createRow(0);
            String[] columnHeaders = {"ID Presupuesto", "ID Cliente", "ID Vendedor", "Número de Pedido", "Fecha de Emisión", "Fecha de Vencimiento", "Estado"};
            for (int i = 0; i < columnHeaders.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnHeaders[i]);
            }

            // Escribir los datos
            int rowNum = 1;
            for (Presupuesto presupuesto : presupuestos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(presupuesto.getID_Presupuesto());
                row.createCell(1).setCellValue(presupuesto.getID_Cliente());
                row.createCell(2).setCellValue(presupuesto.getID_Vendedor());
                row.createCell(3).setCellValue(presupuesto.getNumero_Pedido());

                // Escribir fechas con el formato adecuado
                Cell cellEmision = row.createCell(4);
                cellEmision.setCellValue(presupuesto.getFecha_Emision());
                cellEmision.setCellStyle(dateCellStyle);

                Cell cellVencimiento = row.createCell(5);
                cellVencimiento.setCellValue(presupuesto.getFecha_Vencimiento());
                cellVencimiento.setCellStyle(dateCellStyle);

                row.createCell(6).setCellValue(presupuesto.getEstado());
            }

            // Ajustar el tamaño de las columnas automáticamente
            for (int i = 0; i < columnHeaders.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Guardar archivo
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }

            System.out.println("Informe generado exitosamente en: " + filePath);
        } catch (IOException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método auxiliar para obtener presupuestos por estado
    public List<Presupuesto> listarPresupuestosPorEstado(String estado) {
        List<Presupuesto> presupuestos = new ArrayList<>();
        String sql = "SELECT * FROM presupuesto WHERE Estado = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, estado);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                // Mapea los resultados
                Presupuesto presupuesto = new Presupuesto(
                        rs.getInt("ID_Presupuesto"),
                        rs.getInt("ID_Cliente"),
                        rs.getInt("ID_Vendedor"),
                        rs.getInt("Numero_Pedido"),
                        rs.getDate("Fecha_Emision").toLocalDate(),
                        rs.getDate("Fecha_Vencimiento").toLocalDate(),
                        rs.getString("Estado")
                );
                presupuestos.add(presupuesto);
            }
        } catch (SQLException e) {
            System.out.println("Error al consultar la base de datos: " + e.getMessage());
            e.printStackTrace();
        }

        return presupuestos;
    }
}
