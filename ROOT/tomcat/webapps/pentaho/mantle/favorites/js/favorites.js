var favoritesList = [];
var mode = 'run';

function init() {
	window.parent.mantle_addHandler('FavoritesChangedEvent', this.onFavoritesChanged.bind(this));
	populateFavorites();
}

function onFavoritesChanged(e) {
	this.populateFavorites();
}

function populateFavorites() {
	var template = document.getElementById('item-template');
	var panel = document.getElementById('favorites-panel');

	getFavorites();

	panel.innerHTML = '';
	for (let i in favoritesList) {
		var element = document.importNode(template.content, true).firstElementChild;
		var titleLabel = element.getElementsByClassName('item-title')[0];

		titleLabel.text = favoritesList[i]['title'];
		titleLabel.addEventListener('click', () => {
			openRepositoryFile(favoritesList[i]['fullPath'], mode);
		});

		var favButton = element.getElementsByClassName('favorite-button')[0];
		favButton.addEventListener('click', e => {
			toggleFavorite(e.target, favoritesList[i]['fullPath'], favoritesList[i]['title']);
		});

		panel.append(element);
	}
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
		mode = 'edit';
	}

	var extension = path.split('.').pop();

	if (!($('body').hasClass('pdfReaderEmbeded') && extension == 'pdf')) {
		parent.mantle_setPerspective('opened.perspective');
	}
	window.parent.mantle_openRepositoryFile(path, mode);
}
