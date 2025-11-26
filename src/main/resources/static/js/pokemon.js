function openLoginModal() {
    document.getElementById("pokemonModal").style.display = "block";
}

function closeLoginModal() {
    document.getElementById("pokemonModal").style.display = "none";
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

async function addPokemon(pokemonId) {
    try {
        // Get the JWT token from localStorage
        const token = localStorage.getItem("jwt"); // adjust key name if different

        if (!token) {
            throw new Error("No JWT token found in localStorage");
        }

        // Make the API call
        const response = await fetch(`./api/v1/collection/${pokemonId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}` // attach the JWT
            },
            body: JSON.stringify({}) // or send any body if required
        });

		if (response.ok) {
		    const data = await response.json();
		    showToast(data.message);
		    return data;
		} else {
		    showToast(data.message, true);
		    throw new Error(`API error: ${response.status}`);
		}

        
        
        
    } catch (error) {
        console.error("Failed to add Pokemon:", error);
    }
}


async function removePokemon(pokemonId) {
    try {
        // Get the JWT token from localStorage
        const token = localStorage.getItem("jwt");

        if (!token) {
            throw new Error("No JWT token found in localStorage");
        }

        // Make the API call
        const response = await fetch(`./api/v1/collection/${pokemonId}`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error(`API error: ${response.status}`);
        }

        const data = await response.json();
        console.log("Pokemon removed:", data);
        return data;
    } catch (error) {
        console.error("Failed to remove Pokemon:", error);
    }
}


async function loadPokemonPage(id) {
    const response = await fetch(`./pokemon/${id}`);
    const html = await response.text();

    // מחליף את כל הדוקומנט
    document.open();
    document.write(html);
    document.close();
}


document.addEventListener("DOMContentLoaded", () => {
    loadPagination();
});

async function loadPagination() {
    const response = await fetch("api/v1/list?page=1&size=10");
    const data = await response.json();

    renderPagination(data.total/data.size + (data.total % data.size === 0 ? 0 : 1));
}

function renderPagination(totalPages) {
    const container = document.getElementById("paginationControls");
    container.innerHTML = "";

    // Previous
    container.innerHTML += `<span class="page-btn">&lt;</span>`;

    // First 3 pages
    for (let i = 1; i <= Math.min(5, totalPages); i++) {
        container.innerHTML += `<span class="page-btn">${i}</span>`;
    }

    // Next
    container.innerHTML += `<span class="page-btn">&gt;</span>`;
}
