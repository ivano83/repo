<!doctype html>
<!--
Designed by: http://www.cssing.org
Released for free under a Creative Commons Attribution 3.0 License: http://creativecommons.org/licenses/by/3.0/
Name: Portfolio
Description:  A two-columns, responsive design template.
Template number: 16
Version: 1.0
Released: 4.3.13
-->
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Portfolio</title>
<link href='http://fonts.googleapis.com/css?family=Yesteryear' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Salsa' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Karla' rel='stylesheet' type='text/css'>
<link href="css/styles.css" rel="stylesheet" type="text/css">
</head>
<body onload="javascript:evidenziaMenu('m_cerimonia');">
<div class="wrapper">
	<header>
    	<?php  include('common/header.php'); ?>
        <div class="clearfloat"></div>
        <div class="pageTitle">
        	<h3>Blog</h3>
        </div>
    </header>
    <div class="welcome">
        <div class="post">
        	<div class="picBorder fixedPic"><img src="images/blogPic1.jpg" alt="about"></div>
            <h2>It was popularised in the 1960s</h2>
            <span>July - 25 - 2012 in <a href="#">Category 1</a>, <a href="#">Category 2</a></span>
            <div class="horSeparator"></div>
            <p class="postContent">Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and </p>
            <p class="button"><a href="">More</a></p>
        </div>
        <div class="post">
        	<div class="picBorder fixedPic"><img src="images/blogPic2.jpg" alt="about"></div>
            <h2>Also the leap into electronic</h2>
            <span>July - 25 - 2012</span>
            <div class="horSeparator"></div>
            <p class="postContent">Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and </p>
            <p class="button"><a href="">More</a></p>
        </div>
    </div>
    <div class="sidebar">
    	<h2>Categories</h2>
        <ul class="greyList">
        	<li><a href="">Category 1</a></li>
            <li><a href="">Category 2</a></li>
            <li><a href="">Category 3</a></li>
            <li><a href="">Category 4</a></li>
            <li><a href="">Category 5</a></li>
            <li><a href="">Category 6</a></li>
            <li><a href="">Category 7</a></li>
            <li><a href="">Category 8</a></li>
        </ul>
        <h2>Archives</h2>
        <ul class="greyList">
        	<li><a href="">May 2012</a></li>
            <li><a href="">April 2012</a></li>
            <li><a href="">March 2012</a></li>
            <li><a href="">February 2011</a></li>
            <li><a href="">December 2011</a></li>
            <li><a href="">November 2011</a></li>
            <li><a href="">September 2011</a></li>
            <li><a href="">August 2011</a></li>
        </ul>
        <div class="clearfloat"></div>
        <h2>Recent Posts</h2>
        <ul class="greyList">
        	<li><a href="">Lorem Ipsum</a></li>
            <li><a href="">Has been the</a></li>
            <li><a href="">Industry's standard</a></li>
            <li><a href="">Dummy text ever since</a></li>
            <li><a href="">The 1500s, when an</a></li>
            <li><a href="">Unknown printer</a></li>
            <li><a href="">Took a galley of type</a></li>
            <li><a href="">And scrambled it to</a></li>
        </ul>
    </div>
    <div class="clearfloat"></div>
</div>
<footer>
<?php  include('common/footer.php'); ?>
</footer>
</body>
</html>
