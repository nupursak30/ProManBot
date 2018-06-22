# Milestone: DEPLOYMENT

## Deployment  

All the Deployment related scripts can be found in [deploy](https://github.ncsu.edu/dgupta9/ProManBot/tree/DEPLOY/deploy) folder.

### Deployment Steps

#### Amazon EC2 Instance Setup    
Create an Amazon EC2 instance that will host the bot application remotely. Also, note down the key pair (.pem file) generated during the EC2 instantiation process. This key will be required along with the public DNS of this newly created EC2 instance for accessing the machine remotely.

****Ansible Setup and Configuration****   

***Ansible Installation***  
Follow these steps to install Ansible on the configuration server(Config Server VM):

 ```bash
    ansible-box> $ sudo apt-add-repository ppa:ansible/ansible
    ansible-box> $ sudo apt-get update
    ansible-box> $ sudo apt-get install ansible
 ```

***Setting up the inventory file and ssh keys***  
Copy the key pair(.pem file) generated during the Node VM Amazon EC2 instance instantiation in the `aws_ec2.pem` file present in the `~/.ssh/` folder of Config Server VM.   

Also, create an `inventory` file in the `deploy` folder of the Config Server VM  with the following contents: 

```ini    
[production]
<Public_DNS_of_NodeVM> ansible_ssh_user=ubuntu ansible_ssh_private_key_file=~/.ssh/aws_ec2.pem
```

***Setting up the environment variables***  
Using Ansible, setup the environment variables on the Node VM. All the environment variables are looked up by the Ansible Playbook(setup.yml) from the `bot-config.ini` file present in the same folder as that of the Ansible playbook (`deploy` folder)   
The `bot-config.ini` file has the following contents:  
```
[production]
PGHOST= <PostGre_DB_Server_IP>
PGUSER= <PostGre_Database_Username>
PGDATABASE=<PostGre_Database_name>
PROMANTOKEN= <Slack_Token>
SLACK_EMAIL_ID= <Slack_Account_Email_Id>
SLACK_PASSWORD= <Slack_Account_Password>
MOCKON= false
CLIENT_ID= <Slack_Bot_App_ID> 
CLIENT_SECRET= <Slack_Bot_App_SecretToken>
HOSTBASEURL= <Public_DNS_Host_Machine>
PORT_NO= 80
```
***Testing Ansible***   
The Ansible Playbook is saved by the name `setup.yml`
Execute the following command on the Config Server VM to run the ansible playbook:  
    ```ansible-playbook setup.yml -i inventory -s```  
After executing the ansible playbook, ProManBot will get deployed successfully on Amazon EC2 instance (Node VM)

**Deployment Architecture**

![Deploy Architecture Diagram](https://github.ncsu.edu/dgupta9/ProManBot/blob/DEPLOY/Deploy%20Architecture%20Diagram.png)

## Acceptance test instructions  
**TA user account details for Slack :**  
*Slack workspace :* seproject-workspace.slack.com  
*Slack email ID :* csc510promanbot@gmail.com  
*Slack password :* promanbotCSC510  
*Display Name on Slack:* CSC510_TA 

**TA user account details for Trello :**  
*Trello username :* seproject16  
*Trello email ID :* se.project@mail.com  
*Trello password :* promanbot  

* Any changes/updates done by the ProManBot on the Trello cards will be reflected inside the 'AgileTeam' Storyboard on Trello
* User 'CSC510_TA' is already linked to the Trello account and has already joined the `#bots` channel on Slack 
* **Please Note:** Due to one of the limitations mentioned below, sometimes the bot doesn't respond. In case that happens, just restart the entire conversation again (By typing 'Hi @ProManBot')    
* The following instructions are to be executed in the `#bots` channel on Slack

| Use Case No.|Scenario No. | Scenario | Test Steps ( Type (or can Click if click button is available) the steps given below) | Expected Outcome |
|:---|:---|:---|:--------------------------------------------------------------------------------|:--------------------------------------------------------------------------------|
| U1 | S1 | User wants to open a card and add a new checklist item in the card | <ul><li>1. Hi @ProManBot </li><li>2. Open card(Type or click on button) </li><li>3. <Card_Name>(eg. attach preview icon) </li><li>4. Add todo item(Type or Click on button) </li><li>5. <item_name> (eg. new entry) </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'What is the card name?' </li><li>3. If card exists in Trello, Bot replies 'I found that card' and shows card description with the following four options: a. Add todo item   b. Mark a todo item   c. Remove a todo item   d. List checklist items </li><li>4. Bot asks 'Enter the name of the checklist item you want to add:' </li><li>5. When user enters the new checklist item name, Bot replies'I have added the checklist item <item_name>' </li><li> In the Trello's AgileTeam storyboard, a new checklist item <item_name> will be added in the card <Card_Name> </li></ul> |
| U1 | S2 | User wants to open a card and list all the checklist items present in the card | <ul><li>1. Hi @ProManBot </li><li>2. Open card(Type or click on button) </li><li>3. <Card_Name>(eg. attach preview icon) </li><li>4. List checklist items(Type or Click on button </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'What is the card name?' </li><li>3. If card exists in Trello, Bot replies 'I found that card' and shows card description with the following four options: a. Add todo item   b. Mark a todo item   c. Remove a todo item   d. List checklist items </li><li>4. Bot lists checklist items present in the card </li><li> In the Trello's AgileTeam storyboard, the checklist items present in the card <Card_Name> will be same as that displayed by the Bot on Slack </li></ul> |
| U1 | S3 | User wants to open a card and mark a checklist item of the card | <ul><li>1. Hi @ProManBot </li><li>2. Open card(Type or click on button) </li><li>3. <Card_Name>(eg. attach preview icon) </li><li>4. Mark a todo item(Type or Click on button) </li><li>5. <item_name> (eg. item1) </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'What is the card name?' </li><li>3. If card exists in Trello, Bot replies 'I found that card' and shows card description with the following four options: a. Add todo item   b. Mark a todo item   c. Remove a todo item   d. List checklist items </li><li>4. Bot asks 'Enter the name of the checklist item you want to mark:' </li><li>5. If the checklist item specified by the user exists inside the card, Bot replies'I have marked the checklist item <item_name>' </li><li> In the Trello's AgileTeam storyboard, the checklist item <item_name> will be marked in the card <Card_Name> </li></ul> |
| U1 | S4 | User wants to open a card and remove a checklist item from the card | <ul><li>1. Hi @ProManBot </li><li>2. Open card(Type or click on button) </li><li>3. <Card_Name>(eg. attach preview icon) </li><li>4. Remove a todo item(Type or Click on button) </li><li>5. <item_name> (eg. item1) </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'What is the card name?' </li><li>3. If card exists in Trello, Bot replies 'I found that card' and shows card description with the following four options: a. Add todo item   b. Mark a todo item   c. Remove a todo item   d. List checklist items </li><li>4. Bot asks 'Enter the name of the checklist item you want to delete:' </li><li>5. If the checklist item specified by the user exists inside the card, Bot replies'I have deleted the checklist item <item_name>' </li><li> In the Trello's AgileTeam storyboard, the checklist item <item_name> will be deleted and be no longer present in the card <Card_Name> </li></ul> |
| U1 | S5 | User wants to open a card that is not present in the story board | <ul><li>1. Hi @ProManBot </li><li>2. Open card(Type or click on button) </li><li>3. <Card_Name>(eg. dummy card) </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'What is the card name?' </li><li>3. As the card doesn't exist in Trello, Bot replies 'I couldn't find the card name <Card_Name> in your story board' and gives instructions to restart the conversation </li></ul> |
| U1 | S6 | User wants to open a card and mark/remove a checklist item from the card (which is not present in the card) | <ul><li>1. Hi @ProManBot </li><li>2. Open card(Type or click on button) </li><li>3. <Card_Name>(eg. attach preview icon) </li><li>4. Mark/Remove a todo item(Type or Click on button) </li><li>5. <item_name> (eg. dummy item) </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'What is the card name?' </li><li>3. If card exists in Trello, Bot replies 'I found that card' and shows card description with the following four options: a. Add todo item   b. Mark a todo item   c. Remove a todo item   d. List checklist items </li><li>4. Bot asks 'Enter the name of the checklist item you want to mark/delete:' </li><li>5. As the checklist item doesn't exist in the card, Bot replies 'Item <item_name> is not present. Verify that you have entered the correct item name and also verify that the checklist item is present in the specified card' and gives instructions to restart the conversation </li></ul> |
| U2 | S1 | User wants to open a card and send notification to all card members | <ul><li>1. Hi @ProManBot </li><li>2. Send notification(Type or click on button) </li><li>3. <Card_Name>(eg. attach preview icon) </li><li>4. <user_message>(Type the notification message) </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification  </li><li>2. Bot asks 'Can you provide the card name?' </li><li>3. If card exists in Trello, Bot replies 'Here is the card' and provides card description and asks the user 'Enter the message' </li><li>4. Bot replies, 'I have sent your message to all members of this card' </li><li> In the Trello card <Card_Name>, the message that is sent as a notification to all the card members can be seen under the `Activity` section present inside the card </li></ul> | 
| U3 | S1 | User wants to view weekly summary of due and completed cards for the default current week date range | <ul><li>1. Hi @ProManBot </li><li>2. Create weekly summary </li><li> 3. no </li></ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification </li><li>2. Bot replies 'Creating weekly summary from 11/27/2017 to 12/03/2017 , would you like to change dates?' </li><li>3. If cards exists in Trello for the date range Bot replies 'Here is the summary of complete and incomplete cards for the given period!' and shows complete and incomplete cards, If cards does not exist Bot replies 'No cards found for the given date range'</li><li> Cards present in the 'Done' list of Trello's 'AgileTeam' board which have their proposed due dates within the date range mentioned by the user, will be displayed under 'Completed Cards' on Slack </li><li>Cards present in the 'To Do' and 'Doing' lists of Trello's 'AgileTeam' board which have their proposed due dates within the date range mentioned by the user, will be displayed under 'Due Cards' on Slack </li></ul> |
| U3 | S2 | User wants to view weekly summary of due and completed cards for different date range |<li>1. Hi @ProManBot </li><li>2. Create weekly summary</li><li>3. Yes from 08/08/2017 to 12/07/2017</ul> | <ul><li>1. Bot replies with three options: a. Open a card    b. Create weekly summary   c. Send notification </li><li>2. Bot replies 'Creating weekly summary from 11/27/2017 to 12/03/2017 , would you like to change dates?'</li><li>3. If cards exists in Trello for the date range Bot replies 'Here is the summary of complete and incomplete cards for the given period!' and shows complete and incomplete cards, If cards does not exist Bot replies 'No cards found for the given date range' </li><li> Cards present in the 'Done' list of Trello's 'AgileTeam' board which have their proposed due dates within the date range mentioned by the user, will be displayed under 'Completed Cards' on Slack </li><li>Cards present in the 'To Do' and 'Doing' lists of Trello's 'AgileTeam' board which have their proposed due dates within the date range mentioned by the user, will be displayed under 'Due Cards' on Slack </li></ul> |

***Limitations***  
* Since we are using quit, abort, end etc. keywords to close the conversation with the Bot anytime, we can not use the same keywords as the card or checklist item name.
* For usecase-3 the date format should be in MM/DD/YYYY format.
* Trello card names and todo items are case-sensitive.
* Due to [issue](https://github.com/howdyai/botkit/issues/719), sometimes, the conversation messages are not displayed on the Slack.

### Task Tracking
To summarize all the issues with weekly progress, we have created a [WORKSHEET.md](https://github.ncsu.edu/dgupta9/ProManBot/blob/DEPLOY/WORKSHEET.md) file.

### Screen cast
The screencast has been uploaded where the configuration management tool 'Ansible' deploys the bot on Amazon AWS : [Link](https://www.youtube.com/watch?v=PU_fi-foZEc)
