// Helper functions to store/retrieve the token
function setToken(jwtToken) {
    localStorage.setItem('jwt', jwtToken);
}

function getToken() {
    return localStorage.getItem('jwt');
}

function removeToken() {
    localStorage.removeItem('jwt');
}
