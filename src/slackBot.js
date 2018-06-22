// Importing the 'botkit' library
var Botkit = require('botkit');
var randomstring = require("randomstring");

var dbHelper = require('./databaseHelper.js');
var botInteractions = require('./botInteractions.js')
var mock = require('./mock.js');
var authentication = require('./authenticationHelper.js');

var controller = Botkit.slackbot({
  interactive_replies: true,
  debug: false  
});


controller.setupWebserver(process.env.PORT_NO,function(err,webserver) {
  controller.createWebhookEndpoints(webserver);
  authentication.setupWebhookEndpoints(webserver);
});


//fetch trello App key from db
dbHelper.setupTrelloAppKey(function(appkey){
  if(typeof appkey != 'undefined'){
    global.APP_KEY = appkey;
    global.TRELLO_TOKEN_MAP = {}; // initialize the map
    global.OAUTH_NOUNCE = {}; // initialize the oauth nounce to user map

    
    // initialize mocking
    if(process.env.MOCKON=="true"){
      mock.startMock();
      console.log("Mocking REST API is Enabled");
    }else{
      console.log("Mocking REST API is Disabled");
    }


    // connect the bot to a stream of messages
    controller.spawn({
      token: process.env.PROMANTOKEN,
    }).startRTM()
  }else{
    console.log("No Trello APP key. Please generate one before running this bot.")
  }
});


// Default Bot Invocation
controller.hears([/hey/i,/hi/i,/hey promanbot/i],['mention', 'direct_message', 'direct_mention'], function(bot,message) 
{
  //console.log("#SLACK USER ID: "+message.user);
  // check for user trello account link
  
  // Needed for interaction button
  var dummyBot = {
    token: process.env.PROMANTOKEN,
    user_id: message.user,
    createdBy: message.user,
    app_token: process.env.PROMANTOKEN,
    name: 'ProManBot',
  }; 

  controller.storage.teams.save({id: message.team, foo:'bar', bot: dummyBot}, function(err) { console.log(err) });

  if(typeof global.TRELLO_TOKEN_MAP[message.user] != 'undefined'){
    // ALL Good
    startMainThread(bot,message);
  }else{
    // trello token either in DB or Trello Account not setup at all
    dbHelper.getTrelloToken(message.user,function(trelloToken){
      //console.log("#TRELLO TOKEN : "+trelloToken);
      if (typeof trelloToken != 'undefined'){
        // return to usual logic
          global.TRELLO_TOKEN_MAP[message.user] = trelloToken;
          startMainThread(bot,message);
      }else{
        // User hasn't setup OAUTH token for trello
        askForSetup(bot, message);
      }
    });
  }
});

function startMainThread(bot, message){
  bot.startConversation(message,function(err,convo) {
    convo.say('Good to see you.');
    convo.ask({
      attachments:[
        {
            title: 'How can I help you?\nType or click the below options',
            callback_id: '123',
            attachment_type: 'default',
            color:"#7750a5",
            actions: [
              {
                  "name":"Open a card",
                  "text": "Open a card",
                  "value": "Open a card",
                  "type": "button",
              },
              {
                "name":"Create weekly summary",
                "text": "Create weekly summary",
                "value": "Create weekly summary",
                "type": "button",
              },
              {
                "name":"Send notification",
                "text": "Send notification",
                "value": "Send notification",
                "type": "button",
              },
            ]
        },
        {
          "text": "Type `quit` or `abort` to close the conversation."
        }
      ]
    },[
        {
          pattern: /.*open.*card$/i,
          callback: botInteractions.handleOpenCard
        },
        {
          pattern: /.*open.*card.*/i,
          callback: botInteractions.handleOpenCardWithArgs
        },
        {
          pattern: /(.*create.*summary.*)|(.*summary.*)|(.*weekly.*summary.*)|(.*complete.*incomplete.*)/i,
          callback: botInteractions.getCardsForWeeklySummary
        },
        {
        pattern: /(.*Send.*notification.*)|(.*Remind.*)|(.*Notify.*)/i,
          callback: botInteractions.handleNotifyUser
        },
        {
          pattern: '.*',
          callback: function(response, convo){
            convo.say('I could not understand your response. Can you please repeat?');
            convo.repeat();
            convo.next();
          }
        }
    ]);
    convo.next();
  });
}

function askForSetup(bot, message){
  bot.startConversation(message,function(err,convo) {
    convo.say('It seems like you haven\'t linked you trello account.');
    convo.ask({
      attachments:[
        {
            title: 'Do you want to setup now ?\nType or click the below options',
            callback_id: '123',
            attachment_type: 'default',
            color:"#7750a5",
            actions: [
              {
                  "name":"Yes",
                  "text": "Yes",
                  "value": "Yes",
                  "type": "button",
              },
              {
                "name":"No",
                "text": "No",
                "value": "No",
                "type": "button",
              },
            ]
        }
      ]
    },[
      {
        pattern:"Yes",
        callback: function(response,convo){
          var randNounce = randomstring.generate(5);
          var temp = function(){
            startMainThread(bot,message);
          }
          global.OAUTH_NOUNCE[randNounce]=[message.user,temp];
          convo.say('Open this link to setup : <'+process.env.HOSTBASEURL+"/setup?userid="+message.user+"&nounce="+randNounce+"| Trello Authorization>");
          convo.next();
        }
      },
      {
        pattern: "No",
        callback: function(response,convo){
          console.log('Fall back to error message');
          convo.say('Ok, we can do it sometime later. I will have to leave since I can\'t access you trello.');
          convo.next();
        }
      }
    ]);
  });
}