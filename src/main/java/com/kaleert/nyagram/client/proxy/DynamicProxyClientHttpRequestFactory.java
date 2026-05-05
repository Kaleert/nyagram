package com.kaleert.nyagram.client.proxy;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Фабрика HTTP-соединений с поддержкой динамических прокси на каждый запрос.
 *
 * @since 1.1.5
 */
public class DynamicProxyClientHttpRequestFactory extends SimpleClientHttpRequestFactory {

    @Override
    protected HttpURLConnection openConnection(URL url, Proxy proxy) throws IOException {
        NyagramProxy currentProxy = ProxyContextHolder.get();

        if (currentProxy == null || currentProxy.proxy() == null || currentProxy.proxy() == Proxy.NO_PROXY) {
            return super.openConnection(url, proxy);
        }

        if (currentProxy.username() != null && currentProxy.password() != null) {
            ProxyAuthenticator.setCredentials(currentProxy.username(), currentProxy.password());
        } else {
            ProxyAuthenticator.clear();
        }

        return super.openConnection(url, currentProxy.proxy());
    }

    @Override
    protected void prepareConnection(HttpURLConnection connection, String httpMethod) throws IOException {
        super.prepareConnection(connection, httpMethod);
        
        NyagramProxy currentProxy = ProxyContextHolder.get();
        
        if (currentProxy != null && currentProxy.proxy().type() == Proxy.Type.HTTP
                && currentProxy.username() != null && currentProxy.password() != null) {

            String auth = currentProxy.username() + ":" + currentProxy.password();
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
            connection.setRequestProperty("Proxy-Authorization", "Basic " + encodedAuth);
        }
    }
}