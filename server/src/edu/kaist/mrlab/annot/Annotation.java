package edu.kaist.mrlab.annot;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Annotation {

	public void makeUploadData(DBOProperty property) {
		
		try{
			
			BufferedReader br = Files.newBufferedReader(Paths.get("data/sopair/stc-elu-out/" + property));
			BufferedWriter bw = Files.newBufferedWriter(Paths.get("data/annotation/upload/" + property));

			String input = null;
			while ((input = br.readLine()) != null) {
				if (input.length() != 0) {

					StringTokenizer st = new StringTokenizer(input, "\t");
					
					String subject = st.nextToken();
					String object = st.nextToken();
					String sentence = st.nextToken();
					String parsed = st.nextToken();
					String docid = st.nextToken();
					String stcid = st.nextToken();

					JSONParser jp = new JSONParser();
					JSONObject parsedObject = (JSONObject) jp.parse(parsed);
					JSONArray eluArray = (JSONArray) parsedObject.get("ELU");

					int sbjStartOffset = -1;
					int sbjEndOffset = -1;
					int objStartOffset = -1;
					int objEndOffset = -1;

					Iterator<?> it = eluArray.iterator();

					String newSentence = "";

					while (it.hasNext()) {

						JSONObject entity = (JSONObject) it.next();

						String uri = String.valueOf(entity.get("uri"));

						if (subject.equals(uri)) {

							sbjStartOffset = Integer.parseInt(entity.get("start_offset").toString());
							sbjEndOffset = Integer.parseInt(entity.get("end_offset").toString());

						}

						if (object.equals(uri)) {

							objStartOffset = Integer.parseInt(entity.get("start_offset").toString());
							objEndOffset = Integer.parseInt(entity.get("end_offset").toString());

						}

					}
					
//					System.out.println(sbjStartOffset + " " + sbjEndOffset + " " + objStartOffset + " " + objEndOffset + " " + sentence.length());

					if (sbjStartOffset < 0 || objStartOffset < 0 || sbjEndOffset > sentence.length()
							|| objEndOffset > sentence.length()) {
						continue;
					}

					if (sbjStartOffset < objStartOffset) {

						String t1 = sentence.substring(0, sbjStartOffset);
						String t2 = sentence.substring(sbjStartOffset, sbjEndOffset);
						String t3 = sentence.substring(sbjEndOffset, objStartOffset);
						String t4 = sentence.substring(objStartOffset, objEndOffset);
						String t5 = sentence.substring(objEndOffset, sentence.length());

						newSentence = t1 + "(sbj)" + t2 + "(/sbj)" + t3 + "(obj)" + t4 + "(/obj)" + t5;

					}

					if (objStartOffset < sbjStartOffset) {

						String t1 = sentence.substring(0, objStartOffset);
						String t2 = sentence.substring(objStartOffset, objEndOffset);
						String t3 = sentence.substring(objEndOffset, sbjStartOffset);
						String t4 = sentence.substring(sbjStartOffset, sbjEndOffset);
						String t5 = sentence.substring(sbjEndOffset, sentence.length());

						newSentence = t1 + "(obj)" + t2 + "(/obj)" + t3 + "(sbj)" + t4 + "(/sbj)" + t5;

					}
					
					if(sbjStartOffset == objStartOffset && sbjEndOffset == objEndOffset){
						continue;
					}

//					System.out.println(newSentence);
					bw.write(subject + "\t" + object + "\t" + newSentence + "\t" + docid + "\t" + stcid + "\n");

				}
			}
			
			bw.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		

	}

	public static void main(String[] ar) {

		Annotation an = new Annotation();

		for (DBOProperty property : DBOProperty.values()) {
			System.out.println(property + " working...");
			an.makeUploadData(property);
		}

	}
}
