(function() {
    'use strict';
    angular
        .module('orthGuideApp')
        .factory('Church', Church);

    Church.$inject = ['$resource'];

    function Church ($resource) {
        var resourceUrl =  'api/churches/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
