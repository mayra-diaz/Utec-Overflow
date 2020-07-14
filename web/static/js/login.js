const signUpButton = document.getElementById('signUp');
const signInButton = document.getElementById('signIn');
const container = document.getElementById('container');

signUpButton.addEventListener('click', () => container.classList.add('right-panel-active'));

signInButton.addEventListener('click', () => container.classList.remove('right-panel-active'));

//Crear un nuevo usuario
function button_press_Create(){
  var username = $("#usernameCreate").val();
  var name = $("#nameCreate").val();
  var fullname = $("#fullnameCreate").val();
  var password = $("#passwordCreate").val();


  var complete_user = JSON.stringify(
    {
      "username" : username,
      "name" : name,
      "fullname" : fullname,
      "password" : password,
    }
  );

  $.ajax({
    url: '/users',
    type: 'POST',
    contentType: 'application/json',
    data: complete_user,
    dataType: 'json',
  });
}

//Logear a un usuario existente
function button_press_Login(){
  var username = $("#usernameLogin").val();
  var password = $("#passwordLogin").val();

  var complete_login = JSON.stringify(
    {
      "username" : username,
      "password" : password,
    }
  );
  $.ajax({
    url: '/authenticate',
    type: 'POST',
    contentType: 'application/json',
    data: complete_login,
    dataType:'json',
  });
}
