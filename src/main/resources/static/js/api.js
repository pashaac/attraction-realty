var reverseGeolocation = function (args) { // {lat: 0.0, lng: 0.0}
    console.log(window.location.href + 'geolocation/reverse?' + $.param(args));
    $.get('geolocation/reverse', $.param(args), function (city) {
        map.fitBounds(new google.maps.LatLngBounds(
            {lat: city.boundingBox.southWest.latitude, lng: city.boundingBox.southWest.longitude},
            {lat: city.boundingBox.northEast.latitude, lng: city.boundingBox.northEast.longitude}));
        boundingBoxContainer.push(rectangle('#ffffff', 1.0, 1, 0.1, city.boundingBox));
        collectCityAttraction({cityId: city.id, source: 'GOOGLE'});
    });
};

$.put = function (url, args, callback) {
    return $.ajax({
        type: 'PUT',
        url: window.location.href + url + '?' + args,
        contentType: 'application/json',
        success: callback
    });
};

$.get = function (url, args, callback) {
    return $.ajax({
        type: 'GET',
        url: window.location.href + url + '?' + args,
        success: callback
    });
};

var collectCityAttraction = function (args) { // {cityId: 1, source: 'FOURSQUARE'}
    console.log(window.location.href + 'venue/city/attraction?' + $.param(args));
    $.get('venue/city/attraction', $.param(args), function (venues) {
        if (venues && venues.length) {
            venues.forEach(function (venue) {
                new google.maps.Marker({
                    position: {lat: venue.location.latitude, lng: venue.location.longitude},
                    map: map,
                    title: venue.title,
                    animation: google.maps.Animation.DROP,
                    icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|A2E726',
                })
            });
            emulateCollectionAlgorithmGrid(args);
        } else {
            console.log(window.location.href + 'venue/city/attraction/mine?' + $.param(args));
            $.put('venue/city/attraction/mine', $.param(args), function (venues) {
                venues.forEach(function (venue) {
                    new google.maps.Marker({
                        position: {lat: venue.location.latitude, lng: venue.location.longitude},
                        map: map,
                        title: venue.title,
                        animation: google.maps.Animation.DROP,
                        icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|A2E726',
                    })
                });
                emulateCollectionAlgorithmGrid(args);
            });
        }
    });
};

var emulateCollectionAlgorithmGrid = function (args) {
    console.log(window.location.href + 'venue/city/attraction/grid?' + $.param(args));
    $.get('venue/city/attraction/grid', $.param(args), function (boundingBoxes) {
        boundingBoxes.forEach(function (boundingBox) {
            boundingBoxContainer.push(rectangle('#ffffff', 1.0, 1, 0.1, boundingBox));
        })
    });
};