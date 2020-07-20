var signUpButton = document.getElementById('signUp');
var signInButton = document.getElementById('signIn');
var container = document.getElementById('container');

if(signUpButton){
  signUpButton.addEventListener('click', () =>
     container.classList.add('right-panel-active'));
}

if(signInButton){
  signInButton.addEventListener('click', () =>
     container.classList.remove('right-panel-active'));
}

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
    url: '/new_user',
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

document.getElementById("PostUserButton").disabled = true;
document.getElementById("LoginButton").disabled = true;


$("#usernameCreate, #nameCreate, #fullnameCreate, #passwordCreate").on("keyup", function(){
    if($("#usernameCreate").val() != "" && $("#nameCreate").val() != "" && $("#fullnameCreate").val() != "" && $("#passwordCreate").val() != ""){
      document.getElementById("PostUserButton").disabled = false;
    }
});

$("#usernameLogin, #passwordLogin").on("keyup", function(){
    if($("#usernameLogin").val() != "" && $("#passwordLogin").val() != ""){
      document.getElementById("LoginButton").disabled = false;
    }
});
