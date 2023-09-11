import java.sql.*;
import java.util.Scanner;

public class StudentDatabase {
	
	private static Connection con = null;
	private static Scanner scanner = new Scanner(System.in);

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StudentDatabase studentDb = new StudentDatabase();

		String url = "jdbc:mysql://localhost:3306/";
		String db = "sandradb1";
		String user = "root";
		String password = "admin";
		String driver = "com.mysql.cj.jdbc.Driver";

		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url + db, user, password);

			System.out.println("Student Database -> Enter choice: ");
			System.out.println("1. Insert record. ");
			System.out.println("2. Login with ID and password. ");
			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
			case 1:
				studentDb.insertRecord();
				break;
			case 2:
				studentDb.login();
				break;
			default:
				break;
			}

			con.close();

		} catch (Exception e) {
			throw new RuntimeException("Something went wrong!  "+e.getMessage());
		}

	}


	//Method to insert a new record in table "Student"
	private void insertRecord() throws SQLException {

		String sql = "insert into student(StudentID,StudentName,StudentAge,StudentEmail,StudentPassword) values(?,?,?,?,?)";

		PreparedStatement preparedStatement = con.prepareStatement(sql);

		System.out.println("Enter ID: ");
		preparedStatement.setInt(1, Integer.parseInt(scanner.nextLine()));

		System.out.println("Enter Name: ");
		preparedStatement.setString(2, scanner.nextLine());

		System.out.println("Enter Age: ");
		preparedStatement.setInt(3, Integer.parseInt(scanner.nextLine()));

		System.out.println("Enter Email: ");
		preparedStatement.setString(4, scanner.nextLine());
		
		System.out.println("Enter Password: ");
		preparedStatement.setString(5, scanner.nextLine());

		int rows = preparedStatement.executeUpdate();

		if (rows > 0)
			System.out.println("Record inserted successfully!");
	}
	
	
	//Method to validate ID and password
	private void login() throws SQLException {
		
		StudentDatabase studentDb = new StudentDatabase();

		System.out.println("Enter the Student ID to login:");
		int number = Integer.parseInt(scanner.nextLine());
		System.out.println("Enter the password to login:");
		String passw = scanner.nextLine();

		String sql = "Select * from sandradb1.student where StudentID = " + number + " and StudentPassword = '" + passw + "'";
		
		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(sql);

		if (result.next()) {
			System.out.println("You have succesfully logged!");

			int id = result.getInt("StudentID");
			String name = result.getString("StudentName");
			int age = result.getInt("StudentAge");
			String email = result.getString("StudentEmail");
			String password = result.getString("StudentPassword");
			System.out.println("  Welcome " + name);
			System.out.println("  ID: " + id + " - Age: " + age + " - Email: " + email);

			
			System.out.println("Now Enter Choice -> ");
			System.out.println("1. Insert record. ");
			System.out.println("2. Select record by ID. ");
			System.out.println("3. Get all students.");
			System.out.println("4. Update record.");
			System.out.println("5. Delete record.");
			int choice = Integer.parseInt(scanner.nextLine());

			switch (choice) {
			case 1:
				studentDb.insertRecord();
				break;
			case 2:
				studentDb.selectRecord();
				break;
			case 3:
				studentDb.selectAllRecords();
				break;
			case 4:
				studentDb.updateRecord();
				break;
			case 5:
				studentDb.deleteRecord();
				break;	
			default:
				break;
			}

		} else {
			System.out.println("No Student Found...Wrong StudentID or StudentPassword");
		}

	}


	//Method to get details about one student
	private void selectRecord() throws SQLException {

		System.out.println("Enter the Student ID to find result:");
		int number = Integer.parseInt(scanner.nextLine());

		String sql = "Select * from sandradb1.student where StudentID = " + number;

		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(sql);

		if (result.next()) {

			int id = result.getInt("StudentID");
			String name = result.getString("StudentName");
			int age = result.getInt("StudentAge");
			String email = result.getString("StudentEmail");
			String password = result.getString("StudentPassword");
			System.out.println("Student Data -> ");
			System.out.println("  ID: " + id);
			System.out.println("  Name: " + name);
			System.out.println("  Age: " + age);
			System.out.println("  Email: " + email);
			System.out.println("  Password: " + password);

		} else {
			System.out.println("No record found...");
		}

	}

	//Method to get details about all Students
	private void selectAllRecords() throws SQLException {
		// TODO Auto-generated method stub
		CallableStatement callableStatement = con.prepareCall("{call getAllStudents() }");
		ResultSet result = callableStatement.executeQuery();
		int cont = 0;
		
		while (result.next()) {
			cont++;
			int id = result.getInt("StudentID");
			String name = result.getString("StudentName");
			int age = result.getInt("StudentAge");
			String email = result.getString("StudentEmail");
			System.out.println("Student_" + cont + " -> ID: " + id + ", Name: " + name + ", Age: " + age + ", Email: " + email);
			System.out.println("---------------------------------------------------------------------------");
		}
	}

	
	//Method to update a record in table "Student"
	private void updateRecord() throws SQLException {

		System.out.println("Enter the Student ID to update record:");
		int number = Integer.parseInt(scanner.nextLine());

		String sql = "Select * from sandradb1.student where studentID = " + number;

		Statement statement = con.createStatement();
		ResultSet result = statement.executeQuery(sql);

		if (result.next()) {

			int id = result.getInt("StudentID");
			String name = result.getString("StudentName");
			int age = result.getInt("StudentAge");
			String email = result.getString("StudentEmail");
			System.out.println("Student Data -> ");
			System.out.println("  ID: " + id);
			System.out.println("  Name: " + name);
			System.out.println("  Age: " + age);
			System.out.println("  Email: " + email);
			
			System.out.println("What do you want to update? ");
			System.out.println("1. Name");
			System.out.println("2. Age");
			System.out.println("3. Email");
			int choice = Integer.parseInt(scanner.nextLine());
			
			String sqlQuery = "Update sandradb1.student set ";
			switch (choice) {
			case 1:
				System.out.println("Enter new name: ");
				String newName = scanner.nextLine();

				sqlQuery = sqlQuery + "StudentName = ? where StudentID = " + number;
				PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
				preparedStatement.setString(1, newName);
				int rows = preparedStatement.executeUpdate();
				if (rows > 0)
					System.out.println("Record updated successfully!");
				break;
			case 2:
				System.out.println("Enter new age: ");
				int newAge = Integer.parseInt(scanner.nextLine());

				sqlQuery = sqlQuery + "StudentAge = ? where StudentID = " + number;
				PreparedStatement preparedStatement2 = con.prepareStatement(sqlQuery);
				preparedStatement2.setInt(1, newAge);
				int rows2 = preparedStatement2.executeUpdate();
				if (rows2 > 0)
					System.out.println("Record updated successfully!");
				break;
			case 3:
				System.out.println("Enter new email: ");
				String newEmail = scanner.nextLine();

				sqlQuery = sqlQuery + "StudentEmail = ? where StudentID = " + number;
				PreparedStatement preparedStatement3 = con.prepareStatement(sqlQuery);
				preparedStatement3.setString(1, newEmail);
				int rows3 = preparedStatement3.executeUpdate();
				if (rows3 > 0)
					System.out.println("Record updated successfully!");
				break;

			default:
				break;
			}

		} else {
			System.out.println("No record found...");
		}
	}

	
	//Method to Delete a record in table "Student"
	private void deleteRecord() throws SQLException {
		
		System.out.println("Enter the Student ID to delete:");
		int number = Integer.parseInt(scanner.nextLine());
		
		String sql = "Delete from sandradb1.student where StudentID= " + number;
		Statement statement = con.createStatement();
		int rows = statement.executeUpdate(sql);

		if (rows > 0) {
			System.out.println("Record is deleted successfully!");
		
		} else {
			System.out.println("No record found...");
		}

	}
}
