package dataReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


public class SelectResults {

	
	private static final String SELECT_STATEMENT = "SELECT * FROM importData ";
	public void Connect(){
		Connection connection = Main.getConnection(); // manages connection
		Statement statement = null; // query statement
		ResultSet resultSet = null; // manages results
		
		try{
			//create statement object
			statement = connection.createStatement();
			//create and execute query
			resultSet = statement.executeQuery(
					SELECT_STATEMENT );
		
		
			// process query results
			ResultSetMetaData metaData = resultSet.getMetaData();
			int numberOfColumns = metaData.getColumnCount();
			System.out.println( "Table of the Database:\n" );
			
			for ( int i = 1; i <= numberOfColumns; i++ )
				System.out.printf( "%-8s\t\t",metaData.getColumnName(i) );
				System.out.println("");
			
			while(resultSet.next()){
				for(int j=1;j<=numberOfColumns;j++)
					System.out.printf( "%-8s\t\t",resultSet.getObject(j) );
					System.out.println("");	
			}
		}catch(SQLException sqlException){
			sqlException.printStackTrace();
		}
		finally{
			try{
				resultSet.close();
				statement.close();
				connection.close();
			}catch(Exception exception ){
				exception.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		System.out.println("Output");
		SelectResults connector = new SelectResults();
		connector.Connect();
	}
}
