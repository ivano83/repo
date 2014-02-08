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
<?php  include('common/head.php'); ?>
</head>
<body onload="javascript:evidenziaMenu('m_cerimonia');">
<div class="wrapper">
	<header>
    	<?php  include('common/header.php'); ?>
        <div class="clearfloat"></div>
        <div class="pageTitle">
        	<h3>Blog</h3>
        </div>
        <div class="clearfloat"></div>
        <div id="slideshow">
        	<ul class="slides">
                <li><img src="images/castello/castello_1.jpg" width="960" height="340" alt="castello 1" /></li>
                <li><img src="images/castello/castello_2.jpg" width="960" height="340" alt="castello 2" /></li>
                <li><img src="images/castello/castello_3.jpg" width="960" height="340" alt="castello 3" /></li>
                <li><img src="images/castello/castello_4.jpg" width="960" height="340" alt="castello 4" /></li>
                <li><img src="images/castello/rito_civile_1.jpg" width="960" height="340" alt="castello 5" /></li>
                <li><img src="images/castello/rito_civile_2.jpg" width="960" height="340" alt="castello 6" /></li>
            </ul>
        	<span class="arrow previous"></span>
            <span class="arrow next"></span>
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
<script src="js/script.js"></script>
</body>
</html>
