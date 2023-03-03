//routes
const validateLoginRoute = document.getElementById("validate-login-route").value;
const taskListRoute = document.getElementById("task-list-route").value;
const logoutRoute = document.getElementById("logout-route").value;
const markTaskRoute = document.getElementById("mark-task-route").value;
const removeTaskRoute = document.getElementById("remove-task-route").value;

//csrf token
const csrfToken3 = document.getElementById("csrf-token").value;

//load page
loadPage();

function toTaskView() {
  document.getElementById("login-div").hidden = true;
  document.getElementById("task-list-div").hidden = false;
  document.getElementById("logout-div").hidden = false;
}

function loadPage() {
  console.log("loading page..");

  fetch(taskListRoute).then( result => result.json()).then(data => {
    console.log(data[0]);
    if(data[0]) {
      toTaskView();
      loadTasks();
    }
  });
  //todo: this solution works but still flashes login on refresh, may look to fix later
}

function login() {
  const username = document.getElementById("login-username").value;
  const password = document.getElementById("login-password").value;
  console.log("logging in: " + username + ", " + password);

  fetch(validateLoginRoute, {
    method: "Post",
    headers: { "Content-Type": "application/json", "Csrf-Token": csrfToken3},
    body: JSON.stringify({username, password})
  }).then( result => result.json()).then( data => {
    console.log("response: " + data);

    if(data) {
      document.getElementById("login-message").innerText = "";

      document.getElementById("login-username").value = "";
      document.getElementById("login-password").value = "";

      document.getElementById("login-div").hidden = true;
      document.getElementById("task-list-div").hidden = false;
      document.getElementById("logout-div").hidden = false;
      loadTasks();
    } else {
      document.getElementById("login-message").innerText = "Login Failed!";
    }
  });
}

function loadTasks() {
  console.log("lading tasks..");

  fetch(taskListRoute).then(result => result.json()).then(data => {
    const ul = document.getElementById("task-list-ul")
    ul.innerHTML = ""

    for( let i = 0; i < data.length; i++) {
      console.log(data[i]);
      const li = document.createElement("li")

      const text = document.createTextNode(data[i][0])
      const textBold = document.createElement("b")
      textBold.appendChild(text)

      if(data[i][1]) {
        li.appendChild(textBold)
      } else {
        li.appendChild(text)
      }

      const mark = document.createElement("button")
      const remove = document.createElement("button")
      const space = document.createTextNode("\t")

      mark.innerHTML = "mark"
      mark.onclick = e => { markItem(i) }
      remove.innerHTML = "remove"
      remove.onclick = e => { removeItem(i) }

      li.appendChild(space)
      li.appendChild(mark)
      li.appendChild(space)
      li.appendChild(remove)

      ul.appendChild(li)
    }
  });
}

function logout() {
  console.log("logging out..");

  fetch(logoutRoute).then(result => result.json()).then(data => {
    if(data) {
      toTaskView()
    }
  });
}

function markItem(index) {
  console.log("mark: " + index)

  fetch(markTaskRoute, {
    method: "post",
    headers: { "Content-Type": "application/json", "Csrf-Token": csrfToken3},
    body: JSON.stringify(index)
  }).then(result => result.json()).then( data => {
    if(data) {
      loadTasks()
    } else {
      document.getElementById("task-message").innerHTML = "Mark failed! index:" + index
    }
  });
}

function removeItem(index) {
  console.log("remove: " + index)

  fetch(removeTaskRoute, {
    method: "post",
    headers: { "Content-Type": "application/json", "Csrf-Token": csrfToken3},
    body: JSON.stringify(index)
  }).then(result => result.json()).then( data => {
    if(data) {
      loadTasks()
    } else {
      document.getElementById("task-message").innerHTML = "Remove failed! index:" + index
    }
  });
}