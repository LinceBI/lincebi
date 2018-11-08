var MAX_RECENTS = 50;
var recentsList = [];
var favoritesList = [];
var mode = 'run';

function init() {
	window.parent.mantle_addHandler("RecentsChangedEvent", this.onRecentsChanged.bind(this));
	window.parent.mantle_addHandler("FavoritesChangedEvent", this.onRecentsChanged.bind(this));
	populateRecents();
}

function onRecentsChanged() {
    this.populateRecents();
}

function populateRecents() {	
	var template = document.getElementById('item-template');
	var panel = document.getElementById('recents-panel');
	
	getRecents();
	getFavorites();
	
	panel.innerHTML = '';
	for (let i in recentsList) {
		
		if (i < MAX_RECENTS) {
			var element = document.importNode(template.content, true).firstElementChild;
			var titleLabel = element.getElementsByClassName('item-title')[0];
			
			titleLabel.text = recentsList[i]['title'];
			titleLabel.addEventListener('click', () => {
				openRepositoryFile(recentsList[i]['fullPath'], mode);
			});
			
			var favButton = element.getElementsByClassName('favorite-button')[0];
			favButton.querySelector('i').classList.add(getFavoriteIconState(recentsList[i]['fullPath']));
			favButton.addEventListener('click', (e) => {
				toggleFavorite(e.target, recentsList[i]['fullPath'], recentsList[i]['title']);
			});
			
			panel.append(element);
		}
	}
}

function getRecents() {
	recentsList = [];

	$.ajax({
		async: false,
		url: '../../api/user-settings/recent',
		dataType: 'json',
		success: function(response) {
			if (response && response.length) {
				recentsList = response;
			}
		}
	});
}

function getFavorites() {
	favoritesList = [];

	$.ajax({
		async: false,
		url: '../../api/user-settings/favorites',
		dataType: 'json',
		success: function(response) {
			if (response && response.length) {
				favoritesList = response;
			}
		}
	});
}

function getFavoriteIconState(path) {
	for (let i in favoritesList) {
		if (favoritesList[i]['fullPath'] == path) {
			return 'fa-star';
		}
	}
	
	return 'fa-star-o';
}

function toggleFavorite(icon, path, title) {
	if (icon.classList.contains('fa-star-o')) {
		window.parent.mantle_addFavorite(path, title);
	} else {
		window.parent.mantle_removeFavorite(path); 
	}

	icon.classList.toggle('fa-star');
	icon.classList.toggle('fa-star-o');
}

function openRepositoryFile(path, mode) {
    if (!path) {
      return;
    }
    if (!mode) {
      mode = "edit";
    }

    var extension = path.split(".").pop();

    if (!($("body").hasClass("pdfReaderEmbeded") && extension == "pdf")) {
		parent.mantle_setPerspective('opened.perspective');
    }
    window.parent.mantle_openRepositoryFile(path, mode);
}