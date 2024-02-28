document.getElementById("button").addEventListener("click", submitUser)

const username = document.getElementById("user")
const email = document.getElementById("email")
const password = document.getElementById("pass")
const form = document.getElementById("form")
const errorMessage = document.getElementById("formerror")


function submitUser() {
    var usernameValue = username.value
    var emailValue = email.value
    var passwordValue = password.value

    var formData = {
        username: usernameValue,
        email: emailValue,
        password: passwordValue
    }

    var jsonFormattedForm = JSON.stringify(formData)

    var endpoint = "http://localhost:8080/user"

    fetch(endpoint, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: jsonFormattedForm
    }).then(data => {
        if(data.status == 200) {
            console.log("OK!");
            errorMessage.innerHTML = "";
            return {success: true}
        }
        else {
            return data.text()
        }
    }).then(data => {
        if(!data.success) {
            errorMessage.innerHTML = data
            errorMessage.style.color = "red"
        } else {
            form.submit()
        }
    })
}