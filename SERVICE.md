# Milestone: SERVICE
### Service
As we are dealing with the actual REST APIs in this milestone instead of using the mocking functionality implemented in the previous milestone, the environment variable 'MOCKON' is set to 'false' so as to invoke the actual REST API calls.

For this milestone and for our project, we have used the following REST APIs to fetch data from Trello and perform necessary actions using the Slack Bot.

### Rest API Details
To perform HTTP calls to REST API, we have used the module "request" and [restAPIHelper.js](src/restAPIHelper.js) contains all the modules to perform and parse JSON response from Trello server.

#### Use Case #1
* REST-API call to fetch and list all the Trello Boards that a member has access to: 
  * URL : https://api.trello.com/1/members/me/boards?key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : GET
  
* REST-API call to fetch all the lists present within a Trello's story board: 
  * URL : https://api.trello.com/1/boards/{BOARD_ID}/lists?key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : GET
  
* REST-API call to fetch all the cards present within a Trello's story board: 
  * URL : https://api.trello.com/1/batch/?urls={LISTS_IDS}&key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : GET
  
* REST-API call to add a new checklist item on the Trello card: 
  * URL : https://api.trello.com/1/checklists/{CHECKLIST_ID}/checkItems?name={CHECKLIST_ITEM_NAME}&key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : POST
  
* REST-API call to mark a checklist item already present on the Trello card: 
  * URL : https://api.trello.com/1/cards/{CARD_ID}/checkItem/{ITEM_ID}?state=complete&key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : PUT
  
* REST-API call to remove a checklist item already present on the Trello card:  
  * URL : https://api.trello.com/1/checklists/{CHECKLIST_ID}/checkItems/{ITEM_ID}?key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : DELETE
  
* REST-API call to list all the checklist items present on the Trello card:  
  * URL : https://api.trello.com/1/checklists/{CHECKLIST_ID}/checkItems?key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : GET

#### Use Case #2
* A new comment is added on the card to send notifications about tasks to all the members attached to a Trello card.   
* REST-API call to add a new comment on the card:  
  * URL : https://api.trello.com/1/cards/{CARD_ID}/actions/comments?text={COMMENT_VALUE}&key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : POST

#### Use Case #3

* In order to create a weekly summary of all the complete and incomplete cards, all the cards present within a Trello's story board needs to be fetched. 
* REST-API call to fetch all the cards present within a Trello's story board: 
  * URL : https://api.trello.com/1/batch/?urls={LISTS_IDS}&key={APP_KEY}&token={TOKEN_VALUE} 
  * METHOD : GET
* After fetching all the cards, we filter the cards based on the card date to check whether the card is in the given range and categorize it in one of the two lists i.e. completed cards list or due card list.

### Authentication
To invoke REST API's on Trello, bot need to have a user token associated with the respective trello account. Trello support 2 ways to authenticate client, either via authorize route using client.js or implementing basic OAUTH flow. We used the first method since the client.js handles all the interaction with Trello and generates a token which is as good as an OAUTH token and is supported by all the Trello REST API's. We store this token in the database inside the table "SlackTrelloUserMap". The [authenticationHelper.js](src/authenticationHelper.js) contains all the modules to handle authentication.

_Below is the flow diagram:_
```
 ____________                                   ____________________                        __________________
|  ProManBot | (1)User Intracts first time     |ProManBot Auth Page | (2)Redirects to      |                  |
| (Slack UI) |----& is asked to authenticate-->|  load client.js    |------------------->  | Trello Auth Page |
|____________|     with Trello                 |____________________|  Trello Auth Page    |__________________|
          A                                          |         A                                      |
          |                                          |         |                                      |
          |_______________(4)Client.js parses  ______|         |__________(3)User Cicks on____________|
                            response and sends                               "Allow"
                            back token to bot
```

### Interactive Components
To improve the user interface of the bot, we have various interactive  components that allow user to perform operations by just clicking the option. This feature provided an additional option to the user in addition to the regular chat interaction. In our implementation, we have included buttons that respond the same way as if the user had typed their conversation. However, Interactive Components required following components:
  * **Public Request URL**: When a button is clicked, Slack platform sends a request to a URL to notify the action. However, since we are currently running our bot on local server, we require a proxy service to redirect the call from public network to the localhost server. Hence, we used NGROK which creates a tunnel between our localhost and the public webserver. 
  
  ```
     ____________                                  _________________                        ____________________
    |            |    Button is clicked to        |  NGROK Public   |     SECURED TUNNEL   | Bot Server running |
    | Slack      | ---------------------------    |   Address       |  =================== | on local server    |
    |____________|    respond to bot query        |_________________|                      |____________________|
  
  ```
  
  * **Bot reply as attachments**: To display buttons on the Slack Chat Interface, we sent the button information in the form of attachments in convo.ask(). The attachment is a predefined JSON message which is used to generate parameters such as button name and response value. 

### Screen cast
The screencast has been uploaded where all the bot interactions are shown alongwith their effect on Trello : [Link](https://www.youtube.com/watch?v=jvNUr7_CCwU)

### Task Tracking
To summarize all the issues with weekly progress, we have created a [WORKSHEET.md](https://github.ncsu.edu/dgupta9/ProManBot/blob/master/WORKSHEET.md) file which is subdivided into Weeks and Use Cases.
