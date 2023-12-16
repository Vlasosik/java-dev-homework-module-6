package org.example.dao;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.example.util.Database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;

public class DatabaseInitService {
    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new FileReader("sql/init_db.sql"))) {
            Connection connection = Database.getInstance().getConnection();
            ScriptRunner scriptRunner = new ScriptRunner(connection);
            scriptRunner.runScript(reader);
        }catch (IOException ex){
            ex.printStackTrace();
        }finally {
            Database.closeConnection();
        }
    }
}
