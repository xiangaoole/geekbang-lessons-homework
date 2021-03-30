package org.geektimes.rest.demo;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;
import java.net.URI;

public class RestClientDemo {

    public static void main(String[] args) {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://127.0.0.1:8080/hello/world")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        String content = response.readEntity(String.class);

        System.out.println(content);

    }

    private static void testGet() {
        Client client = ClientBuilder.newClient();
        Response response = client
                .target("http://127.0.0.1:8080/hello/world")      // WebTarget
                .request() // Invocation.Builder
                .get();                                     //  Response

        String content = response.readEntity(String.class);

        System.out.println(content);
    }


    /**
     * test the /RegisterServlet page
     *
     * @return
     */
    private static void testPost() {
        URI uri = URI.create("http://localhost:8080/RegisterServlet");
        // META-INF/services/javax.ws.rs.client.ClientBuilder for ClientBuilder
        // META-INF/services/javax.ws.rs.ext.RuntimeDelegate for UriBuilder
        Client client = ClientBuilder.newClient();

        Response response = client.target(uri) // WebTarget
                .request() // Invocation Builder
                .post(buildUserInfoEntity());
        String entity = response.readEntity(String.class);
        System.out.println(entity);
    }

    private static Entity<Form> buildUserInfoEntity() {
        Form form = new Form();
        // the user info
        form.param("username", "Harold Gao");
        form.param("password", "123456");
        form.param("phone_number", "12345678901");
        form.param("email", "haroldgao@github.com");
        return Entity.form(form);
    }
}
