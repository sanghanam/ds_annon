package edu.kaist.mrlab.annot;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("pl3")
public class PathFinder {
	
	@Path("check_user")
	@POST
	@Produces("application/json; charset=utf-8")
	public Response checkUser(String dataJStr) throws ParseException {
		System.out.println("user login");
		JSONObject dataJObj = (JSONObject) new JSONParser().parse(dataJStr);
		String name = dataJObj.get("name").toString();
		String pass = dataJObj.get("pass").toString();
		try {
			JSONObject propJArr = new FeedbackManager().checkUser(name, pass);
			return Response.ok().entity(propJArr.toJSONString())
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Path("list_prop")
	@POST
	@Produces("application/json; charset=utf-8")
	public Response listProperties() {
		try {
			JSONArray propJArr = new FeedbackManager().listProperties();
			return Response.ok().entity(propJArr.toJSONString())
					.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

//	@Path("list_all_prop")
//	@POST
//	@Produces("application/json; charset=utf-8")
//	public Response listAllProperties() throws SQLException, ClassNotFoundException {
//		JSONArray propJArr = new FeedbackManager().listAllProperties();
//		return Response.ok().entity(propJArr.toJSONString())
//				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
//				.header("Access-Control-Allow-Origin", "*")
//				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
//	}

//	@Path("get_res")
//	@POST
//	@Produces("application/json; charset=utf-8")
//	public Response listPseudoFeedbackResult(String dataJStr)
//			throws ParseException, SQLException, ClassNotFoundException {
//		JSONObject dataJObj = (JSONObject) new JSONParser().parse(dataJStr);
//		JSONObject extrJObj = new FeedbackManager().listFeedbackResult((String) dataJObj.get("p"));
//		return Response.ok().entity(extrJObj.toJSONString())
//				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
//				.header("Access-Control-Allow-Origin", "*")
//				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
//	}

	@Path("samp_extr")
	@POST
	@Produces("application/json; charset=utf-8")
	public Response listPseudoFeedback(String dataJStr) throws ParseException, SQLException, ClassNotFoundException {
		System.out.println(dataJStr.toString());
		JSONObject dataJObj = (JSONObject) new JSONParser().parse(dataJStr);
		JSONObject extrJObj = new FeedbackManager().sampleExtracts((String) dataJObj.get("p"));
		return Response.ok().entity(extrJObj.toJSONString())
				.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
	}

	@Path("update_feedback")
	@POST
	@Consumes("application/json; charset=utf-8")
	public Response updatePseudoFeedback(String dataJStr) {
		try {
			System.out.println(dataJStr);
			JSONObject dataJObj = (JSONObject) new JSONParser().parse(dataJStr);
			String userName = (String) dataJObj.get("userName");
			JSONArray extrJArr = (JSONArray) dataJObj.get("extrArr");
			new FeedbackManager().feedbackExtracts(userName, extrJArr);
			return Response.ok().entity("{\"result\":\"aa\"}").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Path("unlock_extr")
	@POST
	@Consumes("application/json; charset=utf-8")
	public Response unlockExtractions(String dataJStr) {
		try {
			System.out.println(dataJStr);
			JSONArray extrJArr = (JSONArray) new JSONParser().parse(dataJStr);
			new FeedbackManager().unlockExtracts(extrJArr);
			return Response.ok().entity("{\"result\":\"aa\"}").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
					.header("Access-Control-Allow-Origin", "*")
					.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@OPTIONS
	@Consumes("application/x-www-form-urlencoded; charset=utf-8")
	@Produces("text/plain; charset=utf-8")
	public Response getOptions(@FormParam("text") String input) throws Exception {

		return Response.ok().header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS")
				.header("Access-Control-Allow-Origin", "*")
				.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept").build();

	}

}
