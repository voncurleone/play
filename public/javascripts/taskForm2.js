//routes
const taskListRoute = $("#task-list-route").val();
const logOutRoute = $("#log-out-route").val();
const markTaskRoute = $("#mark-task-route").val();

$("#task-form").load(taskListRoute);

function addTask() {
  console.log("adding task")
}

function remove(index) {
  console.log("removing: " + index)
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