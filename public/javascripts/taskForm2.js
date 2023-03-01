//routes
const taskListRoute = $("#task-list-route").val();
const logOutRoute = $("#log-out-route").val();
const markTaskRoute = $("#mark-task-route").val();
const removeTaskRoute = $("#remove-task-route").val();
const addTaskRoute = $("#add-task-route").val();

$("#task-form").load(taskListRoute);

function addTask() {
  const task = $("#task-text").val();
  const marked = $("#task-marked")[0].checked;
  console.log("adding task: " + task + ", marked: " + marked);
  $("#task-text").val("");
  $("#task-marked")[0].checked = false;

  $.post(addTaskRoute,
    {task, marked, csrfToken},
    data => {
      $("#task-form").html(data);
    });
}

function remove(index) {
  console.log("removing: " + index);

  $.post(removeTaskRoute,
    {index, csrfToken},
    data => {
      $("#task-form").html(data);
    });
}

function mark(index) {
  console.log("marking: " + index);

  $.post(markTaskRoute,
    {index, csrfToken},
    data => {
      $("#task-form").html(data);
    });
}

function logOut() {
  console.log("logging out");
  location = logOutRoute;
}