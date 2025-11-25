function openLoginModal() {
    document.getElementById("pokemonModal").style.display = "block";
}

function closeLoginModal() {
    document.getElementById("pokemonModal").style.display = "none";
}


async function addPokemon(pokemonId) {
    try {
        // Get the JWT token from localStorage
        const token = localStorage.getItem("jwt"); // adjust key name if different
console.log("Token:", token);
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

        if (!response.ok) {
            throw new Error(`API error: ${response.status}`);
        }

        const data = await response.json();
        console.log("Pokemon added:", data);
        return data;
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
