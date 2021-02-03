package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by jt on 6/13/20.
 */
@SpringBootTest
public class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    public Beer beerTeDelete() {
        Random rand = new Random();
        return beerRepository.saveAndFlush(
                Beer.builder()
                    .beerName("Delete my beer")
                    .beerStyle(BeerStyleEnum.IPA)
                    .minOnHand(12)
                    .quantityToBrew(200)
                    .upc(String.valueOf(rand.nextInt(99999999)))
                    .build()
        );
    }

    @Test
    void deleteBeerHttpBasic() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/" + beerTeDelete().getId())
                .with(httpBasic("spring", "guru")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void deleteBeerHttpBasicUserRole() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerHttpBasicCustomerRole() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311")
                .with(httpBasic("scott", "tiger")))
                .andExpect(status().isForbidden());
    }

    @Test
    void deleteBeerNoAuth() throws Exception{
        mockMvc.perform(delete("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeers() throws Exception{
        mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerById() throws Exception{
        mockMvc.perform(get("/api/v1/beer/" + beerTeDelete().getId()))
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcADMIN() throws Exception{
        mockMvc.perform(
                    get("/api/v1/beerUpc/0631234200036")
                            .with(httpBasic("spring", "guru"))
                )
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcUSER() throws Exception{
        mockMvc.perform(
                    get("/api/v1/beerUpc/0631234200036")
                            .with(httpBasic("user", "password"))
                )
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcCUSTOMER() throws Exception{
        mockMvc.perform(
                    get("/api/v1/beerUpc/0631234200036")
                            .with(httpBasic("scott", "tiger"))
                )
                .andExpect(status().isOk());
    }

    @Test
    void findBeerByUpcNoAuth() throws Exception{
        mockMvc.perform(get("/api/v1/beerUpc/0631234200036"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void findBeerFormADMIN() throws Exception{
        mockMvc.perform(
                    get("/beers")
                        .param("beerName", "")
                        .with(httpBasic("spring", "guru"))
                ).andExpect(status().isOk());
    }

    @Test
    void findBeerFormUSER() throws Exception{
        mockMvc.perform(
                get("/beers")
                        .param("beerName", "")
                        .with(httpBasic("user", "password"))
        ).andExpect(status().isOk());
    }

    @Test
    void findBeerFormCUSTOMER() throws Exception{
        mockMvc.perform(
                get("/beers")
                        .param("beerName", "")
                        .with(httpBasic("scott", "tiger"))
        ).andExpect(status().isOk());
    }

    @Test
    void findBeerFormNoAuth() throws Exception{
        mockMvc.perform(
                get("/beers")
                        .param("beerName", "")
        ).andExpect(status().isUnauthorized());
    }
}
