function putView(){
    $("#all_web").empty();
    template = "<div class=\'container\' id=\'container\'>	<div class=\'form-container sign-up-container\'>";
    template += "<!--Crear un nuevo usuario--> <h1>Crear Cuenta</h1>";
    template += "<div class=\'social-container\'></div><span></span>";
    template += "<input id=\'usernameCreate\' type=\'text\' placeholder=\'Usuario\' />";
    template += "<input id=\'nameCreate\' type=\'text\' placeholder=\'Nombre\' />";
    template += "<input id=\'fullnameCreate\' type=\'text\' placeholder=\'Apellido\' />";
    template += "<input id=\'passwordCreate\' type=\'password\' placeholder=\'Contraseña\' />";
    template += "<button id=\'PostUserButton\' onclick='button_press_Create();'>Crear Cuenta</button>";
    template += "<!--Crear un nuevo usuario-->";
    template += "</div> <div class=\'form-container sign-in-container\'>";
    template += "<!--Login del usuario--><form><img src=\'/static/images/utecLogoSmall.png\' alt=""><div class=\'social-container\'></div><span></span>";
    template += "<input id=\'usernameLogin\' type=\'username\' placeholder=\'Usuario\' />";
    template += "<input id=\'passwordLogin\' type=\'password\' placeholder=\'Contraseña\' />";
    template += "<button id=\'LoginButton\' onclick='button_press_Login();'>Iniciar Sesión</button></form><!--Login del usuario--></div>";
    template += "<div class=\'overlay-container\'><div class=\'overlay\'><div class=\'overlay-panel overlay-left\'><h1>¡Bienvenido de Vuelta!</h1><p>Para ingresar y ver todas las novedades regístrese usando su cuenta</p><button class=\'ghost\' id=\'signIn\'>Iniciar Sesión</button>";
    template += "</div><div class=\'overlay-panel overlay-right\'><h1>¿No Está Registrado?</h1><p>Cree una cuenta para empezar a resolver sus dudas</p>";
    template += "<button class=\'ghost\' id=\'signUp\'>Crear Cuenta</button></div></div></div></div>";
    $("#all_web").append(template);
}


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
    success: function(){
            $("#logged").empty();
            template = '<a href="login.html">Profile</a>';
            $("#logged").append(template);
            window.location="/static/html/index.html"
        },
  });
}
