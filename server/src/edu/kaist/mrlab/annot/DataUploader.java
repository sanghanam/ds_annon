package edu.kaist.mrlab.annot;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import kr.ac.kaist.mrlab.util.MySQLDAO;

public class DataUploader {

	private static MySQLDAO mdao;

	public void uploadDump(DBOProperty property) throws Exception {

		BufferedReader br = Files.newBufferedReader(Paths.get("data/annotation/upload/" + property));

		PreparedStatement pstmt = mdao.prepareStatement("INSERT INTO re_annon (prop, sbj, obj, stc, doc, line) VALUES (?, ?, ?, ?, ?, ?)");
		
		String input;
		while ((input = br.readLine()) != null) {
			input = input.trim();
			if (input.isEmpty()) {
				continue;
			}
			
//			System.out.println(input);
			
			
			StringTokenizer st = new StringTokenizer(input, "\t");
			
			String sbj = st.nextToken();
			String obj = st.nextToken();
			String stc = st.nextToken();
			String doc = st.nextToken();
			String line = st.nextToken();
			
			pstmt.setString(1, property.name());
			pstmt.setString(2, sbj);
			pstmt.setString(3, obj);
			pstmt.setString(4, stc);
			pstmt.setString(5, doc);
			pstmt.setString(6, line);
			
			pstmt.executeUpdate();

		}
		br.close();

		pstmt.close();

	}

	public static void main(String[] ar) throws Exception {

		DataUploader du = new DataUploader();

		mdao = new MySQLDAO(Configuration.dbAddr, Configuration.dbUser, Configuration.dbPass);

		for (DBOProperty property : DBOProperty.values()) {
			System.out.println(property + " working...");
			du.uploadDump(property);
		}
		
		mdao.close();

	}
}
