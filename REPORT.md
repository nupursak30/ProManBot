
## REPORT

### Problems Solved

Promanbot is designed to solve many Project Management related issues when it comes to using the Trello Board as it has a lot of limitations on the features related to effective Agile Software Development Process. Promanbot dealt with solving three major issues as follows:
* Trello Card Interactions e.g. Add, Mark, Remove, and listing Checklist/Todo Items of the card
* Sending Real-Time Notifications to all the Members of a Card
* Generating a Weekly Summary of all the Card's Status (Complete and Pending)

--- 

### Primary Features
The bot initializes by asking the first time user to do their Trello account's setup. After the Trello account is setup, the bot shows the main menu, which provides three options to the user.   

![Main Menu](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/intro.png)  

---

#### UseCase1: Allow users to interact with TODO items


**1. Add a new TODO item**   

![Add an item](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC1_add.png)  



**2. Mark TODO items**  

![Mark a item](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC1_mark.png)  



**3. Remove TODO items**  

![Remove an item](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC1_delete.png)  



**4. List All TODO items**  

![List items](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC1_list.png)  

---

#### UseCase2: Send notifications to card member

![Send notifications](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC2.png)  


---

#### UseCase3: Create summary of completed and pending cards

**1. Current week**

![Current week summary](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC3_no.png)  



**2. Custom date range**

![Custom week](https://github.ncsu.edu/dgupta9/ProManBot/blob/REPORT/screenshots/UC3_yes.png)  

---

### Reflection on the development process and project

The development phase of ProManBot was distributed across 5 milestones where every phase had its own set of challenges. During the Design process, we encountered the dilemma of choosing either the Repository or Blackboard Data Centered Architecture, and based on various factors such as user interactions and data handling, we selected Repository Architecture. In our 3 uses cases, we were able to precisely describe the problem, and it aided in task tracking and test planning. One of the difficult decisions we made was evaluating the platform to deploy the bot on and we selected Slack due to its good community support. The bot was developed in **NodeJS** programming language and this enabled us to learn about event based programming. 

During the development phase, we realised that **botkit** library doesn’t support full conversational flow which allows bot to remember previous interactions. To overcome this limitation, we added a ‘**quit**’ conversation functionality which allows users to move out and restart conversation at any point of the interaction. Based on the feedback, we improvised our Bot Interaction’s UX by adding additional components such as ‘Action’ buttons. Since buttons are a part of **interactive components** in the Slack platform, which don’t work using RTM, we had to implement a request URL.

Another challenge was to connect the bot with Trello REST API, which required OAUTH token. To overcome this difficulty, we included the library **client.js** provided by Trello and stored the token securely into the database. 

We chose Amazon EC2 over Heroku and VCL for our Bot deployment as EC2 has 99.99% availability as compared to that of VCL and Heroku on the other hand, has no native support for Ansible. Finally, when we deployed the bot application on the Amazon EC2 instances, we found that bot wasn’t responding due to default security setting on AWS. To resolve this issue, we had to add new firewall rules in **Security Groups** allowing connections for certain ports and addresses. 

Overall, it was a great learning experience and helped us tremendously to hone our skills on NodeJS, AWS, Bot design, Configuration Management, Trello Platform, REST API, and Selenium Testing.

---

### Limitations  
1. Since we are using ‘**quit**’ and ‘**abort**’ keywords to end the conversation with the Bot, we cannot have Trello card or checklist item which hold the same name as these keywords.
2. For usecase-3 the date format should be in **MM/DD/YYYY** format. So, input in any other date format will generate an error. 
Also, Trello card names and todo items are case-sensitive, hence, if the input isn't exact in terms of Case sensitivity with the existing trello card name, the Trello REST APIs won’t recognize it and wont return any entry.  
3. Currently, our project is limited to cards present in a single Trello storyboard and it doesn’t deal with cards across several storyboards simultaneously. 
4. The user needs to type the name of the card/checklist item manually and hence the user needs to know the Trello card/checklist item name beforehand. Currently there is no drop down menu available which can list all the available card/checklist items.

---

### Future Work
1. The User-Bot interaction/conversation can be improved by using advanced Natural Language Processing APIs or Algorithms.
2. Card names and Checklist/Todo items can be displayed in a drop-down with search feature.
3. The application can be further extended to support card interactions across multiple storyboards simultaneously.
4. We can further integrate JIRA and GIT APIs for multi-platform support.
5. The application can also be integrated with Alexa or Facebook messenger apart from Slack platform.

---

### Demonstration

Presentation video : [Link](https://www.youtube.com/watch?v=nusdcVZknBs)
