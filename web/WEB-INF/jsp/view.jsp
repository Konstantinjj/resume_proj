<%@ page import="com.urise.webapp.model.Resume" %>
<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.ContactType" %>
<%@ page import="com.urise.webapp.model.SectionType" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactType, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>
    <p>
        <c:forEach var="sectionType" items="<%=SectionType.values()%>">
            <jsp:useBean id="sectionType"
                         type="com.urise.webapp.model.SectionType"/>

            <%--    <h3>${sectionType.title}</h3>--%>
    <p>
        <c:choose>
            <c:when test="${sectionType == 'PERSONAL' || sectionType == 'OBJECTIVE'}">${resume.getSection(sectionType)}</c:when>

        <c:when test="${sectionType == 'ACHIEVEMENT' || sectionType == 'QUALIFICATIONS'}">
            <c:set var="listSection" value="<%=resume.getSection(sectionType)%>"/>
            <jsp:useBean id="listSection"
                         class="com.urise.webapp.model.ListSection"/>
        <c:choose>
        <c:when test="${listSection.points != null}">
    <h3>${sectionType.title}</h3>
    <c:forEach var="point" items="${listSection.points}">
        <li>${point} </li>
    </c:forEach>
    </c:when>
    </c:choose>
    </c:when>

    <c:when test="${sectionType == 'EXPERIENCE' || sectionType == 'EDUCATION'}">
        <c:set var="organizationSection" value="<%=resume.getSection(sectionType)%>"/>
        <jsp:useBean id="organizationSection"
                     class="com.urise.webapp.model.OrganizationSection"/>

        <c:choose>
            <c:when test="${organizationSection.organizations != null}">
                <h3>${sectionType.title}</h3>
                <c:forEach var="org" items="${organizationSection.organizations}">
                    <a href='${org.header.url == null ? "" : org.header.url}'>${org.header.name == null ? "" : org.header.name }</a>
                    <br>
                    <c:choose>
                        <c:when test="${org.paragraphs != null}">
                            <table>
                                <c:forEach var="paragraph" items="${org.paragraphs}">
                                    <tr>
                                        <td>${paragraph.startDate} - ${paragraph.endDate}</td>
                                        <td><b>${paragraph.title}</b></td>
                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>${paragraph.description}</td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </c:when>
                    </c:choose>
                    <br>
                </c:forEach>
            </c:when>
        </c:choose>

    </c:when>
    </c:choose>
    <br>
    </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
