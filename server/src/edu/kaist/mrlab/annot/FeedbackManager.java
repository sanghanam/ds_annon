package edu.kaist.mrlab.annot;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.kaist.mrlab.util.MySQLDAO;

public class FeedbackManager {
	
	public JSONObject checkUser(String name, String pass) throws SQLException, ClassNotFoundException {

		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);

		
		JSONObject extrJObj = new JSONObject();
		
		PreparedStatement pstmt = mdao.prepareStatement(
				"SELECT name, pass FROM user_table WHERE name=? AND pass=?");
		pstmt.setString(1, name);
		pstmt.setString(2, pass);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {

			String gotName = rs.getString(1);
			String gotPass = rs.getString(2);
			
			if(gotName.equals(name) && gotPass.equals(pass)){
				extrJObj.put("success", "pass");
			} else{
				extrJObj.put("success", "reject");
			}

		}

		rs.close();
		pstmt.close();
		mdao.close();
		
		if(!extrJObj.containsKey("success")){
			extrJObj.put("success", "reject");
		}

		System.out.println("login function");

		return extrJObj;
	}

	public JSONArray listAllProperties() throws SQLException, ClassNotFoundException {
		JSONArray propJArr = new JSONArray();

		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);
		PreparedStatement pstmt = mdao.prepareStatement(
				"SELECT sbj, obj, stc, id FROM annon_advanced WHERE prop=? AND answer IS NULL AND locked=0 ORDER BY rand() LIMIT 0, 10");
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			propJArr.add(rs.getString(1));
		}
		rs.close();
		pstmt.close();
		mdao.close();

		return propJArr;
	}

	public JSONArray listProperties() throws SQLException, ClassNotFoundException {
		JSONArray propJArr = new JSONArray();
		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);
		String sql = "SELECT T.prop, total, done from "
				+ "(SELECT prop, count(prop) as total FROM annon_advanced GROUP BY prop) as T " + " left outer join "
				+ " (SELECT prop, count(prop) as done FROM annon_advanced WHERE locked=1 GROUP BY prop) as D "
				+ " on T.prop = D.prop;";

		PreparedStatement pstmt = mdao.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			JSONObject prpJObj = new JSONObject();
			prpJObj.put("prop", rs.getString(1));
			prpJObj.put("total", rs.getInt(2));
			Integer done = rs.getInt(3);
			if (done == null) {
				prpJObj.put("done", 0);
			} else {
				prpJObj.put("done", done);
			}
			propJArr.add(prpJObj);
		}
		rs.close();
		pstmt.close();
		mdao.close();

		return propJArr;
	}

	public JSONObject listFeedbackResult(String p) throws SQLException, ClassNotFoundException {

		JSONArray positiveJArr = new JSONArray();
		JSONArray unknownJArr = new JSONArray();
		JSONArray negativeJArr = new JSONArray();
		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);
		PreparedStatement pstmt = mdao
				.prepareStatement("SELECT idx, extr, label FROM data_re WHERE locked=TRUE AND rel=? ORDER BY grp");
		pstmt.setString(1, p);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			Long idx = rs.getLong(1);
			String extr = rs.getString(2);
			extr = extr.replace("<SBJ>", "<b>");
			extr = extr.replace("<OBJ>", "<b>");
			extr = extr.replace("</SBJ>", "</b><sub>sbj</sub>");
			extr = extr.replace("</OBJ>", "</b><sub>obj</sub>");
			Integer feed = rs.getInt(3);

			JSONObject tmpExtrJObj = new JSONObject();
			tmpExtrJObj.put("idx", idx);
			tmpExtrJObj.put("extr", extr);
			tmpExtrJObj.put("feed", feed.toString());

			if (tmpExtrJObj.get("feed").equals("1")) {
				positiveJArr.add(tmpExtrJObj);
			} else if (tmpExtrJObj.get("feed").equals("0")) {
				unknownJArr.add(tmpExtrJObj);
			} else {
				negativeJArr.add(tmpExtrJObj);
			}
		}
		rs.close();
		pstmt.close();
		mdao.close();

		JSONArray extrJArr = new JSONArray();
		extrJArr.addAll(unknownJArr);
		extrJArr.addAll(positiveJArr);
		extrJArr.addAll(negativeJArr);
		JSONObject extrJObj = new JSONObject();
		extrJObj.put("p", p);
		extrJObj.put("samp", extrJArr);

		return extrJObj;
	}

	@SuppressWarnings("unchecked")
	public JSONObject sampleExtracts(String p) throws SQLException, ClassNotFoundException {

		JSONArray extrJArr = new JSONArray();
		ArrayList<Integer> idList = new ArrayList<Integer>();

		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);

		PreparedStatement pstmt = mdao.prepareStatement(
				"SELECT sbj, obj, stc, id FROM annon_advanced WHERE prop=? AND answer IS NULL AND locked=0 ORDER BY rand() LIMIT 0, 10");
		pstmt.setString(1, p);
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {

			String sbj = rs.getString(1).replaceAll("http://ko.dbpedia.org/resource/", "");
			String obj = rs.getString(2).replaceAll("http://ko.dbpedia.org/resource/", "");
			String stc = rs.getString(3);
			Integer id = rs.getInt(4);
			idList.add(id);

			// System.out.println(sbj);
			// System.out.println(obj);
			// System.out.println(stc);

			JSONObject tmpExtrJObj = new JSONObject();
			tmpExtrJObj.put("triple", "(" + sbj + ", " + p + ", " + obj + ")");
			tmpExtrJObj.put("stc", stc);
			tmpExtrJObj.put("id", id);

			extrJArr.add(tmpExtrJObj);

		}

		pstmt = mdao.prepareStatement("UPDATE annon_advanced SET locked=1 WHERE id=?");
		for (Integer id : idList) {
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		}

		rs.close();
		pstmt.close();
		mdao.close();
		JSONObject extrJObj = new JSONObject();
		extrJObj.put("p", p);
		extrJObj.put("samp", extrJArr);

		System.out.println("sample response done.");

		return extrJObj;
	}

	public void feedbackExtracts(String userName, JSONArray extrJArr) throws SQLException, ClassNotFoundException {
		System.out.println("Get feeds from user " + userName);
		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);
		PreparedStatement pstmt = mdao.prepareStatement(
				"UPDATE annon_advanced SET answer=?, name=?, evi_str=?, evi_start_offset=?, evi_end_offset=? WHERE id=?");
		for (int i = 0; i < extrJArr.size(); i++) {
			JSONObject extrJObj = (JSONObject) extrJArr.get(i);
			Long feed = Long.parseLong(extrJObj.get("feed").toString());
			Long id = Long.parseLong(extrJObj.get("id").toString());
			String eviStr = extrJObj.get("evi_str").toString();
			Long eviStartOffset = Long.parseLong(extrJObj.get("evi_start_offset").toString());
			Long eviEndOffset = Long.parseLong(extrJObj.get("evi_end_offset").toString());
			pstmt.setInt(1, feed.intValue());
			pstmt.setString(2, userName);
			pstmt.setString(3, eviStr);
			pstmt.setLong(4, eviStartOffset);
			pstmt.setLong(5, eviEndOffset);
			pstmt.setLong(6, id);
			pstmt.executeUpdate();
		}
		pstmt.close();
		mdao.close();
	}

	public void unlockExtracts(JSONArray extrJArr) throws SQLException, ClassNotFoundException {
		System.out.println("cancel command");
		MySQLDAO mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);
		PreparedStatement pstmt = mdao.prepareStatement("UPDATE annon_advanced SET locked=0 WHERE id=?");
		for (int i = 0; i < extrJArr.size(); i++) {
			JSONObject extrJObj = (JSONObject) extrJArr.get(i);
			Long id = (Long) extrJObj.get("id");
			pstmt.setLong(1, id);
			pstmt.executeUpdate();
		}
		pstmt.close();
		mdao.close();
	}

}
