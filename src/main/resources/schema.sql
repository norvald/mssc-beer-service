CREATE TABLE BEER (
  id                 varchar(40) not null,
  beer_name          varchar(255) not null,
  beer_style         varchar(255),
  created_date       DATE not null,
  last_modified_date DATE not null,
  min_on_hand        int,
  quantity_to_brew   int,
  price              numeric,
  upc                varchar(255),
  version            int,
  primary key (id)
);