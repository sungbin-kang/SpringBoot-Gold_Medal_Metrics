# Gold Medal Metrics - SpringBoot Project


## Overview

This is an Olympic metrics reporting web application called Gold Medal Metrics. Gold Medal Metrics allows users to:
- View countries in a list with their population, GDP, and number of Olympic gold medals.
- Sort the list of countries by any of these attributes, as well as alphabetically by name.
- View a detailed description of a country, with statistics on their Olympic wins.
- View a list of every Olympic win a country has with the year, season, winner name, city, and event.
- Sort the list of Olympic wins by any of these attributes.


## Requirements

- Maven 3
- Java 11


## Implementation Details

### Models and Attributes

- `Country`
    - name
    - code
    - gdp
    - population

- `GoldMedal`
    - year
    - city
    - season
    - name
    - country
    - gender
    - sport
    - discipline
    - event

### RestController Methods

Public methods:

- `getCountries`
- `getCountryDetails`
- `getCountryMedalsList`

Private methods:

- `getCountryMedalsListResponse`
- `getCountryDetailsResponse`
- `getCountrySummaries`
- `sortByMedalCount`
- `getCountrySummariesWithMedalCount`


## How to Use Application

### To start application

For Mac, `./mvnw spring-boot:run`

For Window, `mvnw spring-boot:run`


### With web application


### With curl to Render JSON

**List of Countries**

`curl "localhost:3001/countries?sort_by={sort_by_value}&ascending={ascending_value}"`

User must specify sort_by value with "year", "city", "season", "name", "country", "gender", "sport", "discipline" or "event", and ascending value with "y" or "n". 

For example: 

`curl "localhost:3001/countries?sort_by=name&ascending=y"`
<img src= "" width="500" />
<img width="897" alt="List of Countries" src="https://user-images.githubusercontent.com/72234428/153035065-9f076f79-2ae5-47b6-9b41-1bd4e19c0b54.png">


**Specific Country Details**

User can type specific country to see the details.
`curl "localhost:3001/countries/{countryName}"`

For example:

`curl "localhost:3001/countries/Australia"`

<img src= "" width="500" />


**Specific Country Gold Medal List**

User must specify sort_by value with "year", "city", "season", "name", "country", "gender", "sport", "discipline" or "event", and ascending value with "y" or "n". 

`curl "localhost:3001/countries/{countryName}/medals?sort_by={sort_by_value}&ascending={ascending_value}"`


For example: 

`curl "localhost:3001/countries/Australia/medals?sort_by=year&ascending=n"`

<img src= "" width="500" />
