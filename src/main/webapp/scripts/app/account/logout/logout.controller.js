'use strict';

angular.module('rvwalletApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
