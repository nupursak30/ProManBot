var nock = require('nock');
var data = require('./mocks/mock.json');

module.exports.startMock = function(){

	// Mock for story board
	var pathRegex = /\/1\/members\/me\/boards.*/;
	nock("https://api.trello.com")
	.persist()
	.get(pathRegex)
	.reply(200, JSON.stringify(data.MeBoards) );

	// Mock for all the lists in a board
	pathRegex = /\/1\/boards\/.*\/lists/;
	nock("https://api.trello.com")
	.persist()
	.get(pathRegex)
	.reply(200, JSON.stringify(data.boardLists) );
	
	// Mock for getting all the cards in a List
	pathRegex = /\/1\/batch/;
	nock("https://api.trello.com")
	.persist()
	.get(pathRegex)
	.reply(200, JSON.stringify(data.cardList) );

	// Mock to retrieve checklist items
	pathRegex = /\/1\/checklists\/.*\/checkItems/;
	nock("https://api.trello.com")
	.persist()
	.get(pathRegex)
	.reply(200, JSON.stringify(data.checkListItems) );

	// Mock to add checklist items
	pathRegex = /\/1\/checklists\/.*\/checkItems/;
	nock("https://api.trello.com")
	.persist()
	.post(pathRegex)
	.reply(200, JSON.stringify(data.AddChecklistItem) );

	//Mock to remove a checklist item
	pathRegex = /\/1\/checklists\/.*\/checkItems\/.*/;
	nock("https://api.trello.com")
	.persist()
	.delete(pathRegex)
	.reply(200, JSON.stringify(data.RemoveChecklistItem) );

	// Mock for marking an item present in the checklist of a card
	pathRegex = /\/1\/cards\/.*\/checkItem\/.*/;
	nock("https://api.trello.com")
	.persist()
	.put(pathRegex)
	.reply(200, JSON.stringify(data.MarkListItem[0]) );

	// Mock for adding a comment to a card
	pathRegex = /\/1\/cards\/.*\/actions\/comments.*/;
	nock("https://api.trello.com")
	.persist()
	.post(pathRegex)
	.reply(200, JSON.stringify(data.AddCommentOnCard) );	

};
