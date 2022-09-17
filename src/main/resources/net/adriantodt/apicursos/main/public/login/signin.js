const queryParams = (function (obj, queryString) {
    if (queryString) {
        const arr = queryString.split('#')[0].split('&');
        for (let i = 0; i < arr.length; i++) {
            const a = arr[i].split('=');
            const paramName = a[0];
            const paramValue = typeof (a[1]) === 'undefined' ? true : a[1];
            if (paramName.match(/\[(\d+)?\]$/)) {
                const key = paramName.replace(/\[(\d+)?\]/, '');
                if (!obj[key]) obj[key] = [];
                if (paramName.match(/\[\d+\]$/)) {
                    const index = /\[(\d+)\]/.exec(paramName)[1];
                    obj[key][index] = paramValue;
                } else {
                    obj[key].push(paramValue);
                }
            } else {
                if (!obj[paramName]) {
                    obj[paramName] = paramValue;
                } else if (obj[paramName] && typeof obj[paramName] === 'string') {
                    obj[paramName] = [obj[paramName]];
                    obj[paramName].push(paramValue);
                } else {
                    obj[paramName].push(paramValue);
                }
            }
        }
    }
    return obj;
})({}, window.location.search.slice(1));

document.addEventListener('DOMContentLoaded', function (event) {
    const username = queryParams['username'];
    if (username) {
        document.getElementById('inputUsername').value = username;
    }
    const err = queryParams['err'];
    if (err) {
        const errDiv = document.getElementById('errorDiv');
        if (err === 'invalidcreds') {
            errDiv.classList.add('alert', 'alert-danger');
            errDiv.textContent = 'Credenciais inválidas.';
        }
        if (err === 'unauthorized') {
            errDiv.classList.add('alert', 'alert-danger');
            errDiv.textContent = 'Por favor, faça login.';
        }
    }
    const then = queryParams['then'];
    if (then) {
        const thenInput = document.createElement('INPUT');
        thenInput.name = 'then';
        thenInput.type = 'hidden';
        thenInput.value = then;
        document.getElementById('loginForm').appendChild(thenInput);
    }
}, false);