var map;
var user = {lat: 59.957570, lng: 30.307946}; // ITMO University
var args;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        center: user,
        zoom: 12,
        scaleControl: true,
        styles: map_style
    });
    initSearchBox(map);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (pos) {
            user = {lat: pos.coords.latitude, lng: pos.coords.longitude};
            map.setCenter(user);
        }, function () {
            alert('Your browser does not support geolocation service.\n\nInitial location: ITMO University, Saint-Petersburg, Russia');
        });
    } else {
        alert('Your browser does not support geolocation service.\n\nInitial location: ITMO University, Saint-Petersburg, Russia');
    }

}

var geolocation = function () {
    $.get(window.location.href + 'geolocation?' + $.param(args), function (coordinates) {
        alert('Saint-Petersburg coordinates ' + coordinates.latitude + ' ' + coordinates.longitude);
    });
};
