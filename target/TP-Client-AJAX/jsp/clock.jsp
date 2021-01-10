<%@ page import="java.time.LocalDateTime" %><%
    response.setContentType("application/xml");
    LocalDateTime currentTime = LocalDateTime.now();
%><?xml version="1.0" encoding="UTF-8"?>
<t>
    <h><%= currentTime.getHour() %></h>
    <m><%= currentTime.getMinute() %></m>
    <s><%= currentTime.getSecond() %></s>
</t>
