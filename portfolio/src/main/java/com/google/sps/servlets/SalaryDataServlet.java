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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Returns major-to-salary data as a JSON object, e.g. {"Computer Science": 52000, "Computer Engineering": 55000}. */
@WebServlet("/salary-data")
public class SalaryDataServlet extends HttpServlet {
  private final Gson gson = new Gson();
  private final Map<String, Integer> majorSalaryData = new HashMap<>();

  @Override
  public void init() {
    try (Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/SalaryByMajor.csv"))) {
      scanner.nextLine();
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] cells = line.split(",");

        // Computer Science, 55000 => "Computer Science": 55000
        majorSalaryData.put(cells[0], Integer.valueOf(cells[1]));
      }
    }
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    response.getWriter().println(gson.toJson(majorSalaryData));
  }
}
