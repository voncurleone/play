//routes
//const loginRoute = $("#login-route").val();
const currentRoute = $("#current-route").val();
const validateRoute = $("#validate-route").val();
//const taskListRoute = $("#task-list-route").val();

//csrf token
const csrfToken = $("#csrf-token").val();

$("#contents").load(currentRoute);
//$("#task-form").load(taskListRoute);

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

/*function addTask() {
  console.log("adding task")
}*/