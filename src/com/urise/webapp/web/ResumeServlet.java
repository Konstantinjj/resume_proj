package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.Storage;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");
//        String name = request.getParameter("name");
//        response.getWriter().write(name == null ? "Hello Resumes" : "Hello " + name + "!");
        StringBuilder sb = new StringBuilder();
        sb.append("<table border=\"1\">");

        sb.append("<td>");
        sb.append("uuid");
        sb.append("</font>");
        sb.append("</td>");

        sb.append("<td>");
        sb.append("full_name");
        sb.append("</font>");
        sb.append("</td>");

        sb.append("<tbody>");
        for (Resume r : storage.getAllSorted()) {
            sb.append("<tr>");

            sb.append("<td>");
            sb.append(r.getUuid());
            sb.append("</td>");

            sb.append("<td>");
            sb.append(r.getFullName());
            sb.append("</td>");

            sb.append("</tr>");
        }
        sb.append("/<tbody>");
        sb.append("</table>");

        OutputStream outStream = response.getOutputStream();
        outStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
