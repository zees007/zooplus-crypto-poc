# zooplus-crypto-poc

Remote branch :- DEV

How to run the project:
1. Checkout the codebase from DEV branch
2. Run Maven cmd - mvn clean
3. Run the main project 
4. hit http://localhost:8080

Commit History: log.cvs inside project directory

Tect Stack used: spring boot, HTML, CSS, JavaScript
In Memory H2 db
GeoIP DB for country


My Assumptions: 

1. I've used coin maker api to get the all avaiable crypto currencies.
2. I've used GeoLite2 database (GeoLite2-Country.mmdb) from MaxMind to fetch the country and ISO Code of country from IP address.
3. Coin maker api has feature to provide each country currency symbol and sign. I fetched these info and stored in in-memory db.
4. When we select crypto currency from dropdown with IP address, I called coin maker api with crypto currency code which return below data.

<img width="631" alt="image" src="https://user-images.githubusercontent.com/41251660/165867509-2e588790-7cf3-4c5a-8308-5dec97645242.png">

Output Display (UI):
with IP Adresss:
<img width="953" alt="image" src="https://user-images.githubusercontent.com/41251660/165868407-34682684-1d62-454e-8ed9-e59e6c60e542.png">


Without IP Address:
<img width="952" alt="image" src="https://user-images.githubusercontent.com/41251660/165867731-c6040d55-1e70-40de-8f5f-75e2925bf094.png">




