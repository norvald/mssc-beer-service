package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    @GetMapping(produces = {"application/json"}, path = "/beer")
    public ResponseEntity<BeerPagedList> listBeers(@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                   @RequestParam(value = "beerName", required = false) String beerName,
                                                   @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
                                                   @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }

        BeerPagedList beerList = beerService.listBeers(beerName, beerStyle, showInventoryOnHand, PageRequest.of(pageNumber, pageSize));

        return new ResponseEntity<>(beerList, HttpStatus.OK);
    }

    private final BeerService beerService;

    @GetMapping({"/beer/{beerId}"})
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId,
                                               @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        return new ResponseEntity<>(beerService.getBeerById(beerId, showInventoryOnHand), HttpStatus.OK);
    }

    @GetMapping({"/beerUpc/{upc}"})
    public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc,
                                                @RequestParam(value = "showInventoryOnHand", required = false) Boolean showInventoryOnHand) {
        if (showInventoryOnHand == null) {
            showInventoryOnHand = false;
        }
        return new ResponseEntity<>(beerService.getBeerByUpc(upc, showInventoryOnHand), HttpStatus.OK);
    }


    @PostMapping(path = "/beer") // POST - create new beer
    public ResponseEntity createBeer(@RequestBody @Validated BeerDto beerDto) {
        return new ResponseEntity<>(beerService.createBeer(beerDto), HttpStatus.CREATED);
    }

    @PutMapping({"/beer/{beerId}"})
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
        return new ResponseEntity<>(beerService.updateBeerById(beerId, beerDto), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping({"/beer/{beerId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeerById(@PathVariable UUID beerId) {
        beerService.deleteBeerById(beerId);
    }


}
