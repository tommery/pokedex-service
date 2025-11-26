package com.sita.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.sita.dto.PagedResult;
import com.sita.dto.PokemonDto;
import com.sita.model.OwnedPokemon;
import com.sita.model.User;
import com.sita.repository.OwnedPokemonRepository;
import com.sita.repository.PokemonJsonRepository;
import com.sita.repository.UserRepository;

import com.sita.dto.NameDto;

@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private PokemonJsonRepository pokemonRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OwnedPokemonRepository ownedRepository;

    @InjectMocks
    private PokemonService pokemonService;

    // --------------------------------------------------
    // getAll()
    // --------------------------------------------------
    @Test
    void testGetAll() {
        PokemonDto p = new PokemonDto();
        when(pokemonRepository.getAllList()).thenReturn(List.of(p));

        List<PokemonDto> result = pokemonService.getAll();
        assertEquals(1, result.size());
    }

    // --------------------------------------------------
    // getFiltered() – filter by name
    // --------------------------------------------------
    @Test
    void testGetFilteredByName() {
        PokemonDto a = makePokemon("Pikachu", "Electric");
        PokemonDto b = makePokemon("Charmander", "Fire");

        when(pokemonRepository.getAllList()).thenReturn(List.of(a, b));

        PagedResult<PokemonDto> result =
                pokemonService.getFiltered("pika", null, 0, 10);

        assertEquals(1, result.getItems().size());
        assertEquals("Pikachu", result.getItems().get(0).getName().getEnglish());
    }

    // filter by type
    @Test
    void testGetFilteredByType() {
        PokemonDto a = makePokemon("Pikachu", "Electric");
        PokemonDto b = makePokemon("Squirtle", "Water");

        when(pokemonRepository.getAllList()).thenReturn(List.of(a, b));

        PagedResult<PokemonDto> result =
                pokemonService.getFiltered(null, "Water", 0, 10);

        assertEquals(1, result.getItems().size());
        assertEquals("Squirtle", result.getItems().get(0).getName().getEnglish());
    }

    // pagination
    @Test
    void testGetFilteredPagination() {
        PokemonDto a = makePokemon("A", "X");
        PokemonDto b = makePokemon("B", "X");

        when(pokemonRepository.getAllList()).thenReturn(List.of(a, b));

        PagedResult<PokemonDto> result =
                pokemonService.getFiltered(null, null, 1, 1);

        assertEquals(1, result.getItems().size());
        assertEquals("B", result.getItems().get(0).getName().getEnglish());
    }

    // --------------------------------------------------
    // getPaged()
    // --------------------------------------------------
    @Test
    void testGetPagedInsideBounds() {
        PokemonDto a = makePokemon("A", "X");
        PokemonDto b = makePokemon("B", "X");

        when(pokemonRepository.getAllList()).thenReturn(List.of(a, b));

        PagedResult<PokemonDto> result =
                pokemonService.getPaged(1, 1);

        assertEquals(1, result.getItems().size());
        assertEquals("B", result.getItems().get(0).getName().getEnglish());
    }

    @Test
    void testGetPagedOutOfBounds() {
        when(pokemonRepository.getAllList()).thenReturn(List.of());

        PagedResult<PokemonDto> result =
                pokemonService.getPaged(5, 10);

        assertEquals(0, result.getItems().size());
    }

    // --------------------------------------------------
    // getUser()
    // --------------------------------------------------
    @Test
    void testGetUserSuccess() {
        User u = new User();
        u.setEmail("ash@test.com");

        when(userRepository.findByEmail("ash@test.com"))
                .thenReturn(Optional.of(u));

        User result = pokemonService.getUser("ash@test.com");
        assertEquals("ash@test.com", result.getEmail());
    }

    @Test
    void testGetUserNotFound() {
        when(userRepository.findByEmail("x@test.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> pokemonService.getUser("x@test.com"));
    }

    // --------------------------------------------------
    // getUserCollection()
    // --------------------------------------------------
    @Test
    void testGetUserCollection() {
        OwnedPokemon op1 = new OwnedPokemon(1L, 10);
        OwnedPokemon op2 = new OwnedPokemon(1L, 20);

        PokemonDto p10 = makePokemon("A", "X");
        PokemonDto p20 = makePokemon("B", "Y");

        when(ownedRepository.findByUserId(1L))
                .thenReturn(List.of(op1, op2));

        when(pokemonRepository.getAllMap())
                .thenReturn(Map.of(10, p10, 20, p20));

        List<PokemonDto> result = pokemonService.getUserCollection(1L);
        assertEquals(2, result.size());
    }

    // --------------------------------------------------
    // addPokemonToUser()
    // --------------------------------------------------
    @Test
    void testAddPokemon_UserNotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);

        String result = pokemonService.addPokemonToUser(1L, 10);
        assertEquals("User not found", result);
    }

    @Test
    void testAddPokemon_PokemonNotFound() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(pokemonRepository.getAllMap()).thenReturn(Map.of());

        String result = pokemonService.addPokemonToUser(1L, 10);
        assertEquals("Pokemon not found", result);
    }

    @Test
    void testAddPokemon_AlreadyOwned() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(pokemonRepository.getAllMap()).thenReturn(Map.of(10, makePokemon("A", "X")));
        when(ownedRepository.existsByUserIdAndPokemonId(1L, 10))
                .thenReturn(true);

        String result = pokemonService.addPokemonToUser(1L, 10);
        assertEquals("User already owns this Pokémon", result);
    }

    @Test
    void testAddPokemon_Success() {
        when(userRepository.existsById(1L)).thenReturn(true);
        when(pokemonRepository.getAllMap()).thenReturn(Map.of(10, makePokemon("A", "X")));
        when(ownedRepository.existsByUserIdAndPokemonId(1L, 10))
                .thenReturn(false);

        String result = pokemonService.addPokemonToUser(1L, 10);

        assertEquals("Pokemon added successfully", result);
        verify(ownedRepository).save(any());
    }

    // --------------------------------------------------
    // removePokemonFromUser()
    // --------------------------------------------------
    @Test
    void testRemovePokemon_NotOwned() {
        when(ownedRepository.existsByUserIdAndPokemonId(1L, 10))
                .thenReturn(false);

        String result = pokemonService.removePokemonFromUser(1L, 10);
        assertEquals("User does not own this Pokémon", result);
    }

    @Test
    void testRemovePokemon_Success() {
        when(ownedRepository.existsByUserIdAndPokemonId(1L, 10))
                .thenReturn(true);

        String result = pokemonService.removePokemonFromUser(1L, 10);
        assertEquals("Pokemon removed successfully", result);

        verify(ownedRepository).deleteByUserIdAndPokemonId(1L, 10);
    }

    // --------------------------------------------------
    // getPokemon()
    // --------------------------------------------------
    @Test
    void testGetPokemon() {
        PokemonDto p = makePokemon("A", "X");
        when(pokemonRepository.getAllMap()).thenReturn(Map.of(10, p));

        PokemonDto result = pokemonService.getPokemon(10);
        assertEquals("A", result.getName().getEnglish());
    }

    @Test
    void testGetPokemon_NotFound() {
        when(pokemonRepository.getAllMap()).thenReturn(Map.of());
        assertNull(pokemonService.getPokemon(10));
    }

    // --------------------------------------------------
    // Helper method to build PokemonDto
    // --------------------------------------------------
        private PokemonDto makePokemon(String englishName, String type) {
        PokemonDto p = new PokemonDto();

        NameDto nameDto = new NameDto();
        nameDto.setEnglish(englishName);
        nameDto.setJapanese(englishName);  // or whatever, not used in tests
        nameDto.setChinese(englishName);

        p.setName(nameDto);
        p.setType(List.of(type));

        return p;
    }

}

