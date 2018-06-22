var request = require('request');


var api_fetch_story_board = "https://api.trello.com/1/members/me/boards?key={APP_KEY}&token={TOKEN_VALUE}";
var api_fetch_lists = "https://api.trello.com/1/boards/{BOARD_ID}/lists?key={APP_KEY}&token={TOKEN_VALUE}";
var api_fetch_cards = "https://api.trello.com/1/batch/?urls={LISTS_IDS}&key={APP_KEY}&token={TOKEN_VALUE}";
var api_add_checklistitem = "https://api.trello.com/1/checklists/{CHECKLIST_ID}/checkItems?name={CHECKLIST_ITEM_NAME}&key={APP_KEY}&token={TOKEN_VALUE}";
var api_mark_item = "https://api.trello.com/1/cards/{CARD_ID}/checkItem/{ITEM_ID}?state=complete&key={APP_KEY}&token={TOKEN_VALUE}";
var api_remove_checklistitem = "https://api.trello.com/1/checklists/{CHECKLIST_ID}/checkItems/{ITEM_ID}?key={APP_KEY}&token={TOKEN_VALUE}";
var api_list_checklistitems = "https://api.trello.com/1/checklists/{CHECKLIST_ID}/checkItems?key={APP_KEY}&token={TOKEN_VALUE}";
var api_add_comment = "https://api.trello.com/1/cards/{CARD_ID}/actions/comments?text={COMMENT_VALUE}&key={APP_KEY}&token={TOKEN_VALUE}"

// Wrapper to hide underlying callbacks
module.exports.openCard = function(userID, cardName, convo, callback){
    loadStoryBoard(userID,cardName, convo,callback);
}

module.exports.addTodoItem = function(userID, checkListID, todoItemName, callback){
    addCheckListItem(userID, checkListID, todoItemName, callback);
}

module.exports.markListItem = function(userID, cardID, todoItemID, callback){
    MarkCheckListItem(userID, cardID, todoItemID, callback);
}

module.exports.removeChecklistitem = function(userID, checkListID, todoItemID, callback){
    RemoveChecklistItem(userID, checkListID, todoItemID, callback);
}

module.exports.getListCheckListItems = function(userID, checkListID, callback){
    listChecklistItems(userID, checkListID, callback);
}

module.exports.addCommentOnCard = function(userID, cardID, comment, callback){
    addCommentOnCard(userID, cardID, comment, callback);
}

// Function to load a story board associated with the current user

function loadStoryBoard(userID, cardName, convo, callback){
    var urlBoard = api_fetch_story_board.replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
                    method: 'GET',
                    url: urlBoard  };
    request(options, function (error, response, body) {
        // parse response
        if (error) throw new Error(error);
        body = JSON.parse(body);
        
        loadBoardList(userID,body[0].id,cardName, convo,callback);
    });
}


//Function to load all the lists present in the storyboard

function loadBoardList(userID, boardID,cardName, convo, callback){
    // load all cards in this board
    var urlList= api_fetch_lists.replace("{BOARD_ID}",boardID).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);

    var options = { 
                    method: 'GET',
                    url: urlList  };

    request(options, function (error, response, body) {
        // parse response
        if (error) throw new Error(error);
        body = JSON.parse(body);

        loadCards(userID,body,cardName, convo,callback);

    });
}

//Function to load all the cards present in the To Do List

function loadCards(userID, listArray,cardName, convo,callback){
    // consider only only first story board
    var listCount = listArray.length;
    var batchListInfo = [];

    for(var i=0;i<listCount;i++){
        batchListInfo.push("/lists/"+listArray[i].id+"/cards");
    }
    batchListInfo = batchListInfo.join();

    // load all cards in this board
    var urlCards= api_fetch_cards.replace("{LISTS_IDS}",batchListInfo).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
                    method: 'GET',
                    url: urlCards  };
    request(options, function (error, response, body) {
        // parse response
        if (error) throw new Error(error);
        
        body = JSON.parse(body);
        
        var respCount = body.length;

        var cardList = [];

        for(var i=0;i<respCount;i++){
            respCardList = body[i]['200'];
            for(var j=0;j<respCardList.length;j++){
                cardList.push({
                    'id':respCardList[j].id,
                    'name':respCardList[j].name,
                    'desc':respCardList[j].desc,
                    'idChecklists':respCardList[j].idChecklists[0],
                    'due':respCardList[j].due,
                    'url':respCardList[j].url,
                    'dueComplete':respCardList[j].dueComplete,
                });
            }
        }
        console.log(callback);
        callback(convo,cardName, cardList);
    }); 

}

//Function to list all the checklist items present in the card

function listChecklistItems(userID, checkListID, callback){
    var urlBoard = api_list_checklistitems.replace("{CHECKLIST_ID}",checkListID).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
                    method: 'GET',
                    url: urlBoard  };
    request(options, function (error, response, body) {
        // parse response
        if (error) throw new Error(error);
        callback(error, response, body);
    });
}


//Function to add the user-specified checklist item in a card

function addCheckListItem(userID, checkListID, itemName, callback){
    itemName = encodeURIComponent(itemName)
    var urlCards= api_add_checklistitem.replace("{CHECKLIST_ID}",checkListID).replace("{CHECKLIST_ITEM_NAME}",itemName).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
        method: 'POST',
        url: urlCards  };
        request(options, function (error, response, body) {
            if(error){
                console.log(error);
                callback(false);
            }else{
                callback(true);
            }
        });
}        


//Function to mark the user-specified checklist item of a card

function MarkCheckListItem(userID, cardID, itemID,  callback){
    var urlCards= api_mark_item.replace("{CARD_ID}",cardID).replace("{ITEM_ID}",itemID).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
        method: 'PUT',
        url: urlCards  };
        request(options, function (error, response, body) {
            if(error){
                console.log(error);
                callback(false);
            }else{
                callback(true);
            }
        });
}

//Function to remove the user-specified checklist item from a card

function RemoveChecklistItem(userID, checkListID, itemID, callback){
    var urlCards= api_remove_checklistitem.replace("{CHECKLIST_ID}",checkListID).replace("{ITEM_ID}",itemID).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
        method: 'DELETE',
        url: urlCards  };
        request(options, function (error, response, body) {
            if(error){
                console.log(error);
                callback(false);
            }else{
                callback(true);
            }
        });
}

// REST API call to add comments to a card
function addCommentOnCard(userID, cardID, comment, callback){
    comment = encodeURIComponent(comment)
    var urlCards= api_add_comment.replace("{CARD_ID}",cardID).replace("{COMMENT_VALUE}",comment).replace("{APP_KEY}",global.APP_KEY).replace("{TOKEN_VALUE}",global.TRELLO_TOKEN_MAP[userID]);
    var options = { 
        method: 'POST',
        url: urlCards  };
        request(options, function (error, response, body) {
            if(error){
                console.log(error);
                callback(false);
            }else{
                callback(true);
            }
        });
}        
