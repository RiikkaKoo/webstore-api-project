# VERKKOKAUPPA TIETOKANTA & API HARJOITUSTYÖ

## ALOITUS

## API-PÄÄTEPISTEET



Base URL:

http://localhost:8090/api

### Asiakkaat (Customers)

| Metodi   | Päätepiste                | Kuvaus                            |
|----------|---------------------------|-----------------------------------|
| `GET`    | `/customers`              | Hakee kaikki asiakkaat            |
| `GET`    | `/customers/{id}`         | Hakee asiakkaan ID:n perusteella  |
| `GET`    | `/customers/{id}/address` | Hakee asiakkaan osoitteen         |
| `POST`   | `/customers`              | Lisää uuden asiakkaan             |
| `POST`   | `/customers/{id}/address` | Lisää asiakkaalle uuden osoitteen |
| `PUT`    | `/customers/{id}`         | Päivittää asiakkaan tiedot        |
| `PUT`    | `/customers/{id}/address` | Päivittää asiakkaan osoitteen     |
| `DELETE` | `/customers/{id}`         | Poistaa asiakkaan                 |
| `DELETE` | `/customers/{id}/address` | Poistaa asiakkaan osoitteen       |


### Tuotteet (Products)

| Metodi   | Päätepiste               | Kuvaus                                                 |
|----------|--------------------------|--------------------------------------------------------|
| `GET`    | `/products`              | Hakee kaikki tuotteet                                  |
| `GET`    | `/products/{id}`         | Hakee tuotteen ID:n perusteella                        |
| `GET`    | `/products/removed`      | Hakee valikoimasta poistetut tuotteet                  |
| `GET`    | `/products/removed/{id}` | Hakee valikoimasta poistetun tuotteen ID:n perusteella |
| `POST`   | `/products`              | Lisää uuden tuotteen                                   |
| `PUT`    | `/products/{id}`         | Päivittää tuotteen tiedot                              |
| `DELETE` | `/products/{id}`         | Poistaa tuotteen valikoimasta                          |

## TIETOKANNAN OMINAISUUDET