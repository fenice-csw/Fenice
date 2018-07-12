var openedMenu = new Array();
var closedMenu = new Array();

function menuGroupOnClick(menuId) {
/*
	for (var i = 0; i < openedMenu.length; i++) {
		if (openedMenu[i] == menuId) {
			openedMenu.splice(i, 1);
		}
	}
	for (var i = 0; i < closedMenu.length; i++) {
		if (closedMenu[i] == menuId) {
			closedMenu.splice(i, 1);
		}
	}
	*/
	
	var menu = document.getElementById("node_" + menuId);
	var submenu = document.getElementById("subnode_" + menuId);
	if (submenu.className == "opened") {
		menu.className = "closed";
		submenu.className = "closed";
		//closedMenu.push(menuId);
	} else {
		menu.className = "opened";
		submenu.className = "opened";
		//openedMenu.push(menuId);
	}
}

function menuItemOnClick(anchor) {
	for (var i = 0; i < openedMenu.length; i++) {
		anchor.href += "&open="+openedMenu[i];
	}
	for (var i = 0; i < closedMenu.length; i++) {
		anchor.href += "&close="+closedMenu[i];
	}
}



function initAlbero(menuId) {
	if (document.getElementById) {
		
		var menu = document.getElementById("node_" + menuId);
		if (menu) {
			var submenu = document.getElementById("subnode_" + menuId);
			if (submenu) {
				menu.style.cursor = "pointer";
				submenu.className = menu.className;
				var group = document.getElementById("nodegroup_" + menuId);

				group.onclick = function() {
					menuGroupOnClick(menuId);
				}
			} else {
				var anchor = document.getElementById("nodelink_" + menuId);
				if (anchor) {
					anchor.onclick = function() {
						menuItemOnClick(this);
   					}
				}
			}
		}
	}
}
