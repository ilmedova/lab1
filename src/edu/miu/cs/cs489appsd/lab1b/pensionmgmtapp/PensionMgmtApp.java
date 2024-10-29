package cs.cs489appsd.lab1b.pensionmgmtapp;

import cs.cs489appsd.lab1b.pensionmgmtapp.model.Employee;
import cs.cs489appsd.lab1b.pensionmgmtapp.model.PensionPlan;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class PensionMgmtApp {

    public static void main(String[] args) {
        List<Employee> employees = loadEmployeesData();

        System.out.println("All Employees (JSON format):");
        System.out.println(convertEmployeesToJson(employees));

        System.out.println("\nMonthly Upcoming Enrollees Report (JSON format):");
        System.out.println(convertUpcomingEnrolleesToJson(employees));
    }

    private static List<Employee> loadEmployeesData() {
        List<Employee> employees = new ArrayList<>();

        Employee emp1 = new Employee(1, "Daniel", "Agar", LocalDate.of(2018, 1, 17), 105945.50);
        emp1.setPensionPlan(new PensionPlan("EX1089", LocalDate.of(2023, 1, 17), 100.00));

        Employee emp2 = new Employee(2, "Benard", "Shaw", LocalDate.of(2019, 4, 3), 197750.00);

        Employee emp3 = new Employee(3, "Carly", "Agar", LocalDate.of(2014, 5, 16), 842000.75);
        emp3.setPensionPlan(new PensionPlan("SM2307", LocalDate.of(2019, 11, 4), 1555.50));

        Employee emp4 = new Employee(4, "Wesley", "Schneider", LocalDate.of(2019, 9, 2), 74500.00);

        employees.add(emp1);
        employees.add(emp2);
        employees.add(emp3);
        employees.add(emp4);

        return employees;
    }

    private static String convertEmployeesToJson(List<Employee> employees) {
        List<Employee> sortedEmployees = employees.stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getYearlySalary).reversed())
                .collect(Collectors.toList());

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < sortedEmployees.size(); i++) {
            Employee employee = sortedEmployees.get(i);
            jsonBuilder.append(employeeToJson(employee));
            if (i < sortedEmployees.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }

    private static String convertUpcomingEnrolleesToJson(List<Employee> employees) {
        YearMonth nextMonth = YearMonth.now().plusMonths(1);
        LocalDate firstDayOfNextMonth = nextMonth.atDay(1);
        LocalDate lastDayOfNextMonth = nextMonth.atEndOfMonth();

        List<Employee> eligibleEmployees = employees.stream()
                .filter(emp -> emp.getPensionPlan() == null)  // Not enrolled yet
                .filter(Employee::isEligibleForPensionPlan)  // Employed for 5 years or more
                .filter(emp -> emp.getEmploymentDate().isBefore(lastDayOfNextMonth))
                .sorted(Comparator.comparing(Employee::getEmploymentDate))
                .collect(Collectors.toList());

        StringBuilder jsonBuilder = new StringBuilder();
        jsonBuilder.append("[\n");
        for (int i = 0; i < eligibleEmployees.size(); i++) {
            Employee employee = eligibleEmployees.get(i);
            jsonBuilder.append(employeeToJson(employee));
            if (i < eligibleEmployees.size() - 1) {
                jsonBuilder.append(",\n");
            }
        }
        jsonBuilder.append("\n]");
        return jsonBuilder.toString();
    }

    private static String employeeToJson(Employee employee) {
        StringBuilder json = new StringBuilder();
        json.append("  {\n");
        json.append("    \"employeeId\": ").append(employee.getEmployeeId()).append(",\n");
        json.append("    \"firstName\": \"").append(employee.getFirstName()).append("\",\n");
        json.append("    \"lastName\": \"").append(employee.getLastName()).append("\",\n");
        json.append("    \"employmentDate\": \"").append(employee.getEmploymentDate()).append("\",\n");
        json.append("    \"yearlySalary\": ").append(employee.getYearlySalary()).append(",\n");
        if (employee.getPensionPlan() != null) {
            json.append("    \"pensionPlan\": ").append(pensionPlanToJson(employee.getPensionPlan())).append("\n");
        } else {
            json.append("    \"pensionPlan\": null\n");
        }
        json.append("  }");
        return json.toString();
    }

    private static String pensionPlanToJson(PensionPlan plan) {
        return String.format(
                "{ \"planReferenceNumber\": \"%s\", \"enrollmentDate\": \"%s\", \"monthlyContribution\": %.2f }",
                plan.getPlanReferenceNumber(),
                plan.getEnrollmentDate(),
                plan.getMonthlyContribution()
        );
    }
}
