
This is the customer side of the canteen app. 

In order to run the app, you must do the following: 

1) Download the files in the Stripe API repository and store it in a folder of your choice. 
2) Open up the command prompt and use the "cd" command to change to the folder that contains all of the files of the Stripe API repository. 
3) Enter "node server.js" to run the server.js file 
4) After seeing "Node server listening on port 4242", you can now run the android application, and the Stripe (payment) system should now be working effectively. 
5) When using the app, use the card number "4242 4242 4242 4242", but the rest of the details (e.g. postcode, CVC etc) can be anything. 

Requirements

Registration:
- The user should be able to create an account using their email address and password - Completed

Login/Logout:
-	The user should be able to login to their account using their registered email address and password - Completed
-	The user will not need to re-login (unless they explicitly leave the application) - Completed
-	The user should be able to logout at any time – Completed

Payment:
-	The user should be able to add their card details for payments - Completed
-	The user should be able to delete their card details - Completed

Navigating through the application:
-	The user should be able to select a restaurant/canteen of their choice – Not completed
-	The user should be able to swipe through the menu - Completed
-	The user should be able to search for a particular food item or ingredient - Completed

Adding food:
-	The user should be able to select items on the menu - Completed
-	The user should be able to decide a quantity of their selected item - Completed
-	The user should be able to add their selected item (with quantity) to their basket - Completed
-	The user should be able to view their basket at any time - Completed

Editing/Deleting food :
-	The user should be able to remove items from their basket - Completed
-	The user should be able to change the quantity of a particular item - Completed
-	The user should be able to escape the basket menu and search for more food (to add to their basket) - Completed

After ordering the food:
-	The user should be able to cancel their order (within 15 seconds, for example) after placing their order – Not completed
-	The user should be able to update their order - Not completed
-	The user should be able to view the status of their order - Completed
-	The user should receive a receipt of their order (after they collect it) – Partially completed (since the app can send emails, the employee will have to write the order receipt manually).

General notifications:
-	The user should be notified of currently trending meals – Not completed
-	The user should receive recommendations based on their purchase history – Not completed
