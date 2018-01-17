
var map, user;
var args;
var base = 'http://localhost:8080';

function initMap() {

    user = {lat: 59.957570, lng: 30.307946}; // ITMO University
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (pos) {
            user = {lat: pos.coords.latitude, lng: pos.coords.longitude};
            map = new google.maps.Map(document.getElementById('map'), { zoom: 13, center: user });
            args = {address: "Saint-Petersburg, Russia"};
            geolocation();
        }, function () {
            alert('Your browser does not support geolocation service.\n\nUsing default location: Saint-Petersburg, Russia');
            map = new google.maps.Map(document.getElementById('map'), { zoom: 13, center: user });
        });
    } else {
        alert('Your browser does not support geolocation service.\n\nUsing default location: Saint-Petersburg, Russia');
        map = new google.maps.Map(document.getElementById('map'), { zoom: 13, center: user });
    }

}

var geolocation = function () {
    $.get(base + '/geolocation?' + $.param(args), function (coordinates) {
        alert('Saint-Petersburg coordinates ' + coordinates.latitude + ' ' + coordinates.longitude);
    });
};
