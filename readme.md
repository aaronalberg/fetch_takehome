# Aaron Alberg Fetch Rewards Receipt Processor Challenge




## Background
The reader is probably already familiar with the assignment prompt, but just in case,
you can find it here: [assignment repo](https://github.com/fetch-rewards/receipt-processor-challenge/tree/main)

### Supported Features:
#### Endpoint: Process Receipts

* Path: `/receipts/process`
* Method: `POST`
* Payload: Receipt JSON
* Response: JSON containing an id for the receipt.

#### Endpoint: Get Points

* Path: `/receipts/{id}/points`
* Method: `GET`
* Response: A JSON object containing the number of points awarded.


#### Rules

These rules collectively define how many points should be awarded to a receipt.

* One point for every alphanumeric character in the retailer name.
* 50 points if the total is a round dollar amount with no cents.
* 25 points if the total is a multiple of `0.25`.
* 5 points for every two items on the receipt.
* If the trimmed length of the item description is a multiple of 3, multiply the price by `0.2` and round up to the nearest integer. The result is the number of points earned.
* 6 points if the day in the purchase date is odd.
* 10 points if the time of purchase is after 2:00pm and before 4:00pm.

## Running the web server
### Option 1 - Docker
If you are on a device with an arm64 architecture (Apple Silicon Macs):
```
docker pull aaronalberg/fetch-takehome
```
```
docker run --publish 8080:8080 aaronalberg/fetch-takehome
```

If you are on a device with an amd64 architecture (most other personal machines):
```
docker pull aaronalberg/fetch-takehome-amd64
```
```
docker run --publish 8080:8080 aaronalberg/fetch-takehome-amd64
```

### Option 2 - Intellij IDEA
```
git clone https://github.com/aaronalberg/fetch_takehome.git
```
- Download the Intellij IDEA IDE and open the project
  - To run the web server:
    - Navigate to `src/main/java/com/aaronalberg/fetchtakehome/Application.java`
    - Click the 'play' icon next to the main method 
  - To run the test suite:
    - Navigate to `src/test/java/com/aaronalberg/fetchtakehome/ApplicationTests.java`
    - Click the 'play' icon next to the ApplicationTests class declaration



## Assumptions

- Receipts need to have all fields present in order to be valid
- The `total` on the receipt should be equal to the sum of the prices of the items in order to be valid
- Items on a receipt are not unique or persisted in any way
- Receipts need to have at least one item in order to be valid
- Negative values are not discretely handled and may result in undefined behavior
- Dates can be in the past, present, or future


## Sources

- [Spring initializr](https://start.spring.io/) for generic file structure
- [Docker java guide](https://docs.docker.com/language/java/) for docker specific setup and tooling 
(including example repo found in guide).
- Various stackoverflow and baeldung posts for code snippets
