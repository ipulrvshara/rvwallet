'use strict';

angular.module('rvwalletApp')
    .factory('Sample', function ($resource, DateUtils) {
        return $resource('api/samples/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
