package microgram.impl.clt.rest;

import java.io.IOException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.client.ClientConfig;

import microgram.api.rest.RestMediaStorage;

public class DownloadMediaClient {

	private static final String id = "earth";

	public static void main(String[] args) throws IOException {

		String serverUrl = args.length > 0 ? args[0] : "http://localhost:9999/rest/media";

		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);

		WebTarget target = client.target(serverUrl).path(RestMediaStorage.PATH);
		System.out.println(target);
		
		
		Response r = target.path("84486F586FA514F31F07057F39B68C673B7A091F")
				.request()
				.get();

		if (r.getStatus() == Status.OK.getStatusCode() && r.hasEntity()) {
			byte[] data = r.readEntity(byte[].class);
			System.out.println("data resource length: " + data.length);
		} else
			System.err.println(r.getStatus());

	}

}
