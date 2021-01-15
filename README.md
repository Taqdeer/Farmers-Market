# Farmers' Market
## Context

This project represents a real world scenario of an online market of crops.
The application provides a platform for farmers and MNCs to work together with full transparency without involving any third parties.

- The application will provide a platform to the farmers to display their crops, as well as the price they want for their each specific crop.
- Multi national corporations will be able to look at all the farmers who have signed up on Market app and will be able to directly contact the farmers from whom they want to purchase crops.

### Importance (Interest)

This project has been created after looking at the struggles which farmers face to get the right price for their crops.
Recently, a law has been passed in India to **privatize the agriculture** sector which is being foreseen as a threat to a farmers' land and earnings.
Similar models have been already operating in USA and UK which has resulted in alarming rates of **farmers' suicides**.
Online applications like this may help the farmers to get the **deserved prices** for their crops from MNCs.



## User Stories Phase 1
### User: Farmer
- I want to sign myself up in the online market.
- I want to add crops to my online account
- I want to remove a crop from my online account
- I want to add modify the price, quality and quantity of a specific crop 
- I want to remove myself from the online market.

### User: MNC
- We want to search for the farmers want to sell "rice"
- We want to contact the farmer we picked from the above search results

## User Stories Phase 2
### User: Market Manager
- As market manager, I want to save all the farmers with their specifications who sign up in the online market in a file
- As market manager, I want to load all the signed up farmers from saved file for further operations
- As market manager, I want to print the whole market

Please note: MNCs' have limited access to Market App as they are only allowed to search farmers and contact them.
As farmers' signing up for the market does not depend on MNCs, their data is not relevant to Market and thus neither collected nor saved.

## Phase 4
### Task 2
A checked exception has been added to the Farmer class' method - modifyCrops.
If an invalid trait (i.e. other than quality, quantity or price) is passed as a parameter to change, 
then the method throws an IllegalCropException.
 
### Task 3
UML diagram centers around DrawMarketPanel which is responsible for creating a graphical user interface of the model.
It extends JPanel and implements Action Listener.
It has a market object and the whole idea of this app relies on it. 
Market object keeps list of farmers who sign up in the market and each of those farmers have their list of crops which they want to sell.
It has two more major objects namely JsonReader and JsonWriter, which keep track of saving and loading the data of the market.
Market object also implements the class Writable which is mainly responsible for making a savable and readable version of market's current state.
Main, MarketAppUI are independent classes which run the DrawMarketPanel GUI.
MarketApp class has the same functionality as DrawMarketPanel but only in the console based user interface.

###### Refactoring
- I believe that the model package which consists of Market, Farmer and Crop does not require any refactoring.
  All three classes have very concrete functionality which cannot be refactored further.
- DrawMarketPanel (GUI) is handling a lot of functions which can definitely be refactored into sub-classes.
  If I had time, I would do the following:
  - Create three sub-classes each for Farmer, MNC, and Manager
  - Farmer will be  further divided into two sub-classes - one which will handle the functionality of adding/removing a farmer
    and, the other will deal with the adding, removing, modifying crops of the farmer.
  - MNC class will handle two functions - searching farmers and getting their contacts.
  - Manager class will be responsible for saving, loading, printing the market.
