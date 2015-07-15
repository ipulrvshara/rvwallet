'use strict';

angular.module('rvwalletApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


