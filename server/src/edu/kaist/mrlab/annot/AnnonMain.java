package edu.kaist.mrlab.annot;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;

import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;

import com.sun.jersey.api.container.grizzly2.GrizzlyServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;

public class AnnonMain {

	private static URI getBaseURI() {

		InetAddress Address;
		String IP = null;
		try {
			Address = InetAddress.getLocalHost();
			IP = Address.getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return UriBuilder.fromUri("http://" + IP).port(18503).build();
	}

	public static final URI BASE_URI = getBaseURI();
	protected static HttpServer startServer() throws IOException {
		System.out.println("Starting grizzly...");
		ResourceConfig rc = new PackagesResourceConfig("edu.kaist.mrlab.annot");
		return GrizzlyServerFactory.createHttpServer(BASE_URI, rc);
	}

	public static void main(String[] args) throws IOException {
		HttpServer httpServer = startServer();

		System.out.println(String.format(
				"Jersey app started with WADL available at "
						+ "%s application.wadl\nTry out %s/pl3/_command_ \nHit enter to stop it...",
				BASE_URI, BASE_URI));

		while (true) {

			int a = System.in.read();
			if(a == 120)
				break;
		}

		httpServer.stop();
	}
}
