package com.mycompany.gestion_de_presupuestos;

import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class Menu {
    public static void main(String[] args) {
        BaseDeDatos bd = new BaseDeDatos();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Registrar Usuario");
            System.out.println("2. Cliente");
            System.out.println("3. Gestionar Presupuestos");
            System.out.println("4. Salir");

            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarUsuario(bd, scanner);
                    break;
                case "2":
                    menuCliente(bd, scanner);
                    break;
                case "3":
                    gestionarPresupuestos(scanner, bd);
                    break;
                case "4":
                    System.out.println("Saliendo del sistema.");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private static void registrarUsuario(BaseDeDatos bd, Scanner scanner) {
        System.out.print("ID: ");
        int ID_Usuario = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre de Usuario: ");
        String Nombre_Usuario = scanner.nextLine();
        System.out.print("Contraseña: ");
        String Contrasena = scanner.nextLine();
        System.out.print("Rol (administrador/vendedor): ");
        String Rol = scanner.nextLine();

        Usuario nuevoUsuario = new Usuario(ID_Usuario, Nombre_Usuario, Contrasena, Rol);
        bd.guardarUsuario(nuevoUsuario);
        System.out.println("Usuario registrado exitosamente.");
    }

    private static void menuCliente(BaseDeDatos bd, Scanner scanner) {
        while (true) {
            System.out.println("\nMenú Cliente:");
            System.out.println("1. Registrar Cliente");
            System.out.println("2. Modificar Cliente");
            System.out.println("3. Eliminar Cliente");
            System.out.println("4. Volver al Menú Principal");

            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    registrarCliente(bd, scanner);
                    break;
                case "2":
                    modificarCliente(bd, scanner);
                    break;
                case "3":
                    eliminarCliente(bd, scanner);
                    break;
                case "4":
                    return; // Regresar al menú principal
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    private static void registrarCliente(BaseDeDatos bd, Scanner scanner) {
        System.out.print("ID del Cliente: ");
        int ID_Cliente = Integer.parseInt(scanner.nextLine());
        System.out.print("Nombre del Cliente: ");
        String Nombre_Cliente = scanner.nextLine();
        System.out.print("Apellido del Cliente: ");
        String Apellido_Cliente = scanner.nextLine();
        System.out.print("Domicilio: ");
        String Domicilio = scanner.nextLine();
        System.out.print("Teléfono: ");
        String Telefono = scanner.nextLine();

        Cliente nuevoCliente = new Cliente(ID_Cliente, Nombre_Cliente, Apellido_Cliente, Domicilio, Telefono);
        bd.registrarCliente(nuevoCliente);
        System.out.println("Cliente registrado exitosamente.");
    }

    private static void modificarCliente(BaseDeDatos bd, Scanner scanner) {
        System.out.print("Ingrese el ID del cliente que desea modificar: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.println("Ingrese los nuevos datos del cliente:");
        System.out.print("Nuevo Nombre del Cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Nuevo Apellido del Cliente: ");
        String apellido = scanner.nextLine();
        System.out.print("Nuevo Domicilio: ");
        String domicilio = scanner.nextLine();
        System.out.print("Nuevo Teléfono: ");
        String telefono = scanner.nextLine();

        Cliente cliente = new Cliente(id, nombre, apellido, domicilio, telefono);
        bd.modificarCliente(cliente);
    }

    private static void eliminarCliente(BaseDeDatos bd, Scanner scanner) {
        System.out.print("Ingrese el ID del cliente que desea eliminar: ");
        int id = Integer.parseInt(scanner.nextLine());

        bd.eliminarCliente(id);
        System.out.println("Cliente eliminado exitosamente.");
    }

    public static void gestionarPresupuestos(Scanner scanner, BaseDeDatos bd) {
        while (true) {
            System.out.println("\nMenú de Presupuestos:");
            System.out.println("1. Generar Presupuesto");
            System.out.println("2. Listar Presupuestos");
            System.out.println("3. Modificar Presupuesto");
            System.out.println("4. Eliminar Presupuesto");
            System.out.println("5. Volver al Menú Principal");

            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    generarPresupuesto(scanner, bd);
                    break;
                case "2":
                    listarPresupuestos(bd);
                    break;
                case "3":
                    modificarPresupuesto(scanner, bd);
                    break;
                case "4":
                    eliminarPresupuesto(scanner, bd);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
            }
        }
    }

    public static void generarPresupuesto(Scanner scanner, BaseDeDatos bd) {
    System.out.print("ID Cliente: ");
    int ID_Cliente = Integer.parseInt(scanner.nextLine());
    System.out.print("ID Vendedor: ");
    int ID_Vendedor = Integer.parseInt(scanner.nextLine());
    System.out.print("Número de Pedido: ");
    int Numero_Pedido = Integer.parseInt(scanner.nextLine());
    LocalDate fechaEmision = LocalDate.now();
    LocalDate fechaVencimiento = fechaEmision.plusDays(5);
    String estado = "Pendiente";

    Presupuesto nuevoPresupuesto = new Presupuesto(0, ID_Cliente, ID_Vendedor, Numero_Pedido,
                                                   fechaEmision, fechaVencimiento, estado);
    bd.guardarPresupuesto(nuevoPresupuesto);
    System.out.println("Presupuesto generado exitosamente.");
}

    public static void listarPresupuestos(BaseDeDatos bd) {
        List<Presupuesto> presupuestos = bd.listarPresupuestos();
        System.out.println("Listado de Presupuestos:");
        for (Presupuesto presupuesto : presupuestos) {
            System.out.println(presupuesto);
        }
    }

    public static void modificarPresupuesto(Scanner scanner, BaseDeDatos bd) {
               System.out.print("ID del Presupuesto a modificar: ");
        int ID_Presupuesto = Integer.parseInt(scanner.nextLine());
        System.out.print("Nuevo Estado: ");
        String nuevoEstado = scanner.nextLine();

        bd.modificarPresupuesto(ID_Presupuesto, nuevoEstado);
        System.out.println("Presupuesto modificado exitosamente.");
    }

    public static void eliminarPresupuesto(Scanner scanner, BaseDeDatos bd) {
        System.out.print("ID del Presupuesto a eliminar: ");
        int ID_Presupuesto = Integer.parseInt(scanner.nextLine());

        bd.eliminarPresupuesto(ID_Presupuesto);
        System.out.println("Presupuesto eliminado exitosamente.");
    }
}
