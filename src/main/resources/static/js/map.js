var map;

function initMap() {
    map = new google.maps.Map(document.getElementById('map'), mapConfiguration());
    initSearchBox(map);

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (pos) {
            reverseGeolocation({lat: pos.coords.latitude, lng: pos.coords.longitude});
        }, function () {
            alert('Your browser does not support geolocation service.\n\nInitial location: ITMO University, Saint-Petersburg, Russia\n\nUse search box to find the city of interest');
        });
    } else {
        alert('Your browser does not support geolocation service.\n\nInitial location: ITMO University, Saint-Petersburg, Russia\n\nUse search box to find the city of interest');
    }

}
