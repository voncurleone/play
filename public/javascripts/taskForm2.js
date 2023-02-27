//routes
const taskListRoute = $("#task-list-route").val();
const logOutRoute = $("#log-out-route").val();
const markTaskRoute = $("#mark-task-route").val();
const removeTaskRoute = $("#remove-task-route").val();

$("#task-form").load(taskListRoute);

function addTask() {
  console.log("adding task")
}

function remove(index) {
  console.log("removing: " + index)

  $.post(removeTaskRoute,
    {index, csrfToken},
    data => {
      $("#task-form").html(data)
    });
}

function mark(index) {
  console.log("marking: " + index)

  $.post(markTaskRoute,
    {index, csrfToken},
    data => {
      $("#task-form").html(data)
    });
}

function logOut() {
  console.log("logging out")
  location = logOutRoute;
}