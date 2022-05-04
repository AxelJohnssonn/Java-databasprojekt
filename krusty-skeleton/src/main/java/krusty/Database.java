package krusty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import spark.Request;
import spark.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

import static krusty.Jsonizer.toJson;

public class Database {
	/**
	 * Modify it to fit your environment and then use this string when connecting to your database!
	 */
	private static final String jdbcString = "jdbc:mysql://puccini.cs.lth.se/hbg04";

	// For use with MySQL or PostgreSQL
	private static final String jdbcUsername = "hbg04";
	private static final String jdbcPassword = "wxy677wl";

	private static final String Customers = "INSERT INTO Customers(customer, address, teleNbr) VALUES" +
			"    ('Finkakor AB', 'Helsingborg', '07696969')," +
			"    ('Småbröd AB', 'Malmö', '07696969')," +
			"    ('Kaffebröd AB', 'Landskrona', '07696969')," +
			"    ('Bjudkakor AB', 'Ystad', '07696969')," +
			"    ('Kalaskakor AB', 'Trelleborg', '07696969')," +
			"    ('Partykakor AB', 'Kristianstad', '07696969')," +
			"    ('Gästkakor AB', 'Hässleholm', '07696969')," +
			"    ('Skånekakor AB', 'Perstorp, '07696969')" +
			";";
	private static final String Cookie = "INSERT INTO Cookie(cName) VALUES" +
			"    ('Amneris')," +
			"    ('Berliner')," +
			"    ('Nut cookie')," +
			"    ('Nut ring')," +
			"    ('Tango')," +
			"    ('Almond delight')" +
			";";
	private static final String Ingredient = "INSERT INTO Ingredients(ingredientName, stockAmount,buyDate, unit) VALUES" +
			"    ('Bread crumbs', 500000,'2022-05-03', 'g')," +
			"    ('Butter', 500000, '2022-05-03','g')," +
			"    ('Chocolate', 500000, '2022-05-03','g')," +
			"    ('Chopped almonds', 500000, '2022-05-03','g')," +
			"    ('Cinnamon', 500000, '2022-05-03','g')," +
			"    ('Egg whites', 500000, '2022-05-03','ml')," +
			"    ('Eggs', 500000, '2022-05-03','g')," +
			"    ('Fine-ground nuts', 500000, '2022-05-03','g')," +
			"    ('Flour', 500000, '2022-05-03','g')," +
			"    ('Ground, roasted nuts', 500000, '2022-05-03','g')," +
			"    ('Icing sugar', 500000, '2022-05-03','g')," +
			"    ('Marzipan', 500000, '2022-05-03','g')," +
			"    ('Potato starch', 500000, '2022-05-03','g')," +
			"    ('Roasted, chopped nuts', 500000, '2022-05-03','g')," +
			"    ('Sodium bicarbonate', 500000, '2022-05-03','g')," +
			"    ('Sugar', 500000, '2022-05-03','g')," +
			"    ('Vanilla sugar', 500000, '2022-05-03','g')," +
			"    ('Vanilla', 500000, '2022-05-03','g')," +
			"    ('Wheat flour', 500000, '2022-05-03','g')" +
			";";

	private static final String Contain = "INSERT INTO Contain (cName, ingredientName, rAmount) VALUES" +
			"    ('Almond delight', 'Butter', 400),('Almond delight', 'Chopped almonds', 279),('Almond delight', 'Cinnamon', 10),('Almond delight', 'Flour', 400),('Almond delight', 'Sugar', 270)," +
			"    ('Amneris', 'Butter', 250),('Amneris', 'Eggs', 250),('Amneris', 'Marzipan', 750),('Amneris', 'Potato starch', 25),('Amneris', 'Wheat flour', 25)," +
			"    ('Berliner', 'Butter', 250),('Berliner', 'Chocolate', 50),('Berliner', 'Eggs', 50),('Berliner', 'Flour', 350),('Berliner', 'Icing sugar', 100),('Berliner', 'Vanilla sugar', 5)," +
			"    ('Nut cookie', 'Bread crumbs', 125),('Nut cookie', 'Chocolate', 50),('Nut cookie', 'Egg whites', 350),('Nut cookie', 'Fine-ground nuts', 750),('Nut cookie', 'Ground, roasted nuts', 625),('Nut cookie', 'Sugar', 375)," +
			"    ('Nut ring', 'Butter', 450),('Nut ring', 'Flour', 450),('Nut ring', 'Icing sugar', 190),('Nut ring', 'Roasted, chopped nuts', 225)," +
			"    ('Tango', 'Butter', 200),('Tango', 'Flour', 300),('Tango', 'Sodium bicarbonate', 4),('Tango', 'Sugar', 250),('Tango', 'Vanilla', 2)" +
			";";

	private Connection conn;

	public void connect() {
		try {
			conn = DriverManager.getConnection(jdbcString, jdbcUsername, jdbcPassword);
		} catch (SQLException e) {
			System.err.println(e);
		}
	}
	
	// TODO: Implement and change output in all methods below!
	public String getCustomers(Request req, Response res) {
        String sql = "SELECT customer AS name, address FROM Customers";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			return toJson(rs, "costumers");
		} catch (SQLException e) {
			return "";
		}
    }

	public String getRawMaterials(Request req, Response res) {
        String sql = "SELECT ingredientName AS name, stockAmount AS amount, unit FROM Ingredients";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			return toJson(rs, "raw-materials");
		} catch (SQLException e) {
			return "";
		}
    }

    public String getCookies(Request req, Response res) {
        String sql = "SELECT cName AS name FROM Cookie";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			return toJson(rs, "cookies");
		} catch (SQLException e) {
			return "";
		}
    }

    public String getRecipes(Request req, Response res) {
        String sql = "SELECT cName as cookie, Contain.ingredientName as raw_material, stockAmount as amount, unit FROM Contain, Ingredient WHERE Contain.ingredientName = Ingredients.ingredientName";
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			return Jsonizer.toJson(rs, "recipes");
		} catch (SQLException e) {
			return "";
		}
    }
	
	public String getPallets(Request req, Response res) throws SQLException {
		String sql = "SELECT palletNbr as id, cName AS cookie, palletDate as production_date, customer, blocked FROM Pallet INNER JOIN Orders ON Orders.ordNbr = Pallet.ordNbr"; // TO DO: Fix sql statement
		String title = "pallets";
		StringBuilder sb = new StringBuilder();

		sb.append(sql);

		List<String> paramList = Arrays.asList("from", "to", "cookie", "blocked");
		HashMap<String, String> map = new HashMap<String, String>();

		for (String param : paramList) {
			if (req.queryParams(param) != null) {
				map.put(param, req.queryParams(param));
			}
		}

		if (map.size() > 0) {
			sb.append(" WHERE");
		}

		int size = 1;

		for (Map.Entry<String, String> entry : map.entrySet()) {
			switch (entry.getKey()) {
				case "from":
					sb.append(" palletDate >= ?");
					break;
				case "to":
					sb.append(" palletDate <= ?");
					break;
				case "blocked":
					sb.append(" blocked = ?");
					break;
				case "cookie":
					sb.append(" cName = ?");
					break;
				default:
					break;
			}
			if (map.size() > size) {
				size++;
				sb.append(" AND");
			}
		}

		PreparedStatement stmt = conn.prepareStatement(sb.toString());

		int i = 1;

		for (Map.Entry<String, String> entry : map.entrySet()) {
			switch (entry.getKey()) {
				case "from":
					stmt.setDate(i, Date.valueOf(req.queryParams("from")));
					break;
				case "to":
					stmt.setDate(i, Date.valueOf(req.queryParams("to")));
					break;
				case "blocked":
					stmt.setString(i, req.queryParams("blocked"));
					break;
				case "cookie":
					stmt.setString(i, req.queryParams("cookie"));
					break;
				default:
					break;
			}
			i++;
		}

		ResultSet rs = stmt.executeQuery();

		String result = Jsonizer.toJson(rs, title);
		System.out.println(result);

		return result;
	}

	public String reset(Request req, Response res) {
		String[] tables = {"Cookie", "Customers", "Ingredients", "Orders", "Contain", "Pallet", "Amount"};
		List values = Arrays.asList("Cookie", "Customers", "Ingredients", "Contain");

		for (String table : tables) {
			try {
				Statement stmt = conn.createStatement();
				stmt.execute("SET FOREIGN_KEY_CHECKS=0");
				String sql = "TRUNCATE TABLE " + table;
				stmt.execute(sql);

				if (values.contains(table)) {
					stmt.executeUpdate(getValueQuery(table));
				}

				stmt.execute("SET FOREIGN_KEY_CHECKS=1");
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
		return "{\n\t\"status\": \"ok\"\n}";
	}
		private String getValueQuery(String table) {
			switch (table) {
				case "cookie":
					return Cookie;
				case "customers":
					return Customers;
				case "ingredient":
					return Ingredient;
				case "Contain":
					return Contain;
				default:
					return "";
			}
		}

	public String createPallet(Request req, Response res) throws SQLException {
		String cookie = req.queryParams("cookie");

		if (cookie == null) {
			return "{\n\t\"status\": \"error\"\n}";
		} else if (!checkCookie(cookie)) {
			return "{\n\t\"status\": \"unknown cookie\"\n}";
		}

		String sql = "UPDATE Ingredients SET stockAmount = stockAmount - ? WHERE ingredientName = ?";
		HashMap<String, Integer> map = cookieRecipe(cookie);

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, entry.getValue()*54);
			stmt.setString(2, entry.getKey());
			stmt.executeUpdate();
		}

		sql = "INSERT INTO Orders ( customer) VALUES (\"Finkakor AB\")";
		Statement stmt = conn.createStatement();

		stmt.executeUpdate(sql, stmt.RETURN_GENERATED_KEYS);
		ResultSet rs = stmt.getGeneratedKeys();

		int createdId = 0;
		if (rs.next()) {
			createdId = rs.getInt(1);
		}

		sql = "INSERT INTO pallet (palletDate, dDate, blocked, cName, ordNbr) VALUES (NOW(), NOW(), \"no\", \"" + cookie + "\"," + createdId + ")";

		stmt.executeUpdate(sql);

		return "{\n\t\"status\": \"ok\" ,\n \n\t\"id\":" +  createdId + "}";
	}
	private HashMap<String, Integer> cookieRecipe(String cookie) throws SQLException {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String sql = "SELECT cName, ingredientName, rAmount FROM Contain WHERE cName=\"" + cookie + "\"";
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery(sql);

		while(rs.next()) {
			map.put(rs.getString(2), (Integer) rs.getInt(3));
		}

		return map;
	}
	private boolean checkCookie(String cookie) throws SQLException {
		PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Cookie WHERE cName=?");
		stmt.setString(1, cookie);
		ResultSet rs = stmt.executeQuery();

		return rs.next();
	}
	private String getJson(String sql, String title) {
		try {
			PreparedStatement stmt = conn.prepareStatement(sql);
			ResultSet rs = stmt.executeQuery();

			return Jsonizer.toJson(rs, title);
		} catch (SQLException e) {
			return "";
		}
	}
	}
