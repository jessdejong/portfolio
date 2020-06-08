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

import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/account")
public class LoginLogoutServlet extends HttpServlet {
  private UserService userService = UserServiceFactory.getUserService();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");

    // Build json in the form {"url": <url>, "email": <email>}
    String json = "{";
    if (userService.isUserLoggedIn()) {
      json += "\"url\":" + "\"" + userService.createLogoutURL(/*redirectUrlPostLogOut=*/ "/") + "\",";
      json += "\"email\":" + "\"" + userService.getCurrentUser().getEmail() + "\""; 
    }
    else {
      json += "\"url\":" + "\"" + userService.createLoginURL(/*urlToRedirectToAfterUserLogsIn=*/ "/") + "\",";
      json += "\"email\":null";
    }
    json += "}";
    response.getWriter().println(json);
  }
}
