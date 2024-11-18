package Vista;

import ConexionBD.BaseDeDatos;
import Modelos.Cliente;
import Modelos.Presupuesto;
import Modelos.Usuario;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.List;
import java.time.LocalDate;

public class Menu {
    public static void main(String[] args) {
        BaseDeDatos bd = new BaseDeDatos();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\nSISTEMA DE GESTION DE PRESUPUESTOS");
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
            } catch (NumberFormatException e) {
                System.out.println("Error: La opción ingresada debe ser un número.");
            }
        }
    }

    private static void registrarUsuario(BaseDeDatos bd, Scanner scanner) {
        try {
            int ID_Usuario = bd.obtenerSiguienteIDUsuario();

            System.out.print("Nombre de Usuario: ");
            String Nombre_Usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String Contrasena = scanner.nextLine();
            System.out.print("Rol (administrador/vendedor): ");
            String Rol = scanner.nextLine();

            Usuario nuevoUsuario = new Usuario(ID_Usuario, Nombre_Usuario, Contrasena, Rol);
            bd.guardarUsuario(nuevoUsuario);
            System.out.println("Usuario registrado exitosamente con ID: " + ID_Usuario);
        } catch (Exception e) {
            System.out.println("Error al registrar el usuario: " + e.getMessage());
        }
    }

    private static void menuCliente(BaseDeDatos bd, Scanner scanner) {
        while (true) {
            try {
                System.out.println("\nMenú Cliente:");
                System.out.println("1. Registrar Cliente");
                System.out.println("2. Modificar Cliente");
                System.out.println("3. Eliminar Cliente");
                System.out.println("4. Listar Clientes");
                System.out.println("5. Volver al Menú Principal");

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
                        listarClientes(bd);
                        break;
                    case "5":
                        return;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: La opción ingresada debe ser un número.");
            }
        }
    }

    private static void registrarCliente(BaseDeDatos bd, Scanner scanner) {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Error: ID del Cliente debe ser un número válido.");
        }
    }

    private static void modificarCliente(BaseDeDatos bd, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del cliente que desea modificar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Verificar si el cliente existe
            Cliente clienteExistente = bd.obtenerClientePorID(id);
            if (clienteExistente == null) {
                System.out.println("Error: El ID del cliente no existe.");
                return;
            }

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
            System.out.println("Cliente modificado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número válido.");
        }
    }

    private static void eliminarCliente(BaseDeDatos bd, Scanner scanner) {
        try {
            System.out.print("Ingrese el ID del cliente que desea eliminar: ");
            int id = Integer.parseInt(scanner.nextLine());

            // Verificar si el cliente existe
            Cliente clienteExistente = bd.obtenerClientePorID(id);
            if (clienteExistente == null) {
                System.out.println("Error: El ID del cliente no existe.");
                return;
            }

            bd.eliminarCliente(id);
            System.out.println("Cliente eliminado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID debe ser un número válido.");
        }
    }

    private static void listarClientes(BaseDeDatos bd) {
        List<Cliente> clientes = bd.listarClientes();
        System.out.println("Listado de Clientes:");
        for (Cliente cliente : clientes) {
            System.out.println(cliente.getID_Cliente() + " - " + cliente.getNombre_Cliente() + " " + cliente.getApellido_Cliente());
        }
    }

    public static void gestionarPresupuestos(Scanner scanner, BaseDeDatos bd) {
        while (true) {
            try {
                System.out.println("\nSISTEMA DE GESTION DE PRESUPUESTOS");
                System.out.println("\nMenú de Presupuestos:");
                System.out.println("1. Generar Presupuesto");
                System.out.println("2. Listar Presupuestos");
                System.out.println("3. Modificar Presupuesto");
                System.out.println("4. Eliminar Presupuesto");
                System.out.println("5. Generar Informe de Presupuestos");
                System.out.println("6. Volver al Menú Principal");

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
                        generarInformePresupuestos(scanner, bd);
                        break;
                    case "6":
                        return;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: La opción ingresada debe ser un número.");
            }
        }
    }

    public static void generarPresupuesto(Scanner scanner, BaseDeDatos bd) {
        try {
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
        } catch (NumberFormatException e) {
            System.out.println("Error: Debe ingresar valores numéricos válidos para el Cliente, Vendedor y Pedido.");
        }
    }

    public static void listarPresupuestos(BaseDeDatos bd) {
        List<Presupuesto> presupuestos = bd.listarPresupuestos();
        System.out.println("Listado de Presupuestos:");
        for (Presupuesto presupuesto : presupuestos) {
            System.out.println(presupuesto);
        }
    }

    public static void modificarPresupuesto(Scanner scanner, BaseDeDatos bd) {
        try {
            System.out.print("ID del Presupuesto a modificar: ");
            int ID_Presupuesto = Integer.parseInt(scanner.nextLine());

            // Verificar si el presupuesto existe
            Presupuesto presupuestoExistente = bd.obtenerPresupuestoPorID(ID_Presupuesto);
            if (presupuestoExistente == null) {
                System.out.println("Error: El ID del presupuesto no existe.");
                return;
            }

            System.out.print("Nuevo Estado: ");
            String nuevoEstado = scanner.nextLine();
            bd.modificarPresupuesto(ID_Presupuesto, nuevoEstado);
            System.out.println("Presupuesto modificado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID del Presupuesto debe ser un número válido.");
        }
    }

    public static void eliminarPresupuesto(Scanner scanner, BaseDeDatos bd) {
        try {
            System.out.print("ID del Presupuesto a eliminar: ");
            int ID_Presupuesto = Integer.parseInt(scanner.nextLine());

            // Verificar si el presupuesto existe
            Presupuesto presupuestoExistente = bd.obtenerPresupuestoPorID(ID_Presupuesto);
            if (presupuestoExistente == null) {
                System.out.println("Error: El ID del presupuesto no existe.");
                return;
            }

            bd.eliminarPresupuesto(ID_Presupuesto);
            System.out.println("Presupuesto eliminado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Error: El ID del Presupuesto debe ser un número válido.");
        }
    }
    
private static void generarInformePresupuestos(Scanner scanner, BaseDeDatos bd) {
    System.out.println("Seleccione el estado de los presupuestos:");
    System.out.println("1. Pendiente");
    System.out.println("2. Finalizado");
    System.out.println("3. Cancelado");
    System.out.print("Ingrese el número correspondiente al estado: ");

    int opcion = scanner.nextInt();
    scanner.nextLine(); 

    String estado = switch (opcion) {
        case 1 -> "Pendiente";
        case 2 -> "Finalizado";
        case 3 -> "Cancelado";
        default -> {
            System.out.println("Opción no válida. Generando informe para el estado 'Pendiente' por defecto.");
            yield "Pendiente";
        }
    };

    String userHome = System.getProperty("user.home");
    
    String filePath = Paths.get(userHome, "informe_presupuestos.xlsx").toString();

    bd.generarInformePresupuestos(estado, filePath);
    System.out.println("Informe generado exitosamente en: " + filePath);
}

}
