<%@ page import="java.util.List" %>
<%@ page import="com.urise.webapp.model.*" %>
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
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}" required></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactType.values()%>">
            <dl>
                <dt>${type.title}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <hr>
        <c:forEach var="type" items="<%=SectionType.values()%>">

            <c:set var="section" value="${resume.getSection(type)}"/>
            <h3>${type.title}</h3>
            <c:if test="${section != null}">
                <jsp:useBean id="section" type="com.urise.webapp.model.AbstractSection"/>
                <c:choose>

                    <c:when test="${type=='PERSONAL' || type =='OBJECTIVE'}">
                        <textarea name='${type}' cols=75 rows=3><%=section%></textarea>
                    </c:when>

                    <c:when test="${type=='QUALIFICATIONS' || type=='ACHIEVEMENT'}">
                    <textarea name='${type}' cols=75
                              rows=4><%=String.join("\n", ((ListSection) section).getPoints())%></textarea>
                    </c:when>

                    <c:when test="${type=='EXPERIENCE' || type =='EDUCATION'}">
                        <c:forEach var="org" items="<%=((OrganizationSection) section).getOrganizations()%>"
                                   varStatus="index">
                            <br>
                            <div>
                                <dl style="display: inline;">
                                    <dt>Название организации:</dt>
                                    <dd><input type="text" name="${type}" size="40" value="${org.header.name}"></dd>
                                </dl>
                                <dl style="display: inline; margin-left: 20px">
                                    <dt>Сайт организации:</dt>
                                    <dd><input type="text" name="${type}_URL" size="40" value="${org.header.url}"></dd>
                                </dl>
                                <br>
                            </div>

                            <div style="margin-top: 10px; margin-left: 40px">
                                <c:forEach var="par" items="${org.paragraphs}">
                                    <jsp:useBean id="par" type="com.urise.webapp.model.Paragraph"/>
                                    <dl style="display: inline;">
                                        <dt>Начальная дата:</dt>
                                        <dd>
                                            <input type="text" name="${type}${index.index}_SD" size=10
                                                   value="${par.startDate}" placeholder="YYYY-MM-DD">
                                        </dd>
                                    </dl>
                                    <dl style="display: inline; margin-left: 20px">
                                        <dt>Конечная дата:</dt>
                                        <dd>
                                            <input type="text" name="${type}${index.index}_ED" size=10
                                                   value="${par.endDate}" placeholder="YYYY-MM-DD">
                                    </dl>
                                    <dl>
                                        <dt>Должность:</dt>
                                        <dd><input type="text" name='${type}${index.index}_TITLE' size=75
                                                   value="${par.title}">
                                    </dl>
                                    <dl>
                                        <dt>Описание:</dt>
                                        <dd><textarea name="${type}${index.index}_DESCRIPTION" rows=5
                                                      cols=75>${par.description}</textarea></dd>
                                    </dl>
                                    <br>
                                </c:forEach>
                            </div>

                        </c:forEach>
                    </c:when>

                </c:choose>
            </c:if>
        </c:forEach>
        <button type="submit">Сохранить</button>
        <button type="button" class="btn btn-secondary"
                onclick="window.location.href='http://localhost:8080/resumes/resume'">Отменить
        </button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
