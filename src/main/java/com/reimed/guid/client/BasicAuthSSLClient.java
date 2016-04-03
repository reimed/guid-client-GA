/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package com.reimed.guid.client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BasicAuthSSLClient {

  /**
   * Returns a {@link HttpClient} which allows SSL and basic authentication.
   * 
   * @param username
   *          of the basic authentication
   * @param password
   *          of the basic authentication
   * @return a {@link HttpClient} which allows SSL and basic authentication
   */
  @SneakyThrows({ NoSuchAlgorithmException.class, KeyStoreException.class,
      KeyManagementException.class })
  public static HttpClient create(@NonNull String username,
      @NonNull String password) {
    SSLContextBuilder builder = new SSLContextBuilder();
    builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());

    SSLContext ctx = builder.build();
    SSLConnectionSocketFactory sslsf =
        new SSLConnectionSocketFactory(ctx, new HostnameVerifier() {

          @Override
          public boolean verify(String hostname, SSLSession session) {
            return true;
          }

        });

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(username, password));

    return HttpClients.custom()
        .setDefaultCredentialsProvider(credentialsProvider)
        .setSSLSocketFactory(sslsf).build();
  }

}
