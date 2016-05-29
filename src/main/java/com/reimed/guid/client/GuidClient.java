/*
 * Copyright (c) 2015 ReiMed Co. to present.
 * All rights reserved.
 */
package com.reimed.guid.client;

import static com.github.wnameless.jsonapi.JsonApi.resourceDocument;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.io.BaseEncoding.base64Url;

import java.io.IOException;
import java.net.URI;
import java.security.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import lombok.NonNull;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;

import tw.guid.central.core.GuidHashCodes;
import tw.guid.central.core.GuidSet;
import tw.guid.central.core.PrefixedHashBundle;
import tw.guid.central.core.PublicGuid;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wnameless.jsonapi.ResourceDocument;
import com.github.wnameless.jsonapi.ResourceObject;
import com.github.wnameless.jsonapi.ResourcesDocument;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public final class GuidClient {

  private static final String API_ENDPOINT = "/api/v1";

  private final String apiUrl;
  private final HttpClient httpClient;
  private final ObjectMapper mapper = new ObjectMapper();

  public GuidClient(@NonNull URI centralServer, @NonNull String username,
      @NonNull String password) {
    apiUrl = centralServer.toString().replaceFirst("/$", "") + API_ENDPOINT;
    httpClient = BasicAuthSSLClient.create(username, password);
  }

  public GuidClient(@NonNull URI centralServer, PublicKey pubKey) {
    this(centralServer, base64Url().encode(pubKey.getEncoded()));
  }

  public GuidClient(@NonNull URI centralServer, @NonNull String base64PubKey) {
    apiUrl = centralServer.toString().replaceFirst("/$", "") + API_ENDPOINT;
    httpClient = BasicAuthSSLClient.create("token", base64PubKey);
  }

  public PublicGuid compute(@NonNull String prefix,
      @NonNull GuidHashCodes hashCodes) throws IOException {
    return compute(prefix, hashCodes.getHash1(), hashCodes.getHash2(),
        hashCodes.getHash3());
  }

  /**
   * Computes a GUID by given hash codes.
   * 
   * @param prefix
   *          of the GUID
   * @param hashCode1
   *          of the GUID
   * @param hashCode2
   *          of the GUID
   * @param hashCode3
   *          of the GUID
   * @return a public GUID
   * @throws IOException
   *           if request failed
   */
  public PublicGuid compute(@NonNull String prefix, @NonNull String hashCode1,
      @NonNull String hashCode2, @NonNull String hashCode3) throws IOException {
    PrefixedHashBundle hb = new PrefixedHashBundle();
    hb.setPrefix(prefix);
    hb.setHash1(hashCode1);
    hb.setHash2(hashCode2);
    hb.setHash3(hashCode3);

    ResourceDocument<PrefixedHashBundle> req = resourceDocument(hb, "guids");

    HttpPost post = new HttpPost(apiUrl + "/guids");
    post.addHeader("Content-Type", "application/json");
    post.setEntity(new StringEntity(mapper.writeValueAsString(req), UTF_8));

    HttpResponse res = httpClient.execute(post);
    String json = IOUtils.toString(res.getEntity().getContent());
    ResourceDocument<PublicGuid> guid =
        mapper.readValue(json,
            new TypeReference<ResourceDocument<PublicGuid>>() {});

    return guid.getData().getAttributes();
  }

  /**
   * Groups a collection of GUIDs into groups; each group represents an
   * identical individual.
   * 
   * @param guids
   *          a collection of GUIDs
   * 
   * @return groups of GUIDs; each group represents an identical individual
   * @throws IOException
   *           if request failed
   */
  public List<Set<PublicGuid>> group(@NonNull Collection<PublicGuid> guids)
      throws IOException {
    ResourceDocument<GuidSet<PublicGuid>> req =
        resourceDocument(new GuidSet<PublicGuid>(guids), "guids");

    HttpPost post = new HttpPost(apiUrl + "/groupings");
    post.addHeader("Content-Type", "application/json");
    post.setEntity(new StringEntity(mapper.writeValueAsString(req), UTF_8));

    HttpResponse res = httpClient.execute(post);
    String json = IOUtils.toString(res.getEntity().getContent());
    ResourcesDocument<GuidSet<PublicGuid>> sets =
        mapper.readValue(json,
            new TypeReference<ResourcesDocument<GuidSet<PublicGuid>>>() {});

    return newArrayList(Iterables.transform(sets.getData(),
        new Function<ResourceObject<GuidSet<PublicGuid>>, Set<PublicGuid>>() {

          @Override
          public Set<PublicGuid> apply(ResourceObject<GuidSet<PublicGuid>> input) {
            return input.getAttributes().getSet();
          }

        }));
  }

  /**
   * Requests a loaned GUID.
   * 
   * @param prefix
   *          of the loaned GUID
   * @return a public GUID
   * @throws IOException
   *           if request failed
   */
  public PublicGuid loan(@NonNull String prefix) throws IOException {
    HttpPost post = new HttpPost(apiUrl + "/loans/" + prefix);
    post.addHeader("Content-Type", "application/json");

    HttpResponse res = httpClient.execute(post);
    String json = IOUtils.toString(res.getEntity().getContent());
    ResourceDocument<PublicGuid> guid =
        mapper.readValue(json,
            new TypeReference<ResourceDocument<PublicGuid>>() {});

    return guid.getData().getAttributes();
  }

  /**
   * Settles a loaned GUID to make it a valid permanent GUID.
   * 
   * @param publicGuid
   *          a loaned GUID
   * @param hashCodes
   *          hash codes if the loaned GUID
   * @return a public GUID
   * @throws IOException
   *           if request failed
   */
  public PublicGuid settleLoan(PublicGuid publicGuid, GuidHashCodes hashCodes)
      throws IOException {
    ResourceDocument<GuidHashCodes> req =
        resourceDocument(hashCodes, "hashcodes");

    HttpPut put =
        new HttpPut(apiUrl + "/loans/" + publicGuid.getPrefix() + "/"
            + publicGuid.getCode());
    put.addHeader("Content-Type", "application/json");
    put.setEntity(new StringEntity(mapper.writeValueAsString(req), UTF_8));

    HttpResponse res = httpClient.execute(put);
    String json = IOUtils.toString(res.getEntity().getContent());
    ResourceDocument<PublicGuid> guid =
        mapper.readValue(json,
            new TypeReference<ResourceDocument<PublicGuid>>() {});

    return guid.getData().getAttributes();
  }

}
