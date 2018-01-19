var map, user;
var args;

function initMap() {

    user = {lat: 59.957570, lng: 30.307946}; // ITMO University
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (pos) {
            user = {lat: pos.coords.latitude, lng: pos.coords.longitude};
            buildMap(user);
        }, function () {
            alert('Your browser does not support geolocation service.\n\nWas used default location: Saint-Petersburg, Russia');
            buildMap(user);
        });
    } else {
        alert('Your browser does not support geolocation service.\n\nWas used default location: Saint-Petersburg, Russia');
        buildMap(user);
    }

}

function buildMap(center) {
    map = new google.maps.Map(document.getElementById('map'), {
        center: center,
        zoom: 12,

        styles: [
            {elementType: 'geometry', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.stroke', stylers: [{color: '#242f3e'}]},
            {elementType: 'labels.text.fill', stylers: [{color: '#746855'}]},
            {
                featureType: 'administrative.   locality',
                elementType: 'labels.text.fill',
                stylers: [{color: '#d59563'}]
            },
            {
                featureType: 'poi',
                elementType: 'labels.text.fill',
                stylers: [{color: '#d59563'}]
            },
            {
                featureType: 'poi.park',
                elementType: 'geometry',
                stylers: [{color: '#263c3f'}]
            },
            {
                featureType: 'poi.park',
                elementType: 'labels.text.fill',
                stylers: [{color: '#6b9a76'}]
            },
            {
                featureType: 'road',
                elementType: 'geometry',
                stylers: [{color: '#38414e'}]
            },
            {
                featureType: 'road',
                elementType: 'geometry.stroke',
                stylers: [{color: '#212a37'}]
            },
            {
                featureType: 'road',
                elementType: 'labels.text.fill',
                stylers: [{color: '#9ca5b3'}]
            },
            {
                featureType: 'road.highway',
                elementType: 'geometry',
                stylers: [{color: '#746855'}]
            },
            {
                featureType: 'road.highway',
                elementType: 'geometry.stroke',
                stylers: [{color: '#1f2835'}]
            },
            {
                featureType: 'road.highway',
                elementType: 'labels.text.fill',
                stylers: [{color: '#f3d19c'}]
            },
            {
                featureType: 'transit',
                elementType: 'geometry',
                stylers: [{color: '#2f3948'}]
            },
            {
                featureType: 'transit.station',
                elementType: 'labels.text.fill',
                stylers: [{color: '#d59563'}]
            },
            {
                featureType: 'water',
                elementType: 'geometry',
                stylers: [{color: '#17263c'}]
            },
            {
                featureType: 'water',
                elementType: 'labels.text.fill',
                stylers: [{color: '#515c6d'}]
            },
            {
                featureType: 'water',
                elementType: 'labels.text.stroke',
                stylers: [{color: '#17263c'}]
            }
        ]
    });
}

var geolocation = function () {
    $.get(window.location.href + 'geolocation?' + $.param(args), function (coordinates) {
        alert('Saint-Petersburg coordinates ' + coordinates.latitude + ' ' + coordinates.longitude);
    });
};
