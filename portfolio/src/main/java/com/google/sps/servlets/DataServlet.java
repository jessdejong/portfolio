// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  private static final String COMMENT_ENTITY = "Comment";
  private static final String COMMENT_CONTENT_PROPERTY = "content";
  private static final String COMMENT_TIMESTAMP_PROPERTY = "timestamp";
  private static final String NUM_COMMENTS_PARAMETER = "num-comments";

  private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private Gson gson = new Gson();
  private Query commentsQuery = new Query(COMMENT_ENTITY).addSort("timestamp", SortDirection.DESCENDING);

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // get number of comments to display
    int numComments = getNumCommentsParameter(request);
    if (numComments == -1) {
      response.setContentType("text/html");
      response.getWriter().println("Please enter a non-negative integer.");
      return;
    }

    // Get comments from datastore, and add numComments number of comments to comments list
    PreparedQuery results = datastore.prepare(commentsQuery);
    List<String> comments = new ArrayList<>();
    int numCommentsAdded = 0;
    for (Entity entity : results.asIterable()) {
      if (numCommentsAdded++ >= numComments) {
        break;
      }
      comments.add((String) entity.getProperty(COMMENT_CONTENT_PROPERTY));
    }

    // Send json as the response
    String json = convertToJsonUsingGson(comments);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getStringParameter(request, "text-input", "");
    long timestamp = System.currentTimeMillis();
    // Add comment entity to Datastore
    Entity commentEntity = new Entity(COMMENT_ENTITY);
    commentEntity.setProperty(COMMENT_CONTENT_PROPERTY, comment);
    commentEntity.setProperty(COMMENT_TIMESTAMP_PROPERTY, timestamp);
    datastore.put(commentEntity);
    // Redirect to about me page on post
    response.sendRedirect("/index.html");
  }

  /** Returns the request parameter (for Strings), or the default value if not specified */
  private String getStringParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return value == null ? defaultValue : value;
  }

  /** Get number of comments to display from request */ 
  private int getNumCommentsParameter(HttpServletRequest request) {
    String numCommentsString = request.getParameter(NUM_COMMENTS_PARAMETER);

    // Convert string value to int
    int numComments;
    try {
      numComments = Integer.parseInt(numCommentsString);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + numCommentsString);
      return -1;
    }

    if (numComments < 0) {
      System.err.println("Value is out of range: " + numCommentsString);
      return -1;
    }
    return numComments;
  }

  /** Use Gson Library to convert list of comments to Json */
  private String convertToJsonUsingGson(List<String> list) {
    return gson.toJson(list);
  }
}
