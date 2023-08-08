import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagementSystem {
    private List<Employee> employees;
    private Scanner scanner;
    private final String DATA_FILE = "employees.txt";

    public EmployeeManagementSystem() {
        employees = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadEmployeesFromFile();
    }

    public void addEmployee(Employee employee) {
        employees.add(employee);
    }

    public void viewEmployees() {
        for (Employee employee : employees) {
            System.out.println(employee);
        }
    }

    public void updateEmployee(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                System.out.print("Enter new name: ");
                String name = scanner.nextLine();
                System.out.print("Enter new age: ");
                int age = scanner.nextInt();
                System.out.print("Enter new position: ");
                String position = scanner.nextLine();
                System.out.print("Enter new salary: ");
                double salary = scanner.nextDouble();

                employee.setName(name);
                employee.setAge(age);
                employee.setPosition(position);
                employee.setSalary(salary);
                System.out.println("Employee details updated successfully.");
                return;
            }
        }
        System.out.println("Employee with ID " + id + " not found.");
    }

    public void deleteEmployee(int id) {
        employees.removeIf(employee -> employee.getId() == id);
        System.out.println("Employee with ID " + id + " deleted successfully.");
    }

    public Employee searchEmployee(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return employee;
            }
        }
        return null;
    }

    private boolean isIdUnique(int id) {
        for (Employee employee : employees) {
            if (employee.getId() == id) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidAge(int age) {
        return age >= 0;
    }

    private boolean isValidSalary(double salary) {
        return salary >= 0;
    }

    // Method to save employees to a file
    private void saveEmployeesToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (Employee employee : employees) {
                writer.println(employee.getId() + "," + employee.getName() + "," + employee.getAge() + "," + employee.getPosition() + "," + employee.getSalary());
            }
        } catch (IOException e) {
            System.out.println("Error saving data to file: " + e.getMessage());
        }
    }

    // Method to load employees from a file
private void loadEmployeesFromFile() {
    try {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            file.createNewFile();
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] data = line.split(",");
            int id = Integer.parseInt(data[0]);
            String name = data[1];
            int age = Integer.parseInt(data[2]);
            String position = data[3];
            double salary = Double.parseDouble(data[4]);

            Employee employee = new Employee(id, name, age, position, salary);
            employees.add(employee);
        }
        reader.close();
    } catch (IOException e) {
        System.out.println("Error loading data from file: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        EmployeeManagementSystem ems = new EmployeeManagementSystem();
        Scanner scanner = new Scanner(System.in);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            ems.saveEmployeesToFile();
            scanner.close();
        }));

        while (true) {
            System.out.println("\nEmployee Management System");
            System.out.println("1. Add Employee");
            System.out.println("2. View Employees");
            System.out.println("3. Update Employee");
            System.out.println("4. Delete Employee");
            System.out.println("5. Search Employee");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter employee ID: ");
                    int id = scanner.nextInt();
                    if (!ems.isIdUnique(id)) {
                        System.out.println("Employee with ID " + id + " already exists. Please use a unique ID.");
                        break;
                    }
                    System.out.print("Enter employee name: ");
                    String name = scanner.next();
                    System.out.print("Enter employee age: ");
                    int age = scanner.nextInt();
                    if (!ems.isValidAge(age)) {
                        System.out.println("Age must be a non-negative value.");
                        break;
                    }
                    System.out.print("Enter employee position: ");
                    String position = scanner.nextLine();
                    System.out.print("Enter employee salary: ");
                    double salary = scanner.nextDouble();
                    if (!ems.isValidSalary(salary)) {
                        System.out.println("Salary must be a non-negative value.");
                        break;
                    }

                    Employee employee = new Employee(id, name, age, position, salary);
                    ems.addEmployee(employee);
                    System.out.println("Employee added successfully.");
                    break;
                case 2:
                    System.out.println("All Employees:");
                    ems.viewEmployees();
                    break;
                case 3:
                    System.out.print("Enter employee ID to update: ");
                    int updateId = scanner.nextInt();
                    ems.updateEmployee(updateId);
                    break;
                case 4:
                    System.out.print("Enter employee ID to delete: ");
                    int deleteId = scanner.nextInt();
                    ems.deleteEmployee(deleteId);
                    break;
                case 5:
                    System.out.print("Enter employee ID to search: ");
                    int searchId = scanner.nextInt();
                    Employee foundEmployee = ems.searchEmployee(searchId);
                    if (foundEmployee != null) {
                        System.out.println("Employee found: " + foundEmployee);
                    } else {
                        System.out.println("Employee not found.");
                    }
                    break;
                case 6:
                    System.out.println("Exiting the Employee Management system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}

