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
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
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
  private static final String COMMENT_EMAIL_PROPERTY = "user-email";
  private static final String COMMENT_TIMESTAMP_PROPERTY = "timestamp";
  private static final String DEFAULT_STRING = "";
  private static final String NUM_COMMENTS_PARAMETER = "num-comments";
  private static final String TEXT_INPUT_PARAMETER = "text-input";

  private final DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
  private final Gson gson = new Gson();
  private final Query commentsQuery = new Query(COMMENT_ENTITY).addSort("timestamp", SortDirection.DESCENDING);
  private final UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Get number of comments to display
    int numCommentsRequested = getIntegerParameter(request, NUM_COMMENTS_PARAMETER, Integer.MIN_VALUE);
    if (numCommentsRequested < 0) {
      response.setContentType("text/html");
      response.getWriter().println("Please enter a non-negative integer.");
      return;
    }

    // Get comments from datastore, and add numCommentsRequested number of comments to comments list
    PreparedQuery results = datastore.prepare(commentsQuery);
    List<Comment> comments = new ArrayList<>();
    int numCommentsAdded = 0;
    if (numCommentsRequested != 0) {
      for (Entity entity : results.asIterable()) {
        comments.add(new Comment((String) entity.getProperty(COMMENT_CONTENT_PROPERTY),
            (String) entity.getProperty(COMMENT_EMAIL_PROPERTY)));
        numCommentsAdded++;
        if (numCommentsAdded >= numCommentsRequested) {
          break;
        }
      }
    }

    // Send json as the response
    String json = convertToJsonUsingGson(comments);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    if (userService.isUserLoggedIn()) {
      // Add comment entity to Datastore
      String comment = getStringParameter(request, TEXT_INPUT_PARAMETER, DEFAULT_STRING);
      String userEmail = userService.getCurrentUser().getEmail();
      Entity commentEntity = new Entity(COMMENT_ENTITY);
      commentEntity.setProperty(COMMENT_CONTENT_PROPERTY, comment);
      commentEntity.setProperty(COMMENT_TIMESTAMP_PROPERTY, System.currentTimeMillis());
      commentEntity.setProperty(COMMENT_EMAIL_PROPERTY, userEmail);
      datastore.put(commentEntity);
    }
    // Redirect to about me page on post
    response.sendRedirect("/index.html");
  }

  /** Returns the request parameter (for Strings), or the default value if not specified */
  private String getStringParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return value == null ? defaultValue : value;
  }

  /** Returns the request parameter (for Integers), or the default value if not specified */
  private int getIntegerParameter(HttpServletRequest request, String name, int defaultValue) {
    String value = getStringParameter(request, NUM_COMMENTS_PARAMETER, DEFAULT_STRING);

    // Convert string value to integer
    try {
      return value.isEmpty() ? defaultValue : Integer.parseInt(value);
    } catch (NumberFormatException e) {
      System.err.println("Could not convert to int: " + value);
      return Integer.MIN_VALUE;
    }
  }

  /** Use Gson Library to convert list of comments to Json */
  private <GenericType> String convertToJsonUsingGson(List<GenericType> list) {
    return gson.toJson(list);
  }
}
