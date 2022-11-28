package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerListCache", condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, Boolean showInventoryOnHand, PageRequest pageRequest) {
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        log.debug("listBeers called");

        if(StringUtils.hasText(beerName) && beerStyle!=null) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if(StringUtils.hasText(beerName) && beerStyle==null) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if(!StringUtils.hasText(beerName) && beerStyle!=null) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage= beerRepository.findAll(pageRequest);
        }
        if (showInventoryOnHand) {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDtoWithInventory)
                    .collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        } else {
            beerPagedList = new BeerPagedList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::beerToBeerDto)
                    .collect(Collectors.toList()),
                    PageRequest.of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());
        }
        return beerPagedList;
    }

    @Cacheable(cacheNames = "beerUpcCache", key= "#upc", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerByUpc(String upc, Boolean showInventoryOnHand) {
        log.debug("getBeerByUpc called");

        if(showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository
                            .findOptionalByUpc(upc)
                            .orElseThrow(NotFoundException::new)
            );
        } else {
            return beerMapper.beerToBeerDto(
                    beerRepository
                            .findOptionalByUpc(upc)
                            .orElseThrow(NotFoundException::new)
            );

        }
    }

    @Cacheable(cacheNames = "beerCache", key= "#beerId", condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getBeerById(UUID beerId, Boolean showInventoryOnHand) {
        log.debug("getBeerById called");

        if(showInventoryOnHand) {
            return beerMapper.beerToBeerDtoWithInventory(
                    beerRepository
                            .findById(beerId)
                            .orElseThrow(NotFoundException::new)
            );
        } else {
            return beerMapper.beerToBeerDto(
                    beerRepository
                            .findById(beerId)
                            .orElseThrow(NotFoundException::new)
            );

        }
    }

    @Override
    public BeerDto createBeer(BeerDto beerDto) {
        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Override
    public BeerDto updateBeerById(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);
        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(beerRepository.save(beer));
    }

    @Override
    public void deleteBeerById(UUID beerId) {
        beerRepository.deleteById(beerId);
    }

}
