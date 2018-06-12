package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.naming.spi.DirStateFactory.Result;

import it.polito.tdp.ufo.model.AnnoCount;
import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.StringPair;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<AnnoCount> getAnni() {
		
		String sql = "select distinct YEAR(datetime) as anno, count(id) as cnt " + 
				"from sighting " + 
				"where country='us' " + 
				"group by anno " + 
				"order by anno asc" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			List<AnnoCount> result = new ArrayList<>() ;
			
			while( res.next( )) {
				result.add( new AnnoCount(
						Year.of(res.getInt("anno")),
						res.getInt("cnt") ) ) ;
			}
			
			conn.close();
			return result ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<String> getStati(Year anno) {
		String sql = "SELECT DISTINCT state " + 
				"FROM sighting " + 
				"WHERE country='us' " + 
				"AND year(datetime)=? " + 
				"order by state asc" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<String> result = new ArrayList<>() ;
			
			st.setInt(1, anno.getValue());
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add(res.getString("state")) ;
			}
			
			conn.close();
			return result ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public boolean esisteArco(String stato1, String stato2, Year anno) {
		String sql = "select count(*) as c " + 
				"from sighting s1, sighting s2 " + 
				"where year(s1.datetime)=year(s2.datetime) " + 
				"and year(s1.datetime)=? " + 
				"and s1.country='us' " + 
				"and s2.country='us' " + 
				"and s1.state=? " + 
				"and s2.state=? " + 
				"and s2.datetime>s1.datetime" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno.getValue());
			st.setString(2, stato1);
			st.setString(3, stato2);

			ResultSet res = st.executeQuery() ;
			res.first() ;
			
			int risultati = res.getInt("c") ;
			
			conn.close();
			
			if(risultati==0)
				return false ;
			else
				return true ;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false ;
		}
		
	}
	
	public List<StringPair> getEdges(Year anno) {
		String sql = "select s1.state as state1 , s2.state as state2, count(*) " + 
				"from sighting s1, sighting s2 " + 
				"where year(s1.datetime)=year(s2.datetime) " + 
				"and year(s1.datetime)=? " + 
				"and s1.country='us' " + 
				"and s2.country='us' " + 
				"and s2.datetime>s1.datetime " + 
				"and s1.state<>s2.state " + 
				"group by s1.state, s2.state " ;
		
		try {
			Connection conn = DBConnect.getConnection() ;
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, anno.getValue());
			
			ResultSet res = st.executeQuery() ;
			
			List<StringPair>list = new ArrayList<>() ;
			while(res.next()) {
				list.add(new StringPair(res.getString("state1"), res.getString("state2"))) ;
			}
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
		
	}

}
