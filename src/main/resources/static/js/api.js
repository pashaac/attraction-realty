var reverseGeolocation = function (args) {
    console.log(window.location.href + 'geolocation/reverse?' + $.param(args));
    $.get(window.location.href + 'geolocation/reverse?' + $.param(args), function (city) {
        map.fitBounds(new google.maps.LatLngBounds(
            {lat: city.boundingBox.southWest.latitude, lng: city.boundingBox.southWest.longitude},
            {lat: city.boundingBox.northEast.latitude, lng: city.boundingBox.northEast.longitude}));
        boundingBoxContainer.push(rectangle('#ffffff', 1.0, 1, 0.1, city.boundingBox));
    });
};

var venueMineCityAttraction = function (args) { // args = {city: city, source: 'FOURSQUARE'}
    console.log(window.location.href + 'venue/mine/city/attraction?' + $.param({source: args.source}));
    $.ajax({
        url: window.location.href + 'venue/mine/city/attraction?' + $.param({source: args.source}),
        dataType: 'json',
        type: 'get',
        contentType: 'application/json',
        data: JSON.stringify(args.city),
        success: function (venues) {
            venues.forEach(function (venue) {
                return new google.maps.Marker({
                    position: {lat: venue.location.latitude, lng: venue.location.longitude},
                    map: map,
                    icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|A2E726',
                    title: venue.title
                })
            });
        }
    });
    //
    // $.get(window.location.href + 'venue/mine/city/attraction?' + $.param(args), function (venues) {
    //     venues.forEach(function (venue) {
    //         return new google.maps.Marker({
    //             position: {lat: venue.location.latitude, lng: venue.location.longitude},
    //             map: map,
    //             icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|A2E726',
    //             title: venue.title
    //         })
    //     });
    // });
};