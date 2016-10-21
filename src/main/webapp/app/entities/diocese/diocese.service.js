(function() {
    'use strict';
    angular
        .module('orthGuideApp')
        .factory('Diocese', Diocese);

    Diocese.$inject = ['$resource'];

    function Diocese ($resource) {
        var resourceUrl =  'api/diocese/:id';

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
