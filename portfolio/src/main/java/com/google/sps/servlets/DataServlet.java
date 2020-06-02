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
  private Gson gson;
  private List<String> listOfComments = new ArrayList<String>();

  @Override
  public void init() {
    gson = new Gson();
    /*
    listOfComments = new ArrayList<String>();
    listOfComments.add("THIS IS THE BEST WEBSITE I HAVE EVER SEEN.");
    listOfComments.add("Wow, the UI and the functionality of this website piece together seamlessly. 10/10.");
    listOfComments.add("B.W.E.D. (Best Website Ever Duh)");
    */
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Send json as the response
    String json = convertToJsonUsingGson(listOfComments);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = getParameter(request, "text-input", "");
    listOfComments.add(comment);

    // Respond with result of request
    response.setContentType("type/html;");
    response.getWriter().println(comment);
    
    // Redirect back to the home page
    response.sendRedirect("/index.html");
  }

  /* Returns the request parameter, or the default value if not specified */
  private String getParameter(HttpServletRequest request, String name, String defaultValue) {
    String value = request.getParameter(name);
    return value == null ? defaultValue : value;
  }

  /* Use Gson Library to convert list of comments to Json */
  private String convertToJsonUsingGson(List<String> list) {
    return gson.toJson(list);
  }
}
