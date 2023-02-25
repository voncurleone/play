//routes
const loginRoute = $("#login-route").val();
const currentRoute = $("#current-route").val();
const validateRoute = $("#validate-route").val();

//csrf token
const csrfToken = $("#csrf-token").val();

$("#contents").load(currentRoute);

function validateLogin() {
  console.log("validating")
  const username = $("#login-name").val();
  const password = $("#login-pass").val();

  $.post(validateRoute,
    {username, password, csrfToken},
    data => {
      $("#contents").html(data);
    });
}

function remove(index) {
  console.log("removing: " + index)
}

function mark(index) {
  console.log("marking: " + index)
}