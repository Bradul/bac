document.getElementById("button").addEventListener("click", submit);

const usernameField = document.getElementById("user");
const passwordField = document.getElementById("pass");
const form = document.getElementById("form");
const errorMessage = document.getElementById("formerror");

function submit(event) {
    event.preventDefault();

    var username = usernameField.value;
    var password = passwordField.value;
    var formData = {
        username: username,
        password: password
    };
    var formJSON = JSON.stringify(formData);

    var endpoint = "http://localhost:8080/user/login";

    fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: formJSON
    })
    .then(data => {
        console.log(data)
        if(data.status == 200) {
            console.log("OK!");
            errorMessage.innerHTML = "";
            return {success: true}
        }
        else {
            return data.text()
        }
    })
    .then(data => {
        if(!data.success) {
            errorMessage.innerHTML = data
            errorMessage.style.color = "red"
        }
    })
}