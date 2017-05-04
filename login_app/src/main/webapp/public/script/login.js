//$("#loginBtn").click(function () {
function login() {
alert("ggg");
    var obj = {
        login : document.forms.loginForm.login,
        password : document.forms.loginForm.password
    };
    $.ajax({
        url: "login",
        contentType : 'application/json;charset=UTF-8;',
        type: "POST",
        data: obj
    });
};