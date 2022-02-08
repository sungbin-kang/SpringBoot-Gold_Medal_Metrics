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

https://user-images.githubusercontent.com/72234428/153036257-301dfa0b-7741-4179-baad-fe463af9c96f.mov



### With curl to Render JSON

**List of Countries**

`curl "localhost:3001/countries?sort_by={sort_by_value}&ascending={ascending_value}"`

User must specify sort_by value with "year", "city", "season", "name", "country", "gender", "sport", "discipline" or "event", and ascending value with "y" or "n". 

For example: 

`curl "localhost:3001/countries?sort_by=name&ascending=y"`

<img width="897" alt="List of Countries" src="https://user-images.githubusercontent.com/72234428/153035065-9f076f79-2ae5-47b6-9b41-1bd4e19c0b54.png">


**Specific Country Details**

User can type specific country to see the details.
`curl "localhost:3001/countries/{countryName}"`

For example:

`curl "localhost:3001/countries/Australia"`

<img width="897" alt="Specific Country Details" src="https://user-images.githubusercontent.com/72234428/153035203-b562f134-5e9b-49f5-8e10-19824ecfec3b.png">


**Specific Country Gold Medal List**

User must specify sort_by value with "year", "city", "season", "name", "country", "gender", "sport", "discipline" or "event", and ascending value with "y" or "n". 

`curl "localhost:3001/countries/{countryName}/medals?sort_by={sort_by_value}&ascending={ascending_value}"`


For example: 

`curl "localhost:3001/countries/Australia/medals?sort_by=year&ascending=n"`

<img width="956" alt="Specific Country Gold Medal List" src="https://user-images.githubusercontent.com/72234428/153035257-d9236396-aac2-4190-b50a-941e4af76a31.png">

