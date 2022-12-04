package guru.springframework.msscbeerservice.services.inventory;

import guru.springframework.msscbeerservice.domain.Beer;

public interface BeerInventoryService {
    Integer getOnHandInventory(Beer beer);
}
