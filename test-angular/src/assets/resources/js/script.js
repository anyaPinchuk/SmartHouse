$(document).ready(function () {
  $('.slider').slider();
  $('select').material_select();

});

function start() {
  gapi.load('auth2', function () {
    auth2 = gapi.auth2.init({
      client_id: '459855976439-t8f5fq12f8uskrpl0h8tn34d89aapftq.apps.googleusercontent.com',
    });
  });
}
