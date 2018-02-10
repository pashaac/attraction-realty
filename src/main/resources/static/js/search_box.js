function initSearchBox(map) {
    var input = document.getElementById('city-input');
    var searchBox = new google.maps.places.SearchBox(input);
    map.controls[google.maps.ControlPosition.TOP_LEFT].push(input);

    map.addListener('bounds_changed', function () {
        searchBox.setBounds(map.getBounds());
    });

    searchBox.addListener('places_changed', function () {
        var bounds = new google.maps.LatLngBounds();
        searchBox.getPlaces().forEach(function (place) {
            if (place.geometry) {
                if (place.geometry.viewport) {
                    bounds.union(place.geometry.viewport);
                } else {
                    bounds.extend(place.geometry.location);
                }
            }
        });
        cleanBoundingBoxContainer();
        reverseGeolocation({lat: bounds.getCenter().lat(), lng: bounds.getCenter().lng()});
    });
}