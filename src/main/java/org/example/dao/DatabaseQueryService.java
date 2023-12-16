package org.example.dao;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.util.Database;
import org.example.pojo.LongestProject;
import org.example.pojo.ProjectPrices;
import org.example.pojo.YoungestOldestWorkers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DatabaseQueryService {
    private static final String SELECT_LONGEST_PROJECT = """
                   SELECT project.ID,
                   EXTRACT(MONTH FROM project.START_DATE) AS START_MONTH,
                   EXTRACT(MONTH FROM project.FINISH_DATE) AS FINISH_MONTH
                   FROM project
                   ORDER BY START_MONTH, FINISH_MONTH DESC;
            """;
    private static final String SELECT_MAX_PROJECT_CLIENT = """
                   SELECT client.NAME, COUNT(project.CLIENT_ID) AS PROJECT_COUNT
                   FROM project
                   JOIN client ON project.CLIENT_ID = CLIENT.ID
                   GROUP BY client.NAME
                   ORDER BY PROJECT_COUNT DESC;
            """;
    private static final String SELECT_MAX_SALARY_WORKER = """
                    SELECT NAME,SALARY FROM worker
                    WHERE SALARY = (SELECT MAX(SALARY) FROM worker);
            """;
    private static final String SELECT_YOUNGEST_WORKERS = """
                   SELECT 'YOUNGEST' AS TYPE, NAME, BIRTHDAY
                   FROM worker
                   WHERE BIRTHDAY = (SELECT MIN(BIRTHDAY) FROM worker)
                   UNION
                   SELECT 'ELDEST' AS TYPE, NAME, BIRTHDAY
                   FROM worker
                   WHERE BIRTHDAY = (SELECT MAX(BIRTHDAY) FROM worker)
                   ORDER BY BIRTHDAY ASC;                    
            """;
    private static final String SELECT_PROJECT_PRICES = """
                   SELECT project_worker.WORKER_ID AS WORKER_ID,
            	   SUM(worker.SALARY * EXTRACT(MONTH FROM project.START_DATE) * EXTRACT(YEAR FROM project.FINISH_DATE)) AS PRICE
                   FROM project
                   JOIN project_worker ON project.ID = project_worker.PROJECT_ID
                   JOIN worker ON project_worker.WORKER_ID = worker.ID
                   GROUP BY project_worker.WORKER_ID
                   ORDER BY PRICE DESC;    
            """;

    public static void main(String[] args) {
        printLongestProject();
        printFindClientWithMaxProject();
        printMaxSalaryWorker();
        printYoungestOldestWorkers();
        printProjectPrices();
    }

    public static void printLongestProject() {
        List<LongestProject> longestProjects = new DatabaseQueryService().longestProjects();
        for (LongestProject project : longestProjects) {
            System.out.println("projectID: " + project.getId());
            System.out.println("start Month: " + project.getStartMonth());
            System.out.println("finish Month: " + project.getFinishMonth());
            System.out.println("---------------------");
        }
    }

    public static void printFindClientWithMaxProject() {
        Map<String, Long> findClientWithMaxProjects = new DatabaseQueryService().findClientWithMaxProjects();
        for (Map.Entry<String, Long> project : findClientWithMaxProjects.entrySet()) {
            System.out.println("name: " + project.getKey());
            System.out.println("projectCount: " + project.getValue());
        }
        System.out.println("---------------------");
    }

    public static void printMaxSalaryWorker() {
        Map<String, BigDecimal> printMaxSalaryWorker = new DatabaseQueryService().maxSalaryWorker();
        for (Map.Entry<String, BigDecimal> listSalary : printMaxSalaryWorker.entrySet()) {
            System.out.println("name: " + listSalary.getKey());
            System.out.println("salary: " + listSalary.getValue());
        }
        System.out.println("---------------------");
    }

    public static void printYoungestOldestWorkers() {
        List<YoungestOldestWorkers> printYoungestOldestWorkers = new DatabaseQueryService().youngestOldestWorkers();
        for (YoungestOldestWorkers listWorkers : printYoungestOldestWorkers) {
            System.out.println("text: " + listWorkers.getText());
            System.out.println("name: " + listWorkers.getName());
            System.out.println("birthday: " + listWorkers.getBirthday());
        }
        System.out.println("---------------------");
    }

    public static void printProjectPrices() {
        List<ProjectPrices> printProjectPrices = new DatabaseQueryService().projectPrices();
        for (ProjectPrices project : printProjectPrices) {
            System.out.println("workerId: " + project.getWorkerId());
            System.out.println("price: " + project.getPrice());
        }
        System.out.println("---------------------");
    }

    private List<LongestProject> longestProjects() {
        List<LongestProject> longestProjects = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LONGEST_PROJECT);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                LongestProject project = new LongestProject();
                project.setId(resultSet.getLong("id"));
                project.setStartMonth(resultSet.getBigDecimal("start_month"));
                project.setFinishMonth(resultSet.getBigDecimal("finish_month"));
                longestProjects.add(project);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return longestProjects;
    }

    private Map<String, Long> findClientWithMaxProjects() {
        Map<String, Long> findClientWithMaxProjects = new TreeMap<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MAX_PROJECT_CLIENT);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Long projectCount = resultSet.getLong("project_count");
                findClientWithMaxProjects.put(name, projectCount);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return findClientWithMaxProjects;
    }

    private Map<String, BigDecimal> maxSalaryWorker() {
        Map<String, BigDecimal> maxSalaryWorker = new TreeMap<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_MAX_SALARY_WORKER);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                BigDecimal salary = resultSet.getBigDecimal("salary");
                maxSalaryWorker.put(name, salary);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return maxSalaryWorker;
    }

    private List<YoungestOldestWorkers> youngestOldestWorkers() {
        List<YoungestOldestWorkers> youngestOldestWorkers = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_YOUNGEST_WORKERS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                YoungestOldestWorkers project = new YoungestOldestWorkers();
                project.setText(resultSet.getString("type"));
                project.setName(resultSet.getString("name"));
                project.setBirthday(LocalDate.parse(resultSet.getString("birthday")));
                youngestOldestWorkers.add(project);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return youngestOldestWorkers;
    }

    private List<ProjectPrices> projectPrices() {
        List<ProjectPrices> projectPrices = new ArrayList<>();
        try (Connection connection = Database.getInstance().getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_PROJECT_PRICES);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                ProjectPrices project = new ProjectPrices();
                project.setWorkerId(resultSet.getLong("worker_id"));
                project.setPrice(resultSet.getBigDecimal("price"));
                projectPrices.add(project);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            Database.closeConnection();
        }
        return projectPrices;
    }

    public static class DatabaseInitService {

        public static void main(String[] args) {

            try (BufferedReader reader = new BufferedReader(new FileReader("sql/init_db.sql"))) {
                Connection connection = Database.getInstance().getConnection();
                ScriptRunner scriptRunner = new ScriptRunner(connection);
                scriptRunner.runScript(reader);
                System.out.println("Database initialization successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                Database.closeConnection();
            }
        }
    }

    public static class DatabasePopulateService {
        public static void main(String[] args) {
            try (BufferedReader reader = new BufferedReader(new FileReader("sql/populate_db.sql"))) {
                Connection connection = Database.getInstance().getConnection();
                ScriptRunner scriptRunner = new ScriptRunner(connection);
                scriptRunner.runScript(reader);
                System.out.println("Database populated successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                Database.closeConnection();
            }
        }
    }
}
