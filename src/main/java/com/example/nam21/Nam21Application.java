package com.example.nam21;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@SpringBootApplication
public class Nam21Application {

	public static void main(String[] args) throws SQLException {
		SpringApplication.run(Nam21Application.class, args);

	}



}
