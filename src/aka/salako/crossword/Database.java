package aka.salako.crossword;

import java.sql.*;

public class Database {
	public static void main(String args[])
	{
		try {
			//étape 1: charger la classe driver
			Class.forName("com.mysqm.jdbc.Driver");
			
			//étape 2: créer un objet de connexion
			Connection connexion =  DriverManager.getConnection("jdbc:mysql://localhost:3306/java", "", "");
			connexion.close();
		} catch (ClassNotFoundException | SQLException e) {
			System.err.println("Driver non chargé !");
			e.printStackTrace();
		}
	}
}
