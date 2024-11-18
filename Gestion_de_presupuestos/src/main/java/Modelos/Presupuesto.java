package Modelos;

import ConexionBD.DatabaseConnection;
import java.nio.file.Paths;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Presupuesto {
    private int ID_Presupuesto;
    private int ID_Cliente;
    private int ID_Vendedor;
    private int Numero_Pedido;
    private LocalDate Fecha_Emision;
    private LocalDate Fecha_Vencimiento;
    private String Estado;

    public Presupuesto(int ID_Presupuesto, int ID_Cliente, int ID_Vendedor, int Numero_Pedido,
                   LocalDate Fecha_Emision, LocalDate Fecha_Vencimiento, String Estado){
        this.ID_Presupuesto = ID_Presupuesto;
        this.ID_Cliente = ID_Cliente;
        this.ID_Vendedor = ID_Vendedor;
        this.Numero_Pedido = Numero_Pedido;
        this.Fecha_Emision = Fecha_Emision;
        this.Fecha_Vencimiento = Fecha_Vencimiento;
        this.Estado = Estado;
    }

    public int getID_Presupuesto() {
        return ID_Presupuesto;
    }

    public void setID_Presupuesto(int ID_Presupuesto) {
        this.ID_Presupuesto = ID_Presupuesto;
    }

    public int getID_Cliente() {
        return ID_Cliente;
    }

    public void setID_Cliente(int ID_Cliente) {
        this.ID_Cliente = ID_Cliente;
    }

    public int getID_Vendedor() {
        return ID_Vendedor;
    }

    public void setID_Vendedor(int ID_Vendedor) {
        this.ID_Vendedor = ID_Vendedor;
    }

    public int getNumero_Pedido() {
        return Numero_Pedido;
    }

    public void setNumero_Pedido(int Numero_Pedido) {
        this.Numero_Pedido = Numero_Pedido;
    }

    public LocalDate getFecha_Emision() {
        return Fecha_Emision;
    }

    public void setFecha_Emision(LocalDate Fecha_Emision) {
        this.Fecha_Emision = Fecha_Emision;
    }

    public LocalDate getFecha_Vencimiento() {
        return Fecha_Vencimiento;
    }

    public void setFecha_Vencimiento(LocalDate Fecha_Vencimiento) {
        this.Fecha_Vencimiento = Fecha_Vencimiento;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    @Override
    public String toString() {
        return "ID Presupuesto: " + ID_Presupuesto + ", ID Cliente: " + ID_Cliente +
           ", ID Vendedor: " + ID_Vendedor + ", Número de Pedido: " + Numero_Pedido +
           ", Fecha de Emisión: " + Fecha_Emision + ", Fecha de Vencimiento: " + Fecha_Vencimiento +
           ", Estado: " + Estado;
    }

    public static void generarPresupuesto(int ID_Cliente, int ID_Vendedor, int Numero_Pedido) {
        LocalDate fechaEmision = LocalDate.now();
        LocalDate fechaVencimiento = fechaEmision.plusDays(5);
        String estado = "Pendiente";

        String sql = "INSERT INTO presupuesto (ID_Cliente, ID_Vendedor, Numero_Pedido, Fecha_Emision, Fecha_Vencimiento, Estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Cliente);
            pstmt.setInt(2, ID_Vendedor);
            pstmt.setInt(3, Numero_Pedido);
            pstmt.setDate(4, java.sql.Date.valueOf(fechaEmision));
            pstmt.setDate(5, java.sql.Date.valueOf(fechaVencimiento));
            pstmt.setString(6, estado);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public static List<Presupuesto> listarPresupuestos() {
        List<Presupuesto> presupuestos = new ArrayList<>();
        String sql = "SELECT * FROM presupuesto";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
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
            System.out.println(e.getMessage());
        }
        return presupuestos;
    }

    public static void modificarPresupuesto(int ID_Presupuesto, String nuevoEstado) {
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

    public static void eliminarPresupuesto(int ID_Presupuesto) {
        String sql = "DELETE FROM presupuesto WHERE ID_Presupuesto = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, ID_Presupuesto);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


public static void generarInforme() {
        List<Presupuesto> presupuestos = listarPresupuestos();
        Map<String, List<Presupuesto>> presupuestosPorEstado = presupuestos.stream()
                .collect(Collectors.groupingBy(Presupuesto::getEstado));

        String userHome = System.getProperty("user.home");
        String documentsPath = Paths.get(userHome, "Documents", "InformePresupuestos.xlsx").toString();

        try (Workbook workbook = new XSSFWorkbook()) {
            
            CellStyle dateCellStyle = workbook.createCellStyle();
            CreationHelper createHelper = workbook.getCreationHelper();
            dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-mm-dd"));

            
            for (Map.Entry<String, List<Presupuesto>> entry : presupuestosPorEstado.entrySet()) {
                String estado = entry.getKey();
                List<Presupuesto> presupuestosEstado = entry.getValue();

                
                Sheet sheet = workbook.createSheet(estado);

                
                Row headerRow = sheet.createRow(0);
                String[] columnHeaders = {"ID Presupuesto", "ID Cliente", "ID Vendedor", "Número de Pedido", "Fecha de Emisión", "Fecha de Vencimiento"};
                for (int i = 0; i < columnHeaders.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(columnHeaders[i]);
                }

                
                int rowNum = 1;
                for (Presupuesto p : presupuestosEstado) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(p.getID_Presupuesto());
                    row.createCell(1).setCellValue(p.getID_Cliente());
                    row.createCell(2).setCellValue(p.getID_Vendedor());
                    row.createCell(3).setCellValue(p.getNumero_Pedido());

                    
                    Cell cellEmision = row.createCell(4);
                    cellEmision.setCellValue(p.getFecha_Emision());
                    cellEmision.setCellStyle(dateCellStyle);

                    Cell cellVencimiento = row.createCell(5);
                    cellVencimiento.setCellValue(p.getFecha_Vencimiento());
                    cellVencimiento.setCellStyle(dateCellStyle);
                }

                
                for (int i = 0; i < columnHeaders.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

           
            try (FileOutputStream fileOut = new FileOutputStream(documentsPath)) {
                workbook.write(fileOut);
                System.out.println("Informe generado en: " + documentsPath);
            }
        } catch (IOException e) {
            System.out.println("Error al generar el informe: " + e.getMessage());
        }
    }
}
