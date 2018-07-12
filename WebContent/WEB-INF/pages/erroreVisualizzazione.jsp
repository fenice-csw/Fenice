<%@ page contentType="text/html;charset=UTF-8" language="java"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<style type="text/css">
#container {
	width: 100%;
	position: relative;
	font-family: sans-serif;
}

#notifica {
	margin-left: auto;
	margin-right: auto;
	max-width: 1280px;
	min-width: 930px;
	padding: 0;
}

#notifica h1 {
	padding: 20px 0 0 0;
	margin: 0;
	font-size: 30px;
	color: #CC0000;
}

#notifica ul {
	list-style-type: none;
	padding: 1em 0 1em 2em !important;
	font-size:14px;
}

#notifica ul li {
	color: #CC0000;
}

#notifica ul li span {
	font-weight: bold;
	color: #CC0000;
}

#notifica div {
	padding: 0;
	margin: 0;
}

#notifica div.page {
	position: relative;
	margin: 20px 0 50px 0;
	padding: 60px;
	background: -moz-linear-gradient(0% 0 360deg, #FFEBE8, #F2F2F2 20%, #FFEBE8)
		repeat scroll 0 0 transparent;
	border: 1px solid  #CC0000;
	-webkitbox-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
	-mozbox-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
}
</style>

<body>
	<div id="container">
		<div id="notifica">
			<div class="page">
				<h1>ATTENZIONE!</h1>
				<ul>
					<li>Impossibile visualizzare correttamente il documento</li>
				</ul>
			</div>
		</div>
	</div>
</body>