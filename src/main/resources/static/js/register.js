function openRegisterModal() {
    document.getElementById("registerModal").style.display = "block";
}

function closeRegisterModal() {
    document.getElementById("registerModal").style.display = "none";
}

function showToast(message, isError = false) {
    const toast = document.getElementById("toast");
    toast.textContent = message;

    toast.classList.remove("hidden");
    toast.classList.add("show");

    if (isError) toast.classList.add("error");
    else toast.classList.remove("error");

    setTimeout(() => {
        toast.classList.remove("show");
        setTimeout(() => toast.classList.add("hidden"), 400);
    }, 3000);
}

document.addEventListener("DOMContentLoaded", function() {

    const registerForm = document.getElementById("registerForm");

    if (registerForm) {
        registerForm.addEventListener("submit", async function (event) {
            event.preventDefault();

            const form = event.target;  

            const email = form.elements["email"].value;
            const nickname = form.elements["nickname"].value;
            const password = form.elements["password"].value;

            const response = await fetch("./api/v1/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({ email, nickname, password })
            });

            const message = await response.text();
			closeRegisterModal();
			
			if (response.ok) {
			    showToast("Register successful!");
			} else {
			    showToast("Register failed!", true);
			}
        });
    }
    
});

