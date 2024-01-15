import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class WebClient {

    private HttpClient client;
    private String DEBUG_RESPONSE_KEY = "X-Debug-Info";

    /* instantiate web client */
    /* Read more about Builder pattern https://en.wikipedia.org/wiki/Builder_pattern*/
    public WebClient()
    {
        this.client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
    }

    /* send task (post http request) asynchronously */
    public CompletableFuture<String> sendTask(String url, byte[] requestPayload)
    {
        CompletableFuture<String> response = new CompletableFuture<>();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                                  .uri(URI.create(url))
                                  .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload))
                                  .build();

        response = client.sendAsync
                (httpRequest ,
                HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8)).
                thenApply(HttpResponse::body);

        return response;
    }

    /* send task (post http request) asynchronously with custom headers*/
    public CompletableFuture<String> sendTask(String url, byte[] requestPayload, String headers)
    {
        CompletableFuture<String> reply = new CompletableFuture<>();
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .POST(HttpRequest.BodyPublishers.ofByteArray(requestPayload));

        if (headers != null && !headers.isEmpty())
        {
            String[] headerLines = headers.split("\n");
            for (String headerLine : headerLines)
            {
                String[] headerParts = headerLine.split(":");
                String headerName = headerParts[0].trim();
                String headerValue = headerParts[1].trim();
                requestBuilder.header(headerName, headerValue);
            }
        }
        HttpRequest request = requestBuilder.build();

       reply = client.sendAsync
                   (request,
                    HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
               .thenApply(response ->
                   response.headers().map().get(DEBUG_RESPONSE_KEY).get(0) + response.body());

        return reply;

    }
}
