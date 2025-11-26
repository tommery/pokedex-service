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
    loadPage(1);
    
    document.getElementById("applyFilters").addEventListener("click", () => {
        loadPage(1); 
    });
});

async function loadPage(pageNumber) {
    const size = 10;

	const name = document.getElementById("filterName").value || "";
    const type = document.getElementById("filterType").value || "";

    const params = new URLSearchParams({
        page: pageNumber,
        size,
        name,
        type
    });
    
    const response = await fetch(`./api/v1/list?page=$${params}`);
    const data = await response.json();
    const totalPages = data.total/data.size + (data.total % data.size === 0 ? 0 : 1);

    renderPokemonList(data.items);
    renderPagination(totalPages, pageNumber);

    // Later you'll add renderPokemonList(data.items)
}

function renderPagination(totalPages, currentPage) {
    const container = document.getElementById("paginationControls");
    container.innerHTML = "";
    
    // Previous
    container.innerHTML += `
        <span class="page-btn" onclick="loadPage(${Math.max(1, currentPage - 1)})">&lt;</span>
    `;

    let start = Math.max(1, currentPage - 2);
    let end = Math.min(totalPages, currentPage + 2);

    // תיקון לשולי טווח (שיהיה תמיד 5 מספרים אם אפשר)
    if (currentPage <= 3) {
        end = Math.min(totalPages, 5);
    }
    if (currentPage >= totalPages - 2) {
        start = Math.max(1, totalPages - 4);
    }

    // Page numbers
    for (let i = start; i <= end; i++) {
        container.innerHTML += `
            <span class="page-btn ${i === currentPage ? 'active' : ''}"
                  onclick="loadPage(${i})">${i}</span>
        `;
    }

    // Next
    container.innerHTML += `
        <span class="page-btn" onclick="loadPage(${Math.min(totalPages, currentPage + 1)})">&gt;</span>
    `;
}

function renderPaginationold(totalPages, currentPage) {
    const container = document.getElementById("paginationControls");
    container.innerHTML = "";

    // Previous button
    container.innerHTML += `
        <span class="page-btn" onclick="loadPage(${Math.max(1, currentPage - 1)})">&lt;</span>
    `;

    // Page numbers (simple: first 3 pages + active state)
    for (let i = 1; i <= Math.min(3, totalPages); i++) {
        container.innerHTML += `
            <span class="page-btn ${i === currentPage ? 'active' : ''}"
                  onclick="loadPage(${i})">${i}</span>
        `;
    }

    // Next button
    container.innerHTML += `
        <span class="page-btn" onclick="loadPage(${Math.min(totalPages, currentPage + 1)})">&gt;</span>
    `;
}


function renderPokemonList(pokemons) {
    const container = document.getElementById("pokemonList");
    container.innerHTML = "";

    pokemons.forEach(p => {
        container.innerHTML += `
            <div class="pokemon-item">
                Name: <span>${p.name.english}</span>
                Type: <span>${p.type}</span>

                <button onclick="event.preventDefault(); loadPokemonPage(${p.id})">Details</button>
                <button onclick="removePokemon(${p.id})">Remove</button>
                <button onclick="addPokemon(${p.id})">Add</button>
            </div>
        `;
    });
}

