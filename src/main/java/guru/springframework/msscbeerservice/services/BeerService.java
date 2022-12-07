package guru.springframework.msscbeerservice.services;


import guru.sfg.common.model.BeerDto;
import guru.sfg.common.model.BeerPagedList;
import guru.sfg.common.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {
    BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand);

    BeerDto createBeer(BeerDto beerDto);

    BeerDto updateBeerById(UUID beerId, BeerDto beerDto);

    void deleteBeerById(UUID beerId);

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, Boolean showInventoryOnHand, PageRequest of);

    BeerDto getBeerByUpc(String upc, Boolean showInventoryOnHand);
}
