const taskListRoute = $("#task-list-route").val();

$("#task-form").load(taskListRoute);

function addTask() {
  console.log("adding task")
}

function remove(index) {
  console.log("removing: " + index)
}

function mark(index) {
  console.log("marking: " + index)
}