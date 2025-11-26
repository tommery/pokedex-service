document.addEventListener("DOMContentLoaded", () => {
    loadCollection();
});

async function loadCollection() {
    try {
        const token = localStorage.getItem("jwt"); 
        if (!token) {
            alert("Please log in to view your collection.");
            return;
        }

        const response = await fetch("./api/v1/collection", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error(`Error: ${response.status}`);

        const pokemons = await response.json();
        renderCollection(pokemons);

    } catch (error) {
        console.error("Failed to load collection:", error);
    }
}

function renderCollection(pokemons) {
    const container = document.getElementById("collectionList");
    container.innerHTML = "";

    if (!pokemons || pokemons.length === 0) {
        container.innerHTML = "<p>Your collection is empty.</p>";
        return;
    }

    pokemons.forEach(p => {
        container.innerHTML += `
            <div class="pokemon-item">
                <img src="${p.image.thumbnail}" alt="${p.name.english}">
                <h3>${p.name.english}</h3>
                <p>Type: ${p.type}</p>
                <button onclick="removePokemon(${p.id})">Remove</button>
            </div>
        `;
    });
}

// Example removePokemon function
async function removePokemon(pokemonId) {
    try {
        const token = localStorage.getItem("jwt");
        const response = await fetch(`./api/v1/collection/${pokemonId}`, {
            method: "DELETE",
            headers: { "Authorization": `Bearer ${token}` }
        });
        if (!response.ok) throw new Error(`Failed to remove: ${response.status}`);
        loadCollection(); // Refresh after removal
    } catch (error) {
        console.error(error);
    }
}
