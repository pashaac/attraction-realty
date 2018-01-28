var reverseGeolocation = function (args) {
    console.log(window.location.href + 'geolocation/reverse?' + $.param(args));
    $.get(window.location.href + 'geolocation/reverse?' + $.param(args), function (city) {
        map.fitBounds(new google.maps.LatLngBounds(
            {lat: city.boundingBox.southWest.latitude, lng: city.boundingBox.southWest.longitude},
            {lat: city.boundingBox.northEast.latitude, lng: city.boundingBox.northEast.longitude}
        ));
        rectangle('#ffffff', 1.0, 1, 0.1, city.boundingBox);
    });
};