package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.Storage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ResumeServlet extends HttpServlet {

    private Storage storage;

    @Override
    public void init() throws ServletException {
        storage = Config.get().getSqlStorage();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete" -> {
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            }
            case "view" -> r = storage.get(uuid);
            case "edit" -> {
                r = storage.get(uuid);
                for (SectionType st : SectionType.values()) {
                    switch (st) {
                        case PERSONAL, OBJECTIVE -> {
                            TextSection textSection = (TextSection) r.getSection(st);
                            if (textSection == null) {
                                r.addSection(st, new TextSection(""));
                            }
                        }
                        case ACHIEVEMENT, QUALIFICATIONS -> {
                            ListSection listSection = (ListSection) r.getSection(st);
                            List<String> emptyPoints = new ArrayList<>();
                            if (listSection == null) {
                                r.addSection(st, new ListSection(emptyPoints));
                            }
                        }
                        case EDUCATION, EXPERIENCE -> {
                            OrganizationSection section = (OrganizationSection) r.getSection(st);
                            List<Organization> emptyOrganizations = new ArrayList<>();
                            List<Paragraph> emptyParagraphs = new ArrayList<>();
                            emptyParagraphs.add(new Paragraph());
                            emptyOrganizations.add(new Organization(null, emptyParagraphs));
                            if (section != null) {
                                for (Organization org : section.getOrganizations()) {
                                    List<Paragraph> paragraphs = new ArrayList<>();
                                    paragraphs.add(new Paragraph());
                                    paragraphs.addAll(org.getParagraphs());
                                    emptyOrganizations.add(new Organization(org.getHeader(), paragraphs));
                                }
                            }
                            r.setSection(st, new OrganizationSection(emptyOrganizations));
                        }
                    }
                }
            }
            default -> throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactType type : ContactType.values()) {
            String value = request.getParameter(type.name());
            if (!StringUtil.checkEmpty(value)) {
                r.addContact(type, value);
            } else {
                r.getContacts().remove(type);
            }
        }

        for (SectionType st : SectionType.values()) {
            String value = request.getParameter(st.name());
            String[] values = request.getParameterValues(st.name());
            String replacedValue = null;
            if (value != null) {
                replacedValue = value.replaceAll("\\r\\n", "§")
                        .replaceAll("§+", "\r\n");
            }
            if (values == null || (StringUtil.checkEmpty(value) && values.length < 2)) {
                r.getSections().remove(st);
            } else {
                switch (st) {
                    case PERSONAL, OBJECTIVE -> r.addSection(st, new TextSection(replacedValue));
                    case ACHIEVEMENT, QUALIFICATIONS ->
                            r.addSection(st, new ListSection(List.of(replacedValue.split("\\r\\n"))));
                    case EDUCATION, EXPERIENCE -> {
                        List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(st.name() + "_URL");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!StringUtil.checkEmpty(name)) {
                                List<Paragraph> positions = new ArrayList<>();
                                String prefix = st.name() + i;
                                String[] startDates = request.getParameterValues(prefix + "_SD");
                                String[] endDates = request.getParameterValues(prefix + "_ED");
                                String[] titles = request.getParameterValues(prefix + "_TITLE");
                                String[] descriptions = request.getParameterValues(prefix + "_DESCRIPTION");
                                if (titles != null) {
                                    for (int j = 0; j < titles.length; j++) {
                                        if (!StringUtil.checkEmpty(titles[j])) {
                                            positions.add(new Paragraph(DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), titles[j], descriptions[j]));
                                        }
                                    }
                                }
                                orgs.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        r.setSection(st, new OrganizationSection(orgs));
                    }
                }
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }
}
