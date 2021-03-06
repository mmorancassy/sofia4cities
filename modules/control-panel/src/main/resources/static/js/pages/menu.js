var MenuController = function() {
    
	// DEFAULT PARAMETERS, VAR, CONSTS. 
    var APPNAME = 'Sofia4Cities Control Panel'; 
	var LIB_TITLE = 'Menu Controller';	
    var logControl = 0;
	var LANGUAGE = ['es'];
	var currentLanguage = ''; // loaded from template.
	
	// CONTROLLER PRIVATE FUNCTIONS
	
		
	// LOAD MENU INFORMATION FROM USER-ROLE 
	// get SERVER-JSON from header.html -> headerReg.menu and CREATE HTML MENU.
	var consoleMenu = function(){
		
		logControl ? console.log('|---> consoleMenu() -> Creating HTML Console Menu') : '';
		
		var menu_HTML	= ''
		,submenu_HTML	= ''
		,markUp_HTML	= ''
		,page_LANG 		= ''
		,menu_LANG 		= ''
		,submenus 		= false;
		
		// NO-DATA NO-FUN!
		if (!menuReg){ $.alert({title: 'MENU ERROR!',content: 'No Menu Data!'}); return false; }
		
		// GET JSON FROM CONTROLLER LOAD()
		var menuJson = menuReg;
		
		// GET CURRENT LANGUAGE OR 'ES'.
		page_LANG = currentLanguage || LANGUAGE;
		menu_LANG = page_LANG.toString().toUpperCase();
		
		logControl ? console.log('     |---> menu: ' + menuJson.menu + ' NoSession Path: ' + menuJson.noSession + ' Rol: ' + menuJson.rol + ' Navigation Objects: ' + menuJson.navigation.length + ' Language: ' + menu_LANG ) : '';
		
		// NAV-ITEM MAIN LOOP
		var navItemsArr = menuJson.navigation;
		navItemsArr.map(function(item, index){
			
			// CLEAN VARS FOR EACH LOOP.
			markUp_HTML = menu_HTML = submenu_HTML = '';			
			logControl ? console.log('     |---> navItem-' + index + 'Item: ' + item.title.ES + ';  Submenus: ' + item.submenu.length + ' ' + submenus ) : '';
			
			if ( hasSubmenus(item) ){
				submenus = true;
				menu_HTML  +='<li class="nav-item">'
							+'	<a href="'+ item.url +'" class="nav-link nav-toggle">'
							+'		<i class="'+ item.icon +'"></i>'
							+'		<span class="title">'+ item.title[ menu_LANG ] +'</span>'
							+'		<span class="arrow"></span>'
							+'	</a>'
							+'	<ul class="sub-menu">';
				
				// SUB-NAV-ITEM LOOP			 
				item.submenu.map(function(subitem, subindex){					
					
					submenu_HTML   +='<li class="nav-item">'
									+'	<a href="'+ subitem.url +'" class="nav-link nav-toggle">'
									+'		<i class="'+ subitem.icon +'"></i>'
									+'		<span class="title">'+ subitem.title[ menu_LANG ] +'</span>'
									+'	</a>'
									+'</li>';
							
					logControl ? console.log('     |---> sub navItem-'+ subindex + '; SubItem: ' + subitem.title[ menu_LANG ] + '.') : '';
							
				});
				// add submenus and close submenu ul of nav-item.
				menu_HTML += submenu_HTML + '	</ul>' + '</li>';
				
				// ADD TO FINAL MARKUP AND APPENTO MENU (.page-sidebar-menu)
				markUp_HTML += menu_HTML;
				$(markUp_HTML).appendTo($('.page-sidebar-menu'));				
			}
			else {
				// NAV-ITEM WITHOUT SUBMENU
				submenus = false;				
				menu_HTML  +='<li class="nav-item">'
							+'	<a href="'+ item.url +'" class="nav-link nav-toggle">'
							+'		<i class="'+ item.icon +'"></i>'
							+'		<span class="title">'+ item.title[ menu_LANG ] +'</span>'
							+'	</a>'
							+'</li>';
				
				// ADD AND APPENTO MENU (.page-sidebar-menu) 
				$(menu_HTML).appendTo($('.page-sidebar-menu'));				
			}			
		});
		// SET ACTIVE NAV.
		setActiveNavItem();
	}
	
	// AUX. CHECK IF A NAV-ITEM HAD SUBMENU ITEMS
	var hasSubmenus = function(item){ var checkSubmenus = item.submenu.length > 0 ? true : false; return checkSubmenus;  }
	
	// AUX. GET CURRENT PAGE URL AND DETECT ACTIVE NAV-ITEM 
	var setActiveNavItem = function(){
		
		logControl ? console.log('|---> setActiveNavItem() -> Setting current nav-item Active') : '';
		
		var currentPath = window.location.pathname;		
		logControl ? console.log('|---> CURRENT PATH: ' +  currentPath) : '';
		
		// CHECK FIRST NAV (HOME) EXCEP.
		firstMenu = $('.page-sidebar-menu > li.nav-item.start > a.nav-link.nav-toggle');
		if ( currentPath === '/controlpanel/main' ){ firstMenu.closest('li.nav-item').addClass('open active'); return false;} else { firstMenu.closest('li.nav-item').removeClass('open active');}
		
		// GET ALL NAVS, THEN CHECK URL vs. CURRENT PATH --> ACTIVE.
		var allMenus = $('.page-sidebar-menu > li.nav-item > ul.sub-menu  > li.nav-item > a.nav-link.nav-toggle');
		allMenus.each(function(ilink,navlink){
				
			logControl ? console.log('|---> nav-link-' + ilink + ' URL: ' + navlink + ' PATH: ' + $(this)[0].pathname) : '';
			
			if ( currentPath === $(this)[0].pathname ){					
				currentLi = $(this).closest('li.nav-item');
				currentNav = currentLi.parents('.nav-item');
				
				// APPLY ACTIVE CLASSES
				currentLi.addClass('active open');							
				currentNav.addClass('active open');
				currentNav.find('.arrow').addClass('open');
				return false;				
			}			
		});		
	}
	
	
	// CONTROLLER PUBLIC FUNCTIONS 
	return{
		
		// LOAD() JSON LOAD FROM TEMPLATE TO CONTROLLER
		load: function(Data) { 
			logControl ? console.log(LIB_TITLE + ': load()') : '';
			return menuReg = Data;
		},
		
		lang: function(lang){
			logControl ? console.log(LIB_TITLE + ': lang()') : '';
			logControl ? console.log('|---> lang() -> assign current Language to Console Menu: ' + lang) : '';
			return currentLanguage = lang;
			
		},
		
		// INIT() CONTROLLER INIT CALLS
		init: function(){
			logControl ? console.log(LIB_TITLE + ': init()') : '';
			
			// load menu (role)
			consoleMenu();			
		}		
	};
}();

// AUTO INIT CONTROLLER WHEN READY
jQuery(document).ready(function() {
	
	// LOADING JSON DATA FROM THE TEMPLATE (CONST, i18, ...)
	MenuController.load(menuJson);
	// LOADING CURRENT LANGUAGE FROM THE TEMPLATE
	MenuController.lang(currentLanguage);	
	// AUTO INIT CONTROLLER.
	MenuController.init();
});
