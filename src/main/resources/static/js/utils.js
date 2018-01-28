var cleanBoundingBoxContainer = function () {
    boundingBoxContainer.forEach(function (boundingBox) {
        boundingBox.setMap(null);
    });
    boundingBoxContainer = [];
};