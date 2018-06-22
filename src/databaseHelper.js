
module.exports.getTrelloToken = function(slackUserID, callback){
    const { Pool, Client } = require('pg')
    const pool = new Pool()
    pool.query('SELECT * from SlackTrelloUserMap WHERE slackID=\''+slackUserID+'\'', (err, res) => {
        if(res.rowCount<1){
            callback(undefined)
        }else{
            //console.log(res.rows[0].trellotoken);
            callback(res.rows[0].trellotoken);
        }
        pool.end()
    })
}

module.exports.setupTrelloAppKey = function(callback){
    const { Pool, Client } = require('pg')
    const pool = new Pool()
    pool.query('SELECT * from GlobalParams WHERE name=\'TrelloAppKey\'', (err, res) => {
        if(res.rowCount<1){
            console.log("Failed to retrieve Trello developer key");
            callback(undefined);
        }else{
            var trelloAppKey = res.rows[0].value;
            callback(trelloAppKey);
        }
        pool.end()
    })
}

module.exports.setTrelloToken = function(userid, trelloToken, callback){
    const { Pool, Client } = require('pg')
    const pool = new Pool()
    pool.query("INSERT INTO SlackTrelloUserMap(slackID, trelloToken ) VALUES ('"+userid+"', '"+trelloToken+"');",(err,res)=>{
        if(err){
            callback(false);
        }else{
            callback(true);
        }
    })
}
