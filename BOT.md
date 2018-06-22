## Bot

In this Milestone, we have implemented a bot based on our proposed design and use cases. We have used Slack as our bot interaction platform, Node.js as our backend server, PostGre database for persistence, BotKit.js for bot interactions module, Nock along with JSON for mocking REST APIs and Selenium for testing the end to end use case. Below we will describe every component implemented in this milestone in details.

### Use Case Refinement

Based on the feedback from design milestone, we have updated the use cases in our bot. Also, during the implementation, we found various flows inside a use case as trivial and not adding much value to the conversations and hence were modified. Below are the updated use cases:

1. USE CASE 1 : Flow of events to interact with a todo/checklist items of a card.
```
=> Prerequisite: User has already created cards on a Trello Storyboard either using Trello website or an external application.

=> Main Flow: User asks the bot to open a card by providing card name.The user is shown the matching card along with descriptions[E1] and displayed options to either add[S1], list[S2], mark[S3] or remove a todo/checklist item[S4].

=> Sub Flows:
 - [S1] User asks the bot to add todo item on the card and provides the name of the new todo item to be added. Bot adds a new todo item to the card on Trello and closes the conversation.
 - [S2] User asks to list all the todo items. Bot lists all the todo items (checklist items) attached to the card and closes the conversation.
 - [S3] User asks the bot to mark a todo item as completed, by providing todo item name[E2]. Bot informs the user about the update and closes the conversation.
 - [S4] User asks the bot to remove a todo item, by providing todo item name[E2]. Bot informs the user about the removal and closes the conversation.
  
=> Alternate Flows: 
 - [E1] If no card exists matching the name, the user is shown the error message "I couldn't find the card name <CARD_NAME> in your storyboard" and the bot closes the conversation.
 - [E2] If no todo item with such name exists, the user is shown the error message "Item <TODO_ITEM_NAME> is not present" and the bot asks the user to verify that the specified todo item name is correct and also to verify that the todo item is present in the chosen card and after this, bot closes the conversation.

```
2. USE CASE 2 : Reminding members of a card through Notifications.
```
=> Prerequisite:
- Card exist in the Trello board and is already attached to a team member.

=> Main Flow:
- The manager asks the bot to remind all members attached to a certain card[E1] by providing a message such as 'card due soon' or 'demo coming up next week' etc. Bot adds a comment to the card by mentioning '@card' which in turn sends the notification to all members attached to given card.

=> Sub Flows:   
- None

=> Alternative Flows:   
- [E1] If no card exists matching the name, the user is shown the error message "I couldn't find the card name <CARD_NAME> in your storyboard" and the bot closes the conversation.
```
3. USE CASE 3 : Create weekly summary of completed and incomplete cards
```
=> Prerequisite:
- Cards regarding a board exist in the Trello.
- Team members have updated statuses of their cards.
                 
=> Main Flow:
- Manager asks the bot to create a weekly summary of completed and incomplete cards[E1][E2].

=> Sub Flows:   
- [S1] By default bot will start creating summary for current week starting from Monday as first day of the week and ask manager if they want to create summary of current week or any other week[E1][E2].
- [S2] If manager responds with other date range, bot will start preparing summary of the cards for the date range specified by manager and display it[E1][E2].

=> Alternative Flows:   
- [E1] If team members have not updated status of any of the task for that week, bot will display all the tasks as incomplete even if the due date has passed. 
- [E2] If no card is available for the week or date duration mentioned by the manager, an error is printed saying no cards found for the specified duration.
```


### Mocking

Mocking is a simulation technique which provides a Restful environment without the need to make a real call and it is very useful during the initial phase of a project development when the use cases keeps on changing. In this project, we have used 'Nock' to intercept REST API calls and instead of calling actual REST API, we are sending a response from a predefined JSON file. 

                                                      
                                                      
                                                         ____NOCK Package____                 
     _____________________                                      |                             __________________
    |                     |                                     |                            |  REST API SERVER |
    |   NodeJS Server     | ______ REST API CALL __________>    | ____ X                     |__________________|
    |_____________________|                                     |
                                                                | === +
                  ______________________                        |     |
                 |   Mocking Component  |  <==========================+
                 |______________________|
                 
                 
Mocking components are defined in [mock.js](src/mock.js) and the mocking feature is activated by setting the environment variable "MOCKON" value as "true". We have implemented mocking feature for the following REST API's:

1. Mock for story board
   * Url: https://api.trello.com/1/members/me/boards/id
   * Method: GET
2. Mock for all the lists in a board
    * Url: https://api.trello.com/1/boards/id/lists
    * Method: GET
3. Mock for getting all the cards in a List
    * Url: https://api.trello.com/1/batch
    * Method: GET
4. Mock to retrieve checklist items
    * Url: https://api.trello.com/1/checklists/id/checkItems
    * Method: GET
5. Mock to add checklist items
    * Url: https://api.trello.com/1/checklists/id/checkItems
    * Method: POST
6. Mock to remove a checklist item
    * Url: https://api.trello.com/1/checklists/id/checkItems/id
    * Method: DELETE
7. Mock for marking an item present in the checklist of a card
    * Url: https://api.trello.com/1/cards/id/checkItem/id
    * Method: PUT
8. Mock for adding a comment to a card
    * Url: https://api.trello.com/1/cards/id/actions/comments/
    * Method: POST

### Bot Implementation

#### Bot Platform
For our bot platform, we have used slack.com as the user interface and we are using "botkit.js" library to handle Real Time Messaging (RTM). Also, the following environment variables needs to be set for proper functioning of the bot:
1. PROMANTOKEN (Slack Token)
2. PGHOST (PostGre DB Server IP) 
3. PGDATABASE (PostGre Database name)
4. PGUSER (username for accessing PostGre Database )
5. PGPASSWORD (password for accessing PostGre Database)
6. SLACK_EMAIL_ID (For Selenium Testing)
7. SLACK_PASSWORD (For Selenium Testing)
8. MOCKON (For Mocking: Mocking is enabled if set to true)

#### Bot Integration
The bot is deployed on Slack. User starts the interaction with the bot by typing in 'hi/hey/hey promanbot'. All the user-bot interactions taking place for all use cases are defined in [botInteractions.js](https://github.ncsu.edu/dgupta9/ProManBot/blob/master/src/botInteractions.js) file. Bot uses "convo.ask()" function whenever it wants to ask questions to the user and it uses convo.say() function to respond back to the user queries. For performing REST API calls, we are using the 'Request' library which provides the required functionality and this is implemented in the [restAPIHelper.js](https://github.ncsu.edu/dgupta9/ProManBot/blob/master/src/restAPIHelper.js) file.


### Selenium Testing

Selenium testing is a tool which automates the testing of the functionality of a use case end to end.
In this milestone we have implemented Selenium Unit test cases to test every main flow as well as sub flows of our proposed design.
We have implemented Selenium test cases as per selenium testing user guide and lecture notes and integrated with our Bot's Slack environment.
For every use case, Selenium will interact with our Bot on a Slack channel and give certain commands to the Bot and verify Bot's responses to check if the Bot is responding as desired. This way it is easier for us to find bugs and edge cases in our design and verify the overall functionality of the underlying implementation. The source code for all the Selenium test cases can be accessed from [Selenium](Selenium) folder. 


### Task Tracking

In this project we have used Continuous Integration Agile practice to accomplish our milestones and Github Issues to track progress of each individual task. To summarize all the issues with weekly progress, we have created a [WORKSHEET.md](https://github.ncsu.edu/dgupta9/ProManBot/blob/master/WORKSHEET.md) file which is subdivided into Weeks and Use Cases. 

#### Stories and Tasks

To follow Agile team methodologies, we have used stories to divide tasks amongst each team member. We have achieved this by planning the implementation & work flow in advance and breaking every feature into smaller components called stories.
We have used git issues to track all the stories and each story is assigned story points which indicates how much effort and time is required to finish that particular story/task. This makes it easier to track progress of every team member as well as overall project.
Every team member is assigned tasks in such a way that everyone contributed equally towards every technology used in the project such as Bot Interaction Messaging API or Nock Mocking API or Selenium testing, etc.

### Source Code

The project source code is accessible from [src](src) folder. The src folder also has a [mocks/mock.json](src/mocks/mock.json) file which has all the Mock Response Payloads.


###  Screencast
Screencast URL: [Link](https://www.youtube.com/watch?v=VRpnJMoJPYI&feature=em-share_video_user)
