package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.CoppiaStati;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {
/*
	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}
*/
	public void loadAllStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				idMap.put(state.getId(), state);
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<CoppiaStati> listCoppieStati(Integer anno, String forma, Map<String, State> idMap){
		String sql = "SELECT n.state1 AS st1, n.state2 AS st2, COUNT(s1.shape) AS c " +
					"FROM neighbor AS n, sighting AS s1, sighting AS s2 " +
					"WHERE n.state1 > n.state2 AND s1.state = n.state1 AND s2.state = n.state2 " +
					"AND YEAR(s1.datetime) = ? AND YEAR(s2.datetime) = ? " +
					"AND s1.shape = ? AND s2.shape = ? " +
					"GROUP BY n.state1, n.state2";
		List<CoppiaStati> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			st.setString(3, forma);
			st.setString(4, forma);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				CoppiaStati c = new CoppiaStati(idMap.get(res.getString("st1")), idMap.get(res.getString("st2")), res.getInt("c"));
				list.add(c);
			}
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<String> listFormePerAnno(Integer anno){
		String sql = "SELECT DISTINCT shape FROM sighting WHERE YEAR(DATETIME)=? ORDER BY shape ASC";
		List<String> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			while (res.next()) 
				list.add(res.getString("shape"));
			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Sighting> listAvvistamentiPerAnnoEForma (Integer anno, String forma, Map<String, State> idMap){
		String sql = "SELECT * FROM sighting WHERE YEAR(DATETIME)=? AND shape = ?";
		List<Sighting> list = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setString(2, forma);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), idMap.get(res.getString("state").toUpperCase()), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
		return list;
	}
}

