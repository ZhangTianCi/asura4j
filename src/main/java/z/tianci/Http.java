package z.tianci;

import java.io.*;
import java.net.URI;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.net.URISyntaxException;

import org.apache.http.entity.*;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.mime.FormBodyPart;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.impl.client.HttpClientBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.entity.mime.MultipartEntityBuilder;

/**
 * @author ZhangTianci
 */
public class Http {

    private URI uri;
    private Method method;
    private HttpClient client;
    HttpEntity requestEntity;
    private HashMap<java.lang.String, java.lang.String> heads = new HashMap<java.lang.String, java.lang.String>();
    private HashMap<java.lang.String, java.lang.String> params = new HashMap<java.lang.String, java.lang.String>();
    private ObjectMapper JSON = new ObjectMapper();

    private Http() {
        client = HttpClientBuilder.create().build();
        heads.put("user-agent", "HttpUtil/0.0.1");
    }

    public Http(Method method) {
        this();
        this.method = method;
    }

    public Http(Method method, URI uri) {
        this(method);
        this.setUri(uri);
    }

    public Http(Method method, java.lang.String url) throws URISyntaxException {
        this(method);
        this.setUri(url);
    }

    public Http addParams(java.lang.String name, java.lang.String value) {
        this.params.put(name, value);
        return this;
    }

    public Http addHead(java.lang.String name, java.lang.String value) {
        this.heads.put(name, value);
        return this;
    }

    public Http addHeads(Map<java.lang.String, java.lang.String> heads) {
        heads.forEach((k, v) -> {
            this.heads.put(k, v);
        });
        return this;
    }

    public Http setUri(java.lang.String url) throws URISyntaxException {
        this.uri = new URI(url);
        return this;
    }

    public Http setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    public InputStream send() throws IOException, URISyntaxException {
        HttpRequestBase httpRequestBase = getHttpMethod();
        HttpResponse httpResponse = client.execute(httpRequestBase);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode < 200 || statusCode >= 300) {
            throw new IOException(java.lang.String.valueOf(statusCode),
                    new Throwable("\n\n" + new BufferedReader(
                            new InputStreamReader(
                                    httpResponse.getEntity().getContent()
                            )).lines().collect(Collectors.joining("\n")) + "\n"));
        } else {
            return httpResponse.getEntity().getContent();
        }
    }

    public java.lang.String send(java.lang.String encoding) throws IOException, URISyntaxException {
        final char[] buffer = new char[1024];
        final StringBuilder result = new StringBuilder();
        Reader inputStreamReader = new InputStreamReader(send(), encoding);
        int readSize;
        while ((readSize = inputStreamReader.read(buffer, 0, buffer.length)) > 0) {
            result.append(buffer, 0, readSize);
        }
        return result.toString();
    }

    public <T> T send(Class<T> tClass) throws IOException, URISyntaxException {
        return JSON.readValue(send("UTF-8"), tClass);
    }

    public <T> T send(java.lang.String encoding, Class<T> tClass) throws IOException, URISyntaxException {
        return JSON.readValue(send(encoding), tClass);
    }

    public void setBody(java.lang.String content, ContentType contentType) {
        requestEntity = new StringEntity(content, contentType);
    }

    public void setBody(Object content) throws JsonProcessingException {
        setBody(JSON.writeValueAsString(content), ContentType.APPLICATION_JSON);
    }

    public void setBody(byte[] content, ContentType contentType) {
        requestEntity = new ByteArrayEntity(content, contentType);
    }

    public void setBody(File content, ContentType contentType) {
        requestEntity = new FileEntity(content, contentType);
    }

    public void setBody(InputStream content, ContentType contentType, long contentLength) {
        requestEntity = new InputStreamEntity(content, contentLength, contentType);
    }

    public void setBody(FormBodyPart... content) {
        MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();
        for (FormBodyPart part : content) {
            multipartEntityBuilder.addPart(part);
        }
        requestEntity = multipartEntityBuilder.build();
    }

    private HttpRequestBase getHttpMethod() throws URISyntaxException {
        HttpRequestBase httpRequestBase;
        switch (method) {
            case GET:
                httpRequestBase = new HttpGet();
                break;
            case HEAD:
                httpRequestBase = new HttpHead();
                break;
            case POST:
                httpRequestBase = new HttpPost();
                if (requestEntity != null) {
                    ((HttpPost) httpRequestBase).setEntity(requestEntity);
                }
                break;
            case OPTIONS:
                httpRequestBase = new HttpOptions();
                break;
            case PUT:
                httpRequestBase = new HttpPut();
                if (requestEntity != null) {
                    ((HttpPut) httpRequestBase).setEntity(requestEntity);
                }
                break;
            case DELETE:
                httpRequestBase = new HttpDelete();
                break;
            case TRACE:
                httpRequestBase = new HttpTrace();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + method);
        }
        List<java.lang.String> queries = new ArrayList<>();
        if (uri.getQuery() != null && !"".equals(uri.getQuery().trim())) {
            queries.add(uri.getQuery().trim());
        }
        params.entrySet().forEach(t -> {
            queries.add(java.lang.String.format("%1$s=%2$s", t.getKey(), t.getValue()));
        });
        // 设置请求路径
        httpRequestBase.setURI(new URI(uri.getScheme(),
                uri.getAuthority(),
                uri.getPath(),
                queries.size() > 0 ? queries.stream().collect(Collectors.joining("&")) : null,
                uri.getFragment()));
        // 设置请求头
        heads.entrySet().forEach(t -> {
            httpRequestBase.addHeader(t.getKey(), t.getValue());
        });
        return httpRequestBase;
    }

    public enum Method {GET, HEAD, POST, OPTIONS, PUT, DELETE, TRACE}
}