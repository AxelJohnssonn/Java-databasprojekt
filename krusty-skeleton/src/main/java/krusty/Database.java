package krusty;

import spark.Request;
import java.sql.*;
import java.util.ArrayList;

import spark.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static krusty.Jsonizer.toJson;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg04";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "hbg04";
	private static final String jdbcPassword = "wxy677wl";

	private Connection conn;
	public void connect() {
		// Connect to database here
		
		try {
			conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			System.err.println(e);
		}
	}

	// TODO: Implement and change output in all methods below!

	public String getCustomers(Request req, Response res) {
		
		try {
			
		String sql = "SELECT customer as name, address FROM Customers";
		String title = "customers";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		
		return Jsonizer.toJson(rs, title);

		} catch (SQLException e) {
		return "";
	}
		
	}

	
	
	
	
	
	public String getRawMaterials(Request req, Response res) {
		
		try {
		String sql = "SELECT ingredientName AS name, stockAmount AS amount, unit FROM Ingredients";
		String title = "raw-materials";

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		return Jsonizer.toJson(rs, title);

		} catch (SQLException e) {
		return "";
	}
	
	}
	
	
	
	
	
	

	public String getCookies(Request req, Response res) {
		
		try {
		
		String sql = "SELECT cName AS name FROM Cookie";
		String title = "cookies";

		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		return Jsonizer.toJson(rs, title);

		} catch (SQLException e) {
		return "";
	}
	}
	
	
	
	
	

	public String getRecipes(Request req, Response res) {

		try {
		String sql = "SELECT cName as cookie, Ingredients.ingredientName as raw_material, rAmount as amount, unit" + 
						"FROM Contain, Ingredients WHERE Contain.ingredientName = Ingredients.ingredientName";
		String title = "recipes";
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		return Jsonizer.toJson(rs, title);

		} catch (SQLException e) {
		return "";
	}
	}
	
	
	
	
	

	public String getPallets(Request req, Response res) {

		String sqlbas = "SELECT palletNbr as id, cName AS cookie, palletDate as production_date, customer, blocked " +
		"FROM Pallet left JOIN Orders ON Orders.ordNbr = Pallet.ordNbr";
		StringBuilder sqltext = new StringBuilder();
		String title = "pallets";
		sqltext.append(sqlbas);
		int index1 = 0;
		int index2 = 0;
		int index3 = 0;
		int index4 = 0;
		int count = -1;
		List<String> paramList = Arrays.asList("from", "to", "cookie", "blocked");
		List<String> reqsts = new ArrayList<String>();
		String sort = " order by palletDate desc";
		String sqlKlar;

		for (String param : paramList) {
			if (req.queryParams(param) != null) {
				count++;
				reqsts.add(count,req.queryParams(param));
			}
		}


		count = 0;

		for (String param : paramList) {
			if (req.queryParams(param) != null && count == 0) {
				sqltext.append(" WHERE");
			}

			if (req.queryParams(param) != null) {
				
				switch (param) {
					case "from":
					count++;
					if(count > 1){
						sqltext.append(" AND");
					}
					sqltext.append(" palletDate >= ?");
					index1 = count;
						break;
					case "to":
					count++;
					if(count > 1){
						sqltext.append(" AND");
					}
					sqltext.append(" palletDate <= ?");
					if(count == 1){
						index1 = count;
					}else{
						index2 = count;
					}
						break;
					case "blocked":
					count++;
					if(count > 1){
						sqltext.append(" AND");
					}
					sqltext.append(" blocked = ?");
					if(count == 1){
						index1 = count;
					}else if(count == 2){
						index2 = count;
					}else{
						index3 = count;
					}
						break;
					case "cookie":
					count++;
					if(count > 1){
						sqltext.append(" AND");
					}
					sqltext.append(" cName = ?");
					if(count == 1){
						index1 = count;
					}else if(count == 2){
						index2 = count;
					}else if (count == 3){
						index3 = count;
					}else{
						index4 = count;
					}
						break;
					default:
						break;
				}
			}
		}

		sqlKlar = sqltext.toString() + sort;



		String results;

		if(count == 0){
			
			try {
				Statement stmt = conn.createStatement();
				
				ResultSet rs = stmt.executeQuery(sqlKlar);

				results = Jsonizer.toJson(rs,title);
		
				} catch (SQLException e){
					e.printStackTrace();
					return "";	
				}
		}else if(count == 1){
		
			try {
				
				PreparedStatement stmt = conn.prepareStatement(sqlKlar);
				stmt.setString(index1,reqsts.get(index1 - 1 ));
				ResultSet rs = stmt.executeQuery();
				
				results = Jsonizer.toJson(rs,title);
		
				} catch (SQLException e){
					e.printStackTrace();
					return "";	
				}
		}else if(count == 2){

			try {

				PreparedStatement stmt = conn.prepareStatement(sqlKlar);
				
				stmt.setString(index1,reqsts.get(index1 - 1));
				stmt.setString(index2,reqsts.get(index2 - 1));
				ResultSet rs = stmt.executeQuery();
				results = Jsonizer.toJson(rs,title);
		
				} catch (SQLException e){
					System.out.println("xxxxxxxxxxxxx");
					e.printStackTrace();
					return "";	
				}
		}else if(count == 3){

			try {

				PreparedStatement stmt = conn.prepareStatement(sqlKlar);
				
				stmt.setString(index1,reqsts.get(index1 - 1));
				stmt.setString(index2,reqsts.get(index2 - 1));
				stmt.setString(index3,reqsts.get(index3 - 1));
				ResultSet rs = stmt.executeQuery();
				results = Jsonizer.toJson(rs,title);
		
				} catch (SQLException e){
					e.printStackTrace();
					return "";	
				}
		}else{

			try {

				PreparedStatement stmt = conn.prepareStatement(sqlKlar);
				
				stmt.setString(index1,reqsts.get(index1));
				stmt.setString(index2,reqsts.get(index2));
				stmt.setString(index3,reqsts.get(index3));
				stmt.setString(index4,reqsts.get(index4));
				ResultSet rs = stmt.executeQuery();
				results = Jsonizer.toJson(rs,title);
		
				} catch (SQLException e){
					e.printStackTrace();
					return "";
				}
			}

			
			return results;
	}
	
	
	
	
	

	public String reset(Request req, Response res) {

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Amount";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Contain";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Cookie";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Customers";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Ingredients";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Orders";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE Pallet";
				stmt.execute(sql);
				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();	
			}


			try {
				Statement stmt = conn.createStatement();
				String sql = 
				"INSERT INTO Customers(customer, address, teleNbr) VALUES" +
				"('Finkakor AB', 'Helsingborg', '07696969'), " +
				"('Småbröd AB', 'Malmö', '07696969')," +
				"('Kaffebröd AB', 'Landskrona', '07696969')," +
				"('Bjudkakor AB', 'Ystad', '07696969')," +
				"('Kalaskakor AB', 'Trelleborg', '07696969')," +
				"('Partykakor AB', 'Kristianstad', '07696969')," +
				"('Gästkakor AB', 'Hässleholm', '07696969')," +
				"('Skånekakor AB', 'Perstorp', '07696969');" ;
				stmt.execute(sql);
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				String sql = 
				"INSERT INTO Cookie(cName) VALUES" +
				"('Amneris')," +
				"('Berliner')," +
				"('Nut cookie')," +
				"('Nut ring')," +
				"('Tango')," +
				"('Almond delight');";
				stmt.execute(sql);
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				String sql = 
				"INSERT INTO Ingredients(ingredientName, stockAmount, buyDate, unit) VALUES" +
				"('Bread crumbs', 500000,'2022-05-03', 'g')," +
				"('Butter', 500000,'2022-05-03', 'g')," +
				"('Chocolate', 500000,'2022-05-03', 'g'), " +
           		"('Chopped almonds',500000, '2022-05-03', 'g')," +
            	"('Cinnamon', 500000,'2022-05-03', 'g')," +
            	"('Egg whites', 500000,'2022-05-03', 'ml'), " +
            	"('Eggs', 500000,'2022-05-03', 'g')," +
            	"('Fine-ground nuts', 500000,'2022-05-03', 'g')," +
            	"('Flour', 500000,'2022-05-03', 'g')," +
            	"('Ground, roasted nuts', 500000,'2022-05-03', 'g'), " +
            	"('Icing sugar', 500000,'2022-05-03', 'g')," +
            	"('Marzipan', 500000,'2022-05-03', 'g')," +
            	"('Potato starch', 500000,'2022-05-03', 'g')," +
            	"('Roasted, chopped nuts', 500000,'2022-05-03', 'g')," +
            	"('Sodium bicarbonate', 500000,'2022-05-03', 'g')," +
            	"('Sugar', 500000,'2022-05-03', 'g')," +
            	"('Vanilla sugar', 500000,'2022-05-03', 'g')," +
            	"('Vanilla', 500000,'2022-05-03', 'g')," +
            	"('Wheat flour', 500000,'2022-05-03', 'g');";
				stmt.execute(sql);
			} catch (SQLException e){
				e.printStackTrace();	
			}

			try {
				Statement stmt = conn.createStatement();
				String sql = 
				"INSERT INTO Contain (cName, ingredientName, rAmount) VALUES" +
           		"('Almond delight', 'Butter', 400)," +
            	"('Almond delight', 'Chopped almonds', 279)," +
            	"('Almond delight', 'Cinnamon', 10)," +
            	"('Almond delight', 'Flour', 400)," +
            	"('Almond delight', 'Sugar', 270)," +
            	"('Amneris', 'Butter', 250)," +
            	"('Amneris', 'Eggs', 250)," +
            	"('Amneris', 'Marzipan', 750)," +
            	"('Amneris', 'Potato starch', 25)," +
            	"('Amneris', 'Wheat flour', 25)," +
            	"('Berliner', 'Butter', 250)," +
            	"('Berliner', 'Chocolate', 50)," +
            	"('Berliner', 'Eggs', 50)," +
            	"('Berliner', 'Flour', 350)," +
            	"('Berliner', 'Icing sugar', 100)," +
            	"('Berliner', 'Vanilla sugar', 5)," +
            	"('Nut cookie', 'Bread crumbs', 125)," +
            	"('Nut cookie', 'Chocolate', 50)," +
            	"('Nut cookie', 'Egg whites', 350)," +
            	"('Nut cookie', 'Fine-ground nuts', 750)," +
            	"('Nut cookie', 'Ground, roasted nuts', 625)," +
            	"('Nut cookie', 'Sugar', 375)," +
            	"('Nut ring', 'Butter', 450)," +
            	"('Nut ring', 'Flour', 450)," +
            	"('Nut ring', 'Icing sugar', 190)," +
            	"('Nut ring', 'Roasted, chopped nuts', 225)," +
            	"('Tango', 'Butter', 200)," +
            	"('Tango', 'Flour', 300)," +
            	"('Tango', 'Sodium bicarbonate', 4)," +
            	"('Tango', 'Sugar', 250),('Tango', 'Vanilla', 2);";
				stmt.execute(sql);
			} catch (SQLException e){
				e.printStackTrace();	
			}

		return "{\n\t\"status\": \"ok\"\n}";
	}
	

	
	
	
	
	
	

	public String createPallet(Request req, Response res) {

		String cookie = req.queryParams("cookie");
		String retrievedCookie;
		ArrayList<Integer> updateAmount = new ArrayList<Integer>();
		ArrayList<String> updateName = new ArrayList<String>();
		ArrayList<Integer> updateAmountStock = new ArrayList<Integer>();

		

		try {

			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Cookie WHERE cName = ?");
			stmt.setString(1,cookie);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			retrievedCookie = rs.getString("cName");
			
		} catch (SQLException e) {
			return "";
		}

		 

		if (cookie == null) {
			return "{\n\t\"status\": \"error\"\n}";
		} else if (retrievedCookie == null) {
			return "{\n\t\"status\": \"unknown cookie\"\n}";
		} 
		
		try{ 

            PreparedStatement stmt = conn.prepareStatement( 
				"SELECT Ingredients.ingredientName as raw_material, rAmount as amount " +
				"FROM Contain, Ingredients " + 
				"WHERE Contain.ingredientName = Ingredients.ingredientName and cName = ?");

			stmt.setString(1,cookie);
			ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
				
				updateName.add(rs.getString("raw_material"));
				updateAmount.add(rs.getInt("amount"));
            }
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}


		try{
			PreparedStatement stmt2 = conn.prepareStatement(
				"Select Ingredients.ingredientName, stockAmount " +
				"from Ingredients, Contain " +
				"WHERE Contain.ingredientName = Ingredients.ingredientName and cName = ?" );

			stmt2.setString(1,cookie);
			ResultSet rs2 = stmt2.executeQuery();

			int i = 0;
			while (rs2.next()) {
				
				updateAmountStock.add(rs2.getInt("stockAmount") - (updateAmount.get(i) * 54));
				i++;
            }
			
			stmt2.close();
		}catch(SQLException e){
			e.printStackTrace();
		}

		
		try{
			int k = 0;
			while( k < updateName.size()){

			PreparedStatement stmt = conn.prepareStatement("UPDATE Ingredients SET stockAmount = ? WHERE ingredientName = ?");
			stmt.setInt(1,updateAmountStock.get(k));
			stmt.setString(2,updateName.get(k));
    		stmt.executeUpdate();
			k++;
			}


           }catch(SQLException e){
               e.printStackTrace();
           }


		int createdId = 0;

		try{

			Statement s = conn.createStatement();
     		s.executeUpdate("INSERT INTO Orders (customer) VALUES  ('Finkakor AB')",Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()) { 
				createdId = rs.getInt(1); 
			  } 
			
		}catch(SQLException e){
					e.printStackTrace();
				}


		int palletID = 0;

		try{
			Statement s = conn.createStatement();
     		s.executeUpdate("INSERT INTO Pallet (palletDate, blocked, dDate, location, cName, ordNbr) " +
			 "VALUES  (NOW(), 'no', NOW(), 'In production', '" + retrievedCookie +"', " + createdId + ")",Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = s.getGeneratedKeys();
			if (rs.next()) { 
				palletID = rs.getInt(1); 
			  } 
			
		}catch(SQLException e){
					e.printStackTrace();
				}


		return "{\n\t\"status\": \"ok\" ,\n \n\t\"id\":" +  palletID + "}";
	}
	
	
	
}