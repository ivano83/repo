<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>MyCatalog</title>
    <link rel="stylesheet" href="resources/css/screen.css" type="text/css" media="screen, projection"></link>
    <link rel="stylesheet" href="resources/css/print.css" type="text/css" media="print"></link>
    <!--[if IE]>
    <link rel="stylesheet" href="resources/css/ie.css" type="text/css" media="screen, projection">
    <![endif]-->

</head>
<body>
    <div class="container">
        
        <div class="my_head"><!-- Header -->
        <tiles:insertAttribute name="header" />
        </div>
        <div class="my_menu"><!-- Menu Page -->
        <tiles:insertAttribute name="menu" />
        </div>
        <div class="my_body"><!-- Body Page -->
        <tiles:insertAttribute name="body" />
        </div>
        <div class="my_footer"><!-- Footer Page -->
        <tiles:insertAttribute name="footer" />
        </div>
    </div>
</body>
</html>